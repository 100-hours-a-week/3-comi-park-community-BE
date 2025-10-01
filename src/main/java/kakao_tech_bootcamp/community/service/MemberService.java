package kakao_tech_bootcamp.community.service;

import jakarta.transaction.Transactional;
import kakao_tech_bootcamp.community.common.exceptions.BadRequestException;
import kakao_tech_bootcamp.community.common.exceptions.ConflictException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.MemberAvailabilityDto;
import kakao_tech_bootcamp.community.dto.MemberCreateRequestDto;
import kakao_tech_bootcamp.community.dto.MemberResponseDto;
import kakao_tech_bootcamp.community.dto.MemberUpdateRequestDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void existsEmail(MemberAvailabilityDto memberAvailabilityDto) {
        boolean exists = memberRepository.existsEmail(memberAvailabilityDto.getEmail());

        if (exists) {
            throw new ConflictException("중복된 이메일입니다");
        }
    }

    @Transactional
    public void existsNickname(MemberAvailabilityDto memberAvailabilityDto) {
        boolean exists = memberRepository.existsNickname(memberAvailabilityDto.getNickname());

        if (exists) {
            throw new ConflictException("중복된 닉네임입니다");
        }
    }

    @Transactional
    public void saveMember(MemberCreateRequestDto dto) {
        if (dto.getPassword() != null) {
            if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
                throw new BadRequestException("비밀번호가 일치하지 않습니다");
            }
        }

        // TODO: 비밀번호 암호화

        Member member = new Member(dto.getEmail(), dto.getPassword(), dto.getNickname(), dto.getImage());
        memberRepository.save(member);
    }

    @Transactional
    public MemberResponseDto findMember(Integer id) {
        Member member = memberRepository.findBy(id);

        if (member == null) {
            throw new NotFoundException("회원을 찾을 수 없습니다");
        }

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setId(member.getId());
        memberResponseDto.setEmail(member.getEmail());
        memberResponseDto.setNickname(member.getNickname());
        memberResponseDto.setImage(member.getImage());
        memberResponseDto.setCreatedAt(member.getCreatedAt());

        return memberResponseDto;
    }

    @Transactional
    public void modifyMember(Integer id, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findBy(id);

        if (member == null) {
            throw new NotFoundException("회원을 찾을 수 없습니다");
        }

        if (dto.getNickname() != null) {
            member.setNickname(dto.getNickname());
        }

        if (dto.getPassword() != null) {
            if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
                throw new BadRequestException("비밀번호가 일치하지 않습니다");
            }

            member.setPassword(dto.getPassword());
        }

        if (dto.getImage() != null) {
            member.setImage(dto.getImage());
        }
    }

    @Transactional
    public void removeMember(Integer id) {
        Member member = memberRepository.findBy(id);

        if (member == null) {
            throw new NotFoundException("회원을 찾을 수 없습니다");
        }

        memberRepository.remove(member);
    }
}

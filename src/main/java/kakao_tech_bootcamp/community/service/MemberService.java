package kakao_tech_bootcamp.community.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public void existsByEmail(MemberAvailabilityDto memberAvailabilityDto) {
        if (memberRepository.existsByEmail(memberAvailabilityDto.getEmail())) {
            throw new ConflictException("중복된 이메일입니다");
        }
    }

    @Transactional(readOnly = true)
    public void existsByNickname(MemberAvailabilityDto memberAvailabilityDto) {
        if (memberRepository.existsByNickname(memberAvailabilityDto.getNickname())) {
            throw new ConflictException("중복된 닉네임입니다");
        }
    }

    public void saveMember(MemberCreateRequestDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("중복된 이메일입니다");
        }

        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new ConflictException("중복된 닉네임입니다");
        }

        String encoded = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), encoded, dto.getNickname(), dto.getImage());

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMember(Integer id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setId(member.getId());
        memberResponseDto.setEmail(member.getEmail());
        memberResponseDto.setNickname(member.getNickname());
        memberResponseDto.setImage(member.getImage());
        memberResponseDto.setCreatedAt(member.getCreatedAt());

        return memberResponseDto;
    }

    public void modifyMember(Integer id, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

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

    public void removeMember(Integer id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));
        memberRepository.delete(member);
    }
}

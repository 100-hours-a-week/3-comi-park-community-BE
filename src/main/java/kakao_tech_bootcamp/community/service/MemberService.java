package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.BadRequestException;
import kakao_tech_bootcamp.community.common.exceptions.ConflictException;
import kakao_tech_bootcamp.community.common.exceptions.ForbiddenException;
import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.*;
import kakao_tech_bootcamp.community.entity.Image;
import kakao_tech_bootcamp.community.entity.ImageStatus;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, ImageService imageService, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.imageService = imageService;
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

        Image image = dto.getImage() != null
                ? imageService.modifyImageStatusById(dto.getImage().getId(), ImageStatus.ACTIVE)
                : null;

        String encoded = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), encoded, dto.getNickname(), image);

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMember(Integer id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        ImageResponseDto imageResponseDto = new ImageResponseDto();

        if (member.getImage() != null) {
            Image image = member.getImage();
            imageResponseDto.setId(image.getId());
            imageResponseDto.setUrl(imageService.createUrl(image.getObjectKey()));
            imageResponseDto.setObjectKey(image.getObjectKey());
            imageResponseDto.setFilename(image.getFilename());
            memberResponseDto.setImage(imageResponseDto);
        }

        memberResponseDto.setId(member.getId());
        memberResponseDto.setEmail(member.getEmail());
        memberResponseDto.setNickname(member.getNickname());
        memberResponseDto.setCreatedAt(member.getCreatedAt());

        return memberResponseDto;
    }

    public void modifyMember(Integer currentMemberId, Integer id, MemberUpdateRequestDto dto) {
        if (!Objects.equals(currentMemberId, id)) {
            throw new ForbiddenException("회원 본인 정보에 대해서만 수정할 수 있습니다");
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        if (dto.getNickname() != null) {
            member.setNickname(dto.getNickname());
        }

        if (dto.getPassword() != null) {
            if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
                throw new BadRequestException("비밀번호가 일치하지 않습니다");
            }

            member.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getImage() != null) {
            Image previousImage = member.getImage();

            Image currentImage = imageService.modifyImageStatusById(dto.getImage().getId(), ImageStatus.ACTIVE);
            member.setImage(currentImage);

            /*
             removeImage()에는 데이터베이스뿐만 아니라 물리적인 이미지 파일 삭제까지 포함되어 있음
             물리적인 이미지 파일 삭제는 트랜잭션처럼 롤백되지 못하므로
             모든 데이터베이스 작업이 정상적으로 완료되어 이미지 삭제가 확정된 가장 마지막 시점에 이미지 삭제 처리 진행
             */
            if (previousImage != null) {
                imageService.removeImage(previousImage.getId(), previousImage.getObjectKey());
            }
        }
    }

    public void removeMember(Integer currentMemberId, Integer id) {
        if (!Objects.equals(currentMemberId, id)) {
            throw new ForbiddenException("회원 본인만 탈퇴할 수 있습니다");
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        memberRepository.delete(member); // image on delete cascade

        if (member.getImage() != null) {
            imageService.removeImage(member.getImage().getId(), member.getImage().getObjectKey());
        }
    }
}

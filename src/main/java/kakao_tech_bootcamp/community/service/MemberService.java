package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.MemberExceptionCode;
import kakao_tech_bootcamp.community.dto.request.MemberAvailabilityDto;
import kakao_tech_bootcamp.community.dto.request.MemberCreateRequestDto;
import kakao_tech_bootcamp.community.dto.request.MemberUpdateRequestDto;
import kakao_tech_bootcamp.community.dto.response.ChangedResponseDto;
import kakao_tech_bootcamp.community.dto.response.MemberResponseDto;
import kakao_tech_bootcamp.community.dto.response.basic.ImageDto;
import kakao_tech_bootcamp.community.entity.Image;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.ImageRepository;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public void existsByEmail(MemberAvailabilityDto memberAvailabilityDto) {
        if (memberRepository.existsByEmail(memberAvailabilityDto.getEmail())) {
            throw new CustomException(MemberExceptionCode.DUPLICATED_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public void existsByNickname(MemberAvailabilityDto memberAvailabilityDto) {
        if (memberRepository.existsByNickname(memberAvailabilityDto.getNickname())) {
            throw new CustomException(MemberExceptionCode.DUPLICATED_NICKNAME);
        }
    }

    public MemberResponseDto saveMember(MemberCreateRequestDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
            throw new CustomException(MemberExceptionCode.UNMATCHED_PASSWORD);
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(MemberExceptionCode.DUPLICATED_EMAIL);
        }

        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(MemberExceptionCode.DUPLICATED_NICKNAME);
        }

        Image image = null;

        if (dto.getImage() != null) {
            image = new Image(dto.getImage().getFilename(), dto.getImage().getObjectKey(), dto.getImage().getUrl());
            imageRepository.save(image);
        }

        String encoded = passwordEncoder.encode(dto.getPassword());
        Member member = new Member(dto.getEmail(), encoded, dto.getNickname(), image);

        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMember(Integer id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new CustomException(MemberExceptionCode.NOT_FOUND));
        return MemberResponseDto.of(member);
    }

    public ChangedResponseDto modifyMember(Integer currentMemberId, Integer id, MemberUpdateRequestDto dto) {
        if (!Objects.equals(currentMemberId, id)) {
            throw new CustomException(MemberExceptionCode.FORBIDDEN_UPDATE);
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberExceptionCode.NOT_FOUND));

        ChangedResponseDto changedResponseDto = new ChangedResponseDto();

        if (dto.getPassword() != null) {
            if (!dto.getPassword().equals(dto.getConfirmedPassword())) {
                throw new CustomException(MemberExceptionCode.UNMATCHED_PASSWORD);
            }

            member.changePassword(passwordEncoder.encode(dto.getPassword()));
            changedResponseDto.add("passwordChanged", true);
        }

        if (dto.getNickname() != null) {
            member.changeNickname(dto.getNickname());
            changedResponseDto.add("nickname", member.getNickname());
        }

        /*
          dto.getImageDeleted() → 기존 프로필 이미지 제거할 때만 처리 (기존 프로필 이미지 유지 및 다른 이미지로 변경과는 무관)
          dto.getImage() == null → 단, imageDeleted == true더라도 새 이미지 전달하면(not null) 프로필 이미지 변경으로 간주
          member.getImage() != null → 기존 프로필 있을 때만 제거 처리
         */
        if (dto.getImageDeleted() && dto.getImage() == null && member.getImage() != null) {
            Image previousImage = member.getImage();
            member.changeImage(null);

            // TODO: 이미지 객체 삭제 구현 (S3, 멀티파트 동시 지원하도록)
//            imageService.removeImage(previousImage.getId(), previousImage.getObjectKey());

            changedResponseDto.add("image", ImageDto.of(member.getImage()));
        }

        if (dto.getImage() != null) {
            Image previousImage = member.getImage();
            Image image = new Image(
                    dto.getImage().getFilename(),
                    dto.getImage().getObjectKey(),
                    dto.getImage().getUrl()
            );

            member.changeImage(imageRepository.save(image));

            /*
             removeImage()에는 데이터베이스뿐만 아니라 물리적인 이미지 파일 삭제까지 포함되어 있음
             물리적인 이미지 파일 삭제는 트랜잭션처럼 롤백되지 못하므로
             모든 데이터베이스 작업이 정상적으로 완료되어 이미지 삭제가 확정된 가장 마지막 시점에 이미지 삭제 처리 진행
             */
            // TODO: 이미지 객체 삭제 구현 (S3, 멀티파트 동시 지원하도록)
//            if (previousImage != null) {
//                imageService.removeImage(previousImage.getId(), previousImage.getObjectKey());
//            }

            changedResponseDto.add("image", ImageDto.of(member.getImage()));
        }

        return changedResponseDto;
    }

    public void removeMember(Integer currentMemberId, Integer id) {
        if (!Objects.equals(currentMemberId, id)) {
            throw new CustomException(MemberExceptionCode.FORBIDDEN_DELETE);
        }

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberExceptionCode.NOT_FOUND));

        memberRepository.delete(member); // image on delete cascade

        // TODO: 이미지 객체 삭제 구현 (S3, 멀티파트 동시 지원하도록)
//        if (member.getImage() != null) {
//            imageService.removeImage(member.getImage().getId(), member.getImage().getObjectKey());
//        }
    }
}

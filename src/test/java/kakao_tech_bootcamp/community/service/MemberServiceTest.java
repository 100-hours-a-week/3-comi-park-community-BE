package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.CustomException;
import kakao_tech_bootcamp.community.common.exceptions.code.MemberExceptionCode;
import kakao_tech_bootcamp.community.dto.request.ImageRequestDto;
import kakao_tech_bootcamp.community.dto.request.MemberAvailabilityDto;
import kakao_tech_bootcamp.community.dto.request.MemberCreateRequestDto;
import kakao_tech_bootcamp.community.dto.request.MemberUpdateRequestDto;
import kakao_tech_bootcamp.community.dto.response.basic.ImageDto;
import kakao_tech_bootcamp.community.dto.response.basic.MemberDto;
import kakao_tech_bootcamp.community.entity.Image;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.repository.ImageRepository;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    @DisplayName("이메일 중복체크: 중복되지 않는 경우 예외 발생 X")
    void existsByEmail() {
        // given
        MemberAvailabilityDto availabilityDto = new MemberAvailabilityDto();
        availabilityDto.setEmail("test@test.com");

        // when
        when(memberRepository.existsByEmail("test@test.com")).thenReturn(false);

        // then
        assertThatCode(() -> memberService.existsByEmail(availabilityDto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이메일 중복체크: 이미 존재하는 경우 DUPLICATED_EMAIL 예외 발생")
    void existsByEmailWithDuplicatedEmail() {
        // given
        MemberAvailabilityDto availabilityDto = new MemberAvailabilityDto();
        availabilityDto.setEmail("test@test.com");

        // when
        when(memberRepository.existsByEmail("test@test.com")).thenReturn(true);

        // then
        assertThatThrownBy(() -> memberService.existsByEmail(availabilityDto))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException customException = (CustomException) ex;
                    assertThat(customException.getExceptionCode()).isEqualTo(MemberExceptionCode.DUPLICATED_EMAIL);
                });
    }

    @Test
    @DisplayName("닉네임 중복체크: 중복되지 않는 경우 예외 발생 X")
    void existsByNickname() {
        // given
        MemberAvailabilityDto availabilityDto = new MemberAvailabilityDto();
        availabilityDto.setNickname("test");

        // when
        when(memberRepository.existsByNickname("test")).thenReturn(false);

        // given
        assertThatCode(() -> memberService.existsByNickname(availabilityDto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("닉네임 중복체크: 이미 존재하는 경우 DUPLICATED_NICKNAME 예외 발생")
    void existsByNicknameWithDuplicatedNickname() {
        // given
        MemberAvailabilityDto availabilityDto = new MemberAvailabilityDto();
        availabilityDto.setNickname("test");

        // when
        when(memberRepository.existsByNickname("test")).thenReturn(true);

        // given
        assertThatThrownBy(() -> memberService.existsByNickname(availabilityDto))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException customException = (CustomException) ex;
                    assertThat(customException.getExceptionCode()).isEqualTo(MemberExceptionCode.DUPLICATED_NICKNAME);
                });
    }

    @Test
    @DisplayName("회원 가입: 성공")
    void saveMember() {
        // given
        String email = "test@test.com";
        String nickname = "test";
        String password = "Password123!";
        String confirmedPassword = "Password123!";

        MemberCreateRequestDto createRequestDto = new MemberCreateRequestDto();
        createRequestDto.setEmail(email);
        createRequestDto.setNickname(nickname);
        createRequestDto.setPassword(password);
        createRequestDto.setConfirmedPassword(confirmedPassword);

        when(memberRepository.existsByEmail(email)).thenReturn(false);
        when(memberRepository.existsByNickname(nickname)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encoded" + password);

        Member savedMember = new Member(email, "encoded" + password, nickname, null);
        when(memberRepository.save(ArgumentMatchers.<Member>any())).thenReturn(savedMember);

        // when
        MemberDto memberDto = memberService.saveMember(createRequestDto).getMember();

        // then
        assertThat(memberDto.getEmail()).isEqualTo(createRequestDto.getEmail());
        assertThat(memberDto.getNickname()).isEqualTo(createRequestDto.getNickname());
    }

    @Test
    @DisplayName("회원 가입: 비밀번호와 비밀번호 확인 불일치 시 UNMATCHED_PASSWORD 예외 발생")
    void saveMemberWithUnmatchedPassword() {
        // given
        String email = "test@test.com";
        String nickname = "test";
        String password = "Password123!";
        String confirmedPassword = "Unmatched123!";

        MemberCreateRequestDto createRequestDto = new MemberCreateRequestDto();
        createRequestDto.setEmail(email);
        createRequestDto.setNickname(nickname);
        createRequestDto.setPassword(password);
        createRequestDto.setConfirmedPassword(confirmedPassword);

        // when
        assertThatThrownBy(() -> memberService.saveMember(createRequestDto))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException customException = (CustomException) ex;
                    assertThat(customException.getExceptionCode()).isEqualTo(MemberExceptionCode.UNMATCHED_PASSWORD);
                });
    }

    @Test
    @DisplayName("회원 조회: 성공")
    void findMember() {
        // given
        Integer id = 1;
        Member savedMember = new Member("test@test.com", "Password123!", "test", null);
        when(memberRepository.findById(id)).thenReturn(Optional.of(savedMember));

        // when
        MemberDto memberDto = memberService.findMember(id).getMember();

        // then
        assertThat(memberDto.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(memberDto.getNickname()).isEqualTo(savedMember.getNickname());
    }

    @Test
    @DisplayName("회원 조회: 회원이 존재하지 않으면 NOT_FOUND 예외 발생")
    void findMemberWithNotFound() {
        // given
        Integer id = 1;
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.findMember(id))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException customException = (CustomException) ex;
                    assertThat(customException.getExceptionCode()).isEqualTo(MemberExceptionCode.NOT_FOUND);
                });
    }

    @Test
    @DisplayName("회원 수정: 닉네임 변경 성공")
    void modifyMemberWithNickname() {
        // given
        Integer id = 1;
        MemberUpdateRequestDto updateRequestDto = new MemberUpdateRequestDto();
        updateRequestDto.setNickname("newNickname");
        updateRequestDto.setImageDeleted(false);

        Member member = new Member("test@test.com", "Password123!", "test", null);
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        // when
        Map<String, Object> changes = memberService.modifyMember(id, id, updateRequestDto).getChanges();

        // then
        assertThat(changes.get("nickname")).isEqualTo(updateRequestDto.getNickname());
    }

    @Test
    @DisplayName("회원 수정: 이미지 변경 성공")
    void modifyMemberWithImage() {
        // given
        Integer id = 1;

        ImageRequestDto imageRequestDto = new ImageRequestDto();
        imageRequestDto.setId(1);
        imageRequestDto.setObjectKey(UUID.randomUUID().toString());
        imageRequestDto.setFilename("filename");
        imageRequestDto.setUrl("/api/images/filename");

        MemberUpdateRequestDto updateRequestDto = new MemberUpdateRequestDto();
        updateRequestDto.setImageDeleted(false);
        updateRequestDto.setImage(imageRequestDto);

        Member member = new Member("test@test.com", "Password123!", "test", null);
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Image image = new Image(imageRequestDto.getFilename(), imageRequestDto.getObjectKey(), imageRequestDto.getUrl());
        when(imageRepository.save(ArgumentMatchers.<Image>any())).thenReturn(image);

        // when
        Map<String, Object> changes = memberService.modifyMember(id, id, updateRequestDto).getChanges();
        ImageDto imageDto = (ImageDto) changes.get("image");


        // then
        assertThat(imageDto.getObjectKey()).isEqualTo(imageRequestDto.getObjectKey());
        assertThat(imageDto.getFilename()).isEqualTo(imageRequestDto.getFilename());
        assertThat(imageDto.getUrl()).isEqualTo(imageRequestDto.getUrl());
    }

    @Test
    @DisplayName("회원 수정: 본인이 아니면 FORBIDDEN_UPDATE 예외 발생")
    void modifyMemberWithForbiddenUpdate() {
        // given
        MemberUpdateRequestDto updateRequestDto = new MemberUpdateRequestDto();
        updateRequestDto.setNickname("newNickname");

        // when & then
        assertThatThrownBy(() -> memberService.modifyMember(1, 2, updateRequestDto))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException customException = (CustomException) ex;
                    assertThat(customException.getExceptionCode()).isEqualTo(MemberExceptionCode.FORBIDDEN_UPDATE);
                });
    }

    @Test
    @DisplayName("회원 탈퇴: 성공")
    void removeMember() {
        // given
        Integer id = 1;
        Member member = new Member("test@test.com", "Password123!", "test", null);
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        // when
        memberService.removeMember(id, id);

        // when & then
        verify(memberRepository, times(1)).delete(member); // delete가 1회 호출됨
    }

    @Test
    @DisplayName("회원 탈퇴: 탈퇴할 회원을 찾을 수 없으면 NOT_FOUND 예외 발생")
    void removeMemberWithNotFound() {
        // given
        Integer id = 1;
        when(memberRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.removeMember(id, id))
                .isInstanceOf(CustomException.class)
                .satisfies(ex -> {
                    CustomException customException = (CustomException) ex;
                    assertThat(customException.getExceptionCode()).isEqualTo(MemberExceptionCode.NOT_FOUND);
                });
    }
}
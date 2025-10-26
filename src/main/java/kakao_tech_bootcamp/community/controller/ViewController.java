package kakao_tech_bootcamp.community.controller;

import kakao_tech_bootcamp.community.dto.SectionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {
    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("title", "이용약관");
        model.addAttribute("sections", getTermsSections());
        return "sections";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("title", "개인정보처리방침");
        model.addAttribute("sections", getPrivacySections());
        return "sections";
    }

    private List<SectionDto> getTermsSections() {
        return List.of(
                new SectionDto("총칙", List.of(
                        "서비스명, 운영자명, 연락처, 목적 명시",
                        "예: 본 약관은 ‘아무말대잔치’(이하 ‘서비스’)의 이용과 관련하여 회사(운영자)와 이용자 간의 권리·의무 및 책임사항을 규정함을 목적으로 합니다."
                )),
                new SectionDto("용어 정의", List.of(
                        "“회원”: 서비스에 로그인하여 이용하는 자",
                        "“게시물”: 회원이 서비스에 게시한 텍스트, 이미지, 댓글 등",
                        "“운영자”: 서비스를 관리, 운영하는 자",
                        "“콘텐츠”: 서비스에 게시된 모든 자료"
                )),
                new SectionDto("약관의 효력 및 변경", List.of(
                        "약관은 공지 즉시 효력이 발생합니다.",
                        "변경 시 공지 및 일정 기간의 유예를 둡니다."
                )),
                new SectionDto("회원 가입 및 이용계약", List.of(
                        "이메일, 닉네임 등 필수 정보를 입력해야 합니다.",
                        "허위 정보 입력 시 계정이 삭제될 수 있습니다.",
                        "미성년자의 서비스 이용 제한 여부를 명시합니다."
                )),
                new SectionDto("서비스의 제공 및 변경", List.of(
                        "제공 서비스: 게시글 작성, 댓글 작성, 이미지 업로드, 좋아요 기능 등",
                        "서비스 변경·중단 시 사전에 공지합니다.",
                        "기술적 문제나 정책상 사유로 인한 임시 중단이 가능할 수 있습니다."
                )),
                new SectionDto("회원의 의무", List.of(
                        "법령 및 약관을 준수해야 합니다.",
                        "타인의 명의를 도용해서는 안 됩니다.",
                        "불법정보(음란, 혐오, 저작권 침해 등)를 게시해서는 안 됩니다.",
                        "스팸성 게시물 및 광고 게시를 금지합니다."
                )),
                new SectionDto("게시물의 저작권", List.of(
                        "회원이 작성한 게시물의 저작권은 회원에게 있습니다.",
                        "단, 서비스 운영·홍보 목적의 비상업적 사용은 허용됩니다.",
                        "회원이 게시물을 삭제하더라도 복제본·백업본에 일정 기간 남을 수 있습니다."
                )),
                new SectionDto("서비스 이용 제한", List.of(
                        "다음 사유 발생 시 이용 정지·영구 탈퇴가 가능합니다.",
                        "불법 행위, 명예훼손, 스팸, 음란물 게시 등",
                        "운영자의 판단으로 공공질서를 저해하는 경우 이용을 제한할 수 있습니다."
                )),
                new SectionDto("책임의 한계", List.of(
                        "서비스 이용 중 발생한 피해에 대해 운영자가 고의 또는 중과실이 없는 한 책임지지 않습니다.",
                        "게시물의 신뢰성과 정확성은 이용자 본인의 책임입니다."
                )),
                new SectionDto("이용계약 해지 및 회원탈퇴", List.of(
                        "회원은 언제든지 탈퇴할 수 있습니다.",
                        "탈퇴 시 작성한 게시물은 별도 요청이 없을 경우 삭제되지 않습니다."
                )),
                new SectionDto("분쟁 해결", List.of(
                        "분쟁이 발생한 경우 우선 협의로 해결합니다.",
                        "해결되지 않을 경우, 관할 법원은 운영자 주소지의 관할 법원으로 합니다."
                ))
        );
    }

    private List<SectionDto> getPrivacySections() {
        return List.of(
                new SectionDto("개인정보의 수집 항목", List.of(
                        "필수항목: 이메일, 비밀번호, 닉네임",
                        "선택항목: 프로필 이미지",
                        "자동수집항목: IP주소, 접속기록, 쿠키, 서비스 이용기록"
                )),
                new SectionDto("개인정보의 수집 및 이용 목적", List.of(
                        "회원 식별 및 본인 확인",
                        "서비스 제공 및 콘텐츠 추천",
                        "불량회원의 부정 이용 방지",
                        "고객문의 처리 및 공지사항 전달",
                        "통계 분석 및 서비스 개선"
                )),
                new SectionDto("개인정보의 보유 및 이용기간", List.of(
                        "회원 탈퇴 시 즉시 파기합니다.",
                        "단, 관계 법령에 따라 일정 기간 보관할 수 있습니다.",
                        "로그인 기록: 3개월",
                        "게시물 관련 로그: 6개월",
                        "민원·분쟁 처리 기록: 3년"
                )),
                new SectionDto("개인정보의 제3자 제공", List.of(
                        "원칙적으로 제3자에게 제공하지 않습니다.",
                        "단, 법령에 의거하거나 수사기관의 요청이 있는 경우 예외적으로 제공할 수 있습니다."
                )),
                new SectionDto("개인정보의 처리 위탁", List.of(
                        "이미지 업로드 시 Amazon S3 등의 외부 클라우드 서비스를 이용할 수 있습니다.",
                        "위탁 시 위탁업체명, 업무내용, 보관기간을 명시합니다."
                )),
                new SectionDto("쿠키의 사용", List.of(
                        "로그인 유지 및 세션 관리를 위해 쿠키를 사용합니다.",
                        "이용자는 브라우저 설정을 통해 쿠키 저장을 거부할 수 있습니다."
                )),
                new SectionDto("개인정보의 파기 절차 및 방법", List.of(
                        "개인정보의 수집 및 이용 목적 달성 시 즉시 파기합니다.",
                        "전자적 파일 형태의 정보는 복구할 수 없는 기술적 방법으로 삭제합니다.",
                        "서면 자료는 분쇄 또는 소각 처리합니다."
                )),
                new SectionDto("이용자 권리", List.of(
                        "이용자는 자신의 개인정보에 대해 열람, 정정, 삭제, 처리정지 요구를 할 수 있습니다.",
                        "이메일 또는 문의를 통해 권리를 행사할 수 있습니다."
                )),
                new SectionDto("개인정보 보호책임자", List.of(
                        "개인정보 보호책임자: 홍길동",
                        "이메일: contact@gil-dong.com",
                        "전화번호: 010-XXXX-XXXX"
                )),
                new SectionDto("고지의 의무", List.of(
                        "개인정보처리방침 변경 시 공지사항을 통해 사전 고지합니다.",
                        "시행일자를 명시합니다."
                ))
        );
    }
}

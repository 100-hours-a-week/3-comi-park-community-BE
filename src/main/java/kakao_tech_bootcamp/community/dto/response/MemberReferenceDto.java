package kakao_tech_bootcamp.community.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import kakao_tech_bootcamp.community.common.response.BaseResponse;
import kakao_tech_bootcamp.community.dto.response.basic.ImageDto;
import kakao_tech_bootcamp.community.entity.Member;
import lombok.Getter;

@Getter
public class MemberReferenceDto implements BaseResponse {
    private Integer id;
    private String nickname;
    private ImageDto image;

    @QueryProjection
    public MemberReferenceDto(Integer id, String nickname, ImageDto image) {
        this.id = id;
        this.nickname = nickname;
        /*
        게시글 전체 조회 QueryDSL에서 게시글 이미지가 없을 때도 무조건 ImageResponseDto를 new 하기 때문에
        image: {id: null, objectKey: null}로 반환됨
        따라서 직접 image.getId() == null 여부를 확인해 이미지가 없다면 image: null로 반환되도록 처리함
         */
        this.image = image == null || image.getId() == null ? null : image;
    }

    public static MemberReferenceDto of(Member member) {
        return new MemberReferenceDto(member.getId(), member.getNickname(), ImageDto.of(member.getImage()));
    }
}

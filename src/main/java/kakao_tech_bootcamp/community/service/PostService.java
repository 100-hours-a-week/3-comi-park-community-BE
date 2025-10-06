package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.common.exceptions.NotFoundException;
import kakao_tech_bootcamp.community.dto.PostCreateRequestDto;
import kakao_tech_bootcamp.community.entity.*;
import kakao_tech_bootcamp.community.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostAdditionalRepository postAdditionalRepository;
    private final PostStatRepository postStatRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    public void savePost(Integer currentMemberId, PostCreateRequestDto dto) {
        Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다"));

        Image image = null;

        if (dto.getImage() != null) {
            image = imageRepository.findById(dto.getImage().getId())
                    .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다"));
        }

        savePost(new Post(dto.getTitle(), dto.getContent(), member, image));
    }

    private void savePost(Post post) {
        Post savePost = postRepository.save(post);
        postAdditionalRepository.save(new PostAdditional(savePost));
        postStatRepository.save(new PostStat(savePost.getId()));
    }
}

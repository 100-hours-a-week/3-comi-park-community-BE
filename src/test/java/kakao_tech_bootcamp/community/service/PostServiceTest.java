package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.dto.request.PostCreateRequestDto;
import kakao_tech_bootcamp.community.dto.response.basic.PostDto;
import kakao_tech_bootcamp.community.entity.Member;
import kakao_tech_bootcamp.community.entity.Post;
import kakao_tech_bootcamp.community.repository.MemberRepository;
import kakao_tech_bootcamp.community.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    void savePost() {
        // given
        Integer id = 1;
        PostCreateRequestDto createRequestDto = new PostCreateRequestDto();
        createRequestDto.setTitle("title");
        createRequestDto.setContent("content");

        Member member = new Member("test@test.com", "Password123!", "test", null);
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Post post = new Post(createRequestDto.getTitle(), createRequestDto.getContent(), member, null);
        when(postRepository.save(ArgumentMatchers.<Post>any())).thenReturn(post);

        // when
        PostDto savedPost = postService.savePost(id, createRequestDto).getPost();

        // then
        assertThat(savedPost.getTitle()).isEqualTo(createRequestDto.getTitle());
        assertThat(savedPost.getContent()).isEqualTo(createRequestDto.getContent());
    }
}
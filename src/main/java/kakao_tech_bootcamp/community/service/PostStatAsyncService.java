package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.repository.PostStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor
public class PostStatAsyncService {
    private final PostStatRepository postStatRepository;

    @Async
    public void asyncIncrementViewCount(Integer postId) {
        postStatRepository.incrementViewCountById(postId);
    }

    @Async
    public void asyncIncrementLikeCount(Integer postId) {
        postStatRepository.incrementLikeCountById(postId);
    }

    @Async
    public void asyncDecrementLikeCount(Integer postId) {
        postStatRepository.decrementLikeCountById(postId);
    }
}

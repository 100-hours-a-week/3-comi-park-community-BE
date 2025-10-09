package kakao_tech_bootcamp.community.service;

import kakao_tech_bootcamp.community.repository.PostStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostStatAsyncService {
    private final PostStatRepository postStatRepository;

    @Async
    @Transactional
    public void asyncIncrementViewCount(Integer postId) {
        postStatRepository.incrementViewCountById(postId);
    }
}

package kakao_tech_bootcamp.community.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImageStorageStrategyFactory {
    private final Map<ImageStorage, ImageStorageStrategy> strategies;

    public ImageStorageStrategyFactory(List<ImageStorageStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(ImageStorageStrategy::getImageStorage, strategy -> strategy));
    }

    public ImageStorageStrategy getStrategy(ImageStorage storage) {
        return strategies.get(storage);
    }
}

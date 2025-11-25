package kakao_tech_bootcamp.community.utils.objectKeyStrategy;

public interface ObjectKeyStrategy {
    String generate(String filename);
    ImageCategory getCategory();
}

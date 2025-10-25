package kakao_tech_bootcamp.community.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {
    private static String LOCAL_STORAGE_PATH;
    private static String STATIC_BASE_URL;

    @Value("${storage.local-storage-path}")
    public void setLocalStoragePath(String localStoragePath) {
        LOCAL_STORAGE_PATH = localStoragePath.endsWith("/") ? localStoragePath : localStoragePath + "/";
    }

    @Value("${storage.static-base-url}")
    public void setStaticBaseUrl(String staticBaseUrl) {
        STATIC_BASE_URL = staticBaseUrl.endsWith("/") ? staticBaseUrl : staticBaseUrl + "/";
    }

    public static String getLocalStoragePath() {
        return LOCAL_STORAGE_PATH;
    }

    public static String getStaticBaseUrl() {
        return STATIC_BASE_URL;
    }

    public static String createFilepath(String objectKey) {
        return LOCAL_STORAGE_PATH + objectKey;
    }
}
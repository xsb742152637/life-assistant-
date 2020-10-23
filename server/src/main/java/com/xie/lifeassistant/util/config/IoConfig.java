package com.xie.lifeassistant.util.config;

import com.xie.lifeassistant.util.spring.ApplicationContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "io")
public class IoConfig {
    private static String fileStorage;

    public String getFileStorage() {
        return IoConfig.fileStorage;
    }

    public void setFileStorage(String fileStorage) {
        IoConfig.fileStorage = fileStorage;
    }
}

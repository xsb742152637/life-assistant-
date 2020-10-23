package com.xie.lifeassistant.util.config;

import com.xie.lifeassistant.util.spring.ApplicationContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "basesetting")
public class BaseSettingConfig {
    private static String name;
    private static Boolean isAutoLogin = false;

    public static BaseSettingConfig getInstance(){
        return ApplicationContext.getCurrent().getBean(BaseSettingConfig.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        BaseSettingConfig.name = name;
    }

    public Boolean getIsAutoLogin() {
        return isAutoLogin;
    }

    public void setIsAutoLogin(Boolean isAutoLogin) {
        BaseSettingConfig.isAutoLogin = isAutoLogin;
    }
}

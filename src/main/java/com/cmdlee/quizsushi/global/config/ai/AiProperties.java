package com.cmdlee.quizsushi.global.config.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "ai")
public class AiProperties {
    private List<String> baseUrls;
    private boolean stream;
}

package com.libiao.lblearn1.security.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "lb")
public class AuthProperties {

    private List<String> whiteList;
}

package com.libiao.lblearn1.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "lb.jwt")
public class JwtProperties {

    private Long refreshTtl;

    private Long accessTtl;

    private String tokenName;

    private String secret;

}

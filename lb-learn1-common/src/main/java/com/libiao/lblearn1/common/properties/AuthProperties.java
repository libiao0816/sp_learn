package com.libiao.lblearn1.common.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "lb")
public class AuthProperties {

    /** 管理端（/admin/**）免登录白名单 */
    private List<String> adminWhiteList;

    /** 客户端（/client/**）免登录白名单 */
    private List<String> clientWhiteList;
}

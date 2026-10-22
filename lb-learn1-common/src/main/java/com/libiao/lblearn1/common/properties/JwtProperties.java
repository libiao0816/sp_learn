package com.libiao.lblearn1.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "lb.jwt")
public class JwtProperties {

    private String tokenName;

    /** C 端（/client/**）token 签发与校验配置，内联初始化保证未配置时也有默认值 */
    private Client client = new Client();

    /** 管理端（/admin/**）token 签发与校验配置 */
    private Admin admin = new Admin();

    @Data
    public static class Client {

        /**
         * C 端签名密钥，与管理端隔离：
         * 密钥不同 → 管理端签发的 token 在 C 端过滤器验签直接失败（反之亦然），
         * 避免两套独立用户表（sys_user / user）因 ID 撞号造成跨域越权
         */
        private String secret;

        /** access token 有效期（毫秒），默认 30 分钟 */
        private Long accessTtl = 1800000L;

        /** refresh token 有效期（毫秒），默认 7 天 */
        private Long refreshTtl = 604800000L;
    }

    @Data
    public static class Admin {

        /** 管理端签名密钥，与 C 端隔离（见 Client.secret 说明） */
        private String secret;

        /** access token 有效期（毫秒），默认 30 分钟 */
        private Long accessTtl = 1800000L;

        /** refresh token 有效期（毫秒），默认 7 天 */
        private Long refreshTtl = 604800000L;
    }
}

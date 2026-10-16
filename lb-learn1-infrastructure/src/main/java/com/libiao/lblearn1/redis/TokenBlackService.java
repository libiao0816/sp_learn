package com.libiao.lblearn1.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@AllArgsConstructor
public class TokenBlackService {

    /** Redis Key 前缀: lb:{模块}:{业务} , 后续拼接唯一标识 jti */
    private static final String KEY_PREFIX = "lb:user:black:";

    private final RedisTemplate<String, Object> redisTemplate;

    //private final Map<String, Long> blackList = new ConcurrentHashMap<>();

    private String getBlackUserKey(String jti) {
        return KEY_PREFIX + jti;
    }

    public void addBlackList(String jti,long expireTime) {
        long systemTime = System.currentTimeMillis();
        if(expireTime > systemTime){
            //blackList.put(jti,expireTime);
            // 显式 Duration 指定毫秒, 避免三参重载 timeout 单位歧义导致 key 被写成无过期时间(-1)
            redisTemplate.opsForValue().set(getBlackUserKey(jti), "1", Duration.ofMillis(expireTime - systemTime));
        }
    }

    public boolean isBlackList(String jti) {
        Object o = redisTemplate.opsForValue().get(getBlackUserKey(jti));
        return Objects.nonNull(o);
//        Long expireTime = blackList.get(jti);
//        if(expireTime == null) return false;
//        if(expireTime < System.currentTimeMillis()){
//            blackList.remove(jti);
//            return false;
//        }
    }
}

package com.cmdlee.quizsushi.global.infra.redis;

import com.cmdlee.quizsushi.member.domain.model.RefreshTokenData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final RedisTemplate<String, RefreshTokenData> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private static final long TTL_SECONDS = 86400L;

    public void save(String memberId, String refreshUUID, HttpServletRequest request) {
        String indexKey = getIndexKey(memberId);
        String uuidKey = getUUIDKey(refreshUUID);
        String userAgent = request.getHeader("User-Agent");

        RefreshTokenData data = new RefreshTokenData(
                refreshUUID,
                memberId,
                userAgent,
                Instant.now()
        );

        String oldUUID = stringRedisTemplate.opsForValue().get(indexKey);
        if (oldUUID != null) {
            redisTemplate.delete(getUUIDKey(oldUUID));
        }

        redisTemplate.opsForValue().set(uuidKey, data, TTL_SECONDS, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(indexKey, refreshUUID, TTL_SECONDS, TimeUnit.SECONDS);
    }

    public Optional<RefreshTokenData> find(String refreshUUID) {
        String key = getUUIDKey(refreshUUID);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj instanceof RefreshTokenData data) {
            return Optional.of(data);
        }
        return Optional.empty();
    }

    public void delete(String refreshUUID) {
        String key = getUUIDKey(refreshUUID);
        redisTemplate.delete(key);
    }

    public Optional<String> findUUIDByUserId(String memberId) {
        String key = getIndexKey(memberId);
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(key));
    }

    public void deleteByUserId(String memberId) {
        String indexKey = getIndexKey(memberId);
        String uuid = stringRedisTemplate.opsForValue().get(indexKey);
        if (uuid != null) {
            redisTemplate.delete(getUUIDKey(uuid));
        }
        stringRedisTemplate.delete(indexKey);
    }

    private String getIndexKey(String memberId) {
        return "refresh:index:"+ memberId;
    }

    private String getUUIDKey(String refreshUUID) {
        return "refresh:uuid:" + refreshUUID;
    }
}
package com.common.util.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCommands;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
@Component
@Slf4j
public class RedisUtil {
    @SuppressWarnings("rawtypes")
    @Resource(name = "ownRedisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的key
     **/
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 删除单个key
     **/
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public void removeHashKey(final String hashName, final String ...keys) {
        if (exists(hashName)) {
            redisTemplate.opsForHash().delete(hashName, keys);
        }
    }

    /**
     * 判断key是否存在
     **/
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存中的String
     **/
    public String getString(final String key) {
        Object result = null;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if (result == null) {
            return null;
        }
        return result.toString();
    }

    /**
     * 将String类型写入缓存
     **/
    public boolean setString(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入一个带过期String
     * @param key           key
     * @param value         value
     * @param expireTime    过期时间。单位：秒
     * @return
     */
    public boolean setStringWithTimeout(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入一批数据到hashMap
     **/
    public boolean setHashMap(String mapName, String key, String value) {
        boolean result = false;
        try {

            redisTemplate.opsForHash().put(mapName, key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入一批数据到hashMap
     **/
    public boolean setHashMap(String key, Map<String, String> value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取hashMap中的数据
     **/
    public Map<String, String> getHashMap(String key) {
        Map<String, String> result = null;
        try {
            result = redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取hashMap中的数据
     **/
    public String getHashMapValueByKey(String mapName, String key) {
        String result = null;
        try {
            Map<String, String> map = redisTemplate.opsForHash().entries(mapName);
            if (map != null) {
                return map.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean lock(String key, String value) {

        RedisCallback<Long> callback = (connection) -> {
            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            return commands.setnx(key, value);
        };
        return (Long) redisTemplate.execute(callback) == 1;
    }


    /**
     * redis自动生成自增ID
     *
     * @param name redis中map名称
     * @param hash redis map中键的名称
     * @return
     */
    public Long generateId(String name, String hash) {
        long id = redisTemplate.opsForHash().increment(name, hash, 1L);
        return id;
    }

    /**
     * set集合添值
     **/
    public void setHashSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 查看value在set中是否存在
     **/
    public boolean existInHashSet(String key, String value) {
        log.info(redisTemplate.opsForSet().members(key).toString());
        return redisTemplate.opsForSet().isMember(key, value);
    }
}
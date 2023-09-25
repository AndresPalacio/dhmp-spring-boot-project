package com.zznode.dhmp.boot.autoconfigure.web.internal;

import com.zznode.dhmp.core.utils.AesUtil;
import com.zznode.dhmp.web.client.InternalTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Objects;

/**
 * 默认实现
 *
 * @author 王俊
 * @date create in 2023/8/25
 */
public class DefaultInternalTokenManager implements InternalTokenManager {

    private static final String TOKEN_SPLITTER = ":";
    private static final int TOKEN_PART = 2;

    private final Logger logger  = LoggerFactory.getLogger(DefaultInternalTokenManager.class);
    private final DhmpWebInternalProperties properties;


    public DefaultInternalTokenManager(DhmpWebInternalProperties properties) {
        this.properties = properties;
    }

    @Override
    public String generateToken() {
        // token内容
        String internalTokenValue = properties.getInternalToken();

        // 过期时间
        String expires = String.valueOf(System.currentTimeMillis() + properties.getTokenAliveTime().toMillis());

        return AesUtil.encryptData(internalTokenValue + TOKEN_SPLITTER + expires);
    }

    @Override
    public boolean validate(String token) {
        String decryptToken = AesUtil.decryptData(token);
        String[] split = decryptToken.split(TOKEN_SPLITTER);
        if (split.length != TOKEN_PART) {
            logger.error("Invalid token.");
            return false;
        }
        String tokenValue = split[0];
        if (!Objects.equals(tokenValue, properties.getInternalToken())) {
            // 此处可能出现 在发起远程调用请求之后配置中心修改了token配置，导致获取到的token校验失败, 概率较低, 不想解决
            logger.error("token value is error.");
            return false;
        }
        long expireTime;
        try {
            expireTime = Long.parseLong(split[1]);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }

        Instant now = Instant.now();
        Instant instant = Instant.ofEpochMilli(expireTime);
        // 过期返回false
        return now.isBefore(instant);
    }
}

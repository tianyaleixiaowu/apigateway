package com.maimeng.apigateway.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author wuweifeng
 */
@Component
public class KeyConfiguration {
    @Value("${jwt.rsa-secret}")
    private String userSecret;
    @Value("${client.rsa-secret}")
    private String serviceSecret;
    private byte[] userPubKey;
    private byte[] userPriKey;
    private byte[] servicePriKey;
    private byte[] servicePubKey;

    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException {
        Map<String, byte[]> keyMap = RsaKeyHelper.generateKey(userSecret);
        setUserPriKey(keyMap.get("pri"));
        setUserPubKey(keyMap.get("pub"));
        keyMap = RsaKeyHelper.generateKey(serviceSecret);
        setServicePriKey(keyMap.get("pri"));
        setServicePubKey(keyMap.get("pub"));
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public String getServiceSecret() {
        return serviceSecret;
    }

    public void setServiceSecret(String serviceSecret) {
        this.serviceSecret = serviceSecret;
    }

    public byte[] getUserPubKey() {
        return userPubKey;
    }

    public void setUserPubKey(byte[] userPubKey) {
        this.userPubKey = userPubKey;
    }

    public byte[] getUserPriKey() {
        return userPriKey;
    }

    public void setUserPriKey(byte[] userPriKey) {
        this.userPriKey = userPriKey;
    }

    public byte[] getServicePriKey() {
        return servicePriKey;
    }

    public void setServicePriKey(byte[] servicePriKey) {
        this.servicePriKey = servicePriKey;
    }

    public byte[] getServicePubKey() {
        return servicePubKey;
    }

    public void setServicePubKey(byte[] servicePubKey) {
        this.servicePubKey = servicePubKey;
    }
}

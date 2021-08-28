package com.heytusar.mongocbg.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.heytusar.mongocbg.model.User;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    
    @Autowired
    private Environment environment;

    private Logger log =  LoggerFactory.getLogger(JwtService.class);

    private static final String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    private String makePayload(User user) {
        JSONObject payload = new JSONObject();
        payload.put("sub", user.getId());
        payload.put("aud", "cbgui");
        payload.put("exp", LocalDateTime.now().plusMinutes(60));
        return payload.toString();
    }

    private String readSecret() {
        String secret = environment.getProperty("client.secret");
        return secret;
    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hmacSha256(String data, String secret) {
        try {
    
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
    
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);
    
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    
            return encode(signedBytes);
        } 
        catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    public Map makeJwt(User user) {
        String encodedHeader = encode(header.getBytes());
        String encodedPayload = encode(makePayload(user).getBytes());
        String secret = readSecret();

        String signature = hmacSha256(encodedHeader + "." + encodedPayload, secret);
        String part1 = encodedHeader + "." + encodedPayload;
        String part2 = signature;

        log.info("part1 ----> " + part1);
        log.info("part2 ----> " + part2);

        Map result = new HashMap<String, String>();
        result.put("part1", part1);
        result.put("part2", part2);
        return result;
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public Map verifyAuth(String bearer) {
        Map result = new HashMap<>();
        String[] parts = bearer.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        log.info("payload ---------------------> " + payload.toString());
        //Boolean isExpired = payload.getLong("exp") > (System.currentTimeMillis() / 1000);

        String encodedHeader = encode(header.getBytes());
        String encodedPayload = parts[1];
        String secret = readSecret();

        String signature = hmacSha256(encodedHeader + "." + encodedPayload, secret);

        Boolean isValid = signature.equals(hmacSha256(parts[0] + "." + parts[1], secret));
        log.info("isValid ---------------------> " + isValid);
        result.put("isValid", isValid);
        
        if(isValid) {
            result.put("userId", payload.getString("sub"));
        }
        return result;

    }
}

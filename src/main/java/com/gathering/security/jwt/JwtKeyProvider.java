package com.gathering.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//@Component
public class JwtKeyProvider {
//    private final PublicKey publicKey;
//    public JwtKeyProvider(@Value("${keycloak.publicKey}") String publicKeyPem) throws Exception {
//        this.publicKey = loadPublicKey(publicKeyPem);
//    }
//
//    private PublicKey loadPublicKey(String publicKeyPem) throws Exception {
//        // PEM 형식에서 "-----BEGIN PUBLIC KEY-----" 부분 제거
//        String publicKeyPEM = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----", "")
//                .replace("-----END PUBLIC KEY-----", "")
//                .replaceAll("\\s", "");
//
//        // Base64 디코딩 후 PublicKey 객체 생성
//        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePublic(keySpec);
//    }
//
//    public PublicKey getPublicKey() {
//        return publicKey;
//    }
}

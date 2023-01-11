package com.example.vibecap_back.global.config.security;

import com.example.vibecap_back.global.config.security.Secret;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public String encrypt(String pwd) {
        String result ="";

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // pwd와 salt를 합친 문자열에 SHA-256 적용
            md.update((pwd+ Secret.SALT).getBytes());
            byte[] pwdsalt = md.digest();

            // byte를 10진수 문자열로 변경
            StringBuffer sb = new StringBuffer();
            for (byte b: pwdsalt)
                sb.append(String.format("%02x", b));

            result = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }
}

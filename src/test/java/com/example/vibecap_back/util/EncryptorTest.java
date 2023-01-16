package com.example.vibecap_back.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EncryptorTest {

    @Test
    void salt_암호화_테스트() {
        // given
        String userPwd = "qwer1234";
        String encryptedPwd;
        Encryptor encryptor = new Encryptor();
        // when
        encryptedPwd = encryptor.encrypt(userPwd);
        // then
        Assertions.assertThat(encryptedPwd).isEqualTo(encryptor.encrypt("qwer1234"));
    }
}

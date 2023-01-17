package com.example.vibecap_back.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileWorkerTest {

    @Test
    void 파일_읽기_테스트() {
        byte[] data;

        try {
            data = FileWorker.loadFile("orange.jpeg");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
            Assertions.fail();
        }
    }
}

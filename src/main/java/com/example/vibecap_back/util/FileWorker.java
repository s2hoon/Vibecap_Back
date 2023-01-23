package com.example.vibecap_back.util;

import com.example.vibecap_back.domain.vibe.exception.EmptyImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FileWorker {

    // 프로젝트 root 디렉토리 경로
    private static String rootPath = System.getProperty("user.dir");
    // 이미지 파일이 저장될 디렉토리 경로
    private static final String DIR_PATH = rootPath + "/capturedImgs/";

    /**
     * 파일을 disk에 저장
     * @param file
     * @return
     * 파일이 저장된 disk상의 경로 반환
     * @throws IOException
     * @throws IllegalStateException
     */
    public static String saveFile(MultipartFile file) throws IOException, IllegalStateException {
        if (file.isEmpty())
            return null;
        // 이미지 파일의 원래 이름
        String originalName = file.getOriginalFilename();
        // 파일 식별자
        String uuid = UUID.randomUUID().toString();
        // 파일 확장자 추출
        String extension = originalName.substring(originalName.lastIndexOf("."));
        // 이미지 파일의 새로운 이름
        String savedName = uuid + extension;
        String savedPath = DIR_PATH + savedName;
        file.transferTo(new File(savedPath));
        return savedPath;
    }

    public static String saveFile(byte[] data) {
        // 바이트 배열로 표현된 데이터를 파일로 복원 (disk)에 저장
        return null;
    }

    /**
     * getPath(...) 경로 수정해야 함
     */
    public static byte[] loadFile(String fileName)
            throws InvalidPathException,
            IOException {
        Path path = FileSystems.getDefault()
                .getPath("/Users", "mingeun", "Vibecap", "Vibecap_Back", "assets", "sample-image", fileName);
        return Files.readAllBytes(path);
    }

}

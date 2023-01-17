package com.example.vibecap_back.util;

import com.example.vibecap_back.domain.vibe.exception.EmptyImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FileWorker {

    // 이미지 파일이 저장될 디렉토리 경로
    // TODO DB에 저장하도록 수정
    private static final String DIR_PATH = "/Users/mingeun/Vibecap/prototype01/capturedImgs/";

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

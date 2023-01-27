package com.example.vibecap_back.global.config.storage;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class FireBaseService implements ImageService {
    @Value("${firebase.bucket-name}")
    private String firebaseBucket;
    @Value("${firebase.image-url}")
    private String imageUrl;

    @Override
    public String uploadFiles(MultipartFile file) throws IOException, FileSaveErrorException {
        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            String name = generateFileName(file.getOriginalFilename()); // 랜덤으로 파일 이름 생성
            bucket.create(name, file.getBytes(), file.getContentType());

            return "https://firebasestorage.googleapis.com/v0/b/" + firebaseBucket + "/o/"
                    + name + "?alt=media";
        } catch (IOException e) {
            throw new FileSaveErrorException();
        }
//        String imgUrl = "https://firebasestorage.googleapis.com/v0/b/" + firebaseBucket + "/o/"
//                + fileName + "?alt=media";
    }

    @Override
    public String getImageUrl(String name) {
        return String.format(imageUrl, name);
    }

    @Override
    public String getFileName(String imgUrl) {
        int beginIndex = imgUrl.indexOf("/o/");
        int endIndex = imgUrl.indexOf("?alt=media");
        String fileName = imgUrl.substring(beginIndex + 3, endIndex);

        return fileName;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(originalFileName);

        bucket.create(name, bytes);

        return name;
    }

    @Override
    public void delete(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);

        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }

}
package com.example.vibecap_back.global.config.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class FireBaseService {
    @Value("${firebase.bucket-name}")
    private String firebaseBucket;

    public String uploadFiles(MultipartFile file, String fileName) throws IOException, FileSaveErrorException {
        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            InputStream content = new ByteArrayInputStream(file.getBytes());
            bucket.create(fileName, content, file.getContentType());
        } catch (IOException e) {
            throw new FileSaveErrorException();
        }
        String imgUrl = "https://firebasestorage.googleapis.com/v0/b/" + firebaseBucket + "/o/"
                + fileName + "?alt=media";

        return imgUrl;
    }

    public void deleteFiles(String fileName) throws FileSaveErrorException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        Blob blob = bucket.get(fileName);

        if (blob == null) {
            throw new FileSaveErrorException();
        }
        blob.delete();
    }

//    public String getFileName(String imgUrl) {

//        return fileName;
//    }

}
package com.example.vibecap_back.domain.vibe.application;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class ImageComparator {



    public byte[] fetchThumbnailBytes(String thumbnailUrl) throws IOException {
        URL url = new URL(thumbnailUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    public double compareImages(byte[] img1, byte[] img2) {
        Mat mat1 = Imgcodecs.imdecode(new MatOfByte(img1), Imgcodecs.IMREAD_GRAYSCALE);
        Mat mat2 = Imgcodecs.imdecode(new MatOfByte(img2), Imgcodecs.IMREAD_GRAYSCALE);

        if (mat1.empty() || mat2.empty()) {
            throw new IllegalArgumentException("Cannot compare images, one or both images are empty or invalid");
        }

        Mat hist1 = new Mat();
        Mat hist2 = new Mat();

        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        MatOfInt histSize = new MatOfInt(50);

        Imgproc.calcHist(Arrays.asList(mat1), new MatOfInt(0), new Mat(), hist1, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(mat2), new MatOfInt(0), new Mat(), hist2, histSize, ranges);

        double result = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);
        return result;
    }
}

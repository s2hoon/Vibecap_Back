package com.example.vibecap_back.domain.vibe.application;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class Imagecom {


    public double compareImages(byte[] imgBytes1, byte[] imgBytes2) {
        // Convert the byte arrays to OpenCV matrices
        Mat img1 = Imgcodecs.imdecode(new MatOfByte(imgBytes1), Imgcodecs.IMREAD_COLOR);
        Mat img2 = Imgcodecs.imdecode(new MatOfByte(imgBytes2), Imgcodecs.IMREAD_COLOR);

        // Convert the images to HSV color space
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2HSV);

        // Compute the histogram for each image
        Mat hist1 = new Mat();
        Mat hist2 = new Mat();
        MatOfInt histSize = new MatOfInt(180);
        MatOfFloat ranges = new MatOfFloat(0f, 180f);
        Imgproc.calcHist(Arrays.asList(img1), new MatOfInt(0), new Mat(), hist1, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(img2), new MatOfInt(0), new Mat(), hist2, histSize, ranges);

        // Normalize the histograms
        Core.normalize(hist1, hist1);
        Core.normalize(hist2, hist2);

        // Compare the histograms
        double diff = Imgproc.compareHist(hist1, hist2, Imgproc.CV_COMP_CORREL);
        return diff;
    }
}

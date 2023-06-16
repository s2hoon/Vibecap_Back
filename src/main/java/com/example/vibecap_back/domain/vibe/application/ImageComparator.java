package com.example.vibecap_back.domain.vibe.application;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
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

    public double compareImages(byte[] img1, byte[] img2) throws IOException {
        BufferedImage bufferedImage1 = ImageIO.read(new ByteArrayInputStream(img1));
        BufferedImage bufferedImage2 = ImageIO.read(new ByteArrayInputStream(img2));

        if (bufferedImage1 == null || bufferedImage2 == null) {
            throw new IllegalArgumentException("Cannot compare images, one or both images are empty or invalid");
        }

        int width1 = bufferedImage1.getWidth();
        int height1 = bufferedImage1.getHeight();

        int width2 = bufferedImage2.getWidth();
        int height2 = bufferedImage2.getHeight();

        // Resize images to the same dimensions if they have different sizes
        if (width1 != width2 || height1 != height2) {
            int maxWidth = Math.max(width1, width2);
            int maxHeight = Math.max(height1, height2);

            bufferedImage1 = resizeImage(bufferedImage1, maxWidth, maxHeight);
            bufferedImage2 = resizeImage(bufferedImage2, maxWidth, maxHeight);
        }

        double difference = 0;

        for (int y = 0; y < bufferedImage1.getHeight(); y++) {
            for (int x = 0; x < bufferedImage1.getWidth(); x++) {
                int rgb1 = bufferedImage1.getRGB(x, y);
                int rgb2 = bufferedImage2.getRGB(x, y);

                int red1 = (rgb1 >> 16) & 0xFF;
                int green1 = (rgb1 >> 8) & 0xFF;
                int blue1 = rgb1 & 0xFF;

                int red2 = (rgb2 >> 16) & 0xFF;
                int green2 = (rgb2 >> 8) & 0xFF;
                int blue2 = rgb2 & 0xFF;

                double pixelDifference = Math.sqrt((red1 - red2) * (red1 - red2)
                        + (green1 - green2) * (green1 - green2)
                        + (blue1 - blue2) * (blue1 - blue2));

                difference += pixelDifference;
            }
        }

        double totalPixels = bufferedImage1.getWidth() * bufferedImage1.getHeight();
        double averageDifference = difference / totalPixels;
        return averageDifference;
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }
}

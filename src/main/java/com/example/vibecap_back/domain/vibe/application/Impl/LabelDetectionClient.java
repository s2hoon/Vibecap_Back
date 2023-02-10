package com.example.vibecap_back.domain.vibe.application.Impl;

import com.example.vibecap_back.domain.vibe.application.ImageAnalyzer;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class LabelDetectionClient implements ImageAnalyzer {

    Logger LOGGER = LoggerFactory.getLogger(ImageAnalyzer.class);

    /**
     * 바이트 배열 형태의 이미지에 대한 label을 문자열 배열 형태로 반환
     * @param data
     * 이미지 파일
     * @return
     * 이미지에 대한 label 배열
     */
    @Override
    public List<String> detectLabels(byte[] data) throws ExternalApiException {

        // Image 준비
        ByteString imageBytes = ByteString.copyFrom(data);
        Image image = Image.newBuilder().setContent(imageBytes).build();
        // Image annotation request 생성
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(image)
                .build();
        requests.add(request);
        /**
         * Request를 전송할 클라이언트 초기화, 통신
         * 한 개 이상의 request를 전송할 수 있다.
         * Google cloud server와 통신이 끝나면 close()를 호출하여 자원을 회수한다.
         */
        // 이미지에서 감지된 label 배열 (index가 작은 원소가 score가 가장 높다)
        List<String> labels = new ArrayList<>();
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError())
                    throw new Exception(res.getError().getMessage());
                // 10개의 label 저장
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields()
                            .forEach((k, v) -> {
                                 if (k.toString().contains("description"))
                                     labels.add(v.toString());
                                   // System.out.format("%s : %s\n", k, v.toString());
                            });
                }
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new ExternalApiException();
        }

        return labels;
    }

    /**
     * 해당 이미지와 유사한 이미지를 웹에서 찾아 이미지를 잘 설명하는 label을 결정한다.
     */
    @Override
    public String detectLabelsByWebReference(byte[] data) throws ExternalApiException {

        // 이미지 준비
        ByteString imageBytes = ByteString.copyFrom(data);
        Image image = Image.newBuilder().setContent(imageBytes).build();
        // Request 생성
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Feature feat = Feature.newBuilder().setType(Feature.Type.WEB_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(image)
                .build();
        requests.add(request);
        // Client 초기화, 요청 전송
        List<String> labels = new ArrayList<>();
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError())
                    throw new Exception(res.getError().getMessage());

                // web에서 어떻게 사용되었는지에 대한 정보(링크)는 사용하지 않는다.
                WebDetection annotation = res.getWebDetection();
                for (WebDetection.WebLabel label : annotation.getBestGuessLabelsList()) {
                    labels.add(label.getLabel());
                }

                  // verbose(res.getWebDetection());

            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            throw new ExternalApiException();
        }

        // Web entities를 바탕으로 추측한 label

        // return trimLabel(labels.get(0));
        LOGGER.warn("[VIBE] 이미지에서 추출한 label: " + labels.get(0));
        return labels.get(0);
    }

    /**
     * 특수문자 (부연 설명 제거)
     * ex) "songpa naru park (seokchon lake park)" 에서 "(seokchon lake park)" 제거
     * alphabet으로만 이루어진 문자열 반환
     */
    private String trimLabel(String rawLabel) {

        return null;
    }

    private void verbose(WebDetection annotation) {

        System.out.println("Entity:Id:Score");
        System.out.println("===============");
        for (WebDetection.WebEntity entity : annotation.getWebEntitiesList()) {
            System.out.println(
                    entity.getDescription() + " : " + entity.getEntityId() + " : " + entity.getScore());
        }
        for (WebDetection.WebLabel label : annotation.getBestGuessLabelsList()) {
            System.out.format("%nBest guess label: %s", label.getLabel());
        }
        System.out.println("%nPages with matching images: Score%n==");
        for (WebDetection.WebPage page : annotation.getPagesWithMatchingImagesList()) {
            System.out.println(page.getUrl() + " : " + page.getScore());
        }
        System.out.println("%nPages with partially matching images: Score%n==");
        for (WebDetection.WebImage webImage : annotation.getPartialMatchingImagesList()) {
            System.out.println(webImage.getUrl() + " : " + webImage.getScore());
        }
        System.out.println("%nPages with fully matching images: Score%n==");
        for (WebDetection.WebImage webImage : annotation.getFullMatchingImagesList()) {
            System.out.println(webImage.getUrl() + " : " + webImage.getScore());
        }
        System.out.println("%nPages with visually similar images: Score%n==");
        for (WebDetection.WebImage webImage : annotation.getVisuallySimilarImagesList()) {
            System.out.println(webImage.getUrl() + " : " + webImage.getScore());
        }
    }
}

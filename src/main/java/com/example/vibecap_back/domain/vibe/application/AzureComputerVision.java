package com.example.vibecap_back.domain.vibe.application;

import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
public class AzureComputerVision {

    // Set your Azure Cognitive Services subscription key and endpoint
    String subscriptionKey = "d47f3ab729544eb3a70bba03ba1d264f";
    String endpoint = "https://vibecap.cognitiveservices.azure.com/";

    public String getResponse(byte[] image) {
        try {
            // Create an authenticated instance of the Computer Vision API client
            ComputerVisionClient client = ComputerVisionManager.authenticate(subscriptionKey).withEndpoint(endpoint);


            // Specify the visual features to analyze
            List<VisualFeatureTypes> visualFeatures = Arrays.asList(VisualFeatureTypes.DESCRIPTION);

            // Analyze the image using the Computer Vision API
            ImageAnalysis analysis = client.computerVision().analyzeImageInStream().withImage(image)
                    .withVisualFeatures(visualFeatures).execute();

            // Get the image description
            ImageDescriptionDetails description = analysis.description();
            if (description != null && description.captions() != null && !description.captions().isEmpty()) {
                // Get the first caption
                ImageCaption caption = description.captions().get(0);
                LOGGER.warn(caption.text());
                System.out.println("Image Description: " + caption.text());
                return caption.text();

            } else {
                System.out.println("No description found for the image.");
                return "";
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        };
        return "";
    }


}
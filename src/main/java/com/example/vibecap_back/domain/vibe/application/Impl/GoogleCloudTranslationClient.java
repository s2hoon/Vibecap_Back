package com.example.vibecap_back.domain.vibe.application.Impl;

import com.example.vibecap_back.domain.vibe.application.TextTranslator;
import com.example.vibecap_back.domain.vibe.exception.ExternalApiException;
import com.google.cloud.translate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * reference
 * https://cloud.google.com/translate/docs/reference/libraries/v2/java
 * simple example code using client library
 * https://github.com/googleapis/google-cloud-java/blob/main/google-cloud-examples/src/main/java/com/google/cloud/examples/translate/snippets/DetectLanguageAndTranslate.java
 * simple example code using REST API
 * https://cloud.google.com/translate/docs/basic/translating-text#translating_text
 */
public class GoogleCloudTranslationClient implements TextTranslator {

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudTranslationClient.class);

    @Override
    public String translate(String foreignString) throws ExternalApiException {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        Detection detection = translate.detect(foreignString);
        String detectedLanguage = detection.getLanguage();

        if (detectedLanguage.equals("ko"))
            return foreignString;

        try {
            Translation translation = translate.translate(
                    foreignString,
                    Translate.TranslateOption.sourceLanguage(detectedLanguage),
                    Translate.TranslateOption.targetLanguage("ko"),
                    Translate.TranslateOption.model("base"));

            return translation.getTranslatedText();
        } catch (TranslateException e) {
            LOGGER.warn(e.getMessage());
            throw new ExternalApiException();
        }
    }
}

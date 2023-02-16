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
        /**
         * heuristic: 두 단어 이상일 경우 고유명사일 확률이 높기 때문에 해석하면 의미가 깨지게 된다.
         * 영어를 그대로 사용
         */
        else if (foreignString.split(" ").length > 1) {
            return foreignString;
        }

        try {
            Translation translation = translate.translate(
                    foreignString,
                    Translate.TranslateOption.sourceLanguage(detectedLanguage),
                    Translate.TranslateOption.targetLanguage("ko"),
                    Translate.TranslateOption.model("base"));

            LOGGER.warn("[VIBE] 번역 결과: " + translation.getTranslatedText());
            return translation.getTranslatedText();
        } catch (TranslateException e) {
            LOGGER.warn(e.getMessage());
            throw new ExternalApiException();
        }
    }
}

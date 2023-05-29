package com.example.vibecap_back.domain.vibe.application;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OpenAiChat {



    private final OkHttpClient client;

    public OpenAiChat() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public String chat(String message) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"user\",\"content\":\"" + message + "\"}]}");
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + "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")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return parseResponse(responseBody);
            } else {
                throw new IOException("Unexpected HTTP status code: " + response.code());
            }
        }
    }

    private String parseResponse(String responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody);
            JSONArray choices = json.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            String text = message.getString("content");
            return text.trim();
        } catch (JSONException e) {
            throw new RuntimeException("Failed to parse OpenAI API response", e);
        }
    }

}

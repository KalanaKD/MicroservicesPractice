package com.kd.aiservices.service;

import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${spring.gemini.api.url}")
    private String geminiApiUrl;

    @Value("${spring.gemini.api.key}")
    private String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of
                ("contents", new Object[]{                      //content has an object array
                        Map.of("parts", new Object[]{           //parts has an object array
                            Map.of("text", question)}),          //then the text has the question/prompt
                });
        String response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)   // ✅ API key as query param
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }
}

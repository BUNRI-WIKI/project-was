package com.example.demo.bounded_context.solution.service;

import com.example.demo.bounded_context.solution.dto.DetectRequest;
import com.example.demo.bounded_context.solution.dto.DetectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetectService {

    @Value("${server.detect-api}")
    private String pythonServerUrl;

    public DetectResponse imageDetection(DetectRequest request) {

        WebClient webClient = WebClient.builder()
                .baseUrl(pythonServerUrl)
                .build();

        return webClient.method(HttpMethod.GET)
                .uri("/models/image-detection")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    throw new RuntimeException("4xx");
                })
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    throw new RuntimeException("5xx");
                })
                .bodyToMono(DetectResponse.class)
                .block();
    }
}

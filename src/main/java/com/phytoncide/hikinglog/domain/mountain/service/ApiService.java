package com.phytoncide.hikinglog.domain.mountain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<String> getPosts() {
        return webClientBuilder.build()
                .get()
                .uri("http://apis.data.go.kr/1400000/service/cultureInfoService2")
                .retrieve()
                .bodyToMono(String.class);
    }

}

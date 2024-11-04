package iasi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import iasi.model.OpenAIResponse;

import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    private final String apiKey;
    private final WebClient webClient;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public ChatbotService(
            @Value("${openai.api-key}") String apiKey,
            WebClient.Builder webClientBuilder) {
        this.apiKey = apiKey;
        this.webClient = webClientBuilder
                .baseUrl(API_URL)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> obterResposta(String mensagemUsuario) {
        var requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", mensagemUsuario
                        )
                ),
                "max_tokens", 150
        );

        return webClient
                .post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(OpenAIResponse.class)
                .map(response -> response.getChoices().get(0).getMessage().getContent())
                .onErrorResume(WebClientResponseException.TooManyRequests.class,
                        error -> Mono.just("Desculpe, o sistema está sobrecarregado no momento. Por favor, tente novamente em alguns segundos."))
                .onErrorResume(Exception.class,
                        error -> Mono.just("Desculpe, ocorreu um erro ao processar sua mensagem. Por favor, tente novamente."));
    }
}
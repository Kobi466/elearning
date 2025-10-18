package com.kobi.aiservice.controller;

import com.kobi.aiservice.dto.ChatRequest;
import com.kobi.aiservice.dto.Exercise;
import com.kobi.aiservice.dto.Quiz;
import com.kobi.aiservice.service.AIChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AIChatController {
    AIChatService aiChatService;
    @PostMapping("/chat-exercise")
    List<Exercise> chatExercise(@RequestBody ChatRequest chatRequest) {
        return  aiChatService.chatExercise(chatRequest);
    }
    @PostMapping("/chat-stream")
    Flux<String> chatStream(@RequestBody ChatRequest chatRequest) {
        return  aiChatService.chat(chatRequest);
    }
    @PostMapping("/chat-quiz")
    Quiz chatQuiz(@RequestBody ChatRequest chatRequest) {
        return  aiChatService.generateQuiz(chatRequest);
    }
}

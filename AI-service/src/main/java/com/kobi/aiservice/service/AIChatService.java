package com.kobi.aiservice.service;

import com.kobi.aiservice.dto.ChatRequest;
import com.kobi.aiservice.dto.Exercise;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.lang.reflect.Type;
import java.util.List;

//@RequiredArgsConstructor
@Service
//@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AIChatService {

    private final ChatClient chatClient;

    public AIChatService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public List<Exercise> chatExercise(ChatRequest chatRequest) {
        SystemMessage systemMessage = SystemMessage.builder()
                .text("""
                         Bạn là một trợ lý AI thông minh và là người bạn của tôi và website Bi Learning
                        Bạn sẽ trả lời các câu hỏi của tôi một cách chính xác và nhanh chóng phong cách thân thiện.
                        
                        """)
                .build();
        UserMessage userMessage = UserMessage.builder().
                text(chatRequest.getMessage()).
                build();
        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .build();
        return chatClient.prompt(prompt).call().entity(
                new ParameterizedTypeReference<List<Exercise>>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                }
        );
    }
}

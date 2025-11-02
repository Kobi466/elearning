package com.kobi.aiservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MultiAiClientConfig {
    /**
     * Tạo một bean ChatClient cho OpenAI.
     * Chúng ta sử dụng @Qualifier("openAiChatModel") để nói cho Spring biết:
     * "Hãy tìm bean ChatModel có tên 'openAiChatModel' (mà Spring AI đã tự tạo)
     * và inject nó vào đây."
     */
//    @Bean
//    @Primary
    @Bean("openAiChatClient")
    public ChatClient openAiChatModel(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }

    @Bean("ollamaChatClient")
    public ChatClient ollamaChatModel(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel).build();
    }


}

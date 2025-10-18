package com.kobi.aiservice.service;

import com.kobi.aiservice.dto.ChatRequest;
import com.kobi.aiservice.dto.Exercise;
import com.kobi.aiservice.dto.Quiz;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AIChatService {
    ChatClient chatClient;

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
    public Flux<String> chat(ChatRequest request){
        return this.chatClient.prompt(request.getMessage()).stream().content();
    }

    public Quiz generateQuiz(ChatRequest request) {
        // 1. Tạo một Output Converter
        // Chúng ta nói cho nó biết chúng ta muốn kết quả cuối cùng là một đối tượng Quiz
        var converter = new BeanOutputConverter<>(Quiz.class);
        // 2. Tạo một Prompt Template mới, yêu cầu AI trả lời theo format của converter
        String templateString = """
            Dựa vào nội dung được cung cấp, hãy tạo ra một câu hỏi trắc nghiệm duy nhất.
            Hãy tuân thủ nghiêm ngặt định dạng đầu ra được yêu cầu dưới đây.

            NỘI DUNG:
            {context}

            ĐỊNH DẠNG ĐẦU RA:
            {format}
            """;
        // 3. Điền thông tin vào mẫu
        // .getFormat() sẽ tự động tạo ra một mô tả cấu trúc JSON cho AI
        PromptTemplate promptTemplate = new PromptTemplate(templateString);
        Prompt prompt = promptTemplate.create(Map.of(
                "context", request.getMessage()
                , "format", converter.getFormat()
        ));
        return this.chatClient.prompt(prompt).call().entity(converter);
    }
}

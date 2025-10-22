# 🤖 AI Service - Kobi E-learning Platform

## 📋 Tổng quan

AI Service là "bộ não thông minh" của nền tảng e-learning Kobi, cung cấp các tính năng AI/ML để nâng cao trải nghiệm học tập, tự động hóa quy trình và cá nhân hóa nội dung cho từng học viên.

**Công nghệ:** Spring Boot 3.x + Spring AI Framework  
**Port:** 8090  
**Context Path:** `/ai`  
**AI Models:** Gemini 2.5 Flash (OpenAI-compatible), Ollama (Local LLM)

---

## 🎯 Mục tiêu & Vai trò trong Microservices

### Vai trò trong hệ sinh thái
- **Tích hợp đa mô hình AI:** Hỗ trợ cả cloud-based (Gemini) và local models (Ollama)
- **Cung cấp API thông minh:** Expose REST APIs cho các service khác sử dụng
- **Xử lý bất đồng bộ:** Lắng nghe Kafka events để xử lý các tác vụ AI nặng
- **Tối ưu chi phí:** Sử dụng local models cho các tác vụ đơn giản, cloud models cho tác vụ phức tạp

### Tích hợp với các service khác
```
Course Service → AI Service: Tạo nội dung khóa học, quiz, bài tập
Progress Service → AI Service: Phân tích tiến độ, đề xuất lộ trình
Review Service → AI Service: Phân tích sentiment, tóm tắt đánh giá
Enrollment Service → AI Service: Gợi ý khóa học phù hợp
```

---

## 🚀 Các tính năng chính (Đã & Sẽ triển khai)

### ✅ Phase 1: Core AI Chat (Đã triển khai)

#### 1. **Multi-Model Chat System**
- ✅ Tích hợp Gemini 2.5 Flash qua OpenAI-compatible API
- ✅ Tích hợp Ollama (Llama 3) cho local inference
- ✅ Streaming response với Reactive Programming (Flux)
- ✅ Cấu hình linh hoạt qua YAML

**Endpoints:**
```http
POST /ai/v1/chat-openai
POST /ai/v1/chat-ollama
POST /ai/v1/chat-exercise
```

#### 2. **Auto-Generate Exercise**
- ✅ Tự động tạo bài tập trắc nghiệm từ nội dung
- ✅ Structured output với Spring AI BeanOutputConverter
- ✅ Hỗ trợ format JSON chuẩn

**DTO Structure:**
```java
Exercise {
  question: String
  answerA, answerB, answerC, answerD: String
  correctAnswer: String
}
```

---

### 🔄 Phase 2: Content Generation & Analysis (Đang triển khai)

#### 3. **Smart Quiz Generator** 🎯
**Mục đích:** Tự động tạo bộ câu hỏi đa dạng từ nội dung bài giảng

**Tính năng:**
- Tạo quiz từ video transcript, PDF, text content
- Đa dạng loại câu hỏi: Multiple choice, True/False, Fill-in-blank
- Phân loại độ khó: Easy, Medium, Hard
- Giải thích đáp án chi tiết

**API Design:**
```java
POST /ai/v1/quiz/generate
Request: {
  "content": "Nội dung bài giảng...",
  "questionCount": 10,
  "difficulty": "MEDIUM",
  "language": "vi"
}
Response: {
  "quizzes": [Quiz],
  "metadata": { "generatedAt", "model", "confidence" }
}
```

**Integration:**
- Course Service gọi API này khi instructor upload nội dung mới
- Lưu quiz vào Course Service database
- Kafka event: `QuizGeneratedEvent` → Notification Service

---

#### 4. **Lecture Content Summarizer** 📝
**Mục đích:** Tóm tắt nội dung bài giảng dài thành các điểm chính

**Tính năng:**
- Tóm tắt video transcript, document
- Tạo bullet points chính
- Highlight key concepts
- Tạo flashcards tự động

**API Design:**
```java
POST /ai/v1/content/summarize
Request: {
  "content": "Full lecture content...",
  "summaryType": "BULLET_POINTS | PARAGRAPH | FLASHCARDS",
  "maxLength": 500
}
Response: {
  "summary": "...",
  "keyPoints": ["point1", "point2"],
  "flashcards": [{"front": "Q", "back": "A"}]
}
```

---

#### 5. **Assignment Auto-Grading** ✍️
**Mục đích:** Chấm điểm tự động bài tập tự luận, code assignment

**Tính năng:**
- Đánh giá bài tập văn bản (essay, short answer)
- Chấm code assignment (syntax, logic, best practices)
- Feedback chi tiết và constructive
- Phát hiện plagiarism cơ bản

**API Design:**
```java
POST /ai/v1/grading/evaluate
Request: {
  "assignmentType": "ESSAY | CODE | SHORT_ANSWER",
  "question": "...",
  "studentAnswer": "...",
  "rubric": { "criteria": [...] },
  "maxScore": 100
}
Response: {
  "score": 85,
  "feedback": "...",
  "strengths": ["..."],
  "improvements": ["..."],
  "plagiarismScore": 0.05
}
```

**Integration:**
- Course Service submit bài làm của học viên
- AI Service xử lý bất đồng bộ qua Kafka
- Kafka event: `AssignmentGradedEvent` → Progress Service update điểm

---

### 🎓 Phase 3: Personalization & Recommendation (Kế hoạch)

#### 6. **Personalized Learning Path** 🛤️
**Mục đích:** Tạo lộ trình học tập cá nhân hóa dựa trên profile và progress

**Tính năng:**
- Phân tích learning style (visual, auditory, kinesthetic)
- Đề xuất thứ tự học khóa học tối ưu
- Điều chỉnh độ khó dựa trên performance
- Dự đoán thời gian hoàn thành

**Data Sources:**
- Profile Service: User demographics, interests, goals
- Progress Service: Learning history, quiz scores, time spent
- Course Service: Course metadata, prerequisites

**API Design:**
```java
POST /ai/v1/learning-path/generate
Request: {
  "userId": "123",
  "targetSkills": ["Java", "Spring Boot"],
  "timeAvailable": "3 months",
  "currentLevel": "BEGINNER"
}
Response: {
  "learningPath": [
    {
      "courseId": "...",
      "order": 1,
      "estimatedDuration": "2 weeks",
      "reason": "Foundation course..."
    }
  ],
  "milestones": [...],
  "adaptiveRules": [...]
}
```

---

#### 7. **Smart Course Recommendation** 🎯
**Mục đích:** Gợi ý khóa học phù hợp với từng học viên

**Thuật toán:**
- **Collaborative Filtering:** "Học viên tương tự đã học gì"
- **Content-Based:** Dựa trên interests và skills
- **Hybrid Approach:** Kết hợp cả hai
- **Trending & Popular:** Khóa học hot, đánh giá cao

**API Design:**
```java
GET /ai/v1/recommendations/courses?userId={id}&limit=10
Response: {
  "recommendations": [
    {
      "courseId": "...",
      "score": 0.95,
      "reason": "Based on your interest in...",
      "type": "COLLABORATIVE | CONTENT_BASED | TRENDING"
    }
  ]
}
```

**Kafka Integration:**
- Listen: `CourseCompletedEvent`, `EnrollmentCreatedEvent`
- Update recommendation model real-time
- Publish: `RecommendationUpdatedEvent`

---

#### 8. **Intelligent Study Assistant (Chatbot)** 💬
**Mục đích:** Trợ lý AI 24/7 trả lời câu hỏi học viên

**Tính năng:**
- Trả lời câu hỏi về nội dung khóa học (RAG - Retrieval Augmented Generation)
- Giải thích khái niệm phức tạp
- Gợi ý tài liệu tham khảo
- Hỗ trợ debugging code
- Multi-turn conversation với context

**Architecture:**
```
User Question 
  → Vector Search (Pinecone/Qdrant) tìm relevant content
  → Combine với LLM prompt
  → Generate contextual answer
  → Store conversation history
```

**API Design:**
```java
POST /ai/v1/assistant/chat
Request: {
  "userId": "123",
  "courseId": "456",
  "message": "Giải thích Spring Bean lifecycle",
  "conversationId": "conv-789" // optional
}
Response: {
  "reply": "...",
  "sources": [{"lectureId": "...", "timestamp": "..."}],
  "conversationId": "conv-789",
  "suggestions": ["Có thể bạn muốn hỏi..."]
}
```

**Tech Stack:**
- **Vector DB:** Pinecone, Qdrant, hoặc Milvus
- **Embedding:** OpenAI Embeddings hoặc local models
- **LLM:** Gemini hoặc Ollama với RAG

---

### 📊 Phase 4: Analytics & Insights (Kế hoạch)

#### 9. **Learning Analytics Dashboard** 📈
**Mục đích:** Phân tích hành vi học tập, dự đoán rủi ro

**Tính năng:**
- **Dropout Prediction:** Dự đoán học viên có nguy cơ bỏ học
- **Performance Forecasting:** Dự đoán điểm số cuối khóa
- **Engagement Analysis:** Phân tích mức độ tương tác
- **Learning Pattern Detection:** Phát hiện pattern học tập

**API Design:**
```java
GET /ai/v1/analytics/student/{userId}/insights
Response: {
  "dropoutRisk": { "score": 0.25, "factors": [...] },
  "predictedGrade": 85,
  "engagementLevel": "HIGH",
  "recommendations": [
    "Increase practice exercises",
    "Review chapter 3"
  ]
}
```

---

#### 10. **Review Sentiment Analysis** 😊😐😞
**Mục đích:** Phân tích cảm xúc và chủ đề trong đánh giá khóa học

**Tính năng:**
- Sentiment classification (Positive, Neutral, Negative)
- Topic extraction (Content, Instructor, Platform)
- Trend analysis theo thời gian
- Auto-generate summary report

**API Design:**
```java
POST /ai/v1/analytics/reviews/analyze
Request: {
  "courseId": "123",
  "reviews": [{"text": "...", "rating": 5}]
}
Response: {
  "overallSentiment": "POSITIVE",
  "sentimentDistribution": {"positive": 0.7, "neutral": 0.2, "negative": 0.1},
  "topics": [
    {"topic": "Instructor", "sentiment": "POSITIVE", "mentions": 45}
  ],
  "summary": "Học viên đánh giá cao giảng viên..."
}
```

---

## 🏗️ Kiến trúc kỹ thuật

### Tech Stack
```yaml
Framework: Spring Boot 3.2.x
AI Framework: Spring AI 1.0.x
Language: Java 17+
Build Tool: Maven
AI Models:
  - Gemini 2.5 Flash (via OpenAI-compatible API)
  - Ollama (Llama 3, Mistral, etc.)
Message Broker: Kafka (cho async processing)
Vector DB: Pinecone / Qdrant (cho RAG)
Cache: Redis (cache AI responses)
```

### Configuration Structure
```yaml
spring:
  ai:
    openai:  # Gemini via OpenAI-compatible
      api-key: ${GEMINI_API_KEY}
      base-url: https://generativelanguage.googleapis.com
      model: gemini-2.5-flash
      temperature: 0.1  # Deterministic for grading
    
    ollama:  # Local models
      base-url: http://localhost:11434
      model: llama3:latest
      temperature: 0.7  # Creative for content generation
```

### Multi-Model Strategy
| Use Case | Model | Lý do |
|----------|-------|-------|
| Quiz Generation | Gemini | Cần độ chính xác cao, structured output |
| Content Summarization | Ollama | Xử lý nhanh, không cần internet |
| Grading | Gemini | Đánh giá phức tạp, cần reasoning |
| Chatbot | Gemini + RAG | Cần context window lớn |
| Recommendation | Custom ML | Latency thấp, cost-effective |

---

## 🔌 API Endpoints

### Current Endpoints (v1)
```http
# Chat với AI models
POST /ai/v1/chat-openai
POST /ai/v1/chat-ollama

# Tạo bài tập
POST /ai/v1/chat-exercise

# Health check
GET /ai/actuator/health
```

### Planned Endpoints (v2)
```http
# Content Generation
POST /ai/v2/quiz/generate
POST /ai/v2/content/summarize
POST /ai/v2/flashcards/create

# Grading & Evaluation
POST /ai/v2/grading/evaluate
POST /ai/v2/grading/feedback

# Personalization
POST /ai/v2/learning-path/generate
GET /ai/v2/recommendations/courses
GET /ai/v2/recommendations/next-lesson

# Study Assistant
POST /ai/v2/assistant/chat
GET /ai/v2/assistant/history/{conversationId}

# Analytics
GET /ai/v2/analytics/student/{userId}/insights
POST /ai/v2/analytics/reviews/analyze
GET /ai/v2/analytics/course/{courseId}/performance
```

---

## 📡 Kafka Integration

### Consumed Events (AI Service lắng nghe)
```java
// Khi có nội dung mới → Tự động tạo quiz
@KafkaListener(topics = "course.content.uploaded")
void onContentUploaded(ContentUploadedEvent event) {
  // Generate quiz asynchronously
  // Publish QuizGeneratedEvent
}

// Khi học viên submit bài → Chấm điểm
@KafkaListener(topics = "assignment.submitted")
void onAssignmentSubmitted(AssignmentSubmittedEvent event) {
  // Grade assignment
  // Publish AssignmentGradedEvent
}

// Khi hoàn thành khóa học → Update recommendation model
@KafkaListener(topics = "course.completed")
void onCourseCompleted(CourseCompletedEvent event) {
  // Update ML model
  // Generate new recommendations
}
```

### Published Events (AI Service phát ra)
```java
// Quiz đã được tạo
QuizGeneratedEvent {
  courseId, quizzes[], generatedAt
}

// Bài tập đã được chấm
AssignmentGradedEvent {
  assignmentId, userId, score, feedback
}

// Recommendation đã cập nhật
RecommendationUpdatedEvent {
  userId, recommendations[]
}

// Cảnh báo học viên có nguy cơ bỏ học
DropoutRiskDetectedEvent {
  userId, riskScore, factors[]
}
```

---

## 🗄️ Data Models

### Core DTOs
```java
// Request
ChatRequest { message: String }

// Response
Exercise {
  question, answerA, answerB, answerC, answerD, correctAnswer
}

Quiz {
  question, options: List<String>, correctAnswer
}

// New DTOs (Planned)
QuizGenerationRequest {
  content, questionCount, difficulty, language
}

GradingRequest {
  assignmentType, question, studentAnswer, rubric, maxScore
}

LearningPathRequest {
  userId, targetSkills, timeAvailable, currentLevel
}

RecommendationResponse {
  courseId, score, reason, type
}
```

---

## 🚀 Deployment & Scaling

### Docker Configuration
```dockerfile
FROM openjdk:17-slim
COPY target/ai-service.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables
```bash
# AI Models
GEMINI_API_KEY=your_key_here
OLLAMA_BASE_URL=http://ollama:11434

# Kafka
KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Database (nếu cần lưu conversation history)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ai_service

# Vector DB
PINECONE_API_KEY=your_key
PINECONE_ENVIRONMENT=us-east-1
```

### Scaling Strategy
- **Horizontal Scaling:** Multiple instances cho high traffic
- **Model Caching:** Cache frequent prompts trong Redis
- **Async Processing:** Xử lý heavy tasks qua Kafka
- **Rate Limiting:** Giới hạn requests để tránh cost explosion

---

## 🧪 Testing Strategy

### Unit Tests
```java
@Test
void testQuizGeneration() {
  // Mock AI client
  // Verify structured output
}

@Test
void testGradingLogic() {
  // Test scoring algorithm
  // Verify feedback quality
}
```

### Integration Tests
```java
@SpringBootTest
@AutoConfigureMockMvc
class AIChatControllerTest {
  @Test
  void testChatEndpoint() {
    // Test real API calls
  }
}
```

### Load Testing
- Sử dụng JMeter/Gatling
- Test concurrent requests
- Monitor response time & cost

---

## 📊 Monitoring & Observability

### Metrics to Track
```yaml
# Performance
- AI response time (p50, p95, p99)
- Token usage per request
- Cache hit rate

# Business
- Quiz generation success rate
- Grading accuracy (compared to human)
- Recommendation click-through rate

# Cost
- API cost per day/month
- Cost per user
- Model usage distribution
```

### Logging
```java
@Slf4j
public class AIChatService {
  public String chat(String message) {
    log.info("AI Request: model={}, tokens={}", model, tokens);
    // Process
    log.info("AI Response: latency={}ms, cost=${}", latency, cost);
  }
}
```

---

## 🔐 Security & Best Practices

### API Key Management
- ❌ KHÔNG hardcode API keys
- ✅ Dùng Environment Variables
- ✅ Rotate keys định kỳ
- ✅ Sử dụng Vault cho production

### Rate Limiting
```java
@RateLimiter(name = "aiService", fallbackMethod = "fallback")
public String chat(String message) {
  // AI call
}
```

### Input Validation
```java
@Valid
public record ChatRequest(
  @NotBlank @Size(max = 5000) String message
) {}
```

### Cost Control
- Set max tokens per request
- Implement usage quotas per user
- Monitor daily spending
- Fallback to cheaper models when possible

---

## 📚 Resources & Documentation

### Spring AI Documentation
- [Spring AI Reference](https://docs.spring.io/spring-ai/reference/)
- [OpenAI Integration](https://docs.spring.io/spring-ai/reference/api/clients/openai-chat.html)
- [Ollama Integration](https://docs.spring.io/spring-ai/reference/api/clients/ollama-chat.html)

### AI Model Documentation
- [Gemini API](https://ai.google.dev/docs)
- [Ollama Models](https://ollama.ai/library)

### Best Practices
- [Prompt Engineering Guide](https://www.promptingguide.ai/)
- [RAG Implementation](https://www.pinecone.io/learn/retrieval-augmented-generation/)

---

## 🛣️ Roadmap

### Q1 2025
- ✅ Multi-model chat system
- ✅ Basic quiz generation
- 🔄 Smart quiz generator với difficulty levels
- 🔄 Content summarizer

### Q2 2025
- 📋 Assignment auto-grading
- 📋 Study assistant chatbot (RAG)
- 📋 Vector database integration

### Q3 2025
- 📋 Personalized learning paths
- 📋 Course recommendation engine
- 📋 Learning analytics

### Q4 2025
- 📋 Advanced analytics (dropout prediction)
- 📋 Sentiment analysis
- 📋 Custom ML models training

---

## 👥 Team & Contact

**Maintainer:** Kobi Development Team  
**Service Owner:** AI Team  
**Slack Channel:** #ai-service  
**On-call:** PagerDuty rotation

---

## 📝 Notes

### Current Limitations
- Gemini API có rate limit (60 requests/minute)
- Ollama cần GPU để chạy nhanh
- Chưa có persistent conversation history
- Chưa implement caching

### Future Improvements
- Fine-tune custom models cho domain-specific tasks
- Implement A/B testing cho prompts
- Add multi-language support
- Optimize token usage để giảm cost

---

**Last Updated:** 2025-10-22  
**Version:** 1.0.0  
**Status:** 🟢 Active Development

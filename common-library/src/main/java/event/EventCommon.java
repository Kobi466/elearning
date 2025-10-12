package event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class EventCommon<T>{
    String eventId;
    String eventType;
    Instant timestamp;
    String payload;
}

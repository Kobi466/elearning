package event;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEvent {
    String channel;
    String recipient;
    String template;
    Map<String, Object> param;
}

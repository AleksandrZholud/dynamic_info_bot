package dynamic.chat.bot.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.util.List;

@Builder
public record Message(@Id String id, String type, String text,
                      List<Button> buttons, boolean autoDelete) {}

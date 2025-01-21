package dynamic.chat.bot.model;

import java.util.List;

public record Message(String id, String type, String text,
                      List<Button> buttons, boolean autoDelete) {}

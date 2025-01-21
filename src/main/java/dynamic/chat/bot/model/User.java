package dynamic.chat.bot.model;

import java.util.List;

public record User(String id, String chatId, List<String> historyIds, boolean isSuperUser) {}


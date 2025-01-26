package dynamic.chat.bot.telegrambot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.UpdatesListener;
import dynamic.chat.bot.exception.UserNotFoundException;
import dynamic.chat.bot.model.User;
import dynamic.chat.bot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.IOException;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
public class TelegramBotHandler {
    private final TelegramBot bot;
    private final UserRepository userRepository;

    public TelegramBotHandler(String botToken, UserRepository userRepository) {
        this.bot = new TelegramBot(botToken);
        this.userRepository = userRepository;


        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.callbackQuery() != null) {
                    handleCallback(update.callbackQuery());
                } else if (update.message() != null && update.message().text() != null) {
                    handleUpdate(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void sendNavigationButtons(String chatId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Начало").callbackData("start"),
                new InlineKeyboardButton("Назад").callbackData("back")
        );
        bot.execute(new SendMessage(chatId, "Navigation:").replyMarkup(markup));
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.message().chat().id().toString();
        String callbackData = callbackQuery.data();

        if ("start".equals(callbackData)) {
            sendMessage(chatId, "Welcome to the bot!");
        } else if ("back".equals(callbackData)) {
            String previousMessage = getPreviousMessageFromHistory(chatId);
            if (previousMessage != null) {
                sendMessage(chatId, previousMessage);
            } else {
                sendMessage(chatId, "No previous message found.");
            }
        } else if ("option_2".equals(callbackData)) {
                User user = userRepository.findByChatId(chatId);
                sendMessage(chatId, "Hello user with chat id " + chatId + "!");
        }
    }

    private String getPreviousMessageFromHistory(String chatId) {
        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase mongoDatabase = client.getDatabase("telegram_bot");

            MongoCollection<Document> users = mongoDatabase.getCollection("users");
            Document user = users.find(eq("chat_id", chatId)).first();

            if (user != null && user.containsKey("history")) {
                List<String> history = user.getList("history", String.class);
                if (!history.isEmpty()) {
                    return history.get(history.size() - 1); // Последнее сообщение
                }
            }
        }
        return null; // Если история пуста
    }

    private void deleteMessage(String chatId, int messageId) {
        bot.execute(new DeleteMessage(chatId, messageId));
    }

    private void handleUpdate(Update update) {
        String chatId = update.message().chat().id().toString();
        String text = update.message().text();

        if ("/start".equals(text)) {
            sendMessageWithButtons(chatId, "Welcome to the bot! Choose an option:",
                    new InlineKeyboardButton("Option 1").callbackData("option_1"),
                    new InlineKeyboardButton("Option 2").callbackData("option_2"));
        } else {
            sendMessage(chatId, "Please use the provided buttons.");
        }
    }

    private void sendMessage(String chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    private void sendMessageWithButtons(String chatId, String text, InlineKeyboardButton... buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(buttons);
        bot.execute(new SendMessage(chatId, text).replyMarkup(markup));
    }
}
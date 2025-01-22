package dynamic.chat.bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dynamic.chat.bot.model.Button;
import dynamic.chat.bot.model.Message;
import dynamic.chat.bot.model.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.List;

@Slf4j
public class MongoDBSetup {
    @SneakyThrows
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = client.getDatabase("telegram_bot");

            MongoCollection<Document> messages = db.getCollection("messages");

            Message message = getMessageDto();

            Document messageDoc = Document.parse(objectMapper.writeValueAsString(message));
            messages.insertOne(messageDoc);
            log.info("Test message inserted!");

            MongoCollection<Document> users = db.getCollection("users");

            User user = getUser();

            Document userDoc = Document.parse(objectMapper.writeValueAsString(user));
            users.insertOne(userDoc);
            log.info("Test user inserted!");
        }
    }

    // Added "Valerii" user to mongoDB
    private static User getUser() {
        return User.builder()
                .chatId("313744444")
                .historyIds(List.of("message1"))
                .isSuperUser(false)
                .build();
    }

    private static Message getMessageDto() {
        return Message.builder()
                .type("text")
                .text("Hello, this is a test message!")
                .buttons(List.of(new Button("goto_24", "24"),
                        new Button("Button 2", "message2")))
                .autoDelete(false)
                .build();
    }
}

package dynamic.chat.bot.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

public class MongoDBSetup {
    public static void main(String[] args) {
        try (MongoClient client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = client.getDatabase("telegram_bot");
            MongoCollection<Document> messages = db.getCollection("messages");

            Document testMessage = new Document("_id", "message1")
                    .append("type", "text")
                    .append("text", "Hello, this is a test message!")
                    .append("buttons", List.of(
                            new Document("buttonText", "goto_24").append("id", "24"),
                            new Document("buttonText", "Button 2").append("action", "message2")
                    ))
                    .append("autoDelete", true);

            messages.insertOne(testMessage);
            System.out.println("Test message inserted!");

            MongoCollection<Document> users = db.getCollection("users");

            Document testUser = new Document("_id", "user1")
                    .append("chatId", "123456789")
                    .append("historyIds", List.of("message1"))
                    .append("isSuperUser", false);

            users.insertOne(testUser);
            System.out.println("Test user inserted!");
        }
    }
}

package dynamic.chat.bot.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "telegram_bot";

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connection succeed!");
        } catch (Exception e) {
            log.error("Error connection: {}", e.getMessage());
        }
    }
}

package dynamic.chat.bot.telegrambot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dynamic.chat.bot.repository.UserRepository;

public class BotApplication {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "telegram_bot";
    private static final String USERS_COLLECTION = "users";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public BotApplication() {
        this.mongoClient = MongoClients.create(CONNECTION_STRING);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
    }

    public static void main(String[] args) {
        BotApplication botApplication = new BotApplication();

        String botToken = "5888271741:AAEAbyzloHIq4S67kkA4gp5Fz8jBV-Bc-AE";
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("telegram_bot");// Your bot token
        UserRepository userRepository = new UserRepository(database); // Inject the userCollection
        new TelegramBotHandler(botToken, userRepository);

        System.out.println("Bot is running...");
    }

}

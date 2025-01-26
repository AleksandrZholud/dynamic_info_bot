package dynamic.chat.bot.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dynamic.chat.bot.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import dynamic.chat.bot.model.User;

@Slf4j
public class UserRepository {
    private final MongoDatabase database;
    public UserRepository (MongoDatabase database) {
        this.database = database;
    }

   public User findByChatId(String chatId) {

       MongoCollection<Document> userCollection = database.getCollection("users");
       Document userDoc = userCollection.find(new Document("chatId", chatId)).first();
       if (userDoc == null) {
           log.error("User with chat id: [{}] not found", chatId);
           throw new UserNotFoundException("User with chat id: ["+chatId+"] not found");
       }

       return mapDocumentToUser(userDoc);
   }

    private User mapDocumentToUser(Document document) {
        return User.builder()
                .id(document.getObjectId("_id"))
                .chatId(document.getString("chatId"))
                .historyIds(document.getList("historyIds", String.class))
                .build();
    }

}

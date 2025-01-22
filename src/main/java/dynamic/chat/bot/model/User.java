package dynamic.chat.bot.model;


import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Builder
@Getter
@Document("users")
public class User {
    @Id
    private ObjectId id;
    private String chatId;
    private List<String> historyIds;
    private boolean isSuperUser;

}


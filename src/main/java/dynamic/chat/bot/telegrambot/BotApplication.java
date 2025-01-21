package dynamic.chat.bot.telegrambot;

public class BotApplication {
    public static void main(String[] args) {
        String botToken = "5888271741:AAEAbyzloHIq4S67kkA4gp5Fz8jBV-Bc-AE"; // Замените на ваш токен
        new TelegramBotHandler(botToken);
        System.out.println("Bot is running...");
    }
}

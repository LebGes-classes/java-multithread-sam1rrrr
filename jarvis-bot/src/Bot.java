import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import io.github.cdimascio.dotenv.Dotenv;

public class Bot {
    private static String BOT_TOKEN;
    private static String BOT_USERNAME;

    private static void getCredentials() {
        Dotenv dotenv = Dotenv.configure().directory("jarvis-bot/").load();
        BOT_TOKEN = dotenv.get("BOT_TOKEN");
        BOT_USERNAME = dotenv.get("BOT_USERNAME");
    }

    public static void start() {
        getCredentials();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new JarvisBot(BOT_TOKEN, BOT_USERNAME));
            System.out.println("Jarvis started!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
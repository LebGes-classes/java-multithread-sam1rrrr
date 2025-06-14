import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Reminder implements Runnable {
    private final JarvisBot bot;
    private final long chatId;
    private final int seconds;
    private final String text;

    public Reminder(JarvisBot bot, long chatId, int seconds, String text) {
        this.bot = bot;
        this.chatId = chatId;
        this.seconds = seconds;
        this.text = text;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(seconds * 1000L);
            bot.sendResponse(chatId, "Напоминание! Прошло " + seconds + " секунд\n\nТекст: " + text);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
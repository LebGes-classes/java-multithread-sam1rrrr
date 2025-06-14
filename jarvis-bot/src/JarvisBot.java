import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.regex.*;

public class JarvisBot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private final String BOT_USERNAME;
    private final ExecutorService executorService;

    public JarvisBot(String BOT_TOKEN, String BOT_USERNAME) {
        this.BOT_TOKEN = BOT_TOKEN;
        this.BOT_USERNAME = BOT_USERNAME;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        executorService.execute(() -> handleUpdate(update));
    }

    private void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();
            long chatId = message.getChatId();

            try {
                if (text.startsWith("/start")) {
                    sendResponse(chatId, "Приветствую! Jarvis к вашим услугам\n\nПример: /new <seconds> <text>");
                } else if (text.matches("^/new\\s+(\\d+)\\s+(.+)$")) {
                    Pattern pattern = Pattern.compile("^/new\\s+(\\d+)\\s+(.+)$");
                    Matcher matcher = pattern.matcher(text);

                    if (matcher.find()) {
                        int seconds = Integer.parseInt(matcher.group(1));
                        String reminderText = matcher.group(2);

                        scheduleReminder(chatId, seconds, reminderText);
                        sendResponse(chatId, "Хорошо, напомню через " + seconds + " секунд");
                    } else {
                        sendResponse(chatId, "Неправильный ввод, попробуйте снова\n\nПример: /new <seconds> <text>");
                    }
                } else {
                    sendResponse(chatId, "Неправильный ввод, попробуйте снова\n\nПример: /new <seconds> <text>");
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    sendResponse(chatId, "Я сломался");
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void scheduleReminder(long chatId, int seconds, String text) {
        Reminder task = new Reminder(this, chatId, seconds, text);
        executorService.execute(task);
    }

    public void sendResponse(long chatId, String text) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        execute(message);
    }
}
// supportive class for telegram bot
// (general class for telegram bot is MyTelegram bot)

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

class ForBot{
    public MyTelegramBot bot;
    static public ArrayList<Long> chatIdList;

    ForBot() {
        chatIdList = new ArrayList<Long>();

        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register bot
        try {
            bot = new MyTelegramBot();
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Bot Started!");
    }
}

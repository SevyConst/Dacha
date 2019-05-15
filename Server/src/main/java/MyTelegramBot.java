import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

class MyTelegramBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        // Check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String received = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            if (isRightPassword(chatId, received)) {
                if (received.equals("/turbo")) {
                    handleTurboWord();
                }
            }

        }
    }

    private boolean isRightPassword(long chatId, String received) {
        if (!ForBot.chatIdList.contains(chatId)) {
            if (received.equals(ForProperties.botPassword)) {
                ForBot.chatIdList.add(chatId);
                sendOneMessage(chatId, "ты добавлен!");
                return true;
            } else {
                // don't send anything
                return false;
            }
        } else {
            if (received.equals(ForProperties.botPassword)) {
                sendOneMessage(chatId, "ты уже был добавлен!");
            }
        }
        return true;
    }

    private void handleTurboWord() {
        if (MultiServerPi.turboMode) {
            MultiServerPi.turboMode = false;
            sendToAll("Turbo mode off");
        } else {

            // request for sending ping from socket-client frequently
            MultiServerPi.turboMode = true;

            // notify user
            sendToAll("Turbo mode on");
        }
    }

    public String getBotUsername() {
        // Return bot username
        return "MyTelegramBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return ForProperties.botToken;
    }

    public void sendToAll(String messageStr) {
        if (!ForBot.chatIdList.isEmpty()) {

            for (long chatId : ForBot.chatIdList) {
                sendOneMessage(chatId, messageStr);
            }

            System.out.println("Sent to telegram!");

        }
    }

    public void sendOneMessage(long chatId, String messageStr) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(messageStr);
        try {
            execute(message); // Send to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

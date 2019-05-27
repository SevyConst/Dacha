import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

class MyTelegramBot extends TelegramLongPollingBot {

    private static final String COMMAND_TURBO = "/turbo";
    private static final String COMMAND_REMOVE_ME = "/removeMe";
    private static final String COMMAND_DATE_LAST_CONNECT = "/last";
    private static final String COMMAND_NEXT_TO_LAST = "/nextToLast";

    private static final String patternDate = "HH:mm:ss dd/MM";
    private static final DateFormat dateFormat = new SimpleDateFormat(patternDate);

    public void onUpdateReceived(Update update) {

        // Check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String received = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            if (isRightPassword(chatId, received)) {
                if (received.equals(COMMAND_TURBO)) {
                    handleTurboWord();
                }
                switch (received) {
                    case COMMAND_REMOVE_ME: {
                        // TODO
                        if (removeMe(chatId)) {
                            sendOneMessage(
                                    chatId,
                                    "Ты удален!");
                        } else {
                            sendOneMessage(
                                    chatId,
                                    "Ошибка в процессе удаления");
                        }
                    } break;
                    case COMMAND_DATE_LAST_CONNECT:
                        sendOneMessage(chatId, dateFormat.format(CheckDate.dateLastConnect));
                        break;
                    case COMMAND_NEXT_TO_LAST:
                        sendOneMessage(chatId, "last: "
                                + dateFormat.format(CheckDate.dateLastConnect));
                        sendOneMessage(chatId, "next to last:" +
                                dateFormat.format(CheckDate.dateNextToLastConnect));
                        break;
                }
            }

        }
    }

    private boolean isRightPassword(long chatId, String received) {
        if (!ForChatIDs.chatIdList.contains(chatId)) {
            if (received.equals(ForProperties.botPassword)) {
                if (!ForChatIDs.addChatId(chatId)) {
                    sendOneMessage(chatId, "ошибка в добавлении");
                    return false;
                }
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

    // TODO
    private boolean removeMe(long chatId) {
        try {
            return ForChatIDs.chatIdList.remove(chatId);
        } catch (Exception e) {
            return false;
        }
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
        if (!ForChatIDs.chatIdList.isEmpty()) {

            for (long chatId : ForChatIDs.chatIdList) {
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

package mate.academy.carsharing.service.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Getter
public class MyLongPollingBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void sendResponse(String chatId, String responseText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(responseText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}

package ru.nsk.tgbot;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsk.tgbot.config.BotConfig;

import java.util.List;

@Slf4j
@Component

public class CounterTelegramBot extends TelegramLongPollingBot {

    final BotConfig config;


    public CounterTelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {

        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();
            switch (messageText){
                case "/start":
                    startBot(chatId, memberName);
                    break;
                default: log.info("Unexpected message");
            }
        }
    }

    private void startBot(Long chatId,String userName){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + userName + "! This is Benjamin Laynus.");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }




}

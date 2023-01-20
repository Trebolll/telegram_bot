package ru.nsk.tgbot;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nsk.tgbot.components.Buttons;
import ru.nsk.tgbot.config.BotConfig;
import ru.nsk.tgbot.database.User;
import ru.nsk.tgbot.database.UserRepository;

import static ru.nsk.tgbot.components.BotCommands.HELP_TEXT;

@Slf4j
@Component

public class CounterTelegramBot extends TelegramLongPollingBot {

    private UserRepository userRepository;

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
        long chatId = 0;
        long userId =0;
        String userName = null;
        String receivedMessage;

        if(update.hasMessage()){
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if(update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userId);
            }
            }else if(update.hasCallbackQuery()){
                chatId = update.getCallbackQuery().getMessage().getChatId();
                userId = update.getCallbackQuery().getFrom().getId();
                userName = update.getCallbackQuery().getFrom().getFirstName();
                receivedMessage = update.getCallbackQuery().getData();

                botAnswerUtils(receivedMessage,chatId,userId);
            }
        if (chatId == Long.parseLong(config.getChatId())){
            updateDB(userId, userName);
        }
            }

    private void botAnswerUtils(String receivedMessage, long chatId, long userName){
        switch (receivedMessage){
            case "/start":
                startBot(chatId,userName);
                break;
            case "/help":
                sendHelpText(chatId, HELP_TEXT);
                break;
            default:break;
        }
    }

    private void startBot(Long chatId, long userName){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello AlEX_BOREIUS_OFFICAL you id, " + userName + "! This is Benjamin Laynus.");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }

private void sendHelpText(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

    try {
        execute(message);
        log.info("Reply sent");

    } catch (TelegramApiException e) {
        log.error(e.getMessage());
    }
}

 private void updateDB(long userId, String userName){
        if(userRepository.findById(userId).isEmpty()){
            User user = new User();
            user.setId(userId);
            user.setName(userName);
            user.setMsg_numb(1);
            userRepository.save(user);
            log.info("Added to DB: " + user);
        }else{
            userRepository.updateMsgNumberByUserId(userId);
        }
 }

}

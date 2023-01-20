package ru.nsk.tgbot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {

    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info")
    );

    String HELP_TEXT = "Этот бот поможет подсчитать количество сообщений в чате. " +
            "Вам доступны следующие команды:\n\n" +
            "/start - старт бота\n" +
            "/help - помощь";
}

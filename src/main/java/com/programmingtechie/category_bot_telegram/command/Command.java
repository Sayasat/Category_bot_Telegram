package com.programmingtechie.category_bot_telegram.command;

public interface Command {

    /**
     * Выполнение команды, конкретная логика в реализации.
     */
    String execute(String text, Long chatId);
}

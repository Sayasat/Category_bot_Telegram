package com.programmingtechie.category_bot_telegram.config;

import com.programmingtechie.category_bot_telegram.bot.CategoryBot;
import com.programmingtechie.category_bot_telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final CommandHandler commandHandler;

    /**
     * Регистрация бота в Telegram API
     * @return CategoryBot
     */
    @Bean
    public CategoryBot categoryBot() {
        // Паттерн Factory Method: создание объекта без явного указания точного класса.
        CategoryBot categoryBot = new CategoryBot(botName, botToken, commandHandler);

        try {
            // Паттерн Singleton: TelegramBotsApi используется как синглтон для регистрации одного бота в Telegram API.
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(categoryBot);
        } catch (TelegramApiException e) {
            log.error("Ошибка регистрации бота: {}", e.getMessage());
        }

        // Возвращаем экземпляр CategoryBot, который зарегистрирован в Telegram API
        return categoryBot;
    }
}

package com.programmingtechie.category_bot_telegram.bot;

import com.programmingtechie.category_bot_telegram.command.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;

/**
 * Team Members:
 * - Sayasat Sabit, 230103164
 * - Madiyar Abiken, 230103016
 * - Amir Karimov, 230103345
 *
 *  Project: Category Tree
 */

@Slf4j
public class CategoryBot extends TelegramLongPollingBot {

    private final String botName;
    private final CommandHandler commandHandler;

    /**
     * Конструктор для инициализации CategoryBot.
     *
     * @param botName        имя бота
     * @param botToken       токен доступа к Telegram API
     * @param commandHandler обработчик команд
     */
    public CategoryBot(String botName, String botToken, CommandHandler commandHandler) {
        super(botToken);
        this.botName = botName;
        this.commandHandler = commandHandler;
    }

    /**
     * Метод для получения имени бота.
     */
    @Override
    public String getBotUsername() {
        return this.botName;
    }

    /**
     * Основной метод для обработки обновлений, полученных от Telegram.
     * Это паттерн Template Method, так как структура обработки сообщений задана абстрактным методом в классе TelegramLongPollingBot
     * @param update обновление, полученное от Telegram
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            try {
                // Обрабатываем сообщение
                processMessage(chatId, message);
            } catch (Exception e) {
                log.error("Ошибка при обработке сообщения: {}", e.getMessage(), e);
                sendMessage(chatId, "Произошла ошибка при обработке вашего запроса.");
            }
        }
    }

    /**
     * Метод для обработки текстовых сообщений и документов.
     * Этот метод использует паттерн Strategy для делегирования обработки сообщения в зависимости от его типа.
     */
    private void processMessage(Long chatId, Message message) {
        String responseMessage;

        // Проверка, содержит ли сообщение текст
        if (message.hasText()) {
            responseMessage = handleTextMessage(message, chatId);
        }
        // Проверка, содержит ли сообщение документ
        else if (message.hasDocument()) {
            responseMessage = handleDocumentMessage(message, chatId);
        }
        // Обработка других типов сообщений
        else {
            responseMessage = "Пожалуйста, отправьте команду или загрузите файл .xlsx.";
        }

        // Отправляем ответное сообщение пользователю
        sendMessage(chatId, responseMessage);
    }

    /**
     * Метод для обработки текстовых сообщений.
     */
    private String handleTextMessage(Message message, Long chatId) {
        String messageText = message.getText().trim();
        log.info("Получено текстовое сообщение: {}", messageText);

        return commandHandler.handleCommand(this, messageText, message, chatId);
    }

    /**
     * Метод для обработки загруженных документов (файлов .xlsx).
     */
    private String handleDocumentMessage(Message message, Long chatId) {
        Document document = message.getDocument();
        log.info("Получен документ: {}", document.getFileName());

        if (!document.getFileName().endsWith(".xlsx")) {
            return "Неверный формат файла. Пожалуйста, загрузите файл с расширением .xlsx.";
        }

        try (InputStream inputStream = getFileAsStream(document)) {
            // Делегируем обработку файла CommandHandler
            return commandHandler.handleFileUpload(inputStream, document, chatId);
        } catch (Exception e) {
            log.error("Ошибка при обработке загрузки файла: {}", e.getMessage(), e);
            return "Произошла ошибка при обработке файла. Пожалуйста, попробуйте снова.";
        }
    }

    /**
     * Метод для получения файла из Telegram в виде InputStream.
     * Используется паттерн Facade, который скрывает сложную логику работы с Telegram API за простым интерфейсом.
     */
    private InputStream getFileAsStream(Document document) {
        try {
            log.info("Получение информации о файле: {}", document.getFileName());

            // Получаем информацию о файле из Telegram
            var file = execute(org.telegram.telegrambots.meta.api.methods.GetFile.builder()
                    .fileId(document.getFileId())
                    .build());

            log.info("Загрузка файла с пути: {}", file.getFilePath());

            // Загружаем файл как InputStream
            return downloadFileAsStream(file.getFilePath());
        } catch (TelegramApiException e) {
            log.error("Ошибка при загрузке файла: {}", e.getMessage());
            throw new RuntimeException("Не удалось загрузить файл из Telegram", e);
        }
    }

    /**
     * Метод для отправки текстового сообщения пользователю.
     * Используется паттерн Command, так как этот метод делегирует выполнение задачи отправки сообщения через Telegram API.
     */
    private void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(messageText)
                .build();

        try {
            // Отправляем сообщение через Telegram API
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage());
        }
    }
}

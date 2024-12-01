package com.programmingtechie.category_bot_telegram.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    // Стратегии для обработки команд
    private final AddElementCommand addElementCommand;
    private final RemoveElementCommand removeElementCommand;
    private final ViewTreeCommand viewTreeCommand;
    private final DownloadCommand downloadCommand;
    private final HelpCommand helpCommand;
    private final UploadCommand uploadCommand;

    // Состояние для отслеживания загрузки файла
    private final Map<Long, Boolean> uploadStateMap = new HashMap<>();

    /**
     * Обрабатывает команду от пользователя. Это шаблонный метод, который выполняет проверку команды.
     * Используется Command паттерн для делегирования выполнения соответствующим объектам команд.
     */
    public String handleCommand(AbsSender sender, String messageText, Message message, Long chatId) {
        if (messageText.startsWith("/start")) {
            return helpCommand.welcome();
        }
        if (messageText.startsWith("/help")) {
            return helpCommand.execute(messageText, chatId);
        }
        if (messageText.startsWith("/addElement")) {
            return addElementCommand.execute(messageText, chatId); // Стратегия
        }
        if (messageText.startsWith("/removeElement")) {
            return removeElementCommand.execute(messageText, chatId); // Стратегия
        }
        if (messageText.startsWith("/viewTree")) {
            return viewTreeCommand.execute(messageText, chatId); // Стратегия
        }
        if (messageText.startsWith("/download")) {
            downloadCommand.execute(sender, message);
            return "Категорийное дерево загружается.";
        }
        if (messageText.startsWith("/upload")) {
            uploadStateMap.put(chatId, true);
            return """
            Пожалуйста, загрузите файл .xlsx с данными категорий.
            Формат файла должен быть следующим:
            - Первая колонка: **Category**
            - Вторая колонка: **Parent**
            Пример:
            ```
            Category   | Parent
            Электроника| -
            Телефоны   | Электроника
            Наушники   | Электроника
            ```
            Если хотите отменить загрузку, введите команду /cancel.
            """;
        }
        if (messageText.startsWith("/cancel")) {
            // Останавливает процесс загрузки файла
            uploadStateMap.remove(chatId);
            return "Процесс загрузки файла был отменен.";
        }
        return "Неизвестная команда. Введите /help для получения списка команд.";
    }

    /**
     * Обрабатывает загрузку .xlsx файла.
     * Это проверка состояния загрузки файла, реализует паттерн State.
     */
    public String handleFileUpload(InputStream inputStream, Document document, Long chatId) {
        if (uploadStateMap.getOrDefault(chatId, false)) {
            if (!document.getFileName().endsWith(".xlsx")) {
                return "Неверный формат файла. Пожалуйста, загрузите файл с расширением .xlsx.";
            }
            uploadStateMap.remove(chatId);
            return uploadCommand.handleFileUpload(inputStream, chatId);
        }
        return "Пожалуйста, сначала введите команду /upload перед загрузкой файла.";
    }
}

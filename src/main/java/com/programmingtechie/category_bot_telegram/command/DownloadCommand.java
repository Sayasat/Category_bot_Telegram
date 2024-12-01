package com.programmingtechie.category_bot_telegram.command;

import com.programmingtechie.category_bot_telegram.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
public class DownloadCommand {

    private final CategoryService categoryService;

    public DownloadCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Обрабатывает команду скачивания дерева категорий и отправляет файл.
     *
     * В этом методе используется паттерн Command для обработки команды
     * и паттерн Factory для создания экземпляра InputFile с данными.
     */
    public void execute(AbsSender sender, Message message) {
        Long chatId = message.getChatId();
        log.info("Запрос на скачивание от chatId: {}", chatId);

        try {
            // Используется паттерн Factory для создания объекта InputFile на основе байтового массива
            byte[] excelFile = categoryService.generateCategoryTreeExcel(chatId);
            InputFile file = new InputFile(new ByteArrayInputStream(excelFile), "category_tree_" + chatId + ".xlsx");

            // Формирование сообщения с документом (Telegram API)
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId.toString());
            sendDocument.setDocument(file);
            sendDocument.setCaption("Дерево категорий готово");

            sender.execute(sendDocument);
            log.info("Файл отправлен пользователю с chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке файла для chatId {}: {}", chatId, e.getMessage());
        }
    }
}

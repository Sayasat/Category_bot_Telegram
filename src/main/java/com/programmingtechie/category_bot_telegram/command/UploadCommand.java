package com.programmingtechie.category_bot_telegram.command;

import com.programmingtechie.category_bot_telegram.exception.CustomFileDownloadException;
import com.programmingtechie.category_bot_telegram.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadCommand {

    private final CategoryService categoryService;

    /**
     * Обрабатывает загрузку файла с категориями и сохраняет данные в базу данных
     */
    public String handleFileUpload(InputStream inputStream, Long chatId) {
        try {
            // Загружаем категории из Excel файла и сохраняем их в базу данных
            categoryService.uploadCategoriesFromExcel(inputStream, chatId);
            return "Файл успешно обработан. Категории были загружены в базу данных.";
        } catch (Exception e) {
            // Логируем ошибку, если она произошла
            log.error("Произошла общая ошибка при загрузке файла: {}", e.getMessage());
            // Бросаем кастомное исключение с сообщением об ошибке
            throw new CustomFileDownloadException("Произошла непредвиденная ошибка", e);
        }
    }
}

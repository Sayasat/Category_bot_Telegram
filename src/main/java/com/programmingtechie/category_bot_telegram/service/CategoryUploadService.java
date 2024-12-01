package com.programmingtechie.category_bot_telegram.service;

import com.programmingtechie.category_bot_telegram.model.Category;
import com.programmingtechie.category_bot_telegram.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryUploadService {

    private final CategoryRepository categoryRepository;

    /**
     * Парсит и сохраняет категории из Excel-файла.
     */
    @Transactional
    public String parseAndSaveCategories(InputStream inputStream, Long chatId) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Category> categoryCache = new HashMap<>();
            boolean isFirstRow = true;

            // Чтение строк из Excel
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String categoryName = getCellValue(row, 0).trim();
                String parentCategoryName = getCellValue(row, 1).trim();

                // Обработка корневой категории
                if ("Root".equals(parentCategoryName) || "-".equals(parentCategoryName)) {
                    parentCategoryName = null;
                }

                // Поиск или создание родительской категории
                Category parentCategory = (parentCategoryName != null) ?
                        findOrCreateCategory(parentCategoryName, chatId) : null;

                // Создание или получение категориии
                Category category = categoryCache.computeIfAbsent(categoryName, name -> {
                    Category newCategory = new Category(name, parentCategory);
                    newCategory.setChatId(chatId);
                    categoryRepository.save(newCategory);
                    return newCategory;
                });

                category.setParent(parentCategory);
                category.setChatId(chatId);
                categoryRepository.save(category);
            }

            return "Категории успешно загружены!";
        } catch (Exception e) {
            log.error("Ошибка при обработке Excel: {}", e.getMessage(), e);
            return "Ошибка при обработке Excel-файла.";
        }
    }

    /**
     * Получает значение ячейки.
     */
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.toString().trim();
    }

    /**
     * Находит или создает категорию.
     */
    private Category findOrCreateCategory(String name, Long chatId) {
        Optional<Category> existingCategoryOpt = categoryRepository.findByNameAndChatId(name, chatId);
        if (existingCategoryOpt.isPresent()) {
            return existingCategoryOpt.get();
        }

        Category newCategory = new Category(name, null);
        newCategory.setChatId(chatId);
        return categoryRepository.save(newCategory);
    }
}

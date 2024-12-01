package com.programmingtechie.category_bot_telegram.service;

import com.programmingtechie.category_bot_telegram.model.Category;
import com.programmingtechie.category_bot_telegram.repository.CategoryRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CategoryDownloadServiceImpl implements CategoryDownloadService {

    private final CategoryRepository categoryRepository;

    public CategoryDownloadServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Генерирует Excel-файл с деревом категорий для chatId.
     *
     * @param chatId идентификатор чата.
     * @return байтовый массив Excel-файла.
     */
    @Override
    public byte[] generateCategoryTreeExcel(Long chatId) {
        List<Category> categories = categoryRepository.findByChatId(chatId);

        if (categories.isEmpty()) {
            throw new RuntimeException("Нет категорий для чата с chatId: " + chatId);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Дерево категорий");

            createHeader(sheet);  // Заголовок таблицы
            fillData(sheet, categories);  // Заполнение данными

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();  // Возвращаем байтовый массив
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать Excel файл", e);
        }
    }

    /**
     * Создает заголовок таблицы.
     *
     * @param sheet лист Excel для заголовка.
     */
    private void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Cell cell = headerRow.createCell(0);
        cell.setCellValue("Категория");
        cell.setCellStyle(headerStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Родительская категория");
        cell.setCellStyle(headerStyle);
    }

    /**
     * Заполняет таблицу данными о категориях.
     *
     * @param sheet лист Excel для данных.
     * @param categories список категорий.
     */
    private void fillData(Sheet sheet, List<Category> categories) {
        int rowIndex = 1;
        for (Category category : categories) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(category.getName());
            String parentName = category.getParent() != null ? category.getParent().getName() : " ";
            row.createCell(1).setCellValue(parentName);
        }
    }
}

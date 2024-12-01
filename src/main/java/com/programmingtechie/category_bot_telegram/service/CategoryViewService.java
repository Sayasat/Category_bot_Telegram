package com.programmingtechie.category_bot_telegram.service;

import com.programmingtechie.category_bot_telegram.model.Category;
import com.programmingtechie.category_bot_telegram.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryViewService {

    private final CategoryRepository categoryRepository;

    /**
     * Просмотр категорийного дерева.
     */
    public String viewCategoryTree(String categoryName, Long chatId) {
        StringBuilder response = new StringBuilder("Категорийное дерево:\n");

        if (categoryName != null && !categoryName.trim().isEmpty()) {
            return viewFromCategoryName(categoryName.trim(), response, chatId);
        }

        // Стратегия для корневых категорий
        List<Category> rootCategories = categoryRepository.findByParentIsNullAndChatId(chatId);
        if (rootCategories.isEmpty()) {
            return "Корневые категории отсутствуют.";
        }

        rootCategories.forEach(root -> appendCategoryToResponse(root, response, 0, chatId));
        return response.toString();
    }

    /**
     * Отображение дерева с указанной категории.
     * Паттерн: Iterator - последовательно обходить элементы составных обьектов
     */
    private String viewFromCategoryName(String categoryName, StringBuilder response, Long chatId) {
        Optional<Category> categoryOpt = categoryRepository.findByNameAndChatId(categoryName, chatId);
        if (categoryOpt.isEmpty()) {
            return "Категория \"" + categoryName + "\" не найдена.";
        }
        appendCategoryToResponse(categoryOpt.get(), response, 0, chatId);
        return response.toString();
    }

    /**
     * Рекурсивное добавление категорий в строку.
     * Паттерн: Composite - сгруппировать множество обьектов в древовидную структуру.
     */
    private void appendCategoryToResponse(Category category, StringBuilder response, int level, Long chatId) {
        response.append(" ".repeat(level * 2))
                .append("- ")
                .append(category.getName())
                .append("\n");

        // Рекурсивный вызов для дочерних категорий
        List<Category> children = categoryRepository.findByParentAndChatId(category, chatId);
        children.forEach(child -> appendCategoryToResponse(child, response, level + 1, chatId));
    }
}

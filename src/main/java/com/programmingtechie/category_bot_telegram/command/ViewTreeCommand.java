package com.programmingtechie.category_bot_telegram.command;

import com.programmingtechie.category_bot_telegram.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements Command {

    // Ссылка на сервис для работы с категориями
    private final CategoryService categoryService;

    /**
     * Метод для обработки команды /viewTree, который отображает дерево категорий.
     * При необходимости может отображать подкатегории, если указано имя категории.
     */
    @Override
    public String execute(String text, Long chatId) {
        // Разделяем команду на части по пробелам, чтобы извлечь имя категории, если оно указано
        String[] parts = text.split("\\s+", 2);
        String categoryName = parts.length > 1 ? parts[1].trim() : null;

        // Передаем имя категории (если указано) и идентификатор чата в сервис для отображения дерева
        return categoryService.viewCategoryTree(categoryName, chatId);
    }
}

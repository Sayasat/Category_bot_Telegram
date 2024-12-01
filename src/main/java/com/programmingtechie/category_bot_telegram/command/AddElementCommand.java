package com.programmingtechie.category_bot_telegram.command;

import com.programmingtechie.category_bot_telegram.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddElementCommand implements Command {

    // Сервис для работы с категориями
    private final CategoryService categoryService;

    /**
     * Выполнение команды /addElement.
     * Разделяет команду и вызывает метод сервиса для добавления элемента.
     */
    @Override
    public String execute(String text, Long chatId) {
        String[] commandParts = text.split(" ");
        String response;

        // Проверка на формат команды
        if (commandParts.length < 2) {
            return "Некорректный формат команды. Используйте: /addElement <родитель> <элемент> или /addElement <элемент>.";
        }

        // Добавление элемента без родителя
        if (commandParts.length == 2) {
            String newElement = commandParts[1];
            response = categoryService.addElement(null, newElement, chatId);
        }
        // Добавление элемента с родителем
        else if (commandParts.length == 3) {
            String parentElement = commandParts[1];
            String newElement = commandParts[2];
            response = categoryService.addElement(parentElement, newElement, chatId);
        }
        // Ошибка при неправильном формате
        else {
            response = "Некорректный формат команды. Используйте /addElement <родитель> <элемент> или /addElement <элемент>.";
        }

        return response;
    }
}

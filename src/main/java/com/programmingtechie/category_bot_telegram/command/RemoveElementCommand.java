package com.programmingtechie.category_bot_telegram.command;

import com.programmingtechie.category_bot_telegram.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveElementCommand implements Command {

    // Сервис для работы с категориями
    private final CategoryService categoryService;

    @Override
    public String execute(String text, Long chatId) {
        String[] commandParts = text.split(" ");
        if (commandParts.length == 2) {
            // Удаляем элем
            String elementToRemove = commandParts[1];
            return categoryService.removeElement(elementToRemove, chatId);
        } else {
            // Некорректная команда
            return "Некорректный формат команды. Используйте /removeElement <элемент>.";
        }
    }
}

package com.programmingtechie.category_bot_telegram.service;

import com.programmingtechie.category_bot_telegram.model.Category;
import com.programmingtechie.category_bot_telegram.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryRemoveService {

    private final CategoryRepository categoryRepository;

    /**
     * Удаляет элемент и его дочерние элементы.
     *
     * @param elementName имя элемента.
     * @param chatId идентификатор чата.
     * @return сообщение об удалении.
     */
    public String removeElement(String elementName, Long chatId) {
        Optional<Category> elementOpt = categoryRepository.findByNameAndChatId(elementName, chatId);

        if (elementOpt.isEmpty()) {
            return "Элемент \"" + elementName + "\" не найден.";
        }

        Category element = elementOpt.get();

        // Удаляем элемент и его дочерние элементы
        categoryRepository.delete(element);
        return "Элемент \"" + elementName + "\" и его дочерние элементы удалены.";
    }
}

package com.programmingtechie.category_bot_telegram.service;

import com.programmingtechie.category_bot_telegram.model.Category;
import com.programmingtechie.category_bot_telegram.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryAddService {

    private final CategoryRepository categoryRepository;

    /**
     * Паттерн: Strategy
     * Этот метод определяет, какой тип элемента нужно добавлять. Он использует два разных подхода:
     * 1. Добавление дочернего элемента в родительскую категорию.
     * 2. Добавление корневого элемента в случае, если родитель не указан.
     * В зависимости от наличия родительской категории выбирается соответствующий метод добавления
     */

    public String addElement(String parentName, String childName, Long chatId) {
        if (parentName != null) {
            return addChildToParent(parentName, childName, chatId); // Стратегия для дочернего элемента
        }
        return addRootElement(childName, chatId); // Стратегия для корневого элемента
    }

    // Метод для добавления дочернего элемента в родительскую категорию
    private String addChildToParent(String parentName, String childName, Long chatId) {
        Optional<Category> parentOpt = categoryRepository.findByNameAndChatId(parentName, chatId);
        if (parentOpt.isEmpty()) {
            return "Родительский элемент \"" + parentName + "\" не существует.";
        }
        Category parent = parentOpt.get();
        if (categoryRepository.findByNameAndParentAndChatId(childName, parent, chatId).isPresent()) {
            return "Элемент \"" + childName + "\" уже существует в категории \"" + parentName + "\".";
        }
        Category child = categoryRepository.findByNameAndChatId(childName, chatId).orElse(new Category(childName, parent));
        child.setParent(parent);
        child.setChatId(chatId);
        categoryRepository.save(child);
        return "Элемент \"" + childName + "\" добавлен в категорию \"" + parentName + "\".";
    }

    // Метод для добавления корневого элемента
    private String addRootElement(String childName, Long chatId) {
        if (categoryRepository.findByNameAndChatId(childName, chatId).isPresent()) {
            return "Корневой элемент \"" + childName + "\" уже существует.";
        }
        Category rootCategory = new Category(childName, null);
        rootCategory.setChatId(chatId);
        categoryRepository.save(rootCategory);
        return "Корневой элемент \"" + childName + "\" успешно добавлен.";
    }
}

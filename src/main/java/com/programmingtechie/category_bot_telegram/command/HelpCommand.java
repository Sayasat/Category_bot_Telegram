package com.programmingtechie.category_bot_telegram.command;

import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    @Override
    public String execute(String text, Long chatId) {
        return """
                📋 Вот что я умею:\n 
                🔍 /viewTree — Показать дерево категорий.\n 
                ➕ /addElement <название элемента> — Добавить корневой элемент.\n 
                🔗 /addElement <родительский элемент> <дочерний элемент> — Добавить дочерний элемент.\n 
                ❌ /removeElement <название элемента> — Удалить элемент и его дочерние элементы.\n 
                📄 /download — Скачать дерево категорий в Excel.\n 
                📤 /upload — Загрузить Excel с деревом категорий и обновить базу данных.\n 
                ❓ /help — Получить список команд.
            """;
    }

    public String welcome() {
        return """
                Привет! Я — ваш помощник для управления деревом категорий. 🌳\n\n
                Могу помогать в создании, изменении, удалении категорий и экспортировать данные в Excel.\n\n
                Если что-то непонятно, напишите /help. Давайте начнем! 🚀
                """;
    }
}

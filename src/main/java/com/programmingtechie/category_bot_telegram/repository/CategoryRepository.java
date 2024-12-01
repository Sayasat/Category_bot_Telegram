package com.programmingtechie.category_bot_telegram.repository;

import com.programmingtechie.category_bot_telegram.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNullAndChatId(Long chatId);

    List<Category> findByParentAndChatId(Category category, Long chatId);

    List<Category> findByChatId(Long chatId);

    Optional<Category> findByNameAndChatId(String name, Long chatId);

    Optional<Category> findByNameAndParentAndChatId(String name, Category parent, Long chatId);
}

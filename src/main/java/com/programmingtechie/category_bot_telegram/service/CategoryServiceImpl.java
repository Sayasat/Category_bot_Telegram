package com.programmingtechie.category_bot_telegram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryViewService viewService;
    private final CategoryAddService addService;
    private final CategoryRemoveService removeService;
    private final CategoryDownloadServiceImpl downloadService;
    private final CategoryUploadService uploadService;

    @Override
    public String viewCategoryTree(String categoryName, Long chatId) {
        return viewService.viewCategoryTree(categoryName, chatId);
    }

    @Override
    public String addElement(String parentName, String childName, Long chatId) {
        return addService.addElement(parentName, childName, chatId);
    }

    @Override
    public String removeElement(String elementName, Long chatId) {
        return removeService.removeElement(elementName, chatId);
    }

    @Override
    public byte[] generateCategoryTreeExcel(Long chatId) {
        return downloadService.generateCategoryTreeExcel(chatId);
    }

    @Override
    public String uploadCategoriesFromExcel(InputStream inputStream, Long chatId) {
        return uploadService.parseAndSaveCategories(inputStream, chatId);
    }
}

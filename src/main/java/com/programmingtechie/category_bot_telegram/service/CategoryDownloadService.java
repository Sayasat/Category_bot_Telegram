package com.programmingtechie.category_bot_telegram.service;

public interface CategoryDownloadService {

    byte[] generateCategoryTreeExcel(Long chatId);
}

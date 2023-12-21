package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    void addCategory(Category category);

    List<Category> getAllCategory();

    void deleteCategoryById(long id);

    Optional<Category> getCategoryById(long id);
}

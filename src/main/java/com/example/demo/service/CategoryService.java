package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository categoryRepository;
	
	//レコード追加
	public void saveCategory(Category category) {
		categoryRepository.save(category);
	}
	
	//カテゴリーリスト取得
	public List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}
	
	// レコード1件取得
	public Category editCategory(Integer id) {
		Category category = (Category) categoryRepository.findById(id).orElse(null);
		return category;
	}

}

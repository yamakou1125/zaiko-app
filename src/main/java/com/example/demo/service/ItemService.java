package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Service
public class ItemService {
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ItemRepository itemRepository;
	
	//カテゴリーリスト取得
	public List<Category> findAllCategory() {
		return categoryRepository.findAll();
	}
}

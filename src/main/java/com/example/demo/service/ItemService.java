package com.example.demo.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
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
	
	//アイテムリスト取得
		public List<Item> findAllItem() {
			return itemRepository.findAll();
		}
	
	//レコード追加
	public void saveItem(Item item) {
		item.setCreatedAt(Timestamp.valueOf(getTime()));
		item.setUpdatedAt(Timestamp.valueOf(getTime()));
		itemRepository.save(item);
	}

	//日時取得
	private String getTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}

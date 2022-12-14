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

import io.micrometer.common.util.StringUtils;

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
	public List<Item> findAllItem(String category) {
		if (!StringUtils.isBlank(category)) {
			int castCategory = Integer.parseInt(category);
			return itemRepository.findAllItem(castCategory);
		} else {
			return itemRepository.findAll();
		}
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
	
	// レコード1件取得
	public Item editItem(Integer id) {
		Item item = (Item) itemRepository.findById(id).orElse(null);
		return item;
	}
	
	//レコード削除
	public void deleteItem(Integer id) {
		itemRepository.deleteById(id);
	}
}

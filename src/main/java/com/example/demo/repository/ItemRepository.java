package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	//@Query("select t from Item t where category LIKE :strcategory ORDER BY createdAt DESC")
	//List<Item> findAllItem(@Param("strcategory")String strcategory);
	
	@Query(value = "select * from items "
			+ "where category_id = ?1 "
			+ "ORDER BY created_at DESC", nativeQuery = true)
	List<Item> findAllItem(int category);

}


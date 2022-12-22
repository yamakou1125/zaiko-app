package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.service.ItemService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class TopController {
	@Autowired
	HttpSession session;
	@Autowired
	ItemService itemService;
	
	// TOP画面表示
	@GetMapping("/")
	public ModelAndView topDisp() {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User) session.getAttribute("loginUser");
		List<Item> itemList = itemService.findAllItem();
		List<Category> categoryList = itemService.findAllCategory();
		mav.addObject("loginUser", loginUser);
		mav.addObject("itemList", itemList);
		mav.addObject("categoryList", categoryList);
		mav.setViewName("/top");
		return mav;
	}
}

package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.User;
import com.example.demo.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CategoryController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	HttpSession session;
	// カテゴリー 一覧画面表示
	@GetMapping("/category")
	public ModelAndView newcategory() {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User) session.getAttribute("loginUser");
		mav.addObject("loginUser", loginUser);
		mav.setViewName("/categorytop");
		return mav;
	}
}
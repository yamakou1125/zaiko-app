package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;
	
	//新規ユーザー登録画面
	@GetMapping("/signup")
	public ModelAndView newUser() {
		ModelAndView mav = new ModelAndView();
		User user = new User();
		mav.addObject("user", user);
		mav.setViewName("/signup");
		return mav;
	}

}

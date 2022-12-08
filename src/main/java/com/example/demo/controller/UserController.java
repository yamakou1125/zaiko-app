package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

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
	
	//登録処理
	@PostMapping("/add")
	public ModelAndView addUser(@ModelAttribute("user") User user,
			@RequestParam(name = "confirmation_password") String password2) {
		// パスワードをhash化する
				String strPassword = user.getPassword();

				Hasher hasher = Hashing.sha256().newHasher();
				hasher.putString(strPassword, Charsets.UTF_8);
				HashCode sha256 = hasher.hash();

				String strSha256 = String.valueOf(sha256);
				user.setPassword(strSha256);
		userService.saveUser(user);
		return new ModelAndView("/top");
	}
}

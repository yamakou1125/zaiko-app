package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
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
public class LoginController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	//ログイン画面
	@GetMapping("/login")
	public ModelAndView loginPage() {
		ModelAndView mav = new ModelAndView();
		User user = new User();
		mav.setViewName("/login");
		mav.addObject("user", user);
		return mav;
	}
	
	//ログイン処理
	@PostMapping("/postLogin")
	public ModelAndView postLogin(@ModelAttribute("user") User user,
			@RequestParam(name = "password") String password) {
		ModelAndView mav = new ModelAndView();
		List<String> errorMessages = new ArrayList<String>();

		String name = user.getName();

		if(Strings.isBlank(name)){
			errorMessages.add("ユーザー名を入力してください");
		}

		String strPassword = user.getPassword();

		if (Strings.isBlank(strPassword)) {
			errorMessages.add("パスワードを入力してください");

		}else {
			// パスワードをhash化する
			Hasher hasher = Hashing.sha256().newHasher();
			hasher.putString(strPassword, Charsets.UTF_8);
			HashCode sha256 = hasher.hash();
			String strSha256 = String.valueOf(sha256);

			user = userService.findLoginUser(strSha256, user.getName());

			//該当のユーザーが存在しないとき
			if(user == null) {
				errorMessages.add("アカウント名またはパスワードが誤っています");

			}
		}

		//エラーメッセージを数をチェック
		if (errorMessages.size() != 0) {
			mav.addObject("loginUser", user);
			mav.addObject("errorMessages", errorMessages);
			mav.setViewName("/login");
			return mav;
		}

		session.setAttribute("loginUser", user);

		return new ModelAndView("redirect:/");
	}
}
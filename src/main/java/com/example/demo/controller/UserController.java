package com.example.demo.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	//新規ユーザー登録画面表示
	@GetMapping("/signup")
	public ModelAndView newUser() {
		ModelAndView mav = new ModelAndView();
		User user = new User();
		mav.addObject("user", user);
		mav.setViewName("/signup");
		return mav;
	}
	
	//ユーザー登録処理
	@PostMapping("/add")
	public ModelAndView addUser(@ModelAttribute("user") User user,
			@RequestParam(name = "confirmation_password") String password2) {
		ModelAndView mav = new ModelAndView();
		List<String> errorMessages = new ArrayList<String>();
		
		String name = user.getName();

		User existUser = userService.findExistUser(name);
		
		if(existUser != null) {
			errorMessages.add("この名前は既に使われています");
		}
		
		if(Strings.isBlank(name)){
			errorMessages.add("名前を入力してください");
		}else if (name.length() > 20) {
			errorMessages.add("名前は20字以下で入力してください");
		}
		
		
		// パスワードをhash化する
		String strPassword = user.getPassword();
		
		if (Strings.isBlank(strPassword)) {
			errorMessages.add("パスワードを入力してください");
		}else if(strPassword.length() < 5 || strPassword.length() > 20) {
			errorMessages.add("パスワードは5文字以上20文字以下で入力してください");
		}else if (!strPassword.equals(password2)) {
			errorMessages.add("パスワードと確認用パスワードが一致しません");
		}else {
			Hasher hasher = Hashing.sha256().newHasher();
			hasher.putString(strPassword, Charsets.UTF_8);
			HashCode sha256 = hasher.hash();

			String strSha256 = String.valueOf(sha256);
			user.setPassword(strSha256);
		}
		
		if(Strings.isBlank(password2)){
			errorMessages.add("確認用パスワードを入力してください");
		}
				
		//バリデーションチェック
		if (errorMessages.size() != 0) {
			mav.addObject("errorMessages", errorMessages);
			mav.setViewName("/signup");
			return mav;
		}
				
		userService.saveUser(user);
		return new ModelAndView("/top");
	}
	
	// ユーザー編集画面表示
	@GetMapping("/userEdit/{id}")
	public ModelAndView editUser(@PathVariable Integer id, RedirectAttributes attributes) {
		ModelAndView mav = new ModelAndView();

		//ログインフィルター
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			attributes.addFlashAttribute("loginError", "ログインしてください");
			mav.setViewName("redirect:/login");
			return mav;
		}

		mav.addObject("loginUser", loginUser); // 編集するユーザーをセット
		mav.setViewName("/userEdit"); // 画面遷移先を指定

		return mav;
	}
	
	// ユーザー編集処理
	@PutMapping("/update_user/{id}")
	public ModelAndView updateUser(
			@PathVariable Integer id,
			@ModelAttribute("loginUser") User user,
			@RequestParam(name = "confirmation_password") String password2)
			throws ParseException {

		ModelAndView mav = new ModelAndView();
		List<String> errorMessages = new ArrayList<String>();
		
		//ログインしているユーザーの情報を取得
		User loginUser = (User) session.getAttribute("loginUser");
		int loginUserId = loginUser.getId();

		//編集するユーザーの情報を取得
		User editUser = userService.findById(id);

		//PWを初期化
		String strPassword = null;

		strPassword = user.getPassword();
		String strPassword2 = password2;

		if(id == loginUserId) {
			//バリデーション(パスワード)
			if (Strings.isBlank(strPassword)) {
				errorMessages.add("パスワードを入力してください");
			} else if (5 > strPassword.length()) {
				errorMessages.add("パスワードは5文字以上で入力してください");
			} else if (20 < strPassword.length()) {
				errorMessages.add("パスワードは20文字以下で入力してください");
			}else if (!strPassword.equals(strPassword2)) {
				errorMessages.add("入力したパスワードと確認用パスワードが一致しません");
			} else {
				// パスワードをhash化する
				Hasher hasher = Hashing.sha256().newHasher();
				hasher.putString(strPassword, Charsets.UTF_8);
				HashCode sha256 = hasher.hash();
				String strSha256 = String.valueOf(sha256);
				user.setPassword(strSha256);
			}
		}else {
			String strSha256 = editUser.getPassword();
			user.setPassword(strSha256);
		}

		// 編集する情報をセット
		mav.addObject("loginUser", user);

		user.setId(id);

		//エラーメッセージを数をチェック
		if (errorMessages.size() != 0) {
			mav.addObject("errorMessages", errorMessages);
			session.setAttribute("user", user);
			mav.setViewName("/userEdit");
			return mav;
		}

		// 編集したユーザ情報を更新
		userService.saveUser(user);
		// user情報をsession領域に"user"として保存
		session.setAttribute("loginUser", user);
		// ユーザー管理画面へリダイレクト
		return new ModelAndView("redirect:/");
	}
	
	//ユーザー 退会処理
	@DeleteMapping("/user_delete/{id}")
	public ModelAndView deleteUser(@PathVariable Integer id) {
		userService.deleteUser(id);
		// セッションの無効化
		session.invalidate();
		return new ModelAndView("redirect:/login");
	}

}

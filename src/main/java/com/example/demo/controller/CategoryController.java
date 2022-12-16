package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Category;
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
		List<Category> categoryList = categoryService.findAllCategory();//DBにあるcategory全てを格納
		mav.addObject("loginUser", loginUser);
		mav.addObject("categoryList", categoryList);
		mav.setViewName("/categorytop");
		return mav;
	}
	
	//カテゴリー 新規登録画面表示
	@GetMapping("/newcategory")
	public ModelAndView newCategory() {
		ModelAndView mav = new ModelAndView();
		Category category = new Category();
		mav.addObject("category", category);
		mav.setViewName("/categoryNew");
		return mav;
	}
	
	//カテゴリー登録処理
	@PostMapping("/createcategory")
	public ModelAndView createCategory(@ModelAttribute("category") Category category) {
		categoryService.saveCategory(category);
		return new ModelAndView("/categoryTop");
	}
	
	// カテゴリー 編集画面表示
	@GetMapping("/categoryEdit/{id}")
	public ModelAndView editCategory(@PathVariable Integer id, RedirectAttributes attributes) {
		ModelAndView mav = new ModelAndView();

		//ログインフィルター
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			attributes.addFlashAttribute("loginError", "ログインしてください");
			mav.setViewName("redirect:/login");
			return mav;
		}
		// 編集するカテゴリーを取得
	    Category category = categoryService.editCategory(id);
		mav.addObject("category", category); // 編集するカテゴリーをセット
		mav.setViewName("/categoryEdit"); // 画面遷移先を指定

		return mav;
	}
	
	// カテゴリー編集処理
	@PutMapping("/updatecategory/{id}")
	public ModelAndView updateCategory(@PathVariable Integer id, @ModelAttribute("category") Category category) {

		ModelAndView mav = new ModelAndView();
		//ログインしているユーザーの情報を取得
		User loginUser = (User) session.getAttribute("loginUser");
		List<String> errorMessages = new ArrayList<String>();

		String name = category.getName();

		//バリデーション(カテゴリ名)
		if (Strings.isBlank(name)) {
			errorMessages.add("カテゴリー名を入力してください");
		}


		//エラーメッセージ数をチェック
		if (errorMessages.size() != 0) {
			mav.setViewName("/categoryEdit");
			mav.addObject("errorMessages", errorMessages);
			return mav;
		}

		// UrlParameterのidを更新するentityにセット
		category.setId(id);
		// 編集したカテゴリーを更新
		categoryService.saveCategory(category);
		return new ModelAndView("redirect:/");
	}

}

package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.service.ItemService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ItemController {
	@Autowired
	HttpSession session;

	@Autowired
	ItemService itemService;

	// 在庫登録画面表示
	@GetMapping("new")
	public ModelAndView newItem(RedirectAttributes attributes) {
		ModelAndView mav = new ModelAndView();

		//ログインフィルター
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			attributes.addFlashAttribute("loginError", "ログインしてください");
			mav.setViewName("redirect:/login");
			return mav;
		}

		//これから入力されるitemの情報を格納する空の箱を用意
		Item item = new Item();

		//カテゴリーリスト取得
		List<Category> categoriesList = itemService.findAllCategory();

		//ログインユーザの情報をセット
		mav.addObject("loginUser", loginUser);
		//フォームを使用するために空の箱をセット
		mav.addObject("itemModel", item);
		//カテゴリーリストをセット
        mav.addObject("categoriesList", categoriesList);
        //遷移先を指定
		mav.setViewName("/new");
		return mav;
	}
	
	//アイテム追加処理
	@PostMapping("/additem")
	public ModelAndView addItem(@ModelAttribute("itemModel") Item item, BindingResult result, int userId,
			@RequestParam(name = "category") int categoryId, @RequestParam(name = "expirationDate", required = false) String expirationDate) {

		ModelAndView mav = new ModelAndView();
		List<String> errorMessages = new ArrayList<String>();

		String name = item.getName();

		//バリデーション(アイテム名)
		if (Strings.isBlank(name)) {
			errorMessages.add("アイテム名を入力してください");
		}

		//バリデーション(カテゴリー)
		if (categoryId == 0) {
			errorMessages.add("カテゴリーを選択してください");
		}


		//期限
		if (Strings.isBlank(expirationDate)) {
			errorMessages.add("期限を入力してください");
		}else {
			expirationDate += " 00:00:00";
			Timestamp tm = Timestamp.valueOf(expirationDate);
			item.setExpirationDate(tm);
		}

		//エラーメッセージ数をチェック
		if (errorMessages.size() != 0) {
			mav.setViewName("/new");
			mav.addObject("errorMessages", errorMessages);
			List<Category> categoryList = itemService.findAllCategory();
			//カテゴリリストをセット
	        mav.addObject("categoryList", categoryList);
	        //カテゴリリスト初期値をセット
	        mav.addObject("selectedCategory", categoryId);
	        mav.addObject("accrualDate", expirationDate);
			return mav;
		}

		item.setUserId(userId);
		item.setCategoryId(categoryId);

		itemService.saveItem(item);

		return new ModelAndView("redirect:/");
	}
}

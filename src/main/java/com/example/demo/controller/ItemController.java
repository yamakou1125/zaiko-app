package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}

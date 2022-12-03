package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class TopController {
	@Autowired
	HttpSession session;
	// TOP画面表示
	@GetMapping("/")
	public ModelAndView topDisp() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/top");
		return mav;
	}
}
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	//レコード追加
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	// 名前とパスワードで検索
	public User findLoginUser(String password, String name) {
		return userRepository.findByPasswordAndName(password, name);
	}
	
	// ユーザIDの取得
	public User findById(Integer id) {
		User user =  userRepository.findById(id).orElse(null);
		return user;
	}
	
	//ユーザー退会
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

}

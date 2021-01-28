package com.ssafy.ctrlz.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.ctrlz.model.User;
import com.ssafy.ctrlz.repository.UserRepository;


public interface UserService {
	//회원가입 기능 
//	public void createAccount(String userEmail, String userName, String userPassword);
	public void createAccount(User user);
	
	//로그인 기능
	public Optional<User> loginAccount(String userEmail, String userPassword);
	//내 프로필 기능
	public User profileAccount(Long userId);
	//프로필 편집 기능
	public void updateAccount(Long userId, String userName, String userIntroduce, MultipartFile userImage, String userEmail,String userPassword);
	//회원 탈퇴 기능
	public Optional<User> deleteAccount(Long userId);
}

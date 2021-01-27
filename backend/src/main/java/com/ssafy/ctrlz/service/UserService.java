package com.ssafy.ctrlz.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ssafy.ctrlz.model.User;
import com.ssafy.ctrlz.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	
	//회원가입 기능 
	public void createAccount(String userId,String userEmail, String userName, String userPassword) {
		
		User user = new User();
		user.setUserEmail(userEmail);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		
		userRepository.save(user);
		
	}
	
	//로그인 기능
	public Optional<User> loginAccount(String userEmail, String userPassword) {
		
		return userRepository.findUserByUserEmailAndUserPassword(userEmail,userPassword);
		
		
	}
	
	//내 프로필 기능
	public User profileAccount(String userId){
		
		return userRepository.getUserByUserId(userId);
	}
	
	//프로필 편집 기능
	public void updateAccount(Long userId, String userName, String userIntroduce, String userImage, String userEmail) {
		User user = new User();
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserIntroduce(userIntroduce);
		user.setUserImage(userImage);
		
		userRepository.save(user);
		
	}

}

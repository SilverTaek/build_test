package com.ssafy.ctrlz.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.ctrlz.model.User;
import com.ssafy.ctrlz.repository.UserRepository;

@Service
public class UserServiceImple implements UserService {

	@Autowired
	private UserRepository userRepository;
	//회원가입 기능
	@Override
	public void createAccount(User user) {
//		User user = new User();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(user.getUserPassword().getBytes());
			user.setUserPassword(String.format("%040x", new BigInteger(1,md.digest())));
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		user.setUserEmail(user.getUserEmail());
		user.setUserName(user.getUserName());
		userRepository.save(user);	
	}
//	@Override
//	public void createAccount(String userEmail, String userName, String userPassword) {
//		User user = new User();
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA-256");
//			md.update(userPassword.getBytes());
//			user.setUserPassword(String.format("%040x", new BigInteger(1,md.digest())));
//		}catch(NoSuchAlgorithmException e){
//			e.printStackTrace();
//		}
//		user.setUserEmail(userEmail);
//		user.setUserName(userName);
//		userRepository.save(user);	
//	}
	//로그인 기능
	@Override
	public Optional<User> loginAccount(String userEmail, String userPassword) {
		return userRepository.findUserByUserEmailAndUserPassword(userEmail,userPassword);
	}
	//내 프로필 기능
	@Override
	public User profileAccount(Long userId) {
		return userRepository.getUserByUserId(userId);
	}
	//프로필 편집 기능
	@Override
	public void updateAccount(Long userId, String userName, String userIntroduce, MultipartFile userImage, String userEmail, String userPassword) {
		User user = new User();
		String UPLOAD_PATH = "C:\\Users\\multicampus\\fileupload\\src\\assets\\img";
        UUID uuid = UUID.randomUUID();
        String saveName = uuid+"_"+userImage.getOriginalFilename();

        File saveFile = new File(UPLOAD_PATH, saveName);
        
        try {
            userImage.transferTo(saveFile); // 업로드 파일에 saveFile이라는 껍데기 입힘
        } catch (IOException e) {
            e.printStackTrace();
            
        }
		user.setUserId(userId);
		user.setUserName(userName);
		user.setUserIntroduce(userIntroduce);
		user.setUserImage(saveName);
		user.setUserEmail(userEmail);
		user.setUserPassword(userPassword);
		userRepository.save(user);
	}
	//프로필 탈퇴 기능
	@Override
	public Optional<User> deleteAccount(Long userId) {
		 return userRepository.deleteUserByUserId(userId);
	}

	
}

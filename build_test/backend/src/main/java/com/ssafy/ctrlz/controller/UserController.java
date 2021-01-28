package com.ssafy.ctrlz.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.ctrlz.model.User;
import com.ssafy.ctrlz.repository.UserRepository;
import com.ssafy.ctrlz.service.JwtService;
import com.ssafy.ctrlz.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@Transactional
@Api(tags = "UserController", description = "회원API")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
//	@PostMapping("/register")
//	@ApiOperation(value = "가입하기")
//	public ResponseEntity<String> userRegister(@RequestParam(value = "userEmail", required = false) String userEmail, @RequestParam(value = "userName",required = false) String userName,
//			@RequestParam (value = "userPassword",required = false) String userPassword) {
//		System.out.println("gello");
//		System.out.print(userName);
//		System.out.print(userEmail);
//		System.out.print(userPassword);
//		if(userRepository.getUserByUserEmail(userEmail) != null){
//			return new ResponseEntity<String>("Fail",HttpStatus.NOT_FOUND);
//		}
//		if(userRepository.getUserByUserName(userName) != null) {
//			return new ResponseEntity<String>("Fail",HttpStatus.NOT_FOUND);
//		}
//		userService.createAccount(userEmail, userName, userPassword);
//		return new ResponseEntity<String>("Success", HttpStatus.OK);
//	}
	
	@PostMapping("/register")
	@ApiOperation(value = "가입하기")
	public Object userRegister(@RequestBody User user) {
		
		userService.createAccount(user);
		if(userRepository.getUserByUserEmail(user.getUserEmail()) != null){
			return new ResponseEntity<>("Fail",HttpStatus.NOT_FOUND);
		}
		if(userRepository.getUserByUserName(user.getUserName()) != null) {
			return new ResponseEntity<>("Fail",HttpStatus.NOT_FOUND);
		}
	
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	
	@GetMapping("/login")
	@ApiOperation(value = "로그인", notes = "성공시 jwt 토큰을 반환합니다.")
	public Object userLogin(@RequestParam(required = true) String userEmail, @RequestParam(required = true) String userPassword) {
	
		User user = new User();
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(userPassword.getBytes());
			user.setUserPassword(String.format("%040x", new BigInteger(1,md.digest())));
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		
		String original = user.getUserPassword();
		Optional<User> userOpt = userService.loginAccount(userEmail, original);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = null;
		if(userOpt.isPresent()) {
			User userInfo = userRepository.getUserByUserEmail(userEmail);
			userInfo.setUserPassword(original);
			String token = jwtService.create(userInfo);
			resultMap.put("auth-token", token);
			resultMap.put("accesstoken",token);
			resultMap.put("message", "Success");
			status = HttpStatus.ACCEPTED;
			return new ResponseEntity<>(resultMap, status); 
		}
		else {
			resultMap.put("message", "FAIL");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<>(resultMap, status);
		
		}
	}
	
	@PostMapping("/profile/update")
    @ApiOperation(value = "회원정보 수정")
	public Object userUpdate(Long userId,String userName, String userIntroduce,
			MultipartFile userImage, String userEmail, String userPassword) {
		System.out.println("1");
		System.out.println(userName);
        System.out.println(userImage.getOriginalFilename());
        System.out.println(userImage.getSize());
		if(userRepository.getUserByUserName(userName) != null) {
			return new ResponseEntity<>("Fail",HttpStatus.NOT_FOUND);
		}
		userService.updateAccount(userId,userName,userIntroduce,userImage,userEmail,userPassword);
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	@GetMapping("/profile")
	@ApiOperation(value = "회원정보 조회")
	public Object userProfile(Long userId) {   
         return userService.profileAccount(userId);
     }
	
	@DeleteMapping("/delete/{userId}")
	@ApiOperation(value = "회원 삭제")
	public Object userDelete(@PathVariable Long userId) {
		Optional<User> userOpt = userService.deleteAccount(userId);
		if(userOpt.isPresent()) return new ResponseEntity<>("Success", HttpStatus.OK);
		else return new ResponseEntity<>("Fail", HttpStatus.NOT_FOUND);
	}
}

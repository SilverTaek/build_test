package com.ssafy.ctrlz.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
	
	@PostMapping("/register")
	@ApiOperation(value = "가입하기")
	public Object userRegister(@RequestParam String userName,String userId, String userEmail, String userPassword) {
		
		if(userRepository.getUserByUserEmail(userEmail) != null){
			return new ResponseEntity<>("Fail",HttpStatus.NOT_FOUND);
		}
		
		if(userRepository.getUserByUserName(userName) != null) {
			return new ResponseEntity<>("Fail",HttpStatus.NOT_FOUND);
		}

		userService.createAccount(userId,userEmail, userName, userPassword);
		
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	@GetMapping("/login")
	@ApiOperation(value = "로그인", notes = "성공시 jwt 토큰을 반환합니다.")
	public Object userLogin(@RequestParam(required = true) String userEmail, @RequestParam(required = true) String userPassword) {
		
		Optional<User> userOpt = userService.loginAccount(userEmail, userPassword);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = null;
		if(userOpt.isPresent()) {
			
			User userInfo = userRepository.getUserByUserEmail(userEmail);
			String token = jwtService.create(userInfo);
			resultMap.put("auth-token", token);
			resultMap.put("access-token",token);
			resultMap.put("message", "Success");
			status = HttpStatus.ACCEPTED;
			return new ResponseEntity<Map<String, Object>>(resultMap, status); 
		}
		
		else {
			resultMap.put("message", "FAIL");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			return new ResponseEntity<Map<String, Object>>(resultMap, status);
		
		}
	}
	
	@PostMapping("/profile/update")
    @ApiOperation(value = "회원정보 수정")
	public Object userUpdate(@RequestParam Long userId, String userName, String userIntroduce,
			String userImage, String userEmail) {
		
		
		
		userService.updateAccount(userId,userName,userIntroduce,userImage,userEmail);
		
		
		return new ResponseEntity<>("안녕하세요", HttpStatus.OK);
		
	}

//	@GetMapping("/profile/{userId}")
//	@ApiOperation(value = "회원정보 조회")
//	@ResponseBody
//	public Object userProfile(@PathVariable("userId") String userId) {
//         
//        
//         return userService.profileAccount(userId);
//     }
	@GetMapping("/profile")
	@ResponseBody
	public Object userProfile(String userId) {
		
		return userService.profileAccount(userId);
	}
}

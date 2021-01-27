package com.ssafy.ctrlz.repository;

import java.util.Optional;
import com.ssafy.ctrlz.model.User;

public interface UserRepositoryMapping {
		
	Optional<User> findUserByUserEmailAndUserPassword(String userEmail, String userPassword);
	User getUserByUserEmail(String userEmail);
	User getUserByUserId(String userId);
	User getUserByUserName(String userName);
	
}

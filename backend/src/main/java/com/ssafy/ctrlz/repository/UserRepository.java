package com.ssafy.ctrlz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssafy.ctrlz.model.User;

public interface UserRepository  extends JpaRepository<User,String>, UserRepositoryMapping{
	
	
}

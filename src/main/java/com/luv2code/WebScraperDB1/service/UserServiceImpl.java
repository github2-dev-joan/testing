package com.luv2code.WebScraperDB1.service;

import com.luv2code.WebScraperDB1.entity.User;
import com.luv2code.WebScraperDB1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean authenticate(String username, String password) {
       User user = userRepository.findByUsername(username);

       if(user == null || user.getPassword().equals(password)){
           return false;
       }

       return true;
    }
}

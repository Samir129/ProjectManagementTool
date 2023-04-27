package com.mypersonalproject.projectmanagementtool.services;


import com.mypersonalproject.projectmanagementtool.domain.User;
import com.mypersonalproject.projectmanagementtool.exceptions.UsernameAlreadyExistsException;
import com.mypersonalproject.projectmanagementtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            // Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            //Make Sure that password and confirmpassword match
            // We don't persist or show the confirmpassword
            return userRepository.save(newUser);

        }
        catch(Exception e){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
        }

    }
}

package com.example.JWTlogin.Service;

import com.example.JWTlogin.JWT.JWTutil;
import com.example.JWTlogin.Model.AppUser;
import com.example.JWTlogin.Model.Role;
import com.example.JWTlogin.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service@RequiredArgsConstructor@Slf4j
public class AuthrizationService implements Authservice {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;
    private Authentication authenticate;

    @Override
    public String Login(String Email, String Password) {
        // Create an authentication token with the provided credentials
        var authtoken = new UsernamePasswordAuthenticationToken(Email, Password);
        log.info("Authtoken "+authtoken);
        log.info("passing token to authenticationManager");
        // Authenticate the token using the authentication manager
        Authentication authenticate = authenticationManager.authenticate(authtoken);
        log.info("manager found the authenticate user in context provider");
        // If authentication is successful, generate a JWT token


        log.info(authenticate.getPrincipal().toString());
        AppUser a = new AppUser();
        a.setEmail(authenticate.getPrincipal().toString());
        return JWTutil.generateToken(a.getUsername());
    }

    @Override
   public String SignUp(String Email, String Password, String Name, Role role) {
        //check weather user present in Db
        if(userRepository.existsByEmail(Email)){
            throw new RuntimeException("user already is present");
        }
        log.info("user not found in database");

        //encode the password
        var encodedpassowrd=passwordEncoder.encode(Password);

        //setting roles
        var authorites=new ArrayList<GrantedAuthority>();
        authorites.add(new SimpleGrantedAuthority("USER"));

        //create user object
        var user= AppUser.builder().Passwordd(encodedpassowrd).Name(Name).email(Email).AppUserRole(Role.USER).build();

        //save user
        userRepository.save(user);

        //generateToken
        var token=JWTutil.generateToken(Email);


        return token;
    }
}

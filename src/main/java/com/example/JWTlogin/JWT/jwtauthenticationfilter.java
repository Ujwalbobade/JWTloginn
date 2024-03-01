package com.example.JWTlogin.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class jwtauthenticationfilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //fetch token from request
        var jwttokenOptional=gettokenFromRequest(request);

        //validate JWT token ->JWT utils
        jwttokenOptional.ifPresent(jwttoken->{
            if(JWTutil.validatetoken(jwttoken)){
                //fetch username form token
                var usernameoptional=JWTutil.getusernameformtoken(jwttoken);

                usernameoptional.ifPresent(username->{ //fetch user details with help of username
                    var userDetails=userDetailsService.loadUserByUsername(username);

                    //create authentication token
                    var authenticatontoken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticatontoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //set authentication token to security context
                    SecurityContextHolder.getContext().setAuthentication(authenticatontoken);});


            }
        });

        //pass request and respsone to next filter
        filterChain.doFilter(request,response);

    }

    private Optional<String> gettokenFromRequest(HttpServletRequest request) {
        //extract authenticatiom headert

        var  authHeader = request.getHeader("Authorization");
        //Bearer <JWT token>
        if(StringUtils.hasText(authHeader)&& authHeader.startsWith("Bearer ")){
            return Optional.of(authHeader.substring(7));}
        return Optional.empty();
    }
    }
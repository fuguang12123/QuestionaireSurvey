package com.example.survey.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface    UserDataService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

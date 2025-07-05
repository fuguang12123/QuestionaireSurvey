package com.example.survey.service.ServiceImpl;

import com.example.survey.entity.User;
import com.example.survey.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority; // 引入GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority; // 引入SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List; // 引入List



@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // --- 关键修正：修正了之前的拼写错误 "nwoew" -> "new" ---
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Spring Security的角色通常需要一个 "ROLE_" 前缀
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

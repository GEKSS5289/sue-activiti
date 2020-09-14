package com.sue.sueactiviti7.security;

import com.sue.sueactiviti7.mapper.UserInfoMapper;
import com.sue.sueactiviti7.pojo.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author sue
 * @date 2020/9/13 16:09
 */

@Component
@Configuration
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserInfo userInfo = userInfoMapper.selectByUsername(s);
        if(userInfo == null){
            throw new  UsernameNotFoundException("数据库无此用户");
        }



        return userInfo;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.SecurityUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private SecurityUserService securityUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user= securityUserService.searchUser(username);
//        if (user==null) {
//            return null;
//        }

        System.out.println(user);


        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getKeyword()));
                System.out.println(permission.getKeyword());
            }
        }

        org.springframework.security.core.userdetails.User userDetails=
                new org.springframework.security.core.userdetails.User(username,user.getPassword(),
                        grantedAuthorities);

        return userDetails;
    }
}

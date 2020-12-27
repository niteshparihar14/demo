package com.ecom.demo.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ecom.demo.db.dao.UserDao;
import com.ecom.demo.db.model.MyUser;
import com.ecom.demo.db.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
    private UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        List<User> users = userRepository.findUserByPhone(username);
        if (CollectionUtils.isEmpty(users)) {
            throw new UsernameNotFoundException(username);
        }
        User user = users.get(0);
        return new MyUser(user);
    }
}

package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.CustomUserDetail;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()){
          User  user1 = user.get();
          if(!user1.isActive()){
              throw new RuntimeException("User is disabled by the admin!");
          }

        }
        user.orElseThrow(()-> new UsernameNotFoundException("User not found."));
        return user.map(CustomUserDetail::new).get();
    }
}

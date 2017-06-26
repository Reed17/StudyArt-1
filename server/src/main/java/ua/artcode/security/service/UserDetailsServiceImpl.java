package ua.artcode.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.model.User;
import ua.artcode.service.UserService;

import java.util.Collections;
import java.util.HashSet;

/**
 * Created by v21k on 06.06.17.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) {
        try {
            User user = userService.findByUserName(userName);

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getUserType().toString());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new HashSet<>(Collections.singleton(grantedAuthority)));

        } catch (InvalidUserLoginException e) {
            LOGGER.error(e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}

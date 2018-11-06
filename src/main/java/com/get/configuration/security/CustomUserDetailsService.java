package com.get.configuration.security;

import com.get.model.User;
import com.get.model.UserRole;
import com.get.service.UserRoleService;
import com.get.service.UserService;
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
import java.util.ArrayList;
import java.util.List;

/*
Core interface which loads user-specific data.
It is used throughout the framework as a user DAO and is the strategy used by the DaoAuthenticationProvider.
The interface requires only one read-only method, which simplifies support for new data-access strategies.*/
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    //Locates the user based on the username.
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        logger.info("User : {}", user);
        if (user == null) {
            logger.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(user));
    }
    
    /*Typically used in a pre-authenticated scenario,checking User based on his ROLE.
*/
    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (UserRole userRole : user.getUserRoles()) {
            logger.info("UserProfile : {}", userRole);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getName()));
        }
        logger.info("authorities : {}", authorities);
        return authorities;
    }

}

package service;

import lombok.AllArgsConstructor;
import model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName){
       Optional<User> optionalUser = userRepository.findByUsername(userName);
       User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("No User found with username : "+userName ));
       return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),user.isEnabled(),true,true,true,getAuthorities("USER"));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String role){
        return singletonList(new SimpleGrantedAuthority(role));
    }
}

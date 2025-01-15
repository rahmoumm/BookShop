package BookShop.demo.service;


import BookShop.demo.model.User;
import BookShop.demo.model.UserPrincipal;
import BookShop.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        log.info(user.getPassword());

        if( user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(user);
    }
}

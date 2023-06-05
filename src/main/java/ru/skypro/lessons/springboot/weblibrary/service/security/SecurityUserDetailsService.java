package ru.skypro.lessons.springboot.weblibrary.service.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.exceptions.IdNotFoundException;
import ru.skypro.lessons.springboot.weblibrary.model.security.AuthUser;
import ru.skypro.lessons.springboot.weblibrary.repository.security.AuthUserRepository;
import ru.skypro.lessons.springboot.weblibrary.security.SecurityUserPrincipal;

import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username){
        AuthUser user = authUserRepository.findByUsername(username);
        if(isNull(user)){
            throw new IdNotFoundException("");
        }
        return new SecurityUserPrincipal(user);
    }
}

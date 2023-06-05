package ru.skypro.lessons.springboot.weblibrary.security;

import org.springframework.security.core.GrantedAuthority;
import ru.skypro.lessons.springboot.weblibrary.model.security.Authorities;

public class SecurityGrantedAuthorities implements GrantedAuthority {
    private final String role;

    public SecurityGrantedAuthorities(Authorities authorities) {
        this.role = authorities.getRole();
    }

    @Override
    public String getAuthority() {
        return this.role;
    }
}

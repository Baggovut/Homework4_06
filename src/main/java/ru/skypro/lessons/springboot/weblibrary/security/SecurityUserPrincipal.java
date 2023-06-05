package ru.skypro.lessons.springboot.weblibrary.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.lessons.springboot.weblibrary.model.security.AuthUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUserPrincipal implements UserDetails {
    private final AuthUser user;
    private final List<SecurityGrantedAuthorities> authoritiesList;
    public SecurityUserPrincipal(AuthUser user) {
        this.user = user;
        this.authoritiesList = user.getAuthoritiesList().stream()
                .map(SecurityGrantedAuthorities::new)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authoritiesList);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled() == 1;
    }
}

package ru.skypro.lessons.springboot.weblibrary.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springboot.weblibrary.model.security.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {
    AuthUser findByUsername(String username);
}

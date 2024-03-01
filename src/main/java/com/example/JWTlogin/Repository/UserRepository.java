package com.example.JWTlogin.Repository;

import com.example.JWTlogin.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {

    @Override
    Optional<AppUser> findById(Long aLong);

    AppUser save(AppUser entity);

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}

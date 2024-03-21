package com.example.JWTlogin.Repository;

import com.example.JWTlogin.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {

    @Override
    Optional<AppUser> findById(Long aLong);

    @Override
    List<AppUser> findAll();

    AppUser save(AppUser entity);

    @Query("select a from AppUser a where a.email = ?1")
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}

package com.example.user_service.Repository;

import com.example.user_service.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update User u SET u.password = ?2 where u.username=?1")
    void updatePassword(String username, String password);

    @Modifying
    @Transactional
    @Query("update User u SET u.firstName = ?1, u.lastName = ?2, u.email = ?3, u.contactNumber = ?4 where u.username = ?5")
    void updateUserDetails(String firstName, String lastName, String email, String contactNumber, String username);
}

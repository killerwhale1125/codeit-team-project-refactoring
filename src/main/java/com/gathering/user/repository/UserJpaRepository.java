package com.gathering.user.repository;

import com.gathering.user.model.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public User findByUserName(String userName);

}

package com.luv2code.WebScraperDB1.repository;

import com.luv2code.WebScraperDB1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //No need to specify query it is managed by spring data JPA naming convention
    User findByUsername(String username);


}

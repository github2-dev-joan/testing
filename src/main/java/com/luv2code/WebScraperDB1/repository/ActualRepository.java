package com.luv2code.WebScraperDB1.repository;

import com.luv2code.WebScraperDB1.entity.Actual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActualRepository extends JpaRepository<Actual, Integer> {

}

package com.luv2code.WebScraperDB1.repository;

import com.luv2code.WebScraperDB1.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends JpaRepository<Logs, Integer> {

}

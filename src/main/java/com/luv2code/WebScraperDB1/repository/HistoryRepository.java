package com.luv2code.WebScraperDB1.repository;


import com.luv2code.WebScraperDB1.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> getByRevisionNo(long revision_no);
    @Query(value = "select * from bsh_curr_history where date(published_date) = :searchedDate", nativeQuery = true)
    List<History> getByDate (LocalDate searchedDate);
}

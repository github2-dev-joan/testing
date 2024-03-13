package com.luv2code.WebScraperDB1.service;


import com.luv2code.WebScraperDB1.entity.Actual;
import com.luv2code.WebScraperDB1.entity.History;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface HistoryService{
    void addDataToHistory (List<Actual> table);

    List<History> getHistoryByRevNo(long revisionNo);

    List<History> getHistoryByDate(LocalDate date);

    LocalDate formatStringToDate(String dateString, String pattern);


}

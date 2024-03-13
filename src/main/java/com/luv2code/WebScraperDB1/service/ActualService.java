package com.luv2code.WebScraperDB1.service;

import com.luv2code.WebScraperDB1.entity.Actual;

import java.time.LocalDateTime;
import java.util.List;

public interface ActualService {
    List<Actual> getTableFromWebsite(String website, String classToSearch, LocalDateTime publishedDate, long revisionNumber);

    LocalDateTime getWebsiteDateTime();

    LocalDateTime formatDateTime(String timeString, String formatFrom);


    boolean checkTimeBetweenExRates(LocalDateTime existingPublishedDate, LocalDateTime publishedDate);

    void addDataToActual(List<Actual> table);


    void deleteAllFromActual();

    List<Actual> getAllActual();



    void scheduled ();

}

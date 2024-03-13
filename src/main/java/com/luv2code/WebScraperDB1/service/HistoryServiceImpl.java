package com.luv2code.WebScraperDB1.service;


import com.luv2code.WebScraperDB1.entity.Actual;

import com.luv2code.WebScraperDB1.entity.History;
import com.luv2code.WebScraperDB1.repository.HistoryRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private ModelMapper modelMapper;

    public HistoryServiceImpl(HistoryRepository historyRepository, ModelMapper modelMapper) {
        this.historyRepository = historyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addDataToHistory(List<Actual> actualTable) {

        List<History> table = new ArrayList<>();
        System.out.println("Table param has size:" +actualTable.size() );

        for (Actual actual : actualTable) {
            System.out.println("In for:" + actual.getCurrency());
            History history = modelMapper.map(actual,History.class);
            historyRepository.save(history);
        }

    }

    @Override
    public List<History> getHistoryByRevNo(long revisionNo) {
        return historyRepository.getByRevisionNo(revisionNo);
    }

    @Override
    public List<History> getHistoryByDate(LocalDate date) {
        return historyRepository.getByDate(date);
    }

    @Override
    public LocalDate formatStringToDate(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateString, formatter);
    }



}
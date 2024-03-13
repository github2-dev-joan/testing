package com.luv2code.WebScraperDB1.service;

import com.luv2code.WebScraperDB1.entity.Logs;
import com.luv2code.WebScraperDB1.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LogsServiceImpl implements LogsService{

    @Autowired
    private LogsRepository logsRepository;

    public LogsServiceImpl(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    @Override
    public void addLogsToDB(Logs logToWrite) {
       logsRepository.save(logToWrite);

    }
}

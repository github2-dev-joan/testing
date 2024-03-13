package com.luv2code.WebScraperDB1.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "bsh_rep_logs")
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date_and_time")
    private LocalDateTime dateAndTime;
    @Column(name = "status")
    private String status;
    @Column(name = "level")
    private String loggingLevel;
    @Column(name = "message")
    private String message;

    public Logs(  String status, String loggingLevel, String message) {

        this.dateAndTime = LocalDateTime.now();
        this.status = status;
        this.loggingLevel = loggingLevel;
        this.message = message;
    }

    public Logs() {

    }


    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(String loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Logs{" +
                ", dateAndTime=" + dateAndTime +
                ", status='" + status + '\'' +
                ", loggingLevel='" + loggingLevel + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

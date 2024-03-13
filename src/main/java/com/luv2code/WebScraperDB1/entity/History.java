package com.luv2code.WebScraperDB1.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "bsh_curr_history")
public class History {
    @Id
    private int id;
    @Column(name = "currency")
    private String currency;
    @Column(name = "acronym")
    private String currencyAcronym;
    @Column(name = "ex_rate")
    private double exchangeRate;
    @Column(name = "difference")
    private double difference;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "revision_no")
    private long revisionNo;

    public History(String currency, String currencyAcronym, double exchangeRate, double difference, LocalDateTime publishedDate, long revisionNo) {
        this.currency = currency;
        this.currencyAcronym = currencyAcronym;
        this.exchangeRate = exchangeRate;
        this.difference = difference;
        this.publishedDate = publishedDate;
        this.revisionNo = revisionNo;
    }

    public History() {

    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyAcronym() {
        return currencyAcronym;
    }

    public void setCurrencyAcronym(String currencyAcronym) {
        this.currencyAcronym = currencyAcronym;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public long getRevisionNo() {
        return revisionNo;
    }

    public void setRevisionNo(long revisionNo) {
        this.revisionNo = revisionNo;
    }
}

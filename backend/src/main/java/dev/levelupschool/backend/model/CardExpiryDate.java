package dev.levelupschool.backend.model;

import lombok.Data;

public class CardExpiryDate {
    private int month;
    private int year;
    public CardExpiryDate(int month, int year) {
        this.month = month+1;
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month+1;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }
}

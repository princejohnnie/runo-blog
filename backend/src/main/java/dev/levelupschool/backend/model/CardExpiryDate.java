package dev.levelupschool.backend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardExpiryDate {
    private int month;
    private int year;
    public CardExpiryDate(int month, int year) {
        this.month = month;
        this.year = year;
    }

}

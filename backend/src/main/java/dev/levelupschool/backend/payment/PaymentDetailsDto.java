package dev.levelupschool.backend.payment;

import dev.levelupschool.backend.model.CardExpiryDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PaymentDetailsDto {
    @NotNull
    @Size(min = 16)
    public String cardNumber;

    @NotNull
    @Size(min = 2, max = 64)
    public String cardHolder;

    public CardExpiryDate cardExpiryDate;

    @NotNull
    @Size(min = 3, max = 4)
    public String cardCvv;

    @NotNull
    @Size(min = 4, max = 4)
    public String cardPin;

    @Email
    public String email;

    public int amount;

    public String phone;

    public String address;

    public String subscriptionType;
}

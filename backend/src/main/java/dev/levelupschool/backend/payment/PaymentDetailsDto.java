package dev.levelupschool.backend.payment;

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

    @NotNull
    @Size(min = 7, max = 7)
    public String cardExpiryDate; // YYYY-MM

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
}

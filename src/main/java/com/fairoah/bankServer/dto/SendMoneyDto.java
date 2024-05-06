package com.fairoah.bankServer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SendMoneyDto {
    private String to;
    private BigDecimal amount;
}

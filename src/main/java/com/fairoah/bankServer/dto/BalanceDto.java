package com.fairoah.bankServer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDto {
    private BigDecimal balance;
}

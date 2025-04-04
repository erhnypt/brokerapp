package com.brokerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    private Long id;
    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    private String description;
}

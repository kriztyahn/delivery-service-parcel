package com.mynt.deliveryparcelservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Voucher {

    private String code;

    private BigDecimal discount;

    private LocalDate expiry;
}

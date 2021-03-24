package com.mynt.deliveryparcelservice.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Parcel {

    private float weight;

    private float height;

    private float width;

    private float length;

    private String code;

    private boolean isRejected;

    private BigDecimal deliveryCost;
}

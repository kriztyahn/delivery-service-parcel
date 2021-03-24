package com.mynt.deliveryparcelservice.controller;

import com.mynt.deliveryparcelservice.dto.DeliveryCostDTO;
import com.mynt.deliveryparcelservice.exception.ApplicationException;
import com.mynt.deliveryparcelservice.model.Parcel;
import com.mynt.deliveryparcelservice.service.ParcelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ParcelController {

    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @PostMapping("/api/parcel/price")
    public ResponseEntity<DeliveryCostDTO> computeDeliveryCost(@RequestBody Parcel parcel) throws ApplicationException {
        BigDecimal deliveryCost = parcelService.getDiscountedDeliveryCost(parcel);
        DeliveryCostDTO deliveryCostDTO = new DeliveryCostDTO();
        deliveryCostDTO.setCurrency("PHP");
        deliveryCostDTO.setAmount(deliveryCost);

        return new ResponseEntity<>(deliveryCostDTO, HttpStatus.OK);
    }
}

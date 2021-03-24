package com.mynt.deliveryparcelservice.service;

import com.mynt.deliveryparcelservice.exception.ApplicationException;
import com.mynt.deliveryparcelservice.model.Parcel;
import com.mynt.deliveryparcelservice.model.Voucher;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class ParcelService {

    private final VoucherService voucherService;

    private final KieContainer kieContainer;

    public ParcelService(VoucherService voucherService, KieContainer kieContainer) {
        this.voucherService = voucherService;
        this.kieContainer = kieContainer;
    }

    public BigDecimal computeGrossDeliveryCost(Parcel parcel) throws ApplicationException {
        KieSession kieSession = kieContainer.newKieSession();

        kieSession.setGlobal("parcel", parcel);
        BigDecimal multiplier = BigDecimal.ZERO;
        kieSession.setGlobal("multiplier", multiplier);
        kieSession.setGlobal("parcelService", this);
        kieSession.insert(parcel);
        kieSession.fireAllRules();
        kieSession.dispose();

        if(parcel.isRejected()) {
            throw new ApplicationException("Parcel is Rejected!");
        }

        BigDecimal deliveryCost =  parcel.getDeliveryCost().setScale(2, RoundingMode.HALF_UP);

        return deliveryCost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getDiscountedDeliveryCost(Parcel parcel) throws ApplicationException {
        BigDecimal discount = BigDecimal.ZERO;
        Voucher voucher = new Voucher();

        BigDecimal deliveryCost = computeGrossDeliveryCost(parcel);

        if(null != parcel.getCode()) {
            voucher = voucherService.getVoucher(parcel.getCode(), "apikey");

            if(null != voucher && LocalDate.now().isAfter(voucher.getExpiry())) {
                throw new ApplicationException("Voucher code is already expired!");
            }

            discount = deliveryCost.multiply(voucher.getDiscount()).divide(new BigDecimal(100));
        }

        return deliveryCost.subtract(discount);
    }

    public BigDecimal getDiscountedDeliveryCost(Parcel parcel, Voucher voucher) throws ApplicationException {
        BigDecimal discount = BigDecimal.ZERO;

        BigDecimal deliveryCost = computeGrossDeliveryCost(parcel);

        if(null != parcel.getCode()) {
            voucher = voucherService.getVoucher(parcel.getCode(), "apikey");

            if(null != voucher && LocalDate.now().isAfter(voucher.getExpiry())) {
                throw new ApplicationException("Voucher code is already expired!");
            }

            discount = deliveryCost.multiply(voucher.getDiscount()).divide(new BigDecimal(100));
        }

        return deliveryCost.subtract(discount);
    }

    public float getVolume(Parcel parcel) {
        return parcel.getHeight() * parcel.getLength() * parcel.getWidth();
    }
}

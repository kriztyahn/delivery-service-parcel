package com.mynt.deliveryparcelservice.service;

import com.mynt.deliveryparcelservice.exception.ApplicationException;
import com.mynt.deliveryparcelservice.model.Parcel;
import com.mynt.deliveryparcelservice.model.Voucher;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ParcelServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private ParcelService parcelService;

    @Test()
    public void whenParcelIsRejected() {
        Parcel parcel = new Parcel();
        parcel.setWeight(51);
        parcel.setHeight(10);
        parcel.setLength(10);
        parcel.setWidth(10);

        assertThatThrownBy(() -> parcelService.getDiscountedDeliveryCost(parcel)).isInstanceOf(ApplicationException.class);
    }

    @Test()
    public void whenParcelIsHeavy() throws ApplicationException {
        Parcel parcel = new Parcel();
        parcel.setWeight(11);
        parcel.setHeight(10);
        parcel.setLength(25);
        parcel.setWidth(12.12f);

        BigDecimal cost =  parcelService.getDiscountedDeliveryCost(parcel);
        assertEquals(new BigDecimal(220).setScale(2), cost);
    }

    @Test()
    public void whenParcelIsSmall() throws ApplicationException {
        Parcel parcel = new Parcel();
        parcel.setWeight(5);
        parcel.setHeight(10);
        parcel.setLength(10);
        parcel.setWidth(10);

        BigDecimal cost =  parcelService.getDiscountedDeliveryCost(parcel);
        assertEquals(new BigDecimal(30).setScale(2, RoundingMode.HALF_UP), cost);
    }

    @Test()
    public void whenParcelIsMedium() throws ApplicationException {
        Parcel parcel = new Parcel();
        parcel.setWeight(5);
        parcel.setHeight(10);
        parcel.setLength(24);
        parcel.setWidth(10);

        BigDecimal cost =  parcelService.getDiscountedDeliveryCost(parcel);
        assertEquals(new BigDecimal(96).setScale(2, RoundingMode.HALF_UP), cost);
    }

    @Test()
    public void whenParcelIsLarge() throws ApplicationException {
        Parcel parcel = new Parcel();
        parcel.setWeight(5);
        parcel.setHeight(15);
        parcel.setLength(24);
        parcel.setWidth(10);

        BigDecimal cost =  parcelService.getDiscountedDeliveryCost(parcel);
        assertEquals(new BigDecimal(180).setScale(2, RoundingMode.HALF_UP), cost);
    }

    @Test
    public void whenVoucherIsExpired() {
        Parcel parcel = new Parcel();
        parcel.setWeight(5);
        parcel.setHeight(15);
        parcel.setLength(24);
        parcel.setWidth(10);
        parcel.setCode("MYNT");

        assertThatThrownBy(() -> parcelService.getDiscountedDeliveryCost(parcel)).isInstanceOf(ApplicationException.class);
    }

    @Test
    public void whenVoucherIsInvalid() {
        Parcel parcel = new Parcel();
        parcel.setWeight(5);
        parcel.setHeight(15);
        parcel.setLength(24);
        parcel.setWidth(10);
        parcel.setCode("MYN");

        assertThatThrownBy(() -> parcelService.getDiscountedDeliveryCost(parcel)).isInstanceOf(HttpClientErrorException.class);
    }

    @Test
    public void whenVoucherIsValid() {
        Parcel parcel = new Parcel();
        parcel.setWeight(5);
        parcel.setHeight(15);
        parcel.setLength(24);
        parcel.setWidth(10);
        parcel.setCode("MYN");

        VoucherService voucherService  = mock(VoucherService.class);
        Voucher voucher = new Voucher();
        voucher.setCode(parcel.getCode());
        voucher.setDiscount(new BigDecimal(12.12));
        voucher.setExpiry(LocalDate.now().plusDays(3));

        when(voucherService.getVoucher(parcel.getCode(), "apikey")).thenReturn(voucher);

        assertThatThrownBy(() -> parcelService.getDiscountedDeliveryCost(parcel, voucher)).isInstanceOf(HttpClientErrorException.class);
    }
}

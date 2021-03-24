package com.mynt.deliveryparcelservice.service;

import com.mynt.deliveryparcelservice.model.Voucher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherService {

    private final String VOUCHER_BASE_URL = "https://mynt-exam.mocklab.io/voucher";

    public Voucher getVoucher(String code, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();
        String url = VOUCHER_BASE_URL + "/" + code + "?key=" + apiKey;

        return  restTemplate.getForObject(url, Voucher.class);
    }
}

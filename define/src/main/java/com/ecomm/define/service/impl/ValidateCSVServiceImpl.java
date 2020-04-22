package com.ecomm.define.service.impl;

import com.ecomm.define.domain.BigCommerceProducts;
import com.ecomm.define.repository.BigCommerceProductRepository;
import com.ecomm.define.service.ValidateCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 21/04/2020.
 */
@Service
public class ValidateCSVServiceImpl implements ValidateCSVService {

    private static final String BIG_COMMERCE_CSV = "big-commerce.csv";
    @Autowired
    private BigCommerceProductRepository repository;

    @Override
    public void validate(List<BigCommerceProducts> bigCommerceProductsList) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setRoundingMode(RoundingMode.CEILING);
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(0);
        for (BigCommerceProducts bdProduct : bigCommerceProductsList) {

            if(bdProduct.getMspPrice()!= null && !bdProduct.getMspPrice().isEmpty() &&  !(bdProduct.getMspPrice().equals("N/A")) )
            {
                BigDecimal price_ = new BigDecimal(bdProduct.getMspPrice());
                String roundedPrice = nf.format(price_);
                bdProduct.setMspPrice(roundedPrice);
            }

        }

    }
}

package com.ecomm.define.service.impl;

import com.ecomm.define.domain.BigCommerceProducts;
import com.ecomm.define.repository.BigCommerceProductRepository;
import com.ecomm.define.service.ValidateCSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        nf.setMaximumFractionDigits(0);
        for (BigCommerceProducts bdProduct : bigCommerceProductsList) {
            System.out.println("Trade Price Removed **********"+bdProduct.getTitle());


            if(bdProduct.getMspPrice()!= null && !bdProduct.getMspPrice().isEmpty() &&  !(bdProduct.getMspPrice().equals("N/A")) )
            {
                double price_ = Double.valueOf(bdProduct.getMspPrice());
                String roundedPrice = nf.format(price_);
                bdProduct.setMspPrice(roundedPrice);
            }

          /*  if (bdProduct.getTradePrice() != null && !bdProduct.getTradePrice().isEmpty()) {
                double tradePrice_ = Double.valueOf(bdProduct.getTradePrice());
                String roundedTradePrice = nf.format(tradePrice_);
                bdProduct.setTradePrice(roundedTradePrice);
            } */
        }
        System.out.println("Trade Price not required *******Validating End*******");

    }
}

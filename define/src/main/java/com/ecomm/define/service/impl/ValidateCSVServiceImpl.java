package com.ecomm.define.service.impl;

import com.ecomm.define.domain.BigCommerceProduct;
import com.ecomm.define.service.ValidateCSVService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vamshikirangullapelly on 21/04/2020.
 */
@Service
public class ValidateCSVServiceImpl implements ValidateCSVService {
    public static final String NOT_APPLICABLE = "N/A";
    private final NumberFormat FORMAT = NumberFormat.getNumberInstance();

    @Override
    public List<BigCommerceProduct> validate(List<BigCommerceProduct> bigCommerceProductList) {
        FORMAT.setRoundingMode(RoundingMode.CEILING);
        FORMAT.setGroupingUsed(false);
        FORMAT.setMaximumFractionDigits(0);

        List<BigCommerceProduct> updatedProductList = bigCommerceProductList.stream()
                .parallel()
                .filter(product -> product.getMspPrice() != null && !product.getMspPrice().isEmpty() && !(product.getMspPrice().equals(NOT_APPLICABLE)))
                .map(product -> setMspTradePrice(product))
                .collect(Collectors.toList());
        return updatedProductList;

    }

    private BigCommerceProduct setMspTradePrice(BigCommerceProduct product) {
        BigDecimal mspPrice = new BigDecimal(product.getMspPrice());
        String roundedMspPrice = FORMAT.format(mspPrice);
        product.setMspPrice(roundedMspPrice);
        return product;
    }

}

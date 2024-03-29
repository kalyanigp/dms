package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceCsvProduct;
import com.ecomm.define.platforms.bigcommerce.service.ValidateCSVService;
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
    private final NumberFormat FORMAT = NumberFormat.getNumberInstance();

    @Override
    public List<BigCommerceCsvProduct> validate(List<BigCommerceCsvProduct> bigCommerceCsvProductList) {
        FORMAT.setRoundingMode(RoundingMode.CEILING);
        FORMAT.setGroupingUsed(false);
        FORMAT.setMaximumFractionDigits(0);

        List<BigCommerceCsvProduct> updatedProductList = bigCommerceCsvProductList.stream()
                .map(product -> setPrices(product))
                .collect(Collectors.toList());
        return updatedProductList;

    }

    private BigCommerceCsvProduct setPrices(BigCommerceCsvProduct product) {
        final String tradePrice = product.getTradePrice();
        final String mspPrice = product.getMspPrice();

        if (tradePrice != null && !tradePrice.isEmpty()) {
            BigDecimal tradePrice_ = new BigDecimal(product.getTradePrice());
            String roundedTradePrice = FORMAT.format(tradePrice_);
            product.setTradePrice(roundedTradePrice);
            product.setMspPrice(String.valueOf(Integer.parseInt(roundedTradePrice) * 3));
        }

        if (mspPrice != null && !mspPrice.isEmpty() && !mspPrice.equals("N/A") && !mspPrice.equals("0")) {
            BigDecimal mspPrice_ = new BigDecimal(mspPrice);
            String roundedMspPrice = FORMAT.format(mspPrice_);
            product.setMspPrice(roundedMspPrice);
        }
        return product;
    }
}

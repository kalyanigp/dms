package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
@Qualifier("hillInteriorDataService")
public class GenerateBCHillInteriorDataServiceImpl implements GenerateBCDataService<HillInteriorProduct> {

    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCHillInteriorDataServiceImpl.class);

    private final BigCommerceApiService bigCommerceApiService;

    private final BigcBrandApiRepository brandApiRepository;

    private final MongoOperations mongoOperations;

    @Autowired
    public GenerateBCHillInteriorDataServiceImpl(BigCommerceApiService bigCommerceApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void generateBcProductsFromSupplier(List<HillInteriorProduct> productList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<HillInteriorProduct> updatedCatalogList = productList
                .stream()
                .filter(Objects::nonNull)
                .filter(HillInteriorProduct::isUpdated)
                .collect(Collectors.toList());


        updatedCatalogList.parallelStream().forEach(hillInteriorProduct -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(BcConstants.HILL_INTERIOR + hillInteriorProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(hillInteriorProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(hillInteriorProduct.getProductName()));
                byProductSku.setImageList(hillInteriorProduct.getImages());

                byProductSku.setSku(BcConstants.HILL_INTERIOR + hillInteriorProduct.getSku());
                if(!hillInteriorProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + hillInteriorProduct.getProductName());
                }
                byProductSku.setSupplier(Supplier.HILL_INTERIORS.getName());
                byProductSku.setType(BcConstants.TYPE);

                byProductSku = evaluateDescription(hillInteriorProduct, byProductSku);

                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                byProductSku.setUpc(hillInteriorProduct.getEan());
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                if(!hillInteriorProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + hillInteriorProduct.getProductName());
                }
                setPriceAndQuantity(hillInteriorProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(hillInteriorProduct.getProductName()));
                byProductSku.setImageList(hillInteriorProduct.getImages());
                byProductSku = evaluateDescription(hillInteriorProduct, byProductSku);
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        });
        bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.HILL_INTERIOR, HillInteriorProduct.class);
    }

    private BcProductData evaluateDescription(HillInteriorProduct hillInteriorProduct, BcProductData byProductSku) {
        StringBuilder discriptionBuilder = new StringBuilder();
        if (hillInteriorProduct.getDescription() != null && !hillInteriorProduct.getDescription().isEmpty()) {
            discriptionBuilder.append(hillInteriorProduct.getDescription());
        }
        discriptionBuilder.append("  Dimensions - (");
        byProductSku.setWeight(null);
        byProductSku.setHeight(null);
        byProductSku.setWidth(null);
        byProductSku.setDepth(null);

        if (hillInteriorProduct.getWeight() != null) {
            discriptionBuilder.append(" Weight : ").append(hillInteriorProduct.getWeight()).append("kg");
        }
        if (hillInteriorProduct.getHeight() != null) {
            discriptionBuilder.append(" Height : ").append(hillInteriorProduct.getHeight()).append("cm");
        }
        if (hillInteriorProduct.getWidth() != null) {
            discriptionBuilder.append(" Width : ").append(hillInteriorProduct.getWidth()).append("cm");
        }
        if (hillInteriorProduct.getDepth() != null) {
            discriptionBuilder.append(" Depth : ").append(hillInteriorProduct.getDepth()).append("cm)");
        }
        if (hillInteriorProduct.getFinish() != null && !hillInteriorProduct.getFinish().isEmpty()) {
            discriptionBuilder.append("  Finish - ").append(hillInteriorProduct.getFinish());
        }
        if (hillInteriorProduct.getColour() != null && !hillInteriorProduct.getColour().isEmpty()) {
            discriptionBuilder.append("  Colour - ").append(hillInteriorProduct.getColour());
        }

        byProductSku.setDescription(discriptionBuilder.toString());

        return byProductSku;
    }


    private void processDiscontinuedCatalog(List<HillInteriorProduct> productList) {
        List<HillInteriorProduct> discontinuedList = productList
                .stream()
                .filter(HillInteriorProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(hillInteriorProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.HILL_INTERIOR + hillInteriorProduct.getSku()));
    }

    private void setPriceAndQuantity(HillInteriorProduct hillInteriorProduct, BcProductData byProductSku) {
        evaluatePrice(hillInteriorProduct, byProductSku);
        byProductSku.setInventoryLevel(Math.max(hillInteriorProduct.getStockLevel(), 0));
        byProductSku.setAvailability(BcConstants.PREORDER);
        byProductSku.setAvailabilityDescription("Usually dispatches on or after " + hillInteriorProduct.getStockExpectedOn());
        if (hillInteriorProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
            byProductSku.setAvailabilityDescription("Usually dispatches in 5 to 7 working days.");
        }
    }

    private void evaluatePrice(HillInteriorProduct hillInteriorProduct, BcProductData byProductSku) {
        BigDecimal salePrice = hillInteriorProduct.getSalePrice();
        if (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(salePrice.intValue());
        }
    }
}
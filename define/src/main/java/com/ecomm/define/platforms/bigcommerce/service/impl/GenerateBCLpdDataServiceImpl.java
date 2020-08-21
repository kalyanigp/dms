package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.lpdfurniture.domain.LpdProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecomm.define.platforms.commons.BCUtils.setInventoryParameters;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
@Qualifier("lpdDataService")
public class GenerateBCLpdDataServiceImpl implements GenerateBCDataService<LpdProduct> {

    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCLpdDataServiceImpl.class);

    private final BigCommerceApiService bigCommerceApiService;

    private final BigcBrandApiRepository brandApiRepository;

    private final MongoOperations mongoOperations;

    @Autowired
    public GenerateBCLpdDataServiceImpl(BigCommerceApiService bigCommerceApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void generateBcProductsFromSupplier(List<LpdProduct> productList) {

        LOGGER.info("Started processing Lpd Products. Total modified product {}", productList.size());
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<LpdProduct> updatedCatalogList = productList
                .stream()
                .filter(lpdProduct -> Objects.nonNull(lpdProduct))
                .filter(lpdProduct -> Objects.nonNull(lpdProduct.getPrice()))
                .filter(lpdProduct -> Objects.nonNull(lpdProduct.getWeight()))
                .collect(Collectors.toList());

        try {
            updatedCatalogList.parallelStream().forEach(lpdProduct -> {
                Query query = new Query();
                query.addCriteria(Criteria.where("sku").is(BcConstants.LPD_FURNITURE + lpdProduct.getSku()));
                BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

                if (byProductSku == null) {
                    LOGGER.info("Product not available in BcProductData, creating one for the sku {} & productName {}", BcConstants.LPD_FURNITURE + lpdProduct.getSku(), lpdProduct.getProductName());
                    byProductSku = new BcProductData();
                    setPriceAndQuantity(lpdProduct, byProductSku);
                    byProductSku.setCategories(BCUtils.assignCategories(lpdProduct.getProductName()));
                    byProductSku.setImageList(lpdProduct.getImages());

                    byProductSku.setSku(BcConstants.LPD_FURNITURE + lpdProduct.getSku());
                    if (!lpdProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                        byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + lpdProduct.getProductName());
                    }
                    byProductSku.setSupplier(Supplier.LPD.getName());
                    byProductSku.setType(BcConstants.TYPE);

                    evaluateDescription(lpdProduct, byProductSku);

                    byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                    Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                    if (byName.isPresent()) {
                        byProductSku.setBrandId(byName.get().getId());
                    }
                    byProductSku.setUpc(lpdProduct.getEan());
                    BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                } else {
                    LOGGER.info("Product available with the sku {} , so updating with price and stock.", BcConstants.LPD_FURNITURE + lpdProduct.getSku());
                    if (!lpdProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                        byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + lpdProduct.getProductName());
                    }
                    setPriceAndQuantity(lpdProduct, byProductSku);
                    byProductSku.setCategories(BCUtils.assignCategories(lpdProduct.getProductName()));
                    byProductSku.setImageList(lpdProduct.getImages());
                    evaluateDescription(lpdProduct, byProductSku);
                    BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                }

            });
            LOGGER.info("Finished updating BcProductData.");
            bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.LPD_FURNITURE, LpdProduct.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("ERROR while processing LPD to BCProduct");

        }
    }

    private BcProductData evaluateDescription(LpdProduct lpdProduct, BcProductData byProductSku) {
        StringBuilder discriptionBuilder = new StringBuilder();
        if (lpdProduct.getDescription() != null && !lpdProduct.getDescription().isEmpty()) {
            discriptionBuilder.append(lpdProduct.getDescription());
        }
        discriptionBuilder.append(" <br> Dimensions: ");

        if (lpdProduct.getWeight() != null) {
            int weight = lpdProduct.getWeight().intValue();
            byProductSku.setWeight(weight);
            discriptionBuilder.append("  <br> Weight : ").append(lpdProduct.getWeight()).append("kg");
        }
        if (lpdProduct.getHeight() != null) {
            int height = lpdProduct.getHeight().intValue();
            byProductSku.setHeight(height);
            discriptionBuilder.append("  <br> Height : ").append(lpdProduct.getHeight()).append("mm");
        }
        if (lpdProduct.getWidth() != null) {
            int width = lpdProduct.getWidth().intValue();
            byProductSku.setWidth(width);
            discriptionBuilder.append("  <br> Width : ").append(lpdProduct.getWidth()).append("mm");
        }
        if (lpdProduct.getDepth() != null) {
            int depth = lpdProduct.getDepth().intValue();
            byProductSku.setDepth(depth);
            discriptionBuilder.append("  <br> Depth : ").append(lpdProduct.getDepth()).append("mm");
        }
        if (lpdProduct.getFinish() != null && !lpdProduct.getFinish().isEmpty()) {
            discriptionBuilder.append("  <br> Finish - ").append(lpdProduct.getFinish());
        }
        if (lpdProduct.getColour() != null && !lpdProduct.getColour().isEmpty()) {
            discriptionBuilder.append("  <br> Colour - ").append(lpdProduct.getColour());
        }

        byProductSku.setDescription(discriptionBuilder.toString());
        return byProductSku;
    }

    private void processDiscontinuedCatalog(List<LpdProduct> productList) {
        List<LpdProduct> discontinuedList = productList
                .stream()
                .filter(LpdProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(lpdProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.LPD_FURNITURE + lpdProduct.getSku()));
    }

    private void setPriceAndQuantity(LpdProduct lpdProduct, BcProductData byProductSku) {
        evaluatePrice(lpdProduct, byProductSku);
        int stockLevel = lpdProduct.getStockLevel() == null? 0 : lpdProduct.getStockLevel();
        byProductSku.setInventoryLevel(Math.max(stockLevel, 0));
        if (stockLevel <= 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(BcConstants.RELEASE_DATE_FORMAT);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, 90);
            Date date = calendar.getTime();
            try {
                byProductSku.setPreorderReleaseDate(formatter.format(date));
            } catch (Exception exception) {
                LOGGER.error("Error while processing Preorder release date" + exception.getMessage());
            }
            byProductSku.setAvailabilityDescription("Will be dispatched on or after " + date);
        } else {
            byProductSku.setAvailabilityDescription("Usually dispatches in 15 to 20 working days by our own 2 man delivery service.");
        }
        setInventoryParameters(stockLevel, byProductSku);
    }

    private void evaluatePrice(LpdProduct lpdProduct, BcProductData byProductSku) {
        BigDecimal salePrice = lpdProduct.getSalePrice();
        if (salePrice != null && salePrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(salePrice.intValue());
        }
    }
}
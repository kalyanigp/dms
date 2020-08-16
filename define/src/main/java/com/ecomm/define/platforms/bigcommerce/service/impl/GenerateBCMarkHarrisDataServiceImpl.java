package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
@Qualifier("markHarrisDataService")
public class GenerateBCMarkHarrisDataServiceImpl implements GenerateBCDataService<MarkHarrisProduct> {

    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCMarkHarrisDataServiceImpl.class);
    private final BigCommerceApiService bigCommerceApiService;
    private final BigcBrandApiRepository brandApiRepository;
    private final MongoOperations mongoOperations;
    @Value("${bigcommerce.markharris.profit.limit.high}")
    private String higherLimitHDPrice;
    @Value("${bigcommerce.markharris.profit.percentage.low}")
    private String percentageLow;

    @Autowired
    public GenerateBCMarkHarrisDataServiceImpl(BigCommerceApiService bigCommerceApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void generateBcProductsFromSupplier(List<MarkHarrisProduct> productList) {
        try {
            //Process Discontinued catalog
            processDiscontinuedCatalog(productList);

            //Process updated catalog, if there is any updates available in price & stock & images.
            List<BcProductData> updatedBcProductDataList = new ArrayList<>();
            List<MarkHarrisProduct> updatedCatalogList = productList
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(MarkHarrisProduct::isUpdated)
                    .collect(Collectors.toList());
            for (MarkHarrisProduct markHarrisProduct : updatedCatalogList) {
                Query query = new Query();
                query.addCriteria(Criteria.where("sku").is(BcConstants.MARK_HARRIS + markHarrisProduct.getSku()));
                BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

                if (byProductSku == null) {
                    byProductSku = new BcProductData();
                    setPriceAndQuantity(markHarrisProduct, byProductSku);
                    byProductSku.setCategories(BCUtils.assignCategories(markHarrisProduct.getProductName()));
                    byProductSku.setImageList(markHarrisProduct.getImages());
                    byProductSku.setSku(BcConstants.MARK_HARRIS + markHarrisProduct.getSku());
                    if(!markHarrisProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                        byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + markHarrisProduct.getProductName());
                    }

                    byProductSku.setSupplier(Supplier.MAR_HARRIS.getName());
                    byProductSku.setType(BcConstants.TYPE);

                    if (!markHarrisProduct.getWeight().isEmpty()) {
                        byProductSku.setWeight(Objects.requireNonNull(new BigDecimal(markHarrisProduct.getWeight()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());
                    }
                    if (!markHarrisProduct.getMaxHeight().isEmpty()) {
                        byProductSku.setHeight(Objects.requireNonNull(new BigDecimal(markHarrisProduct.getMaxHeight()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());

                    }

                    if (!markHarrisProduct.getMaxWidth().isEmpty()) {
                        byProductSku.setWidth(Objects.requireNonNull(new BigDecimal(markHarrisProduct.getMaxWidth()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());

                    }
                    if (!markHarrisProduct.getMaxLengthOrDepth().isEmpty()) {

                        byProductSku.setDepth(Objects.requireNonNull(new BigDecimal(markHarrisProduct.getMaxLengthOrDepth()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());
                    }

                    byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                    Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                    if (byName.isPresent()) {
                        byProductSku.setBrandId(byName.get().getId());
                    }
                    StringBuilder additionalDescription = new StringBuilder();
                    if (markHarrisProduct.getDescription() != null) {
                        additionalDescription.append(markHarrisProduct.getDescription());
                    }
                    additionalDescription.append("(Product Dimensions - (");
                    if (markHarrisProduct.getMaxHeight() != null && !markHarrisProduct.getMaxHeight().isEmpty()) {
                        additionalDescription.append(" Maximum Height - ").append(markHarrisProduct.getMaxHeight()).append("cm");
                    }
                    if (markHarrisProduct.getMinHeight() != null && !markHarrisProduct.getMinHeight().isEmpty()) {
                        additionalDescription.append(" Minimum Height - ").append(markHarrisProduct.getMinHeight()).append("cm");
                    }
                    if (markHarrisProduct.getMaxWidth() != null && !markHarrisProduct.getMaxWidth().isEmpty()) {
                        additionalDescription.append(" Maximum Width -  ").append(markHarrisProduct.getMaxWidth()).append("cm");
                    }
                    if (markHarrisProduct.getMinWidth() != null && !markHarrisProduct.getMinWidth().isEmpty()) {
                        additionalDescription.append(" Minimum Width -  ").append(markHarrisProduct.getMinWidth()).append("cm");
                    }
                    if (markHarrisProduct.getMaxLengthOrDepth() != null && !markHarrisProduct.getMaxLengthOrDepth().isEmpty()) {
                        additionalDescription.append(" Maximum Length/Depth -  ").append(markHarrisProduct.getMaxLengthOrDepth()).append("cm");
                    }
                    if (markHarrisProduct.getMinLengthOrDepth() != null && !markHarrisProduct.getMinLengthOrDepth().isEmpty()) {
                        additionalDescription.append(" Minimum Length/Depth -  ").append(markHarrisProduct.getMinLengthOrDepth()).append("cm )");
                    }
                    additionalDescription.append(")");

                    if (markHarrisProduct.getWeight() != null && !markHarrisProduct.getWeight().isEmpty() && !markHarrisProduct.getWeight().equals("0")) {
                        additionalDescription.append("  Product Weight -  ").append(markHarrisProduct.getWeight()).append("kg ");
                    }

                    if (markHarrisProduct.getAssembled() != null && !markHarrisProduct.getAssembled().isEmpty()) {
                        additionalDescription.append(" Assembly Instructions : ").append(markHarrisProduct.getAssembled());
                    }

                    byProductSku.setDescription(additionalDescription.toString());
                    BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                    LOGGER.info("Successfully created BCProductData for {}", markHarrisProduct.getSku());
                } else {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + markHarrisProduct.getProductName());
                    setPriceAndQuantity(markHarrisProduct, byProductSku);
                    byProductSku.setCategories(BCUtils.assignCategories(markHarrisProduct.getProductName()));
                    byProductSku.setImageList(markHarrisProduct.getImages());
                    BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                    LOGGER.info("Successfully updated BCProductData for {}", markHarrisProduct.getSku());
                }
            }
            bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.MARK_HARRIS, MarkHarrisProduct.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
        }
    }


    private void processDiscontinuedCatalog(List<MarkHarrisProduct> productList) {
        List<MarkHarrisProduct> discontinuedList = productList
                .stream()
                .filter(MarkHarrisProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(markHarrisProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.MARK_HARRIS + markHarrisProduct.getSku()));
    }

    private void setPriceAndQuantity(MarkHarrisProduct markHarrisProduct, BcProductData byProductSku) {
        evaluatePrice(markHarrisProduct, byProductSku);
        byProductSku.setInventoryLevel(Math.max(markHarrisProduct.getStockLevel(), 0));
        byProductSku.setAvailability(BcConstants.PREORDER);
        byProductSku.setAvailabilityDescription("Usually dispatches on or after " + markHarrisProduct.getNextArrival());
        if (markHarrisProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        }
    }

    private void evaluatePrice(MarkHarrisProduct markHarrisProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = markHarrisProduct.getSalePrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal retailPrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setRetailPrice(retailPrice.intValue());
            }
        }
    }
}
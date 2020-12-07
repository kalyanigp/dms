package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.coreproducts.domain.CoreProduct;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
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
@Qualifier("coreProductDataService")
public class GenerateBCCoreProductsDataServiceImpl implements GenerateBCDataService<CoreProduct> {

    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCCoreProductsDataServiceImpl.class);
    private final BigCommerceApiService bigCommerceApiService;
    private final BigcBrandApiRepository brandApiRepository;
    private final MongoOperations mongoOperations;
    @Value("${bigcommerce.coreproduct.profit.limit.high}")
    private String higherLimitHDPrice;
    @Value("${bigcommerce.coreproduct.profit.percentage.low}")
    private String percentageLow;
    @Value("${bigcommerce.coreproduct.profit.percentage.high}")
    private String percentageHigh;

    @Autowired
    public GenerateBCCoreProductsDataServiceImpl(BigCommerceApiService bigCommerceApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void generateBcProductsFromSupplier(List<CoreProduct> productList) {
        try {
            //Process Discontinued catalog
            processDiscontinuedCatalog(productList);

            //Process updated catalog, if there is any updates available in price & stock & images.
            List<BcProductData> updatedBcProductDataList = new ArrayList<>();
            List<CoreProduct> updatedCatalogList = productList
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(CoreProduct::isUpdated)
                    .collect(Collectors.toList());
            for (CoreProduct coreProduct : updatedCatalogList) {
                Query query = new Query();
                query.addCriteria(Criteria.where("sku").is(BcConstants.CORE_PRODUCT + coreProduct.getSku()));
                BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

                if (byProductSku == null) {
                    byProductSku = new BcProductData();
                    setPriceAndQuantity(coreProduct, byProductSku);
                    byProductSku.setCategories(BCUtils.assignCategories(coreProduct.getProductName()));
                    byProductSku.setImageList(coreProduct.getImages());
                    byProductSku.setSku(BcConstants.CORE_PRODUCT + coreProduct.getSku());
                    if(!coreProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                        byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + coreProduct.getProductName());
                    }

                    byProductSku.setSupplier(Supplier.CORE_PRODUCT.getName());
                    byProductSku.setType(BcConstants.TYPE);

                    if (!coreProduct.getWeight().isEmpty()) {
                        byProductSku.setWeight(Objects.requireNonNull(new BigDecimal(coreProduct.getWeight()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());
                    }

                    byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                    Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                    if (byName.isPresent()) {
                        byProductSku.setBrandId(byName.get().getId());
                    }
                    setDescription(coreProduct, byProductSku);
                    BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                    LOGGER.info("Successfully created BCProductData for {}", coreProduct.getSku());
                } else {
                    if(!coreProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                        byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + coreProduct.getProductName());
                    }
                    setPriceAndQuantity(coreProduct, byProductSku);
                    setDescription(coreProduct, byProductSku); //One time execution to reset the data

                    byProductSku.setCategories(BCUtils.assignCategories(coreProduct.getProductName()));
                    byProductSku.setImageList(coreProduct.getImages());
                    if (!coreProduct.getWeight().isEmpty()) {
                        byProductSku.setWeight(Objects.requireNonNull(new BigDecimal(coreProduct.getWeight()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());
                    }
                    BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                    LOGGER.info("Successfully updated BCProductData for {}", coreProduct.getSku());
                }
            }
            bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.CORE_PRODUCT, CoreProduct.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
            ex.printStackTrace();
        }
    }

    private void setDescription(CoreProduct coreProduct, BcProductData byProductSku) {
        StringBuilder additionalDescription = new StringBuilder();
        if (!StringUtils.isEmpty(coreProduct.getDescription() )) {
            additionalDescription.append(coreProduct.getDescription());
        }
        additionalDescription.append("<br>Features - ");
        if (!StringUtils.isEmpty(coreProduct.getFeature1())) {
            additionalDescription.append("<br> Feature 1 - ").append(coreProduct.getFeature1());
        }
        if (!StringUtils.isEmpty(coreProduct.getFeature2())) {
            additionalDescription.append("<br> Feature 2 - ").append(coreProduct.getFeature2());
        }
        if (!StringUtils.isEmpty(coreProduct.getFeature3())) {
            additionalDescription.append("<br> Feature 3 - ").append(coreProduct.getFeature3());
        }
        if (!StringUtils.isEmpty(coreProduct.getFeature4())) {
            additionalDescription.append("<br> Feature 4 - ").append(coreProduct.getFeature4());
        }

        additionalDescription.append("<br>Product Dimensions - ");
        if (!StringUtils.isEmpty(coreProduct.getHeight())) {
            additionalDescription.append("<br> Height - ").append(coreProduct.getHeight()).append("mm");
        }
        if (!StringUtils.isEmpty(coreProduct.getWidth())) {
            additionalDescription.append("<br> Width -  ").append(coreProduct.getWidth()).append("mm");
        }
        if (!StringUtils.isEmpty(coreProduct.getDepth())) {
            additionalDescription.append("<br> Depth -  ").append(coreProduct.getDepth()).append("mm");
        }
        additionalDescription.append("<br>");

        if (!StringUtils.isEmpty(coreProduct.getWeight()) && !coreProduct.getWeight().equals("0")) {
            additionalDescription.append(" <br> Product Weight -  ").append(coreProduct.getWeight()).append("kg ");
        }

        if (!StringUtils.isEmpty(coreProduct.getConstruction())) {
            additionalDescription.append(" <br> Construction : ").append(coreProduct.getConstruction());
        }

        if (!StringUtils.isEmpty(coreProduct.getAssemblyInstructions())) {
            additionalDescription.append(" <br> Assembly Instructions : ").append(coreProduct.getAssemblyInstructions());
        }
        additionalDescription.append("<br> "+byProductSku.getAvailabilityDescription());
        byProductSku.setDescription(additionalDescription.toString());
    }


    private void processDiscontinuedCatalog(List<CoreProduct> productList) {
        List<CoreProduct> discontinuedList = productList
                .stream()
                .filter(CoreProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(coreProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.CORE_PRODUCT + coreProduct.getSku()));
    }

    private void setPriceAndQuantity(CoreProduct coreProduct, BcProductData byProductSku) {
        if (coreProduct.getSalePrice() != null) {
            byProductSku.setPrice(coreProduct.getSalePrice().intValue());
        }
        byProductSku.setInventoryLevel(Math.max(coreProduct.getStockLevel(), 0));
        setInventoryParameters(coreProduct.getStockLevel(), byProductSku);

        if (coreProduct.getStockLevel() > 0) {
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        } else {
            byProductSku.setAvailabilityDescription("Will be dispatched on or after " + coreProduct.getNextArrival());
            if (!StringUtils.isEmpty(coreProduct.getNextArrival()) && coreProduct.getNextArrival().contains("day")) {
                SimpleDateFormat formatter = new SimpleDateFormat(BcConstants.RELEASE_DATE_FORMAT);
                GregorianCalendar calendar;
                String strDate = coreProduct.getNextArrival().substring(coreProduct.getNextArrival().indexOf(",")+1);
                int monthOffSet = strDate.trim().indexOf(" ")+1;
                Calendar cal2 = Calendar.getInstance();
                try {
                    cal2.setTime(new SimpleDateFormat("MMM").parse(strDate.trim().substring(0,monthOffSet)));
                } catch (ParseException exception) {
                    LOGGER.error("Error while processing Pre-order release date" + exception.getMessage());
                    exception.printStackTrace();
                }
                int month = cal2.get(Calendar.MONTH);
                int day = Integer.parseInt(strDate.substring(strDate.indexOf(",")-2, strDate.indexOf(",")));

                calendar = new GregorianCalendar(BcConstants.CURRENT_YEAR, month, day, 00, 00, 00);
                Date date = calendar.getTime();
                try {
                    byProductSku.setPreorderReleaseDate(formatter.format(date));
                } catch (Exception exception) {
                    LOGGER.error("Error while processing Preorder release date" + exception.getMessage());
                }
            }
        }
    }
}
package com.ecomm.define.platforms.bigcommerce.service.impl;

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
    @Value("${bigcommerce.markharris.profit.percentage.high}")
    private String percentageHigh;

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

                    byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                    Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                    if (byName.isPresent()) {
                        byProductSku.setBrandId(byName.get().getId());
                    }
                    setDescription(markHarrisProduct, byProductSku);
                    BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                    LOGGER.info("Successfully created BCProductData for {}", markHarrisProduct.getSku());
                } else {
                    if(!markHarrisProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                        byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + markHarrisProduct.getProductName());
                    }
                    setPriceAndQuantity(markHarrisProduct, byProductSku);
                    setDescription(markHarrisProduct, byProductSku); //One time execution to reset the data
                    byProductSku.setCategories(null);

                    byProductSku.setCategories(BCUtils.assignCategories(markHarrisProduct.getProductName()));
                    byProductSku.setImageList(markHarrisProduct.getImages());
                    if (!markHarrisProduct.getWeight().isEmpty()) {
                        byProductSku.setWeight(Objects.requireNonNull(new BigDecimal(markHarrisProduct.getWeight()).setScale(0, BigDecimal.ROUND_HALF_UP)).intValue());
                    }
                    BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                    updatedBcProductDataList.add(bcProductData);
                    LOGGER.info("Successfully updated BCProductData for {}", markHarrisProduct.getSku());
                }
            }
            bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.MARK_HARRIS, MarkHarrisProduct.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getCause().getMessage());
            ex.printStackTrace();
        }
    }

    private void setDescription(MarkHarrisProduct markHarrisProduct, BcProductData byProductSku) {
        StringBuilder additionalDescription = new StringBuilder();
        if (markHarrisProduct.getDescription() != null) {
            additionalDescription.append(markHarrisProduct.getDescription());
        }
        additionalDescription.append("<br>Product Dimensions - ");
        if (markHarrisProduct.getMaxHeight() != null && !markHarrisProduct.getMaxHeight().isEmpty()) {
            additionalDescription.append("<br> Maximum Height - ").append(markHarrisProduct.getMaxHeight()).append("cm");
        }
        if (markHarrisProduct.getMinHeight() != null && !markHarrisProduct.getMinHeight().isEmpty()) {
            additionalDescription.append("<br> Minimum Height - ").append(markHarrisProduct.getMinHeight()).append("cm");
        }
        if (markHarrisProduct.getMaxWidth() != null && !markHarrisProduct.getMaxWidth().isEmpty()) {
            additionalDescription.append("<br> Maximum Width -  ").append(markHarrisProduct.getMaxWidth()).append("cm");
        }
        if (markHarrisProduct.getMinWidth() != null && !markHarrisProduct.getMinWidth().isEmpty()) {
            additionalDescription.append("<br> Minimum Width -  ").append(markHarrisProduct.getMinWidth()).append("cm");
        }
        if (markHarrisProduct.getMaxLengthOrDepth() != null && !markHarrisProduct.getMaxLengthOrDepth().isEmpty()) {
            additionalDescription.append("<br> Maximum Length/Depth -  ").append(markHarrisProduct.getMaxLengthOrDepth()).append("cm");
        }
        if (markHarrisProduct.getMinLengthOrDepth() != null && !markHarrisProduct.getMinLengthOrDepth().isEmpty()) {
            additionalDescription.append("<br> Minimum Length/Depth -  ").append(markHarrisProduct.getMinLengthOrDepth()).append("cm ");
        }
        additionalDescription.append("<br>");

        if (markHarrisProduct.getWeight() != null && !markHarrisProduct.getWeight().isEmpty() && !markHarrisProduct.getWeight().equals("0")) {
            additionalDescription.append(" <br> Product Weight -  ").append(markHarrisProduct.getWeight()).append("kg ");
        }

        if (markHarrisProduct.getAssembled() != null && !markHarrisProduct.getAssembled().isEmpty()) {
            additionalDescription.append(" <br> Assembly Instructions : ").append(markHarrisProduct.getAssembled());
        }
        additionalDescription.append("<br> "+byProductSku.getAvailabilityDescription());
        byProductSku.setDescription(additionalDescription.toString());
    }


    private void processDiscontinuedCatalog(List<MarkHarrisProduct> productList) {
        List<MarkHarrisProduct> discontinuedList = productList
                .stream()
                .filter(MarkHarrisProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(markHarrisProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.MARK_HARRIS + markHarrisProduct.getSku()));
    }

    private void setPriceAndQuantity(MarkHarrisProduct markHarrisProduct, BcProductData byProductSku) {
        if (markHarrisProduct.getSalePrice() != null) {
            byProductSku.setPrice(markHarrisProduct.getSalePrice().intValue());
        }
        byProductSku.setInventoryLevel(Math.max(markHarrisProduct.getStockLevel(), 0));
        setInventoryParameters(markHarrisProduct.getStockLevel(), byProductSku);

        if (markHarrisProduct.getStockLevel() > 0) {
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        } else {
            byProductSku.setAvailabilityDescription("Will be dispatched on or after " + markHarrisProduct.getNextArrival());
            if (!StringUtils.isEmpty(markHarrisProduct.getNextArrival()) && markHarrisProduct.getNextArrival().contains("day")) {
                SimpleDateFormat formatter = new SimpleDateFormat(BcConstants.RELEASE_DATE_FORMAT);
                GregorianCalendar calendar;
                String strDate = markHarrisProduct.getNextArrival().substring(markHarrisProduct.getNextArrival().indexOf(",")+1);
                int monthOffSet = strDate.trim().indexOf(" ")+1;
                Calendar cal2 = Calendar.getInstance();
                try {
                    cal2.setTime(new SimpleDateFormat("MMM").parse(strDate.trim().substring(0,monthOffSet)));
                } catch (ParseException exception) {
                    LOGGER.error("Error while processing Preorder release date" + exception.getMessage());
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
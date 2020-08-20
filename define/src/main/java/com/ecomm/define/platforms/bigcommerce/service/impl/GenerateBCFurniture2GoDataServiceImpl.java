package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
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
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecomm.define.commons.DefineUtils.evaluateDimensions;
import static com.ecomm.define.commons.DefineUtils.getDelimiter;
import static com.ecomm.define.platforms.commons.BCUtils.setInventoryParameters;

/**
 * Created by vamshikirangullapelly on 18/07/2020.
 */
@Service
@Qualifier("furniture2GoDataService")
public class GenerateBCFurniture2GoDataServiceImpl implements GenerateBCDataService<Furniture2GoProduct> {
    private final BigCommerceApiService bigCommerceApiService;
    private final BigcBrandApiRepository brandApiRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(BigCommerceProductApiController.class);
    private final MongoOperations mongoOperations;
    @Value("${bigcommerce.f2g.profit.limit.high}")
    private String higherLimitHDPrice;
    @Value("${bigcommerce.f2g.profit.percentage.low}")
    private String percentageLow;

    @Autowired
    public GenerateBCFurniture2GoDataServiceImpl(BigCommerceApiService bigCommerceApiService,
                                                 BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }


    @Override
    public void generateBcProductsFromSupplier(List<Furniture2GoProduct> productList) {
        LOGGER.info("Started generating Furniture2Go Product to BigCommerce");
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<Furniture2GoProduct> updatedCatalogList = productList
                .stream()
                .filter(Objects::nonNull)
                .filter(Furniture2GoProduct::isUpdated)
                .collect(Collectors.toList());

        updatedCatalogList.parallelStream().forEach(furniture2GoProduct -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(BcConstants.FURNITURE_2_GO + furniture2GoProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);
            String title = furniture2GoProduct.getRange() + " " + furniture2GoProduct.getProductName();
            if (!title.contains(furniture2GoProduct.getFinish())) {
                title = title + " " + furniture2GoProduct.getFinish();
            }

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(furniture2GoProduct.getProductName()));
                byProductSku.setSku(BcConstants.FURNITURE_2_GO + furniture2GoProduct.getSku());
                if (!furniture2GoProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + title);
                }

                byProductSku.setSupplier(Supplier.FURNITURE2GO.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(furniture2GoProduct.getWeight().intValue());
                byProductSku.setImageList(furniture2GoProduct.getImages());
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                evaluateDescription(furniture2GoProduct, byProductSku);
                setSEOParameters(furniture2GoProduct, byProductSku, title);
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                if (!furniture2GoProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + title);
                }
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                evaluateDescription(furniture2GoProduct, byProductSku);
                setSEOParameters(furniture2GoProduct, byProductSku, title);
                byProductSku.setCategories(BCUtils.assignCategories(furniture2GoProduct.getProductName()));
                byProductSku.setImageList(furniture2GoProduct.getImages());
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        });

        bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.FURNITURE_2_GO, Furniture2GoProduct.class);
        LOGGER.info("Finished generating Furniture2Go Product to BigCommerce");
    }

    private void setSEOParameters(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku, String title) {
        byProductSku.setMetaDescription(title);
        byProductSku.setMetaDescription(byProductSku.getDescription());
        List<Object> keywords = new ArrayList<Object>();
        keywords.add(furniture2GoProduct.getRange());
        keywords.add(furniture2GoProduct.getProductName());
        keywords.add(furniture2GoProduct.getFinish());
        keywords.add("Modern Furniture");
        byProductSku.setMetaKeywords(keywords);
    }

    /**
     * This method will delete the discontinued catalog from BigCommerce and from bcProductData table
     *
     * @param productList
     * @throws URISyntaxException
     */
    private void processDiscontinuedCatalog(List<Furniture2GoProduct> productList) {
        LOGGER.info("Started processing discontinued products of Furniture2Go in BigCommerce");
        List<Furniture2GoProduct> discontinuedList = productList
                .stream()
                .filter(Furniture2GoProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(furniture2GoProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.FURNITURE_2_GO + furniture2GoProduct.getSku()));
        LOGGER.info("Finished processing discontinued products of Furniture2Go in BigCommerce");
    }

    private void setPriceAndQuantity(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        evaluatePrice(furniture2GoProduct, byProductSku);
        byProductSku.setInventoryLevel(Math.max(furniture2GoProduct.getStockLevel(), 0));

        if (furniture2GoProduct.getStockLevel() > 0) {
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        } else {
            byProductSku.setAvailabilityDescription("Usually dispatches on or after " + furniture2GoProduct.getStockArrivalDate());
        }

        if (furniture2GoProduct.getStockArrivalDate() != null && DefineUtils.isValidF2GDate(furniture2GoProduct.getStockArrivalDate())) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SS:00");
            GregorianCalendar calendar;
            int year = Integer.parseInt(furniture2GoProduct.getStockArrivalDate().substring(0, 4));
            int month = Integer.parseInt(furniture2GoProduct.getStockArrivalDate().substring(5, 7));
            int day = Integer.parseInt(furniture2GoProduct.getStockArrivalDate().substring(8, 10));

            calendar = new GregorianCalendar(year, month, day, 00, 00, 00);
            Date date = calendar.getTime();
            try {
                byProductSku.setPreorderReleaseDate(formatter.format(date));
            } catch (Exception exception) {
                LOGGER.error("Error while processing Preorder release date" + exception.getMessage());
            }
        } else {
            byProductSku.setPreorderReleaseDate(null);
        }
        setInventoryParameters(furniture2GoProduct.getStockLevel(), byProductSku);
    }

    private void evaluatePrice(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = furniture2GoProduct.getSalePrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal retailPrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setRetailPrice(retailPrice.intValue());
            }
        }
    }

    private void evaluateDescription(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        //Height logic
        StringBuilder dimensionsDescription = new StringBuilder();
        String heightDelimeter = getDelimiter(furniture2GoProduct.getHeight());
        dimensionsDescription.append(" Height :").append(furniture2GoProduct.getHeight()).append("(mm) ");
        if (!heightDelimeter.isEmpty()) {
            List<String> heightList = evaluateDimensions(heightDelimeter, furniture2GoProduct.getHeight());
            if (!heightList.isEmpty()) {
                byProductSku.setHeight(Integer.parseInt(heightList.get(0)));
            }
        } else {
            byProductSku.setHeight(new BigDecimal(furniture2GoProduct.getHeight()).intValue());
        }

        //Width logic
        String widthDelimeter = getDelimiter(furniture2GoProduct.getWidth());
        dimensionsDescription.append(" Width :").append(furniture2GoProduct.getWidth()).append("(mm) ");
        if (!widthDelimeter.isEmpty()) {
            List<String> widthList = evaluateDimensions(widthDelimeter, furniture2GoProduct.getWidth());
            if (!widthList.isEmpty()) {
                byProductSku.setWidth(Integer.parseInt(widthList.get(0)));
            }
        } else {
            byProductSku.setWidth(new BigDecimal(furniture2GoProduct.getWidth()).intValue());
        }

        //Depth logic
        String depthDelimeter = getDelimiter(furniture2GoProduct.getDepth());
        dimensionsDescription.append(" Depth :").append(furniture2GoProduct.getDepth()).append("(mm) ");
        if (!depthDelimeter.isEmpty()) {
            List<String> depthList = evaluateDimensions(widthDelimeter, furniture2GoProduct.getDepth());
            if (!depthList.isEmpty()) {
                byProductSku.setDepth(Integer.parseInt(depthList.get(0)));
            }
        } else {
            byProductSku.setDepth(new BigDecimal(furniture2GoProduct.getDepth()).intValue());
        }
        String stockArrivalDate = furniture2GoProduct.getStockArrivalDate();
        dimensionsDescription.append("Next Dispatch Date : ");
        if (stockArrivalDate != null && !stockArrivalDate.isEmpty()) {
            if (DefineUtils.isValidDate(stockArrivalDate)) {
                dimensionsDescription.append(stockArrivalDate);
            } else {
                dimensionsDescription.append(DefineUtils.plusDays(70));
            }
        } /* else {
            dimensionsDescription.append("Usually dispatches in next 10 working days");
        } */

        if (furniture2GoProduct.getAssemblyInstructions() != null && !furniture2GoProduct.getAssemblyInstructions().isEmpty()) {
            dimensionsDescription.append(" <br> Assembly Instructions - ").append(furniture2GoProduct.getAssemblyInstructions());
        }
        byProductSku.setDescription(furniture2GoProduct.getDescription() + " " + dimensionsDescription.toString());
        dimensionsDescription.append(" <br> 1. " + furniture2GoProduct.getBp1());
        dimensionsDescription.append(" <br> 2. " + furniture2GoProduct.getBp2());
        dimensionsDescription.append(" <br> 3. " + furniture2GoProduct.getBp3());
        dimensionsDescription.append(" <br> 4. " + furniture2GoProduct.getBp4());
        dimensionsDescription.append(" <br> 5. " + furniture2GoProduct.getBp5());
        dimensionsDescription.append(" <br> 6. " + furniture2GoProduct.getBp6());
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecomm.define.commons.DefineUtils.evaluateDimensions;
import static com.ecomm.define.commons.DefineUtils.getDelimiter;

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
                .filter(Furniture2GoProduct::isUpdated)
                .collect(Collectors.toList());

        updatedCatalogList.parallelStream().forEach(furniture2GoProduct -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(BcConstants.FURNITURE_2_GO + furniture2GoProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(furniture2GoProduct.getProductName()));
                byProductSku.setSku(BcConstants.FURNITURE_2_GO + furniture2GoProduct.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + furniture2GoProduct.getProductName() + " " + furniture2GoProduct.getFinish());

                byProductSku.setSupplier(Supplier.FURNITURE2GO.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(furniture2GoProduct.getWeight().intValue());
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                byProductSku.setImageList(furniture2GoProduct.getImages());
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                evaluateDescription(furniture2GoProduct, byProductSku);
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + furniture2GoProduct.getProductName() + " " + furniture2GoProduct.getFinish());
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                evaluateDescription(furniture2GoProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(furniture2GoProduct.getProductName()));
                byProductSku.setImageList(furniture2GoProduct.getImages());
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        });

        bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.FURNITURE_2_GO, Furniture2GoProduct.class);
        LOGGER.info("Finished generating Furniture2Go Product to BigCommerce");
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
        byProductSku.setAvailability(BcConstants.PREORDER);
        byProductSku.setAvailabilityDescription("Usually dispatches on or after " + furniture2GoProduct.getStockArrivalDate());
        if (furniture2GoProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        }
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
        } else {
            dimensionsDescription.append("Usually dispatches in next 10 working days");
        }

        if (furniture2GoProduct.getAssemblyInstructions() != null && !furniture2GoProduct.getAssemblyInstructions().isEmpty()) {
            dimensionsDescription.append(" Assembly Instructions - ").append(furniture2GoProduct.getAssemblyInstructions());
        }
        byProductSku.setDescription(furniture2GoProduct.getDescription() + " " + dimensionsDescription.toString());
    }


}

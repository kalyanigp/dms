package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.commons.Supplier;
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
@Qualifier("artisanDataService")
public class GenerateBCArtisanDataServiceImpl implements GenerateBCDataService<ArtisanProduct> {

    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCArtisanDataServiceImpl.class);
    private final BigCommerceApiService bigCommerceApiService;
    private final BigcBrandApiRepository brandApiRepository;
    private final MongoOperations mongoOperations;

    @Value("${bigcommerce.artisan.profit.limit.high}")
    private String higherLimitHDPrice;

    @Value("${bigcommerce.artisan.profit.limit.high}")
    private String lowerLimitHDPrice;

    @Value("${bigcommerce.artisan.profit.percentage.low}")
    private String percentageLow;

    @Value("${bigcommerce.artisan.profit.percentage.high}")
    private String percentageHigh;

    @Autowired
    public GenerateBCArtisanDataServiceImpl(BigCommerceApiService bigCommerceApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void generateBcProductsFromSupplier(List<ArtisanProduct> productList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<ArtisanProduct> updatedCatalogList = productList
                .stream()
                .filter(Objects::nonNull)
                .filter(ArtisanProduct::isUpdated)
                .collect(Collectors.toList());
        for (ArtisanProduct artisanProduct : updatedCatalogList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(BcConstants.ARTISAN + artisanProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(artisanProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(artisanProduct.getProductName()));
                byProductSku.setImageList(artisanProduct.getImages());

                String ean = artisanProduct.getEan();
                if (ean != null && !ean.isEmpty()) {
                    byProductSku.setUpc(ean);
                }

                byProductSku.setSku(BcConstants.ARTISAN + artisanProduct.getSku());
                if (!artisanProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + artisanProduct.getProductName());
                }
                StringBuilder discriptionBuilder = new StringBuilder();
                discriptionBuilder.append(artisanProduct.getDescription()).append("<br>");
                discriptionBuilder.append(artisanProduct.getBp1()).append("<br>");
                discriptionBuilder.append(artisanProduct.getBp2()).append("<br>");
                discriptionBuilder.append(artisanProduct.getBp3()).append("<br>");
                discriptionBuilder.append(artisanProduct.getBp4()).append("<br>");
                discriptionBuilder.append(artisanProduct.getBp5()).append("<br>");
                discriptionBuilder.append(artisanProduct.getBp6()).append("<br>");

                discriptionBuilder.append("<br>Dimensions: <br>");

                byProductSku.setSupplier(Supplier.ARTISAN.getName());
                byProductSku.setType(BcConstants.TYPE);
                if (artisanProduct.getWeight() != null) {
                    int weight = artisanProduct.getWeight().intValue();
                    byProductSku.setWeight(weight);
                    discriptionBuilder.append(" Weight : ").append(weight).append("kg").append("<br>");
                }
                if (artisanProduct.getHeight() != null) {
                    int height = artisanProduct.getHeight().intValue();
                    byProductSku.setHeight(height);
                    discriptionBuilder.append(" Height : ").append(height).append("mm").append("<br>");
                }
                if (artisanProduct.getWidth() != null) {
                    int width = artisanProduct.getWidth().intValue();
                    byProductSku.setWidth(width);
                    discriptionBuilder.append(" Width : ").append(width).append("mm").append("<br>");
                }
                if (artisanProduct.getDepth() != null) {
                    int depth = artisanProduct.getDepth().intValue();
                    byProductSku.setDepth(depth);
                    discriptionBuilder.append(" Depth : ").append(depth).append("mm").append("<br>");
                }
                discriptionBuilder.append(artisanProduct.getAvailablityMessage());

                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                byProductSku.setDescription(discriptionBuilder.toString());
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setImageList(artisanProduct.getImages());
                if (!artisanProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + artisanProduct.getProductName());
                }
                setPriceAndQuantity(artisanProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(artisanProduct.getProductName()));
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        }
        bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.ARTISAN, ArtisanProduct.class);
    }


    private void processDiscontinuedCatalog(List<ArtisanProduct> productList) {
        List<ArtisanProduct> discontinuedList = productList
                .stream()
                .filter(ArtisanProduct::isDiscontinued)
                .collect(Collectors.toList());

        discontinuedList.parallelStream().forEach(artisanProduct -> bigCommerceApiService.processDiscontinuedCatalog(BcConstants.ARTISAN + artisanProduct.getSku()));
    }

    private void evaluatePrice(ArtisanProduct artisanProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = artisanProduct.getSalePrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal retailPrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setRetailPrice(retailPrice.intValue());
            }
        }
    }

    private void setPriceAndQuantity(ArtisanProduct artisanProduct, BcProductData byProductSku) {
        LOGGER.info("Setting Price and Quantity for {} with date {}", artisanProduct.getSku(), artisanProduct.getArrivalDate());

        if (artisanProduct.getPrice() != null) {
            evaluatePrice(artisanProduct, byProductSku);
        }
        byProductSku.setInventoryLevel(Math.max(artisanProduct.getStockLevel(), 0));
        if (artisanProduct.getStockLevel() > 0) {
            byProductSku.setAvailabilityDescription(BcConstants.ARTISAN_AVAILABLE_DEFAULT);
        } else {
            if (StringUtils.isEmpty(artisanProduct.getArrivalDate())) {
                byProductSku.setAvailabilityDescription(BcConstants.ARTISAN_AVAILABLE_ONDEMAND);
            } else {
                byProductSku.setAvailabilityDescription(BcConstants.ARTISAN_ARRIVALS_SOON + " " + artisanProduct.getArrivalDate());
            }
            if (!StringUtils.isEmpty(artisanProduct.getArrivalDate()) && !artisanProduct.getArrivalDate().contains("End")
                    && !artisanProduct.getArrivalDate().contains("Mid") && !artisanProduct.getArrivalDate().contains("Early")
                    && !artisanProduct.getArrivalDate().contains("Sold")) {
                SimpleDateFormat formatter = new SimpleDateFormat(BcConstants.RELEASE_DATE_FORMAT);
                GregorianCalendar calendar;
                Calendar cal = Calendar.getInstance();
                try {
                    int monthOffSet = artisanProduct.getArrivalDate().trim().indexOf(" ") + 1;
                    cal.setTime(new SimpleDateFormat("MMM").parse(artisanProduct.getArrivalDate().trim().substring(monthOffSet)));
                } catch (ParseException exception) {
                    LOGGER.error("Error while processing Preorder release date" + exception.getMessage());
                    exception.printStackTrace();
                }
                int month = cal.get(Calendar.MONTH);
                int day = Integer.parseInt(artisanProduct.getArrivalDate().trim().replaceAll("[^\\d.]", ""));
                calendar = new GregorianCalendar(BcConstants.CURRENT_YEAR, month, day, 00, 00, 00);
                Date date = calendar.getTime();
                try {
                    byProductSku.setPreorderReleaseDate(formatter.format(date));
                } catch (Exception exception) {
                    LOGGER.error("Error while processing Preorder release date" + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        }
        setInventoryParameters(artisanProduct.getStockLevel(), byProductSku);
    }
}
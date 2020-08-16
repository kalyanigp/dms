package com.ecomm.define.platforms.bigcommerce.service.impl;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Value("${bigcommerce.f2g.profit.limit.high}")
    private String higherLimitHDPrice;

    @Value("${bigcommerce.f2g.profit.percentage.low}")
    private String percentageLow;


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
            if (!artisanProduct.getProductName().contains(Supplier.SELLER_BRAND.getName())) {
                query.addCriteria(Criteria.where("sku").is(BcConstants.ARTISAN + artisanProduct.getSku()));
            }
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
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + artisanProduct.getProductName() + " " + artisanProduct.getBp1());
                }
                StringBuilder discriptionBuilder = new StringBuilder();
                discriptionBuilder.append(artisanProduct.getDescription());
                discriptionBuilder.append("Dimensions - (");


                byProductSku.setSupplier(Supplier.ARTISAN.getName());
                byProductSku.setType(BcConstants.TYPE);
                if (artisanProduct.getWeight() != null) {
                    int weight = artisanProduct.getWeight().intValue();
                    byProductSku.setWeight(weight);
                    discriptionBuilder.append(" Weight : ").append(weight).append("kg");
                }
                if (artisanProduct.getHeight() != null) {
                    int height = artisanProduct.getHeight().intValue();
                    byProductSku.setHeight(height);
                    discriptionBuilder.append(" Height : ").append(height).append("mm");
                }
                if (artisanProduct.getWidth() != null) {
                    int width = artisanProduct.getWidth().intValue();
                    byProductSku.setWidth(width);
                    discriptionBuilder.append(" Width : ").append(width).append("mm");
                }
                if (artisanProduct.getDepth() != null) {
                    int depth = artisanProduct.getDepth().intValue();
                    byProductSku.setDepth(depth);
                    discriptionBuilder.append(" Depth : ").append(depth).append("mm)");
                }
                discriptionBuilder.append("Assembly Instructions - ").append(artisanProduct.getAssemblyInstructions());

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
                    byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + artisanProduct.getProductName() + " " + artisanProduct.getBp1());
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

    private void setPriceAndQuantity(ArtisanProduct artisanProduct, BcProductData byProductSku) {
        byProductSku.setPrice(artisanProduct.getPrice().intValue());
        byProductSku.setInventoryLevel(Math.max(artisanProduct.getStockLevel(), 0));
        byProductSku.setAvailability(BcConstants.PREORDER);
        byProductSku.setAvailabilityDescription("Usually dispatches in 6 to 8 weeks.");
        if (artisanProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        }
    }

}
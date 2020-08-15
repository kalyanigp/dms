package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static com.ecomm.define.platforms.bigcommerce.constants.BcConstants.MAISON_CODE;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
@Qualifier("maisonDataService")
public class GenerateBCMaisonDataServiceImpl implements GenerateBCDataService<MaisonProduct> {

    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCMaisonDataServiceImpl.class);
    private final BigCommerceApiService bigCommerceApiService;
    private final BigcBrandApiRepository brandApiRepository;
    private final MongoOperations mongoOperations;

    @Autowired
    public GenerateBCMaisonDataServiceImpl(BigCommerceApiService bigCommerceApiService
            , BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.bigCommerceApiService = bigCommerceApiService;
        this.mongoOperations = mongoOperations;
        this.brandApiRepository = brandApiRepository;
    }


    @Override
    public void generateBcProductsFromSupplier(List<MaisonProduct> updatedMaisonProductList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(updatedMaisonProductList);

        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<MaisonProduct> updatedCatalogList = updatedMaisonProductList
                .stream()
                .filter(MaisonProduct::isUpdated)
                .collect(Collectors.toList());
        updatedCatalogList.parallelStream().forEach(maisonProd -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(MAISON_CODE + maisonProd.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(maisonProd, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(maisonProd.getTitle()));
                byProductSku.setImageList(Arrays.asList(maisonProd.getImages().split(",")));
                byProductSku.setSku(MAISON_CODE + maisonProd.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + maisonProd.getTitle());
                byProductSku.setUpc(maisonProd.getEan());

                byProductSku.setSupplier(Supplier.MAISON.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(0);
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                byProductSku.setAvailability(BcConstants.PREORDER);
                if (maisonProd.getStockQuantity() > 0) {
                    byProductSku.setAvailability(BcConstants.AVAILABLE);
                }

                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                String packingSpec = maisonProd.getPackingSpec();
                if (packingSpec != null) {
                    packingSpec = packingSpec.toLowerCase();
                    int index = 0;
                    if (packingSpec.contains("kg")) {
                        index = packingSpec.indexOf("kg");
                    }
                    try {
                        if (index > 0) {
                            String weight = packingSpec.substring(index - 3, index);
                            weight = weight.replaceAll(" ", "").replaceAll(":", "");
                            weight = weight.replaceAll("[^\\d.]", "");
                            double dWeight = Double.parseDouble(weight);
                            if ((dWeight == Math.ceil(dWeight)) && !Double.isInfinite(dWeight)) {
                                byProductSku.setWeight((int) dWeight);
                            }
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Exception while calculating weight for sku {}", byProductSku.getSku() + Arrays.toString(ex.getStackTrace()));
                    }
                }
                if (maisonProd.getMaterial() != null) {
                    byProductSku.setDescription(maisonProd.getMaterial().replaceAll(",", ""));
                }
                findDimesions(byProductSku, maisonProd.getSize());

                byProductSku.setDescription(byProductSku.getDescription() + System.lineSeparator() + maisonProd.getSize() + System.lineSeparator() + packingSpec);
                byProductSku.setAvailabilityDescription(getProductAvailability(Double.parseDouble(maisonProd.getTradePrice()), maisonProd.getStockQuantity()));
                byProductSku.setMpn(maisonProd.getSku());
                byProductSku.setPageTitle(maisonProd.getTitle());
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + maisonProd.getTitle());
                setPriceAndQuantity(maisonProd, byProductSku);
                byProductSku.setUpc(maisonProd.getEan());
                byProductSku.setCategories(BCUtils.assignCategories(maisonProd.getTitle()));
                byProductSku.setAvailabilityDescription(getProductAvailability(Double.parseDouble(maisonProd.getTradePrice()), maisonProd.getStockQuantity()));
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        });
        bigCommerceApiService.populateBigCommerceProduct(updatedBcProductDataList, BcConstants.MAISON_CODE, MaisonProduct.class);
    }

    private void setPriceAndQuantity(MaisonProduct maisonProd, BcProductData byProductSku) {
        int priceIntValue = evaluatePrice(maisonProd);
        byProductSku.setPrice(priceIntValue);
        byProductSku.setSalePrice(priceIntValue);
        byProductSku.setInventoryLevel(maisonProd.getStockQuantity() < 0 ? 0 : maisonProd.getStockQuantity());
    }


    private int evaluatePrice(MaisonProduct maisonProd) {
        BigDecimal salePrice = null;
        if (StringUtils.isEmpty(maisonProd.getMspPrice()) || "N/A".equals(maisonProd.getMspPrice()) || "0".equals(maisonProd.getMspPrice())) {
            if (!StringUtils.isEmpty(maisonProd.getTradePrice())) {
                salePrice = new BigDecimal(maisonProd.getTradePrice());
                salePrice = salePrice.multiply(BigDecimal.valueOf(2.5));
            }
        } else {
            salePrice = new BigDecimal(maisonProd.getMspPrice());
        }
        return salePrice.intValue();
    }


    private void findDimesions(BcProductData byProductSku, String size) {

        StringTokenizer st;
        size = size.replaceAll("Tile Size", "");
        size = size.replaceAll("Height", "");
        size = size.replaceAll("Size", "");
        size = size.replaceAll("Frame", "");

        try {
            if (size.indexOf("H") == size.lastIndexOf("H")) {
                size = size.replaceAll("cm", "CM");
                size = size.replaceAll("mm", "MM");

                int unit = size.indexOf("CM");
                if (size.contains("MM")) {
                    unit = size.indexOf("MM");
                }
                if (size.contains("X")) {
                    st = new StringTokenizer(size.substring(0, unit), "X");
                } else {
                    st = new StringTokenizer(size.substring(0, unit), "x");
                }
                String height;
                String width;
                String depth;
                while (st.hasMoreTokens()) {
                    String nextString = st.nextToken();

                    if (nextString.contains("H")) {
                        height = nextString.replaceAll("H", "");
                        height = height.replaceAll(" ", "");
                        height = height.replaceAll("cm", "");
                        height = height.replaceAll("mm", "");
                        height = height.replaceAll("MM", "");

                        if (height.contains(".")) {
                            height = height.substring(0, height.indexOf("."));
                        }
                        if (height.contains("-")) {
                            height = height.substring(0, height.indexOf("-"));
                        }
                        if (height.contains("(")) {
                            height = height.substring(0, height.indexOf("("));
                        }
                        height = height.replaceAll("[^\\d.]", "");
                        byProductSku.setHeight(Double.valueOf(height).intValue());
                    } else if (nextString.contains("W")) {
                        width = nextString.replaceAll("W", "");
                        width = width.replaceAll(" ", "");
                        width = width.replaceAll("cm", "");
                        width = width.replaceAll("mm", "");
                        width = width.replaceAll("MM", "");
                        width = width.replaceAll("[^\\d.]", "");

                        if (width.contains(".")) {
                            width = width.substring(0, width.indexOf("."));
                        }
                        if (width.contains("-")) {
                            width = width.substring(0, width.indexOf("-"));
                        }
                        byProductSku.setWidth(Double.valueOf(width).intValue());
                    } else if (nextString.contains("D") || nextString.contains("L")) {
                        depth = nextString.replaceAll("D", "");
                        depth = depth.replaceAll("L", "");
                        depth = depth.replaceAll(" ", "");
                        depth = depth.replaceAll("[^\\d.]", "");

                        if (depth.contains("cm")) {
                            depth = depth.substring(0, depth.indexOf("cm"));
                            depth = depth.replaceAll("cm", "");
                        }
                        if (depth.contains("mm")) {
                            depth = depth.substring(0, depth.indexOf("mm"));
                            depth = depth.replaceAll("mm", "");
                        }
                        if (depth.contains("MM")) {
                            depth = depth.substring(0, depth.indexOf("MM"));
                            depth = depth.replaceAll("MM", "");
                        }

                        if (depth.contains(".")) {
                            depth = depth.substring(0, depth.indexOf("."));
                        }
                        if (depth.contains("-")) {
                            depth = depth.substring(0, depth.indexOf("-"));
                        }
                        byProductSku.setDepth(Double.valueOf(depth).intValue());
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception while finding dimensions for sku {}", byProductSku.getSku() + ex.getMessage());
        }
    }

    private String getProductAvailability(double tradePrice, int stockQty) {
        if (stockQty > 0) {
            if (tradePrice <= 50) {
                return "Usually dispatches in 3 days";
            } else if (tradePrice <= 150) {
                return "Usually dispatches in 5 days";
            } else if (tradePrice <= 300) {
                return "Usually dispatches in 10 days";
            } else if (tradePrice <= 700) {
                return "Usually dispatches in 15 days";
            } else if (tradePrice > 700) {
                return "Usually dispatches in 25 days";
            }
        }
        return "Your Order will be considered as Back Order. Kindly contact us for delivery timelines";
    }


    private void processDiscontinuedCatalog(List<MaisonProduct> productList) throws URISyntaxException {
        List<MaisonProduct> discontinuedList = productList
                .stream()
                .filter(MaisonProduct::isDiscontinued)
                .collect(Collectors.toList());
        discontinuedList.parallelStream().forEach(maisonProduct -> bigCommerceApiService.processDiscontinuedCatalog(MAISON_CODE + maisonProduct.getSku()));
    }

}
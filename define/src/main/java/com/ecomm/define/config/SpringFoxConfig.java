package com.ecomm.define.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket swaggerBigCommerceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("BigCommerce")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.platforms.bigcommerce.controller"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerMaisonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Maison")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.maison.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerFurniture2GoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Furniture2Go")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.furniture2go.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerArtisanApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Artisan")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.artisan.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerHillInteriorApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("HillInterior")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.hillinterior.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerMarkHarrisApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("MarkHarris")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.markharris.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerLpdApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("LPD")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.lpdfurniture.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    @Bean
    public Docket swaggerCoreProductApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("CORE")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ecomm.define.suppliers.coreproducts.controller"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Define Furniture Product Management System REST API")
                .contact(new Contact("Kalyani Annam", "www.thedefine.co.uk", "kalyani.annam@thedefine.co.uk"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}

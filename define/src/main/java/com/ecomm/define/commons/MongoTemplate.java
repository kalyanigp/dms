package com.ecomm.define.commons;

import com.ecomm.define.config.SpringMongoConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Created by vamshikirangullapelly on 04/07/2020.
 */
public class MongoTemplate {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    public MongoOperations getMongoOperation() {
        return (MongoOperations) ctx.getBean("mongoTemplate");
    }
}

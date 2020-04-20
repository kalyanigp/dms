package com.ecomm.define.domain;


import com.opencsv.bean.CsvBindByName;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
public class Users {

    @Id
    public ObjectId _id;

    @CsvBindByName
    private long id;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String email;

    @CsvBindByName(column = "country")
    private String countryCode;
    @CsvBindByName
    private int age;

    public Users(){

    }

    public Users(long id, String name, String email, String countryCode, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.countryCode = countryCode;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

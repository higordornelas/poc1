package com.higor.poc1.api.model;

import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.core.validation.ZipCode;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

public class AddressDTO {

    private Long id;

    @NotBlank
    private String street;

    @NotBlank
    private String number;

    @NotBlank
    private String district;

    @NotBlank
    private String city;

    @NotBlank
    @ZipCode(groups = {Default.class, DTOValidation.class})
    @Column(name = "zip_code")
    private String zipCode;

    @NotBlank
    private String state;

    private Boolean isMain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }
}

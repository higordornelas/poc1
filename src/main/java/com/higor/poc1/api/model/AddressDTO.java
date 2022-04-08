package com.higor.poc1.api.model;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AddressDTO {

    private long id;

    @NotNull(message = "Street cannot be null")
    private String street;

    @NotNull(message = "Number cannot be null")
    private String number;

    @NotNull(message = "District cannot be null")
    private String district;

    @NotNull(message = "City cannot be null")
    private String city;

    @NotNull(message = "Zip Code cannot be null")
    @Pattern(regexp = "\\d{5}-\\d{3}")
    @Column(name = "zip_code")
    private String zipCode;

    @NotNull(message = "State cannot be null")
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}

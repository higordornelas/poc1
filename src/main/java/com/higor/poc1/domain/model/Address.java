package com.higor.poc1.domain.model;

import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.core.validation.ZipCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Objects;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String street;

    @NotNull
    private String number;

    @NotNull
    private String district;

    @NotNull
    private String city;

    @NotNull
    @ZipCode(groups = {Default.class, DTOValidation.class})
    @Column(name = "zip_code")
    private String zipCode;

    @NotNull
    private String state;

    @Column(name = "is_main")
    private Boolean main;

    public Address(Long id, String street, String number, String district, String city, String zipCode, String state, Boolean main) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.district = district;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.main = main;
    }

    public Address(String street, String number, String district, String city, String zipCode, String state, Boolean main) {
        this.street = street;
        this.number = number;
        this.district = district;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.main = main;
    }

    public Address() {
        this.main = false;
    }

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
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

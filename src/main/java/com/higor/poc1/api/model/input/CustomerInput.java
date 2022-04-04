package com.higor.poc1.api.model.input;

import com.higor.poc1.domain.enumerator.CustomerType;

import java.util.ArrayList;
import java.util.List;

public class CustomerInput {

    private String name;
    private String email;
    private String registerNumber;
    private CustomerType type;
    private String phoneNumber;
    private List<AddressIdInput> addresses = new ArrayList<>();

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

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AddressIdInput> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressIdInput> addresses) {
        this.addresses = addresses;
    }
}

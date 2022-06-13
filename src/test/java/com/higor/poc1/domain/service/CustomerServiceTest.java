package com.higor.poc1.domain.service;

import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCustomerPostSuccess() {
        Customer newCustomer = new Customer();
        Address newAddress = new Address();
        List<Address> addresses = new ArrayList<>();

        newAddress.setId(1L);
        newAddress.setStreet("Av. Rui Barbosa");
        newAddress.setNumber("12");
        newAddress.setDistrict("Centro");
        newAddress.setCity("Palmirinha do Sul");
        newAddress.setZipCode("34110-112");
        newAddress.setState("Mato Grosso do Sul");
        newAddress.setMain(true);

        addresses.add(newAddress);

        newCustomer.setName("Claudio");
        newCustomer.setEmail("claudinho@email.com");
        newCustomer.setRegisterNumber("128.150.116-28");
        newCustomer.setType(CustomerType.LEGAL_PERSON);
        newCustomer.setPhoneNumber("3211-0010");
        newCustomer.setAddresses(addresses);

        addressService.save(newAddress);
        customerService.save(newCustomer);

        assertThat(newAddress, notNullValue());
        assertThat(newCustomer, notNullValue());
    }

    @Test
    void patch() {
    }

    @Test
    void addAdressToCustomer() {
    }

    @Test
    void delete() {
    }

    @Test
    void findOrFail() {
    }

    @Test
    void chooseMainAddress() {
    }

    @Test
    void checkIfHaveMainAddress() {
    }

    @Test
    void checkAndChooseMainAddress() {
    }

    @Test
    void checkMainAddress() {
    }

    @Test
    void testCheckMainAddress() {
    }

    @Test
    void maskData() {
    }

    @Test
    void validate() {
    }
}
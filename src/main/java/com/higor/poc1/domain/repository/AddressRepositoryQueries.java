package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Address;

import java.util.List;

public interface AddressRepositoryQueries {

    List<Address> find(String street, String number, String district, String zipCode, String state);
}

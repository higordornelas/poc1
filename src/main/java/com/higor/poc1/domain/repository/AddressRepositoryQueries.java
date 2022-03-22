package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Address;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AddressRepositoryQueries {

    Page<Address> find(String street, String number, String district, String zipCode, String state);
}

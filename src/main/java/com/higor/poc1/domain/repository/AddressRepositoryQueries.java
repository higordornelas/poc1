package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressRepositoryQueries {

    Page<Address> find(String street, String number, String district, String zipCode, String state, Pageable pageable);
}

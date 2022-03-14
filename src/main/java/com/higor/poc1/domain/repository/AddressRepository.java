package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Address;

import java.util.List;

public interface AddressRepository {

    List<Address> list();
    Address find(Long id);
    Address save(Address address);
    void delete(Address address);
}

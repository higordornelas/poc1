package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Customer;

import java.util.List;

public interface CustomerRepository {

    List<Customer> list();
    Customer find(Long id);
    Customer update(Customer customer);
    void delete(Customer customer);
}

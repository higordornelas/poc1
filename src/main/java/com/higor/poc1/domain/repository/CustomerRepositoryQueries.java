package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryQueries {

    Page<Customer> find(String name, String email, String registerNumber, String type, String phoneNumber, Pageable pageable);
}

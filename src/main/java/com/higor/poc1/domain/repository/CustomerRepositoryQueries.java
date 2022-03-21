package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Customer;

import java.util.List;

public interface CustomerRepositoryQueries {

    List<Customer> find(String name, String email, String registerNumber, String type, String phoneNumber);
}

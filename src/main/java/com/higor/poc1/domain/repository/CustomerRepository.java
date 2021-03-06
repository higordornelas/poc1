package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryQueries {
    @Override
    Page<Customer> findAll(Pageable pageable);
}

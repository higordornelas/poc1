package com.higor.poc1.domain.service;

import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> list() {
        return customerRepository.list();
    }

    public Customer find(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Long customerId, Customer customer) {
        Customer thisCustomer = find(customerId);
        BeanUtils.copyProperties(customer, thisCustomer, "id");
        thisCustomer = save(thisCustomer);

        return thisCustomer;
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}

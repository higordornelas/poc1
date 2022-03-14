package com.higor.poc1.api.controller;

import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomer() {
        return ResponseEntity.ok(customerRepository.list());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> findCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.find(customerId);

        if (customer != null){
            return ResponseEntity.ok(customer);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        try {
            Customer customerToSave = customerRepository.save(customer);
            URI location = URI.create("/customers");

            return ResponseEntity.created(location).body(customerToSave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId, @RequestBody Customer customer) {
        Customer thisCustomer = customerRepository.find(customerId);

        try {
            BeanUtils.copyProperties(customer, thisCustomer, "id");
            thisCustomer = customerRepository.save(thisCustomer);

            return ResponseEntity.ok(thisCustomer);
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long customerId) {
        Customer customer = customerRepository.find(customerId);

        try {
            customerRepository.delete(customer);
            return ResponseEntity.noContent().build();
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

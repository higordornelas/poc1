package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

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

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> patchCustomer(@PathVariable Long customerId, @RequestBody Map<String, Object> fields) {
        Customer thisCustomer = customerRepository.find(customerId);

        try {
            merge(fields, thisCustomer);

            return ResponseEntity.ok(thisCustomer);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void merge(Map<String, Object> sourceFields, Customer customerToPatch) {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer thisCustomer = objectMapper.convertValue(sourceFields, Customer.class);

        sourceFields.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(Customer.class, propertyName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, thisCustomer);

            ReflectionUtils.setField(field, customerToPatch, newValue);
        });
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

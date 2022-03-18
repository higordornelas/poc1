package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import com.higor.poc1.domain.service.CustomerService;
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
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomer() {
        List<Customer> customers = customerRepository.findAll();

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> findCustomer(@PathVariable Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()){
            return ResponseEntity.ok(customer.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        try {
            customer = customerService.savePost(customer);
            URI location = URI.create("/customers");

            return ResponseEntity.created(location).body(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId, @RequestBody Customer customer) {
        try {
            Customer thisCustomer = customerRepository.findById(customerId).orElse(null);

            if (thisCustomer != null) {
                BeanUtils.copyProperties(customer, thisCustomer, "id");
                thisCustomer = customerService.save(thisCustomer);

                return ResponseEntity.ok(thisCustomer);
            }

            return ResponseEntity.notFound().build();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.delete(customerId);
            return ResponseEntity.noContent().build();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<?> patchCustomer(@PathVariable Long customerId, @RequestBody Map<String, Object> fields) {
        Customer thisCustomer = customerRepository.findById(customerId).orElse(null);

        if (thisCustomer == null) {
            return ResponseEntity.notFound().build();
        }

        merge(fields, thisCustomer);

        return updateCustomer(customerId, thisCustomer);
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
}

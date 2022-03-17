package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.service.AddressService;
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

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Address>> getAddress() {
        return ResponseEntity.ok(addressService.list());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> findAddress(@PathVariable Long addressId) {
        Address address = addressService.find(addressId);

        if (address != null){
            return ResponseEntity.ok(address);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        try {
            Address addressToSave = addressService.save(address);
            URI location = URI.create("/addresses");

            return ResponseEntity.created(location).body(addressToSave);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Customer> addAddressToCustomer(@PathVariable Long id, @RequestBody Address address) {
        try {
            Customer customer = addressService.addAdressToCustomer(id, address);
            URI location = URI.create("/addresses");

            return ResponseEntity.created(location).body(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
        Address thisAddress = addressService.find(addressId);

        try {
            BeanUtils.copyProperties(address, thisAddress, "id");
            thisAddress = addressService.save(thisAddress);

            return ResponseEntity.ok(thisAddress);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Long addressId) {
        try {
            addressService.delete(addressId);
            return ResponseEntity.noContent().build();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<?> patchAddress(@PathVariable Long addressId, @RequestBody Map<String, Object> fields) {
        Address thisAddress = addressService.find(addressId);

        try {
            merge(fields, thisAddress);

            return updateAddress(addressId, thisAddress);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void merge(Map<String, Object> sourceFields, Address addressToPatch) {
        ObjectMapper objectMapper = new ObjectMapper();
        Address thisAddress = objectMapper.convertValue(sourceFields, Address.class);

        sourceFields.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(Address.class, propertyName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, thisAddress);

            ReflectionUtils.setField(field, addressToPatch, newValue);
        });
    }
}

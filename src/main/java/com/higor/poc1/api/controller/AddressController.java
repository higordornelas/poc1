package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getAddress() {
        return ResponseEntity.ok(addressRepository.findAll());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> findAddress(@PathVariable Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()){
            return ResponseEntity.ok(address.get());
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
        Optional<Address> thisAddress = addressRepository.findById(addressId);

        try {
            BeanUtils.copyProperties(address, thisAddress.get(), "id");
            Address savedAddress = addressService.save(thisAddress.get());

            return ResponseEntity.ok(savedAddress);
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
        Address thisAddress = addressRepository.findById(addressId).orElse(null);

        if (thisAddress == null) {
            ResponseEntity.notFound().build();
        }

        merge(fields, thisAddress);

        return updateAddress(addressId, thisAddress);
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

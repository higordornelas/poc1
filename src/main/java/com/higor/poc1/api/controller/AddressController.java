package com.higor.poc1.api.controller;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.repository.AddressRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    AddressRepository addressRepository;

    @GetMapping
    public ResponseEntity<List<Address>> getAddress() {
        return ResponseEntity.ok(addressRepository.list());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<Address> findAddress(@PathVariable Long addressId) {
        Address address = addressRepository.find(addressId);

        if (address != null){
            return ResponseEntity.ok(address);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        try {
            Address addressToSave = addressRepository.save(address);
            URI location = URI.create("/addresses");

            return ResponseEntity.created(location).body(addressToSave);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
        Address thisAddress = addressRepository.find(addressId);

        try {
            BeanUtils.copyProperties(address, thisAddress, "id");
            thisAddress = addressRepository.save(thisAddress);

            return ResponseEntity.ok(thisAddress);
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Address> deleteAddress(@PathVariable Long addressId) {
        Address address = addressRepository.find(addressId);

        try {
            addressRepository.delete(address);
            return ResponseEntity.noContent().build();
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

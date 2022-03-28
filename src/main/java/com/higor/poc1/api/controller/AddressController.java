package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Map;

@RestController
@RequestMapping(value = "/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @GetMapping
    public Page<Address> getAddress(
            @PageableDefault(sort = "id",
                    direction = Sort.Direction.ASC, page = 0, size = 10)
                    Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @GetMapping("/{addressId}")
    public Address findAddress(@PathVariable Long addressId) {
        return addressService.findOrFail(addressId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Address addAddress(@Valid @RequestBody Address address) {
        return addressService.save(address);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addAddressToCustomer(@PathVariable Long id, @Valid @RequestBody Address address) {
        return addressService.addAdressToCustomer(id, address);
    }

    @PutMapping("/{addressId}")
    public Address updateAddress(@PathVariable Long addressId, @Valid @RequestBody Address address) {
        Address thisAddress = addressService.findOrFail(addressId);

        BeanUtils.copyProperties(address, thisAddress, "id");

        return addressService.save(thisAddress);
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long addressId) {
        addressService.delete(addressId);
    }

    @PatchMapping("/{addressId}")
    public Address patchAddress(@PathVariable Long addressId, @Valid @RequestBody Map<String, Object> fields) {
        Address thisAddress = addressService.findOrFail(addressId);

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

    @GetMapping("/search")
    public ResponseEntity<Page<Address>> searchAddress(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC, page = 0, size = 10) String street, String number, String district, String zipCode, String state){
        return ResponseEntity.ok(addressRepository.find(street, number, district, zipCode, state));
    }
}

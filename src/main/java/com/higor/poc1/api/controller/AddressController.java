package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.api.assembler.AddressDTOAssembler;
import com.higor.poc1.api.assembler.AddressInputAssembler;
import com.higor.poc1.api.assembler.AddressInputDisassembler;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.input.AddressInput;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private AddressDTOAssembler addressDTOAssembler;

    @Autowired
    private AddressInputAssembler addressInputAssembler;

    @Autowired
    private AddressInputDisassembler addressInputDisassembler;

    @GetMapping
    public Page<AddressDTO> getAddress(
            @PageableDefault(sort = "id",
                    direction = Sort.Direction.ASC, page = 0, size = 10)
                    Pageable pageable) {

        Page<Address> addressPage = addressRepository.findAll(pageable);

        Page<AddressDTO> addressDTOPage = addressPage.map(address -> addressDTOAssembler.toDTO(address));

        return addressDTOPage;
    }

    @GetMapping("/{addressId}")
    public AddressDTO findAddress(@PathVariable Long addressId) {
        Address address = addressService.findOrFail(addressId);
        return addressDTOAssembler.toDTO(address);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO addAddress(@Valid @RequestBody AddressInput addressInput) {
        Address address = addressInputDisassembler.toDomainObject(addressInput);

        return addressDTOAssembler.toDTO(addressService.save(address));
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addAddressToCustomer(@PathVariable Long id, @Valid @RequestBody Address address) {
        return addressService.addAdressToCustomer(id, address);
    }

    @PutMapping("/{addressId}")
    public AddressDTO updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressInput addressInput) {
        Address address = addressInputDisassembler.toDomainObject(addressInput);
        Address thisAddress = addressService.findOrFail(addressId);

        BeanUtils.copyProperties(address, thisAddress, "id");

        return addressDTOAssembler.toDTO(addressService.save(thisAddress));
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long addressId) {
        addressService.delete(addressId);
    }

    @PatchMapping("/{addressId}")
    public AddressDTO patchAddress(@PathVariable Long addressId, @Valid @RequestBody Map<String, Object> fields) {
        Address thisAddress = addressService.findOrFail(addressId);

        merge(fields, thisAddress);

        return updateAddress(addressId, addressInputAssembler.toInput(thisAddress));
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
    public Page<AddressDTO> searchAddress(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC, page = 0, size = 10) Pageable pageable,
                String street, String number, String district, String zipCode, String state){

        Page<Address> addressPage = addressRepository.find(street, number, district, zipCode, state, pageable);
        Page<AddressDTO> addressesDTO = addressPage.map(address -> addressDTOAssembler.toDTO(address));

        return addressesDTO;
    }
}

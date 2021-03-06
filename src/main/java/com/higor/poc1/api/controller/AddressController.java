package com.higor.poc1.api.controller;

import com.higor.poc1.api.assembler.AddressDTOAssembler;
import com.higor.poc1.api.assembler.AddressDTODisassembler;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.service.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;

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
    private AddressDTODisassembler addressDTODisassembler;

    @Autowired
    private Validator validator;

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
    public AddressDTO addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        Address address = addressDTODisassembler.toDomainObject(addressDTO);

        return addressDTOAssembler.toDTO(addressService.save(address));
    }

    @PutMapping("/{addressId}")
    public AddressDTO updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressDTO addressDTO) {
//        Address address = addressDTODisassembler.toDomainObject(addressDTO);
        Address addressToSave = addressService.update(addressId, addressDTO);

        return addressDTOAssembler.toDTO(addressService.save(addressToSave));
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long addressId) {
        addressService.delete(addressId);
    }

    @PatchMapping("/{addressId}")
    public AddressDTO patchAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        Address patched = addressService.patch(addressId, addressDTO);

        return updateAddress(addressId, addressDTOAssembler.toDTO(patched));
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

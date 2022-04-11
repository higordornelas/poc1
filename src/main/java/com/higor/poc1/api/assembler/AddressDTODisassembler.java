package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.domain.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressDTODisassembler {

    public Address toDomainObject(AddressDTO addressDTO) {
        Address address = new Address();
        address.setId(addressDTO.getId());
        address.setStreet(addressDTO.getStreet());
        address.setNumber(addressDTO.getNumber());
        address.setDistrict(addressDTO.getDistrict());
        address.setCity(addressDTO.getCity());
        address.setZipCode(addressDTO.getZipCode());
        address.setState(addressDTO.getState());
        address.setMain(addressDTO.isMain());
        
        return address;
    }
}

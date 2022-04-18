package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.domain.model.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Address> toCollectionModel(List<AddressDTO> addresses) {
        return addresses.stream().map(addressDTO -> toDomainObject(addressDTO)).collect(Collectors.toList());
    }
}

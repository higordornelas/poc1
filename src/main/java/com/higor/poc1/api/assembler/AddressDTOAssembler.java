package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.domain.model.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressDTOAssembler {

    public AddressDTO toDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setNumber(address.getNumber());
        addressDTO.setDistrict(address.getDistrict());
        addressDTO.setCity(address.getCity());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setState(address.getState());
        addressDTO.setMain(address.isMain());

        return addressDTO;
    }

    public List<AddressDTO> toCollectionDTO(List<Address> addresses) {
        return addresses.stream().map(address -> toDTO(address)).collect(Collectors.toList());
    }
}

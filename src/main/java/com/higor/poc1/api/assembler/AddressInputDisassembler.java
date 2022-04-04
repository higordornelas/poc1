package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.input.AddressInput;
import com.higor.poc1.domain.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressInputDisassembler {

    public Address toDomainObject(AddressInput addressInput) {
        Address address = new Address();
        address.setId(addressInput.getId());
        address.setStreet(addressInput.getStreet());
        address.setNumber(addressInput.getNumber());
        address.setDistrict(addressInput.getDistrict());
        address.setCity(addressInput.getCity());
        address.setZipCode(addressInput.getZipCode());
        address.setState(addressInput.getState());
        
        return address;
    }
}

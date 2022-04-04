package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.input.AddressInput;
import com.higor.poc1.domain.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressInputAssembler {

    public AddressInput toInput(Address address) {
        AddressInput addressInput = new AddressInput();
        addressInput.setId(address.getId());
        addressInput.setStreet(address.getStreet());
        addressInput.setNumber(address.getNumber());
        addressInput.setDistrict(address.getDistrict());
        addressInput.setCity(address.getCity());
        addressInput.setZipCode(address.getZipCode());
        addressInput.setState(address.getState());

        return addressInput;
    }
}

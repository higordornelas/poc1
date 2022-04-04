package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.input.AddressIdInput;
import com.higor.poc1.api.model.input.CustomerInput;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerInputAssembler {

    @Autowired
    AddressDTOAssembler addressDTOAssembler;

    public CustomerInput toInput(Customer customer) {
        CustomerInput customerInput = new CustomerInput();
        customerInput.setName(customer.getName());
        customerInput.setEmail(customer.getEmail());
        customerInput.setRegisterNumber(customer.getRegisterNumber());
        customerInput.setType(customer.getType());
        customerInput.setPhoneNumber(customer.getPhoneNumber());

        List<Address> addresses = List.copyOf(customer.getAddresses());
        List<AddressIdInput> addressesIdInput = new ArrayList<>();
        addresses.forEach(address -> {
            AddressIdInput addressIdInput = new AddressIdInput();
            addressIdInput.setId(address.getId());
            addressesIdInput.add(addressIdInput);
        });

        customerInput.setAddresses(addressesIdInput);

        return customerInput;
    }
}

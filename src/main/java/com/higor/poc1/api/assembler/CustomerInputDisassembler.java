package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.api.model.input.AddressIdInput;
import com.higor.poc1.api.model.input.CustomerInput;
import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerInputDisassembler {

    @Autowired
    AddressRepository addressRepository;

    public Customer toDomainObject(CustomerInput customerInput) {
        Customer customer = new Customer();
        customer.setName(customerInput.getName());
        customer.setEmail(customerInput.getEmail());
        customer.setRegisterNumber(customerInput.getRegisterNumber());
        customer.setType(customerInput.getType());
        customer.setPhoneNumber(customerInput.getPhoneNumber());

        List<AddressIdInput> addressesInput = List.copyOf(customerInput.getAddresses());
        List<Address> addresses = new ArrayList<>();
        addressesInput.forEach(addressIdInput -> {
            Address address = new Address();
            address = addressRepository.findById(addressIdInput.getId()).get();
            addresses.add(address);
        });

        customer.setAddresses(addresses);
        return customer;
    }
}

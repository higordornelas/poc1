package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerDTODisassembler {

    @Autowired
    AddressRepository addressRepository;

    public Customer toDomainObject(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setRegisterNumber(customerDTO.getRegisterNumber());
        customer.setType(customerDTO.getType());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());

        List<AddressDTO> addressDTOs = List.copyOf(customerDTO.getAddresses());
        List<Address> addresses = new ArrayList<>();
        addressDTOs.forEach(addressDTO -> {
            Address address = new Address();
            address = addressRepository.findById(addressDTO.getId()).get();
            addresses.add(address);
        });

        customer.setAddresses(addresses);
        return customer;
    }
}

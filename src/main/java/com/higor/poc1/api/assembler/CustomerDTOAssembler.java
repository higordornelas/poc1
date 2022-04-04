package com.higor.poc1.api.assembler;

import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerDTOAssembler {

    @Autowired
    AddressDTOAssembler addressDTOAssembler;

    public CustomerDTO toDTO(Customer customer) {
        List<Address> addresses = List.copyOf(customer.getAddresses());
        List<AddressDTO> addressesDTO = addressDTOAssembler.toCollectionDTO(addresses);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setRegisterNumber(customer.getRegisterNumber());
        customerDTO.setType(customer.getType());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setAddresses(addressesDTO);

        return customerDTO;
    }

    public List<CustomerDTO> toCollectionDTO(List<Customer> customers) {
        return customers.stream().map(customer -> toDTO(customer)).collect(Collectors.toList());
    }
}

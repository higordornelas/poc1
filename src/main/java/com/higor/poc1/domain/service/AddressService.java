package com.higor.poc1.domain.service;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Customer addAdressToCustomer(Long id, Address address) {
        Address addressToSave = save(address);
        Customer customer = customerRepository.findById(id);
        customer.getAddresses().add(addressToSave);
        Customer customerToSave = customerRepository.save(customer);

        return customerToSave;
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}

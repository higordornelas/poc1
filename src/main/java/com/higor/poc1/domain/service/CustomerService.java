package com.higor.poc1.domain.service;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AddressRepository addressRepository;

    public Customer save(Customer customer) {
        List<Address> addressList = customer.getAddresses();
        int size = addressList.size();

        for (int i = 0; (i < size); i++) {
            Long addressId = customer.getAddresses().get(i).getId();
            Address address = addressRepository.findById(addressId).get();
            customer.getAddresses().remove(i);
            customer.getAddresses().add(address);
        }

        return customerRepository.save(customer);
    }

    public Customer savePost(Customer customer) {
        List<Address> addressList = customer.getAddresses();
        int size = addressList.size();

        for (int i = 0; (i < size); i++) {
            Long addressId = customer.getAddresses().get(i).getId();
            Address address = addressRepository.findById(addressId).get();
            customer.getAddresses().set(i, address);
        }

        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}

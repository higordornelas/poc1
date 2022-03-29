package com.higor.poc1.domain.service;

import com.higor.poc1.domain.exception.AdressListFullException;
import com.higor.poc1.domain.exception.EntityNotFoundException;
import com.higor.poc1.domain.exception.ResourceNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    public static final String MSG_CUSTOMER_NOT_FOUND = "There is no Customer with id %d";

    public static final String MSG_RESOURCE_NOT_FOUND = "Designated Address(es) can't be found";

    public static final String MSG_ADRESS_LIST_FULL = "Customer can't have more than 5 addresses!";

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AddressRepository addressRepository;

    public Customer save(Customer customer) {
        if(customer.getAddresses().size() <= 5){
            List<Address> addressList = customer.getAddresses();
            int size = addressList.size();

            try {
                for (int i = 0; (i < size); i++) {
                    Long addressId = customer.getAddresses().get(i).getId();
                    Address address = addressRepository.findById(addressId).get();
                    customer.getAddresses().remove(i);
                    customer.getAddresses().add(address);
                }
            } catch (NoSuchElementException e){
                throw new ResourceNotFoundException(
                        String.format(MSG_RESOURCE_NOT_FOUND));
            }
            return customerRepository.save(customer);
        } else {
            throw new AdressListFullException(String.format(MSG_ADRESS_LIST_FULL));
        }
    }

    public Customer savePost(Customer customer) {
        if(customer.getAddresses().size() <= 5){
            List<Address> addressList = customer.getAddresses();
            int size = addressList.size();

            for (int i = 0; (i < size); i++) {
                Long addressId = customer.getAddresses().get(i).getId();
                Address address = addressRepository.findById(addressId).get();
                customer.getAddresses().set(i, address);
            }

            return customerRepository.save(customer);
        } else {
            throw new AdressListFullException(String.format(MSG_ADRESS_LIST_FULL));
        }
    }

    public void delete(Long customerId) {
        try {
            customerRepository.deleteById(customerId);
        } catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException(
                    String.format(MSG_CUSTOMER_NOT_FOUND, customerId)
            );
        }
    }

    public Customer findOrFail(Long customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(MSG_CUSTOMER_NOT_FOUND, customerId)
                ));
    }
}

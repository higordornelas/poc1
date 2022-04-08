package com.higor.poc1.domain.service;

import com.higor.poc1.domain.exception.AddressNotFoundException;
import com.higor.poc1.domain.exception.AdressListFullException;
import com.higor.poc1.domain.exception.EntityNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    public static final String MSG_ADDRESS_NOT_FOUND = "There is no Address with Id %d";
    public static final String MSG_ADRESS_LIST_FULL = "Customer can't have more than 5 addresses!";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Customer addAdressToCustomer(Long id, Address address) {
        Address addressToSave = save(address);
        Customer customer = customerRepository.findById(id).orElse(null);

        try {
            if(customer.getAddresses().size() <= 5){
                customer.getAddresses().add(addressToSave);
                Customer customerToSave = customerRepository.save(customer);

                return customerToSave;
            } else {
                throw new AdressListFullException(String.format(MSG_ADRESS_LIST_FULL));
            }
        } catch (EntityNotFoundException e) {
            throw new AddressNotFoundException(String.format(MSG_ADDRESS_NOT_FOUND, id));
        }


    }

    public void delete(Long id) {
        try {
            addressRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(
                    String.format(MSG_ADDRESS_NOT_FOUND, id)
            );
        }
    }

    public Address findOrFail(Long addressId){
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(MSG_ADDRESS_NOT_FOUND, addressId)
                ));
    }
}

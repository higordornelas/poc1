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
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class AddressService {

    public static final String MSG_ADDRESS_NOT_FOUND = "There is no Address with Id %d";

    @Autowired
    private AddressRepository addressRepository;

    public Address save(Address address) {
        return addressRepository.save(address);
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

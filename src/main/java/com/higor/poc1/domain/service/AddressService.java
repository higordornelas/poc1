package com.higor.poc1.domain.service;

import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.domain.exception.EntityNotFoundException;
import com.higor.poc1.domain.exception.ResourceNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.repository.AddressRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService {

    public static final String MSG_ADDRESS_NOT_FOUND = "There is no Address with Id %d";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private Validator validator;

    //@Transactional(propagation = Propagation.NESTED)
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

    public Address findOrFail(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(MSG_ADDRESS_NOT_FOUND, addressId)
                ));
    }

    //@Transactional(propagation = Propagation.NESTED)
    public Address patch(Long addressId, AddressDTO addressDTO) {
        Address patched = findOrFail(addressId);

        try {
            if (addressDTO.getStreet() != null) {
                patched.setStreet(addressDTO.getStreet());
            }
            if (addressDTO.getNumber() != null) {
                patched.setNumber(addressDTO.getNumber());
            }
            if (addressDTO.getDistrict() != null) {
                patched.setDistrict(addressDTO.getDistrict());
            }
            if (addressDTO.getCity() != null) {
                patched.setCity(addressDTO.getCity());
            }
            if (addressDTO.getZipCode() != null) {
                patched.setZipCode(addressDTO.getZipCode());
            }
            if (addressDTO.getState() != null) {
                patched.setState(addressDTO.getState());
            }
            if (addressDTO.isMain() != null) {
                patched.setMain(addressDTO.isMain());
            }
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        Set<ConstraintViolation<Address>> violations = validator.validate(patched, DTOValidation.class);

        if (!violations.isEmpty()) {
            List<String> problems = null;

            problems = violations.stream()
                    .map(violation -> {
                        String message = violation.getMessage();
                        return message;
                    })
                    .collect(Collectors.toList());

            throw new ConstraintViolationException("Error occurred: " + problems, violations);
        }

        return patched;
    }

    public Address update(Long addressId, AddressDTO addressDTO) {
        Address thisAddress = findOrFail(addressId);

        BeanUtils.copyProperties(addressDTO, thisAddress, "id");

        return thisAddress;
    }
}

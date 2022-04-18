package com.higor.poc1.domain.service;

import com.higor.poc1.api.assembler.AddressDTOAssembler;
import com.higor.poc1.api.assembler.AddressDTODisassembler;
import com.higor.poc1.api.assembler.CustomerDTOAssembler;
import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.exception.*;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    public static final String MSG_CUSTOMER_NOT_FOUND = "There is no Customer with id %d";

    public static final String MSG_ADDRESS_NOT_FOUND = "There is no Address with Id %d";

    public static final String MSG_ADDRESS_NOT_OF_CUSTOMER = "Customer with Id %d doesn't have an Address with Id %d";

    public static final String MSG_ADRESS_LIST_FULL = "Customer can't have more than 5 addresses!";

    public static final String MSG_ADRESS_IN_USE = "Address already registered to another Customer!";

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerDTOAssembler customerDTOAssembler;

    @Autowired
    private Validator validator;

    @Autowired
    private AddressDTOAssembler addressDTOAssembler;

    @Autowired
    private AddressDTODisassembler addressDTODisassembler;

    public CustomerDTO save(Customer customer) {

        try {
            if(customer.getAddresses().size() <= 5){
                List<Address> addressList = List.copyOf(customer.getAddresses());
                customer.getAddresses().clear();

                addressList.forEach(address -> {
                    Address addressToSave = addressRepository.findById(address.getId()).get();
                    customer.getAddresses().add(addressToSave);
                });

                Customer customerToSave = checkAndChooseMainAddress(customer);
                customerRepository.save(customerToSave);

                return maskData(customerToSave);
            } else {
                throw new AdressListFullException(String.format(MSG_ADRESS_LIST_FULL));
            }
        } catch (NoSuchElementException e) {
            throw new AddressNotFoundException(MSG_ADDRESS_NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new AdressInUseException(MSG_ADRESS_IN_USE);
        }
    }

    @Transactional
    public CustomerDTO patch (Long id, CustomerDTO customerDTO) {
        Customer thisCustomer = findOrFail(id);

        try {
            if (customerDTO.getName() != null) {
                thisCustomer.setName(customerDTO.getName());
            }
            if (customerDTO.getEmail() != null) {
                thisCustomer.setEmail(customerDTO.getEmail());
            }
            if (customerDTO.getRegisterNumber() != null) {
                thisCustomer.setRegisterNumber(customerDTO.getRegisterNumber());
            }
            if (customerDTO.getType() != null) {
                thisCustomer.setType(customerDTO.getType());
            }
            if (customerDTO.getPhoneNumber() != null) {
                thisCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
            }

            validate(thisCustomer);

            if (!customerDTO.getAddresses().isEmpty()) {
                for (AddressDTO addressDTO : customerDTO.getAddresses()) {
                    if (addressDTO.getId() != null) {
                        AddressDTO patched = addressDTOAssembler.toDTO(addressService.patch(addressDTO.getId(), addressDTO));
                        addressService.save(addressService.update(patched.getId(), patched));
                    } else {
                        Address address = addressDTODisassembler.toDomainObject(addressDTO);
                        addAdressToCustomer(id, address);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

        return customerDTOAssembler.toDTO(thisCustomer);
    }

    public CustomerDTO addAdressToCustomer(Long customerId, Address address) {
        Address addressToSave = addressService.save(address);
        Customer customer = customerRepository.findById(customerId).orElse(null);

        try {
            if(customer.getAddresses().size() <= 5){
                customer.getAddresses().add(addressToSave);
                checkAndChooseMainAddress(customer);

                return save(customer);
            } else {
                throw new AdressListFullException(String.format(MSG_ADRESS_LIST_FULL));
            }
        } catch (NullPointerException e) {
            throw new EntityNotFoundException(String.format(MSG_CUSTOMER_NOT_FOUND, customerId));
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

    public Customer chooseMainAddress(Long customerId, Long addressId) {
        Customer customer = findOrFail(customerId);
        Address address = addressService.findOrFail(addressId);

        if(customer.getAddresses().contains(address)){
            for (Address address1 : customer.getAddresses()) {
                address1.setMain(false);
            }
            address.setMain(true);

            return customer;
        } else {
            throw new AddressNotOfCustomerException(MSG_ADDRESS_NOT_OF_CUSTOMER);
        }
    }

    public Customer checkAndChooseMainAddress(Customer customer) {
        boolean hasMain = false;

        for (Address address : customer.getAddresses()) {
            if(address.isMain()) {
                hasMain = true;
            }
        }

        if(!customer.getAddresses().isEmpty()) {
            for (Address address1 : customer.getAddresses()) {
                address1.setMain(false);
            }

            customer.getAddresses().get(0).setMain(true);
        }

        return customer;
    }

    public CustomerDTO maskData (Customer customer) {
        CustomerDTO customerMasked = new CustomerDTO();
        customerMasked = customerDTOAssembler.toDTO(customer);
        String rgx = null;
        String masked = null;

        if (customer.getType().equals(CustomerType.LEGAL_PERSON)) {
            rgx = "([0-9]{3}).[0-9]{3}.[0-9]{3}";
            masked = customer.getRegisterNumber().replaceAll(rgx, "$1.***.***");
        } else if (customer.getType().equals(CustomerType.JURIDICAL_PERSON)) {
            rgx = "([0-9]{2}).[0-9]{3}/[0-9]{4}";
            masked = customer.getRegisterNumber().replaceAll(rgx, "$1.***/****");
        }
        customerMasked.setRegisterNumber(masked);

        rgx = "(?<=.{3}).(?=[^@]*?@)";
        masked = customer.getEmail().replaceAll(rgx, "*");
        customerMasked.setEmail(masked);

        return customerMasked;
    }

    public void validate (Customer customer) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer, DTOValidation.class);

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
    }
}

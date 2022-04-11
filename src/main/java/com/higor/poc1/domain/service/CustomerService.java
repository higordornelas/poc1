package com.higor.poc1.domain.service;

import com.higor.poc1.domain.exception.*;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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

    public Customer save(Customer customer) {

        try {
            if(customer.getAddresses().size() <= 5){
                List<Address> addressList = List.copyOf(customer.getAddresses());
                customer.getAddresses().clear();

                addressList.forEach(address -> {
                    Address addressToSave = addressRepository.findById(address.getId()).get();
                    customer.getAddresses().add(addressToSave);
                });

                Customer customerToSave = checkAndChooseMainAddress(customer);

                return customerRepository.save(customerToSave);
            } else {
                throw new AdressListFullException(String.format(MSG_ADRESS_LIST_FULL));
            }
        } catch (NoSuchElementException e) {
            throw new AddressNotFoundException(MSG_ADDRESS_NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new AdressInUseException(MSG_ADRESS_IN_USE);
        }
    }

    public Customer addAdressToCustomer(Long customerId, Address address) {
        Address addressToSave = addressService.save(address);
        Customer customer = customerRepository.findById(customerId).orElse(null);

        try {
            if(customer.getAddresses().size() <= 5){
                customer.getAddresses().add(addressToSave);
                checkAndChooseMainAddress(customer);
                Customer customerToSave = customerRepository.save(customer);

                return customerToSave;
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

        if (!hasMain && !customer.getAddresses().isEmpty()) {
            customer.getAddresses().get(0).setMain(true);
        }

        return customer;
    }

//    private void verifyCustomer(Customer customer) {
//        BeanPropertyBindingResult result = new BeanPropertyBindingResult(customer, "customer");
//        SpringValidatorAdapter adapter = new SpringValidatorAdapter(this.validator);
//        adapter.validate(customer, result);
//
//        if (result.hasErrors()) {
//            try {
//                throw new MethodArgumentNotValidException(new MethodParameter(
//                        this.getClass().getDeclaredMethod("verifyCard", YourClassName.class), 0), errors);
//            } catch (MethodArgumentNotValidException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}

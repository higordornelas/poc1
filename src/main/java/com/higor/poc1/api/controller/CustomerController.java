package com.higor.poc1.api.controller;

import com.higor.poc1.api.assembler.AddressDTOAssembler;
import com.higor.poc1.api.assembler.AddressDTODisassembler;
import com.higor.poc1.api.assembler.CustomerDTOAssembler;
import com.higor.poc1.api.assembler.CustomerDTODisassembler;
import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.exception.AddressNotFoundException;
import com.higor.poc1.domain.exception.ResourceNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import com.higor.poc1.domain.service.AddressService;
import com.higor.poc1.domain.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDTOAssembler customerDTOAssembler;

    @Autowired
    private CustomerDTODisassembler customerDTODisassembler;

    @Autowired
    private AddressDTODisassembler addressDTODisassembler;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressDTOAssembler addressDTOAssembler;

    @Autowired
    private Validator validator;

    @GetMapping
    public Page<CustomerDTO> getCustomer(
            @PageableDefault(sort = "id",
                    direction = Sort.Direction.ASC, page = 0, size = 10)
                    Pageable pageable) {

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        Page<CustomerDTO> customerDtoPage = customerPage.map(customer -> customerService.maskData(customer));

        return customerDtoPage;
    }

    @GetMapping("/{customerId}")
    public CustomerDTO findCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.findOrFail(customerId);

        return customerService.maskData(customer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerDTODisassembler.toDomainObject(customerDTO);

        return customerService.save(customer);
    }

    @PostMapping("/{id}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addAddressToCustomer(@PathVariable Long id, @Valid @RequestBody AddressDTO addressDTO) {
        Address address = addressDTODisassembler.toDomainObject(addressDTO);
        Customer customer = customerService.addAdressToCustomer(id, address);

        if (address.isMain()) {
            chooseMainAddress(id, address.getId());
        }

        return customerService.save(customer);
    }

    @GetMapping("/{customerId}/addresses/{addressId}/main")
    public CustomerDTO chooseMainAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        Customer setMainAddress = customerService.chooseMainAddress(customerId, addressId);

        return customerService.save(setMainAddress);
    }

    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerDTO customerDTO) {
        try {
            Customer customer = customerDTODisassembler.toDomainObject(customerDTO);
            Customer thisCustomer = customerService.findOrFail(customerId);

            BeanUtils.copyProperties(customer, thisCustomer, "id");

            return customerService.save(thisCustomer);
        } catch (NoSuchElementException e) {
            throw new AddressNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.delete(customerId);
    }

    @PatchMapping("/{customerId}")
    public CustomerDTO patchCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO thisCustomer = customerService.patch(customerId, customerDTO);
        customerService.checkMainAddress(thisCustomer);

        return updateCustomer(customerId, thisCustomer);
    }

    @GetMapping("/search")
    public Page<CustomerDTO> searchCustomer(@PageableDefault(sort = "id",
            direction = Sort.Direction.ASC, page = 0, size = 10) Pageable pageable,
                String name, String email, String registerNumber, String type, String phoneNumber){

        Page<Customer> customerPage = customerRepository.find(name, email, registerNumber, type, phoneNumber, pageable);
        Page<CustomerDTO> customersDTO = customerPage.map(customer -> customerDTOAssembler.toDTO(customer));

        return customersDTO;
    }
}

package com.higor.poc1.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higor.poc1.api.assembler.CustomerDTOAssembler;
import com.higor.poc1.api.assembler.CustomerDTODisassembler;
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
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    CustomerDTOAssembler customerDTOAssembler;

    @Autowired
    CustomerDTODisassembler customerDTODisassembler;

    @GetMapping
    public Page<CustomerDTO> getCustomer(
            @PageableDefault(sort = "id",
                    direction = Sort.Direction.ASC, page = 0, size = 10)
                    Pageable pageable) {

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        Page<CustomerDTO> customerDtoPage = customerPage.map(customer -> customerDTOAssembler.toDTO(customer));

        return customerDtoPage;
    }

    @GetMapping("/{customerId}")
    public CustomerDTO findCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.findOrFail(customerId);
        return customerDTOAssembler.toDTO(customer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        try {
            Customer customer = customerDTODisassembler.toDomainObject(customerDTO);

            return customerDTOAssembler.toDTO(customerService.save(customer));
        } catch (NoSuchElementException e) {
            throw new AddressNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/{id}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addAddressToCustomer(@PathVariable Long id, @Valid @RequestBody Address address) {
        return customerService.addAdressToCustomer(id, address);
    }

    @GetMapping("/{customerId}/addresses/{addressId}/main")
    public CustomerDTO chooseMainAddress(@PathVariable Long customerId, @PathVariable Long addressId) {
        Customer setMainAddress = customerService.chooseMainAddress(customerId, addressId);

        return customerDTOAssembler.toDTO(setMainAddress);
    }

    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerDTO customerDTO) {
        try {
            Customer customer = customerDTODisassembler.toDomainObject(customerDTO);
            Customer thisCustomer = customerService.findOrFail(customerId);

            BeanUtils.copyProperties(customer, thisCustomer, "id");

            return customerDTOAssembler.toDTO(customerService.save(thisCustomer));
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
    public CustomerDTO patchCustomer(@PathVariable Long customerId, @Valid @RequestBody Map<String, Object> fields) {
        Customer thisCustomer = customerService.findOrFail(customerId);

        merge(fields, thisCustomer);

        return updateCustomer(customerId, customerDTOAssembler.toDTO(thisCustomer));
    }

    private void merge(Map<String, Object> sourceFields, Customer customerToPatch) {
        ObjectMapper objectMapper = new ObjectMapper();
        Customer thisCustomer = objectMapper.convertValue(sourceFields, Customer.class);

        sourceFields.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(Customer.class, propertyName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, thisCustomer);

            ReflectionUtils.setField(field, customerToPatch, newValue);
        });
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

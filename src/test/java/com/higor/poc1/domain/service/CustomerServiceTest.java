package com.higor.poc1.domain.service;

import com.higor.poc1.api.assembler.AddressDTOAssembler;
import com.higor.poc1.api.assembler.CustomerDTOAssembler;
import com.higor.poc1.api.assembler.CustomerDTODisassembler;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.exception.AddressListFullException;
import com.higor.poc1.domain.exception.AddressNotFoundException;
import com.higor.poc1.domain.exception.EntityNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    public static final int INDEX = 0;

    public static final String MSG_CUSTOMER_NOT_FOUND = "There is no Customer with id %d";
    Long id = 1L;
    String name = "Kakyoin Noriaki";
    String email = "kakyoinnoriaki@hotmail.com";
    String registerNumber = "021.561.158-69";
    CustomerType type = CustomerType.LEGAL_PERSON;
    String phoneNumber = "98800-1225";

    @Mock
    private AddressService addressService;

    @InjectMocks
    private CustomerService customerService;

    @InjectMocks
    MaskService maskService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    CustomerDTOAssembler customerDTOAssembler;

    @Mock
    AddressDTOAssembler addressDTOAssembler;

    private Customer customer;
    private CustomerDTO customerDTO;
    private Optional<Customer> optionalCustomer;
    private Address address;
    private Address address2;
    private AddressDTO addressDTO;
    private AddressDTO addressDTO2;

    private Optional<Address> optionalAddress;
    private Optional<Address> optionalAddress2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCustomer();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void whenFindAll_thenReturnAList() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> response = customerRepository.findAll();

        assertNotNull(response);
        assertEquals(response.size(), 1);
        assertEquals(Customer.class, response.get(INDEX).getClass());
        assertEquals(id, response.get(INDEX).getId());
        assertEquals(name, response.get(INDEX).getName());
        assertEquals(email, response.get(INDEX).getEmail());
        assertEquals(registerNumber, response.get(INDEX).getRegisterNumber());
        assertEquals(type, response.get(INDEX).getType());
        assertEquals(phoneNumber, response.get(INDEX).getPhoneNumber());
        assertEquals(customer.getAddresses(), response.get(INDEX).getAddresses());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void whenPost_thenReturnSuccess() {
        when(customerRepository.save(any())).thenReturn(customer);
        when(addressRepository.findById(anyLong())).thenReturn(optionalAddress);
        when(customerDTOAssembler.toDTO(any())).thenReturn(customerDTO);
        CustomerDTO response = customerService.save(customer);

        assertNotNull(response);
        assertEquals(CustomerDTO.class, response.getClass());
        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(maskService.maskEmail(email), response.getEmail());
        if (customer.getType() == CustomerType.LEGAL_PERSON) {
            assertEquals(maskService.maskCPF(registerNumber), response.getRegisterNumber());
        } else {
            assertEquals(maskService.maskCNPJ(registerNumber), response.getRegisterNumber());
        }
        assertEquals(type, response.getType());
        assertEquals(phoneNumber, response.getPhoneNumber());
        assertEquals(customerDTO.getAddresses(), response.getAddresses());
    }

    @Test
    public void whenPost_thenReturnAnAddressNotFoundException() {
        try {
            optionalCustomer.get().setId(200L);
            customerService.save(customer);
        } catch (Exception ex) {
            assertEquals(AddressNotFoundException.class, ex.getClass());
            assertEquals(String.format("There is no Address with Id %d", id), ex.getMessage());
        }
    }

    @Test
    public void whenPostCustomerWithFiveOrMoreAddress_thenReturnAddressListFullException() {
        try {
            List<Address> addresses = new ArrayList<>();
            Address address1 = new Address(3L, "First Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
            Address address2 = new Address(4L, "First Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", false);
            Address address3 = new Address(5L, "First Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", false);
            Address address4 = new Address(6L, "First Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", false);
            Address address5 = new Address(7L, "First Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", false);
            Address address6 = new Address(8L, "First Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", false);

            addresses.add(address1);
            addresses.add(address2);
            addresses.add(address3);
            addresses.add(address4);
            addresses.add(address5);
            addresses.add(address6);

            Customer customerToSave = new Customer(6L, "Xavier", "xavier@email.com", "351.007.442-43", CustomerType.LEGAL_PERSON, "3215-7878", addresses);
            customerService.save(customerToSave);
        } catch (Exception ex) {
            assertEquals(AddressListFullException.class, ex.getClass());
            assertEquals("Customer can't have more than 5 addresses!", ex.getMessage());
        }
    }

    @Test
    public void whenPut_thenReturnSuccess() {
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);
        when(addressRepository.findById(anyLong())).thenReturn(optionalAddress);
        when(customerDTOAssembler.toDTO(any())).thenReturn(customerDTO);
        CustomerDTO response = customerService.updateCustomer(customer);

        assertNotNull(response);
        assertEquals(CustomerDTO.class, response.getClass());
        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(maskService.maskEmail(email), response.getEmail());
        if (customer.getType() == CustomerType.LEGAL_PERSON) {
            assertEquals(maskService.maskCPF(registerNumber), response.getRegisterNumber());
        } else {
            assertEquals(maskService.maskCNPJ(registerNumber), response.getRegisterNumber());
        }
        assertEquals(type, response.getType());
        assertEquals(phoneNumber, response.getPhoneNumber());
        assertEquals(customerDTO.getAddresses(), response.getAddresses());
    }

    @Test
    public void whenPut_thenReturnAnEntityNotFoundException() {
        try {
            when(customerRepository.findById(anyLong())).thenThrow(new EntityNotFoundException(String.format(MSG_CUSTOMER_NOT_FOUND, anyLong())));
            customerService.updateCustomer(customer);
        } catch (Exception ex) {
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals(String.format("There is no Customer with id %d", 0L), ex.getMessage());
        }
    }

    @Test
    public void whenPatch_thenReturnSuccess() {
        when(customerRepository.save(any())).thenReturn(customer);
        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);
        when(addressRepository.findById(anyLong())).thenReturn(optionalAddress);
        when(customerDTOAssembler.toDTO(any())).thenReturn(customerDTO);
        CustomerDTO response = customerService.patch(customer.getId(), customerDTO);

        assertNotNull(response);
        assertEquals(CustomerDTO.class, response.getClass());
        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(maskService.maskEmail(email), response.getEmail());
        if (customer.getType() == CustomerType.LEGAL_PERSON) {
            assertEquals(maskService.maskCPF(registerNumber), response.getRegisterNumber());
        } else {
            assertEquals(maskService.maskCNPJ(registerNumber), response.getRegisterNumber());
        }
        assertEquals(type, response.getType());
        assertEquals(phoneNumber, response.getPhoneNumber());
        assertEquals(customerDTO.getAddresses(), response.getAddresses());
    }

    @Test
    public void whenPatch_thenReturnAnEntityNotFoundException() {
        try {
            customerService.patch(999L, customerDTO);
        } catch (Exception ex) {
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals(String.format(MSG_CUSTOMER_NOT_FOUND, 999L), ex.getMessage());
        }
    }

    @Test
    public void addAdressToCustomer() {
    }

    @Test
    public void whenDelete_thenReturnSuccess() {
        doNothing().when(customerRepository).deleteById(anyLong());
        customerService.delete(id);

        verify(customerRepository,times(1)).deleteById(1L);
    }

    @Test
    public void whenDelete_thenReturnCustomerNotFoundException() {
        doThrow(new EntityNotFoundException(String.format(MSG_CUSTOMER_NOT_FOUND, 0L))).when(customerRepository).deleteById(anyLong());
        try {
            customerService.delete(anyLong());
        } catch (Exception ex) {
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals(String.format(MSG_CUSTOMER_NOT_FOUND, 0L), ex.getMessage());
        }
    }

    @Test
    public void whenFindOrFail_thenReturnACustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);

        Customer customerFound = customerService.findOrFail(id);

        assertNotNull(customerFound);
        assertEquals(Customer.class, customerFound.getClass());
        assertEquals(id, customerFound.getId());
    }

    @Test
    public void whenFindOrFail_ThenReturnEntityNotFoundException() {
        when(customerRepository.findById(anyLong())).thenThrow(new EntityNotFoundException(String.format(MSG_CUSTOMER_NOT_FOUND, anyLong())));

        try {
            customerService.findOrFail(id);
        } catch (Exception ex) {
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals(String.format(MSG_CUSTOMER_NOT_FOUND, 0L), ex.getMessage());
        }
    }

    @Test
    public void whenChooseMainAddress_thenReturnAlteredMainAddress() {
        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);
        when(addressService.findOrFail(address2.getId())).thenReturn(address2);

        customerService.chooseMainAddress(customer.getId(), address2.getId());

        assertEquals(customer.getAddresses().get(0), address);
        assertEquals(customer.getAddresses().get(1), address2);
        assertEquals(false, customer.getAddresses().get(0).isMain());
        assertEquals(true, customer.getAddresses().get(1).isMain());
    }

    @Test
    public void maskData() {
        CustomerDTO response = customerService.maskData(customer);

        assertNotNull(response);
        assertEquals(CustomerDTO.class, response.getClass());
        assertEquals(id, response.getId());
        assertEquals(name, response.getName());
        assertEquals(maskService.maskEmail(email), response.getEmail());
        if (customer.getType() == CustomerType.LEGAL_PERSON) {
            assertEquals(maskService.maskCPF(registerNumber), response.getRegisterNumber());
        } else {
            assertEquals(maskService.maskCNPJ(registerNumber), response.getRegisterNumber());
        }
        assertEquals(type, response.getType());
        assertEquals(phoneNumber, response.getPhoneNumber());
        assertEquals(customerDTO.getAddresses(), response.getAddresses());
    }

    @Test
    public void validate() {
    }

    private void startCustomer() {
        List<Address> addresses = new ArrayList<>();
        List<AddressDTO> addressesDTO = new ArrayList<>();

        address = new Address(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
        addresses.add(address);

        address2 = new Address(2L, "Second Av.", "222", "Second District", "Second City", "22222-222", "Second State", false);
        addresses.add(address2);

        addressDTO = new AddressDTO(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
        addressesDTO.add(addressDTO);

        addressDTO2 = new AddressDTO(2L, "Second Av.", "222", "Second District", "Second City", "22222-222", "Second State", false);
        addressesDTO.add(addressDTO2);

        customer = new Customer(id, name, email, registerNumber, type, phoneNumber, addresses);
        optionalCustomer = Optional.of(new Customer(id, name, email, registerNumber, type, phoneNumber, addresses));
        customerDTO = new CustomerDTO(id, name, email, registerNumber, type, phoneNumber, addressesDTO);
        optionalAddress = Optional.of(new Address(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true));
        optionalAddress2 = Optional.of(new Address(2L, "Second Av.", "222", "Second District", "Second City", "22222-222", "Second State", false));
    }
}
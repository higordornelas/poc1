package com.higor.poc1.api.controller;

import com.higor.poc1.api.assembler.CustomerDTODisassembler;
import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import com.higor.poc1.domain.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerControllerTest {

    public static final int INDEX = 0;
    Long id = 1L;
    String name = "Kakyoin Noriaki";
    String email = "kakyoinnoriaki@hotmail.com";
    String registerNumber = "021.561.158-69";
    CustomerType type = CustomerType.LEGAL_PERSON;
    String phoneNumber = "98800-1225";

    @InjectMocks
    private CustomerController customerController;

//    @Mock
//    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    CustomerDTODisassembler customerDTODisassembler;

    private Customer customer;
    private CustomerDTO customerDTO;
    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCustomer();
    }

    @Test
    void whenGetAllCustomers_thenReturnAListOfCustomers() {
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void whenFindCustomer_thenSuccess() {
        when(customerService.findOrFail(anyLong())).thenReturn(customer);
//        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        CustomerDTO response = customerController.findCustomer(id);

        assertNotNull(response);
        assertEquals(CustomerDTO.class, response.getClass());
    }

    @Test
    void whenAddCustomer_thenReturnCreated() {
        when(customerService.save(any())).thenReturn(customerDTO);

        CustomerDTO response = customerController.addCustomer(customerDTO);

        assertNotNull(response);
    }

    @Test
    void addAddressToCustomer() {
    }

    @Test
    void chooseMainAddress() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void patchCustomer() {
    }

    @Test
    void searchCustomer() {
    }

    private void startCustomer() {
        List<Address> addresses = new ArrayList<>();
        List<AddressDTO> addressesDTO = new ArrayList<>();

        address = new Address(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
        addresses.add(address);

        addressDTO = new AddressDTO(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
        addressesDTO.add(addressDTO);

        customer = new Customer(id, name, email, registerNumber, type, phoneNumber, addresses);
        customerDTO = new CustomerDTO(id, name, email, registerNumber, type, phoneNumber, addressesDTO);
    }
}
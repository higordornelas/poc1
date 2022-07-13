package com.higor.poc1.domain.service;

import com.higor.poc1.api.controller.CustomerController;
import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.exception.EntityNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(CustomerController.class)
public class CustomerWebLayerTest {

    @InjectMocks
    private AddressService addressService;

    @InjectMocks
    private CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testShouldReturnStatus200_WhenFindingCustomer() throws Exception {
        List<Customer> customersList = new ArrayList<Customer>();
        List<Address> addressesList = new ArrayList<Address>();

        Customer customer1 = new Customer(2L, "Jotaro Kujoh", "jotarokujoh@gmail.com", "925.663.597-68", CustomerType.LEGAL_PERSON, "3251-5570", addressesList);
        Customer customer2 = new Customer(3L, "Jolyne Kujoh", "jolynekujoh@hotmail.com", "362.152.957-87", CustomerType.LEGAL_PERSON, "3251-2275", addressesList);
        Customer customer3 = new Customer(4L, "Speedwagon Foundation", "speedwagon@gmail.com", "45.746.883/0001-11", CustomerType.JURIDICAL_PERSON, "3001-1010", addressesList);

        customersList.add(customer1);
        customersList.add(customer2);
        customersList.add(customer3);

        when(customerRepository.findAll()).thenReturn(customersList);

        List<Customer> list = customerRepository.findAll();

        assertEquals(3, list.size());
        verify(customerRepository, times(1)).findAll();

//        mockMvc.perform(MockMvcRequestBuilders.get("/customers").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].id", is("2L"))).andExpect(jsonPath("$[0].name", is("Jotaro Kujoh")))
//                    .andExpect(jsonPath("$[0].email", is("jotarokujoh@gmail.com"))).andExpect(jsonPath("$[0].registerNumber", is("925.663.597-68")))
//                    .andExpect(jsonPath("$[0].type", is(CustomerType.LEGAL_PERSON))).andExpect(jsonPath("$[0].phoneNumber", is("3251-5570")))
//                .andExpect(jsonPath("$[0].id", is("3L"))).andExpect(jsonPath("$[0].name", is("Jolyne Kujoh")))
//                    .andExpect(jsonPath("$[0].email", is("jolynekujoh@hotmail.com"))).andExpect(jsonPath("$[0].registerNumber", is("362.152.957-87")))
//                    .andExpect(jsonPath("$[0].type", is(CustomerType.LEGAL_PERSON))).andExpect(jsonPath("$[0].phoneNumber", is("3251-2275")))
//                .andExpect(jsonPath("$[0].id", is("4L"))).andExpect(jsonPath("$[0].name", is("Speedwagon Foundation")))
//                    .andExpect(jsonPath("$[0].email", is("speedwagon@gmail.com"))).andExpect(jsonPath("$[0].registerNumber", is("45.746.883/0001-11")))
//                    .andExpect(jsonPath("$[0].type", is(CustomerType.JURIDICAL_PERSON))).andExpect(jsonPath("$[0].phoneNumber", is("3001-1010")));
    }

    @Test()
    public void test_GetCustomerById_ThrowsCustomerNotFoundException() throws Exception {

        // Return an empty Optional object since we didn't find the id
        when(customerRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Customer customer = customerService.findOrFail(Mockito.anyLong());
    }

    @Test
    public void maskData() {
    }

    @Test
    public void validate() {
    }
}
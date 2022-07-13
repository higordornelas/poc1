package com.higor.poc1.domain.service;

import com.higor.poc1.api.model.AddressDTO;
import com.higor.poc1.api.model.CustomerDTO;
import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import com.google.gson.Gson;

@TestPropertySource("/application-test.properties")
@EnableConfigurationProperties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerServiceIntegrationTest {

    public static final int INDEX = 0;
    Long id = 1L;
    String name = "Kakyoin Noriaki";
    String email = "kakyoinnoriaki@hotmail.com";
    String registerNumber = "021.561.158-69";
    CustomerType type = CustomerType.LEGAL_PERSON;
    String phoneNumber = "98800-1225";

    @LocalServerPort
    private int serverPort;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AddressRepository addressRepository;

    private Customer customer;
    private CustomerDTO customerDTO;
    private Optional<Customer> optionalCustomer;
    private Address address;
    private AddressDTO addressDTO;

    private Optional<Address> optionalAddress;

    final static Gson gson = new Gson();

    @BeforeAll
    public void preCondition() {
        baseURI = "http://localhost";
//        basePath = "/customers";
        port = serverPort;

        startCustomer();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void givenCustomer_whenCreateCustomer_thenReturnSavedCustomer() {
        List<Address> addresses = new ArrayList<>();

        Address addressPost = new Address("Zzyzx Rd.", "10", "Mojave", "Zzyzx", "22408-000", "California", true);

        addresses.add(addressPost);

        Customer customerPost = new Customer("Corey Taylor", "coreytaylor@gmail.com", "219.491.474-95", CustomerType.LEGAL_PERSON, "1234-5678", addresses);

        Response response = given()
                .body(gson.toJson(customerPost))
                .contentType(ContentType.JSON)
                .when()
                        .post("/customers")
                .then()
                .log().all()
                .statusCode(201)
                .extract().response();

        assertEquals("Corey Taylor", response.jsonPath().getString("name"));
        assertEquals("cor********@gmail.com", response.jsonPath().getString("email"));
        assertEquals("219.***.***-95", response.jsonPath().getString("registerNumber"));
        assertEquals("LEGAL_PERSON", response.jsonPath().getString("type"));
        assertEquals("1234-5678", response.jsonPath().getString("phoneNumber"));
    }

    @Test
    public void testAddressPostSuccess() {
        Address addressPost = new Address("Zzyzx Rd.", "10", "Mojave", "Zzyzx", "22408-000", "California", true);

        given()
                .body(gson.toJson(addressPost))
                .contentType(ContentType.JSON)
                .when()
                .post("/addresses")
                .then()
                .statusCode(201);
    }

    @Test
    public void givenListOfCustomer_whenGetAllCustomers_thenReturnCustomerList() {
        List<Customer> allCustomers = new ArrayList<>();

        List<Address> addresses1 = new ArrayList<>();
        List<Address> addresses2 = new ArrayList<>();

        Address address1 = new Address("Zzyzx Rd.", "10", "Mojave", "Zzyzx", "22408-000", "California", true);
        Address address2 = new Address("Street Rd.", "99", "Round", "Iowa", "00555-666", "Texas", true);
        addressRepository.save(address1);
        addressRepository.save(address2);

        addresses1.add(address1);
        addresses2.add(address2);

        Customer customer1 = new Customer("Mick Thomson", "mickthomson@gmail.com", "483.195.526-48", CustomerType.LEGAL_PERSON, "9876-5432", addresses1);
        customerRepository.save(customer1);
        Customer customer2 = new Customer("Chris Fehn", "chrisfehn@gmail.com", "014.639.655-32", CustomerType.LEGAL_PERSON, "7777-6666", addresses2);
        customerRepository.save(customer2);

        allCustomers.add(customer1);
        allCustomers.add(customer2);

        Response response = given().when().get("/customers")
                .then().statusCode(200).extract().response();

        assertNotNull(response);
    }

    @Test
    public void givenCustomerId_whenGetCustomerById_thenReturnCustomer() {
        List<Address> addresses = new ArrayList<>();

        Address address = new Address("Rua Verde", "111", "Centro", "Manaus", "17864-123", "Amazonas", true);
        addressRepository.save(address);

        addresses.add(address);

        Customer customer = new Customer("Cleiton Rasta", "cleitonrasta@gmail.com", "602.631.732-59", CustomerType.LEGAL_PERSON, "1212-1212", addresses);

        customerRepository.save(customer);

        given().when().get("/customers/1")
                .then().statusCode(200);
    }

    @Test
    public void givenInvalidURL_whenGet_thenReturnPageNotFound() {
        given().when().get("/invalid")
                .then().statusCode(404);
    }

    @Test
    public void givenCustomer_whenUpdateCustomer_thenReturnUpdatedCustomer() {
        List<Address> addresses1 = new ArrayList<>();

        Address address = new Address("Rua Verde", "111", "Centro", "Manaus", "17864-123", "Amazonas", true);

        addresses1.add(address);

        Customer customer = new Customer("Cleiton Rasta", "cleitonrasta@gmail.com", "602.631.732-59", CustomerType.LEGAL_PERSON, "1212-1212", addresses1);

        Response response = given()
                .log().all()
                .body(gson.toJson(customer))
                .contentType(ContentType.JSON)
                .when()
                .put("/customers/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        assertEquals("Cleiton Rasta", response.jsonPath().getString("name"));
        assertEquals("cle*********@gmail.com", response.jsonPath().getString("email"));
        assertEquals("602.***.***-59", response.jsonPath().getString("registerNumber"));
        assertEquals("LEGAL_PERSON", response.jsonPath().getString("type"));
        assertEquals("1212-1212", response.jsonPath().getString("phoneNumber"));
    }

    @Test
    public void givenCustomer_whenPatchCustomer_thenReturnPatchedCustomer() {
        List<Address> addresses = new ArrayList<>();
        Address newAddress = new Address();
        Customer newCustomer = new Customer();

        //newAddress.setId(1L);
        newAddress.setStreet("Jozenji Rd.");
        newAddress.setNumber("28");
        newAddress.setDistrict("Morioh");
        newAddress.setCity("S City");
        newAddress.setZipCode("51108-110");
        newAddress.setState("M Prefecture");
        newAddress.setMain(false);

        addresses.add(newAddress);

        //newCustomer.setId(1L);
        newCustomer.setName("Kira Yoshikage");
        newCustomer.setEmail("kirayoshikage@gmail.com");
        newCustomer.setRegisterNumber("357.672.271-87");
        newCustomer.setType(CustomerType.LEGAL_PERSON);
        newCustomer.setPhoneNumber("3211-0010");
        newCustomer.setAddresses(addresses);

        Response response = given()
                .body(gson.toJson(newCustomer))
                .contentType(ContentType.JSON)
                .when()
                .patch("/customers/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        assertEquals("Kira Yoshikage", response.jsonPath().getString("name"));
        assertEquals("kir**********@gmail.com", response.jsonPath().getString("email"));
        assertEquals("357.***.***-87", response.jsonPath().getString("registerNumber"));
        assertEquals("LEGAL_PERSON", response.jsonPath().getString("type"));
        assertEquals("3211-0010", response.jsonPath().getString("phoneNumber"));
    }

    @Test
    public void givenCustomerId_whenDeleteCustomer_thenReturnNoContent() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/customers/1")
                .then()
                .statusCode(204);
    }

    @Disabled
    @Test
    public void addAdressToCustomer() {
        Address newAddress = new Address();

//        newAddress.setId(5L);
        newAddress.setStreet("Ali Basha");
        newAddress.setNumber("21");
        newAddress.setDistrict("El-Darb El-Ahmar");
        newAddress.setCity("Cairo");
        newAddress.setZipCode("19484-000");
        newAddress.setState("Egypt");
        newAddress.setMain(false);

        addressRepository.save(newAddress);

        Response response = given()
                .body(gson.toJson(newAddress))
                .contentType(ContentType.JSON)
                .when()
                .post("/customers/1/addresses")
                .then()
                .log().all()
                .statusCode(201)
                .extract().response();

//        assertEquals(newCustomer, customerRepository.findById(newCustomer.getId()).get());

        assertEquals("Ali Basha", response.jsonPath().getString("street"));
        assertEquals("21", response.jsonPath().getString("number"));
        assertEquals("El-Darb El-Ahmar", response.jsonPath().getString("district"));
        assertEquals("Cairo", response.jsonPath().getString("city"));
        assertEquals("19484-000", response.jsonPath().getString("zipCode"));
        assertEquals("Egypt", response.jsonPath().getString("state"));
    }

    private void startCustomer() {
        List<Address> addresses = new ArrayList<>();
        List<AddressDTO> addressesDTO = new ArrayList<>();

        address = new Address( "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
        addresses.add(address);

        addressRepository.save(address);

        addressDTO = new AddressDTO(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true);
        addressesDTO.add(addressDTO);

        customer = new Customer(name, email, registerNumber, type, phoneNumber, addresses);
        optionalCustomer = Optional.of(new Customer(name, email, registerNumber, type, phoneNumber, addresses));
        customerDTO = new CustomerDTO(id, name, email, registerNumber, type, phoneNumber, addressesDTO);
        optionalAddress = Optional.of(new Address(1L, "Budo-ga Oka Rd.", "72", "Morioh", "S City", "51108-110", "M Prefecture", true));

        customerRepository.save(customer);
    }
}
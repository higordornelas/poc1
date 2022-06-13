package com.higor.poc1;

import com.higor.poc1.domain.enumerator.CustomerType;
import com.higor.poc1.domain.exception.EntityNotFoundException;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.service.AddressService;
import com.higor.poc1.domain.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class Poc1ApplicationTests {

	@Autowired
	private AddressService addressService;

	@Autowired
	private CustomerService customerService;

	@Test
	void testCustomerPostSuccess() {
		Customer newCustomer = new Customer();
		Address newAddress = new Address();
		List<Address> addresses = new ArrayList<>();

		newAddress.setStreet("Av. Rui Barbosa");
		newAddress.setNumber("12");
		newAddress.setDistrict("Centro");
		newAddress.setCity("Palmirinha do Sul");
		newAddress.setZipCode("34110-112");
		newAddress.setState("Mato Grosso do Sul");
		newAddress.setMain(true);

		addresses.add(newAddress);

		newCustomer.setName("Claudio");
		newCustomer.setEmail("claudinho@email.com");
		newCustomer.setRegisterNumber("128.150.116-28");
		newCustomer.setType(CustomerType.LEGAL_PERSON);
		newCustomer.setPhoneNumber("3211-0010");
		newCustomer.setAddresses(addresses);

		addressService.save(newAddress);
		customerService.save(newCustomer);

		assertThat(newAddress, notNullValue());
		assertThat(newCustomer, notNullValue());
	}

	@Test
	void testAddressPostSuccess() {
		Address newAddress = new Address();

		newAddress.setStreet("Av. Rui Barbosa");
		newAddress.setNumber("12");
		newAddress.setDistrict("Centro");
		newAddress.setCity("Palmirinha do Sul");
		newAddress.setZipCode("34110-112");
		newAddress.setState("Mato Grosso do Sul");
		newAddress.setMain(true);

		addressService.save(newAddress);

		assertThat(newAddress, notNullValue());
	}

	@Test
	void shouldFail_whenDeletingInexistingCustomer() {
		EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			customerService.delete(999L);
		}, "EntityNotFoundException was expected");

		Assertions.assertEquals("There is no Customer with id 999", thrown.getMessage());
	}

}

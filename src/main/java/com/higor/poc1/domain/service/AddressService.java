package com.higor.poc1.domain.service;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.AddressRepository;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Address> list() {
        return addressRepository.list();
    }

    public Address find(Long id) {
        return addressRepository.findById(id);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Address update(Long addressId, Address address) {
        Address thisAddress = find(addressId);
        BeanUtils.copyProperties(address, thisAddress, "id");
        thisAddress = save(thisAddress);

        return thisAddress;
    }

    public Customer addAdressToCustomer(Long id, Address address) {
        Address addressToSave = save(address);
        Customer customer = customerRepository.findById(id);
        customer.getAddresses().add(addressToSave);
        Customer customerToSave = customerRepository.save(customer);

        return customerToSave;
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}

package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class CustomerRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<Customer> list() {
        return manager.createQuery("From Customer", Customer.class).getResultList();
    }

    public Customer findById(Long id) {
        return manager.find(Customer.class, id);
    }

    public Customer save(Customer customer) {
        return manager.merge(customer);
    }

    public void deleteById(Long id) {
        Customer customer = findById(id);
        manager.remove(customer);
    }
}

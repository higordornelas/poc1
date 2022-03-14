package com.higor.poc1.infrastructure.repository;

import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class CustomerRepositoryImplementation implements CustomerRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Customer> list() {
        return manager.createQuery("From Customer", Customer.class).getResultList();
    }

    @Override
    public Customer find(Long id) {
        return manager.find(Customer.class, id);
    }

    @Transactional
    @Override
    public Customer update(Customer customer) {
        return manager.merge(customer);
    }

    @Transactional
    @Override
    public void delete(Customer customer) {
        customer = find(customer.getId());
        manager.remove(customer);
    }
}

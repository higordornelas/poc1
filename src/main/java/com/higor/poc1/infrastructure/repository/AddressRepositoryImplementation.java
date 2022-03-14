package com.higor.poc1.infrastructure.repository;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.repository.AddressRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class AddressRepositoryImplementation implements AddressRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Address> list() {
        return manager.createQuery("From Address", Address.class).getResultList();
    }

    @Override
    public Address find(Long id) {
        return manager.find(Address.class, id);
    }

    @Transactional
    @Override
    public Address save(Address Address) {
        return manager.merge(Address);
    }

    @Transactional
    @Override
    public void delete(Address address) {
        address = find(address.getId());
        manager.remove(address);
    }
}

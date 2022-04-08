package com.higor.poc1.domain.enumerator;

import com.higor.poc1.domain.model.Customer;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class CustomerGroupSequenceProvider implements DefaultGroupSequenceProvider<Customer> {

    @Override
    public List<Class<?>> getValidationGroups(Customer customer) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Customer.class);

        if (isSelectedCustomer(customer)) {
            groups.add((customer.getType().getGroup()));
        }

        return groups;
    }

    protected boolean isSelectedCustomer(Customer customer) {
        return customer != null && customer.getType() != null;
    }
}

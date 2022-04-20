package com.higor.poc1.domain.enumerator;

import com.higor.poc1.api.model.CustomerDTO;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class CustomerGroupSequenceProvider implements DefaultGroupSequenceProvider<CustomerDTO> {

    @Override
    public List<Class<?>> getValidationGroups(CustomerDTO customer) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(CustomerDTO.class);

        if (isSelectedCustomer(customer)) {
            groups.add((customer.getType().getGroup()));
        }

        return groups;
    }

    protected boolean isSelectedCustomer(CustomerDTO customer) {
        return customer != null && customer.getType() != null;
    }
}

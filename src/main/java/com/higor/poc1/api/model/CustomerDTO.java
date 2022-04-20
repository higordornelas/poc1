package com.higor.poc1.api.model;

import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.core.validation.EmailCustom;
import com.higor.poc1.api.core.validation.PhoneNumber;
import com.higor.poc1.domain.enumerator.CnpjGroup;
import com.higor.poc1.domain.enumerator.CpfGroup;
import com.higor.poc1.domain.enumerator.CustomerGroupSequenceProvider;
import com.higor.poc1.domain.enumerator.CustomerType;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

@GroupSequenceProvider(CustomerGroupSequenceProvider.class)
public class CustomerDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @EmailCustom(groups = {Default.class, DTOValidation.class})
    private String email;

    @NotBlank
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    private String registerNumber;

    @NotNull
    private CustomerType type;

    @NotBlank
    @PhoneNumber(groups = {Default.class, DTOValidation.class})
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotEmpty
    private List<AddressDTO> addresses = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}

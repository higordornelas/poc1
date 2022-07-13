package com.higor.poc1.domain.model;

import com.higor.poc1.api.core.validation.DTOValidation;
import com.higor.poc1.api.core.validation.EmailCustom;
import com.higor.poc1.api.core.validation.PhoneNumber;
import com.higor.poc1.domain.enumerator.CnpjGroup;
import com.higor.poc1.domain.enumerator.CpfGroup;
import com.higor.poc1.domain.enumerator.CustomerType;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.util.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank
    @EmailCustom(groups = {Default.class, DTOValidation.class})
    private String email;

    @NotBlank(message = "Register number cannot be blank")
    @CPF(groups = CpfGroup.class)
    @CNPJ(groups = CnpjGroup.class)
    @Column(name = "register_number")
    private String registerNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type cannot be null")
    private CustomerType type;

    @NotBlank(message = "Phone Number cannot be null")
    @PhoneNumber(groups = {Default.class, DTOValidation.class})
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.MERGE) //persist
//    @NotEmpty
//    @Valid
    private List<Address> addresses = new ArrayList<>();

    public Customer() {
    }

    public Customer(Long id, String name, String email, String registerNumber, CustomerType type, String phoneNumber, List<Address> addresses) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registerNumber = registerNumber;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
    }

    public Customer(String name, String email, String registerNumber, CustomerType type, String phoneNumber, List<Address> addresses) {
        this.name = name;
        this.email = email;
        this.registerNumber = registerNumber;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

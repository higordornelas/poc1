package com.higor.poc1.domain.model;

import com.higor.poc1.domain.enumerator.CustomerGroupSequenceProvider;
import com.higor.poc1.domain.enumerator.CustomerType;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.springframework.context.annotation.Conditional;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.*;

@Entity
//@GroupSequenceProvider(CustomerGroupSequenceProvider.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Register number cannot be blank")
//    @CPF(groups = CpfGroup.class)
//    @CNPJ(groups = CnpjGroup.class)
    @Column(name = "register_number")
    private String registerNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type cannot be null")
    private CustomerType type;

    @NotBlank(message = "Phone Number cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{4}")
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Address> addresses = new ArrayList<>();

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

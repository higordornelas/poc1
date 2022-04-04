//package com.higor.poc1.api.assembler;
//
//import com.higor.poc1.api.model.AddressDTO;
//import com.higor.poc1.api.model.input.AddressIdInput;
//import com.higor.poc1.api.model.input.CustomerInput;
//import com.higor.poc1.domain.model.Address;
//import com.higor.poc1.domain.model.Customer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class AddressInputAssembler {
//
//    @Autowired
//    AddressDTOAssembler addressDTOAssembler;
//
//    public AddressIdInput toInput(Address address) {
//        AddressI addressDTO = new AddressDTO();
//        addressDTO.setId(address.getId());
//        addressDTO.setStreet(address.getStreet());
//        addressDTO.setNumber(address.getNumber());
//        addressDTO.setDistrict(address.getDistrict());
//        addressDTO.setCity(address.getCity());
//        addressDTO.setZipCode(address.getZipCode());
//        addressDTO.setState(address.getState());
//
//        return customerInput;
//    }
//
//    public List<AddressDTO> toCollectionDTO(List<Address> addresss) {
//        return addresss.stream().map(address -> toDTO(address)).collect(Collectors.toList());
//    }
//}

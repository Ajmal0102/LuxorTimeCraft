package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.User;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    void addNewAddress(Address newAddress, User user);

    List<Address> findAllUserAddresses(User user);

    Optional<Address> findAddressById(Long addressId);

    void updateAddress(Address address);

    void removeAddress(Long addressId);

    void setDefaultAddress(Long addressId, User user);

}

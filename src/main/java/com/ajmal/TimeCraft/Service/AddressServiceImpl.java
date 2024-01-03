package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AddressRepository addressRepository;

    @Override
    public void addNewAddress(Address newAddress, User user) {

        Address address = new Address();
        address.setHouseName(newAddress.getHouseName());
        address.setCity(newAddress.getCity());
        address.setState(newAddress.getState());
        address.setPincode(newAddress.getPincode());
        address.setLandmark(newAddress.getLandmark());
        address.setEnabled(false);
        address.setUser(user);
        addressRepository.save(address);
    }

    @Override
    public List<Address> findAllUserAddresses(User user) {
        return addressRepository.findAllByUser(user);
    }

    @Override
    public Optional<Address> findAddressById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public void updateAddress(Address address) {
        Optional<Address> getAddress= addressRepository.findById(address.getId());
        if(getAddress.isPresent()){
            Address addressList =getAddress.get();

            addressList.setId(address.getId());
            addressList.setHouseName(address.getHouseName());
            addressList.setCity(address.getCity());
            addressList.setState(address.getState());
            addressList.setPincode(address.getPincode());
            addressList.setLandmark(address.getLandmark());
            addressList.setEnabled(true);
            addressRepository.save(addressList);
        }
    }

    @Override
    public void removeAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public void setDefaultAddress(Long addressId, User user) {
        Optional<Address> selectedAddressOptional = addressRepository.findById(addressId);

        if (selectedAddressOptional.isPresent()) {
            Address selectedAddress = selectedAddressOptional.get();

            List<Address> userAddresses = user.getAddresses();


            // Find the current default address (if any)
            Address currentDefaultAddress = userAddresses.stream()
                    .filter(address -> address.isEnabled())
                    .findFirst()
                    .orElse(null);

            if (currentDefaultAddress != null) {
                // Unset the current default address
                currentDefaultAddress.setEnabled(false);
                addressRepository.save(currentDefaultAddress);
            }

            // Set the selected address as the new default
            selectedAddress.setEnabled(true);
            addressRepository.save(selectedAddress);
        }
    }


}

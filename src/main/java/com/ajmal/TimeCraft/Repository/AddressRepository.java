package com.ajmal.TimeCraft.Repository;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByUser(User user);

}

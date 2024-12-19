package com.trustbridge.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trustbridge.orders.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}


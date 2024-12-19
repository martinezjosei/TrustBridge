package com.trustbridge.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trustbridge.orders.entities.CustomerOrder;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
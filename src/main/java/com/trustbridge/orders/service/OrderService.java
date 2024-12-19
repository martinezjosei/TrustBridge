package com.trustbridge.orders.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.trustbridge.orders.entities.CustomerOrder;

public interface OrderService {

	void updateOrder(CustomerOrder customerOrder);

	void createOrder(CustomerOrder newOrder);

	void cancelOrder(Long customerOrderId);

	Optional<CustomerOrder> getOrderById(Long id);

	PagedModel<CustomerOrder> getAllOrders(Pageable pageable);

}

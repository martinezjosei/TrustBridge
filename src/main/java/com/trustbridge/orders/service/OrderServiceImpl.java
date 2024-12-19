package com.trustbridge.orders.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.trustbridge.orders.entities.CustomerOrder;
import com.trustbridge.orders.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{

	private final OrderRepository orderRepository;
    
    private final PagedResourcesAssembler<CustomerOrder> pagedResourcesAssembler;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PagedResourcesAssembler<CustomerOrder> pagedResourcesAssembler) {
        this.orderRepository = orderRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }
    
    // Should decouple DTO/Entity and Business Objects in here but this is a quick project. 
    @Override
    public PagedModel<CustomerOrder> getAllOrders(Pageable pageable) {
        Page<CustomerOrder> ordersPage = orderRepository.findAll(pageable);
        
        PagedModel<EntityModel<CustomerOrder>> entityModels = pagedResourcesAssembler.toModel(ordersPage);
        
        // Map EntityModel<CustomerOrder> to CustomerOrder
        List<CustomerOrder> customerOrders = entityModels.getContent().stream()
                .map(EntityModel::getContent)
                .collect(Collectors.toList());
        
        // Create a new PagedModel<CustomerOrder>
        return PagedModel.of(customerOrders, entityModels.getMetadata());

    }

    @Override
    public Optional<CustomerOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public void updateOrder(CustomerOrder customerOrder) {
        orderRepository.save(customerOrder);
    }

    @Override
    public void createOrder(CustomerOrder newOrder) {
        orderRepository.save(newOrder);  // Save the new order to the database
    }
    
    @Override
    public void cancelOrder(Long customerOrderId) {
        CustomerOrder order = orderRepository.findById(customerOrderId)
              .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setIsCancelled(true);
        orderRepository.save(order);
    }
    
    
    
}
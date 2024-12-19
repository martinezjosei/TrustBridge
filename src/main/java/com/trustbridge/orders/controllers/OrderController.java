package com.trustbridge.orders.controllers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel; 

import com.trustbridge.orders.entities.Customer;
import com.trustbridge.orders.entities.CustomerOrder;
import com.trustbridge.orders.model.NewCustomerOrder;
import com.trustbridge.orders.service.CustomerServiceImpl;
import com.trustbridge.orders.service.OrderServiceImpl;
import com.trustbridge.orders.utils.OrderLoader;
import com.trustbridge.orders.utils.PopulateSingleton;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderServiceImpl orderService;

	private final CustomerServiceImpl customerService;

	@Autowired
	public OrderController(OrderServiceImpl orderService, CustomerServiceImpl customerService) {
		this.orderService = orderService;
		this.customerService = customerService;
	}

	
	@GetMapping("/all")
	public PagedModel<CustomerOrder> getAllCustomersOrders(@PageableDefault(size = 10) Pageable pageable,PagedResourcesAssembler<CustomerOrder> pagedResourcesAssembler) {
		return orderService.getAllOrders(pageable);
	}
	
	
	@GetMapping("/{id}")
	public Optional<CustomerOrder> getOrderById(@PathVariable Long id) {
		return orderService.getOrderById(id);
	}

	@PostMapping("/create")
	public ResponseEntity<String> createOrder(@RequestBody NewCustomerOrder newCustomerOrder) {

		if (newCustomerOrder.getCustomerName() == null || newCustomerOrder.getCustomerName().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer name must be provided.");
		}

		Long newCustomerId = createNewCustomer(newCustomerOrder.getCustomerName());

		// Now you have a new customer, pull it from the database and use it.
		Optional<Customer> existingCustomer = customerService.getCustomerById(newCustomerId);
		if (existingCustomer.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
		}

		CustomerOrder newOrder = new CustomerOrder();
		newOrder.setCustomer(existingCustomer.get()); // Set the existing customer
		newOrder.setDishName(newCustomerOrder.getDishName());
		newOrder.setQuantity(newCustomerOrder.getQuantity());
		newOrder.setIsCancelled(false); // optional field

		orderService.createOrder(newOrder);
		return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
	}

	private Long createNewCustomer(String customerName) {
		Customer newCustomer = new Customer();
		newCustomer.setName(customerName);
		return customerService.createCustomer(newCustomer).getId();
	}


	// This should be in the Service tier 
	@PatchMapping("/{id}")
	public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody CustomerOrder updatedCustomerOrder) {
		Optional<CustomerOrder> existingOrder = orderService.getOrderById(id);

		if (existingOrder.isPresent()) {
			CustomerOrder exsistingOrder = existingOrder.get();
			if (updatedCustomerOrder.getDishName() != null) {
				exsistingOrder.setDishName(updatedCustomerOrder.getDishName());
			}
			if (updatedCustomerOrder.getQuantity() != null) {
				exsistingOrder.setQuantity(updatedCustomerOrder.getQuantity());
			}
			if (updatedCustomerOrder.isCancelled() != null) {
				exsistingOrder.setIsCancelled(updatedCustomerOrder.isCancelled());
			}

			// Update the order now
			orderService.updateOrder(exsistingOrder);
		} else {
			return ResponseEntity.ok("Order was not found");
		}

		return ResponseEntity.ok("Order updated successfully");
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
		orderService.cancelOrder(id);
		return ResponseEntity.ok("Order cancelled successfully");
	}


    @PatchMapping("/customer/{customerId}")
    public ResponseEntity<String> createOrderForCustomer(
            @PathVariable Long customerId, 
            @RequestBody CustomerOrder newOrderToAdd) {
        customerService.addOrderToCustomer(customerId, newOrderToAdd);
        return ResponseEntity.ok("A new Order was added to existing customer successfully.");
    }
	
	// This will populate the database with dummy data
	@GetMapping("/one-time-batch-load")
	public ResponseEntity<String> populateOrders() {

		PopulateSingleton populateSingleton = PopulateSingleton.getInstance();
		if (populateSingleton.isDuplicateFlag()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Duplicate request detected. This action has already been performed.");
		}

		populateSingleton.setDuplicateFlag(true);

		List<NewCustomerOrder> batchOrdersList = null;
		try {
			batchOrdersList = new OrderLoader().loadBatchLoader();
		} catch (FileNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batch file not found.");
		}

		// Now we have a list of NewCustomerOrders so lets iterate and create the
		// customer object first

		for (NewCustomerOrder newBatchOrder : batchOrdersList) {
			Optional<Customer> customerOptional = createNewBatchCustomer(newBatchOrder.getCustomerName());

			if (customerOptional.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
			}

			Long newCustomerId = customerOptional.get().getId();

			// Now you have a new customer, pull it from the database and use it.
			Optional<Customer> existingCustomer = customerService.getCustomerById(newCustomerId);
			if (existingCustomer.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
			}

			Customer newBatchCustomer = existingCustomer.get();

			CustomerOrder newCustomerOrder = new CustomerOrder();
			newCustomerOrder.setCustomer(newBatchCustomer); // Set the existing customer
			newCustomerOrder.setDishName(newBatchOrder.getDishName());
			newCustomerOrder.setQuantity(newBatchOrder.getQuantity());
			newCustomerOrder.setIsCancelled(false); // optional field

			orderService.createOrder(newCustomerOrder);
			System.out.println("Single new batch order created successfully");
		} // for-loop

		return ResponseEntity.status(HttpStatus.CREATED).body("All batch orders created successfully");
	}

	
	private Optional<Customer> createNewBatchCustomer(String custmerName) {
		Long newCustomerId = createNewCustomer(custmerName);

		// Now you have a new customer, pull it from the database and use it.
		Optional<Customer> existingCustomer = customerService.getCustomerById(newCustomerId);

		return existingCustomer;
	}


}

package com.trustbridge.orders.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.trustbridge.orders.model.NewCustomerOrder;

public class OrderLoader {
    public List<NewCustomerOrder> loadOrdersFromFile(String filePath) {
        List<NewCustomerOrder> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        	br.readLine(); // Ignore the label columns
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    String customerName = fields[0].trim();
                    String dishName = fields[1].trim();
                    int quantity = Integer.parseInt(fields[2].trim());
                    orders.add(new NewCustomerOrder(customerName, dishName, quantity));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return orders;
    }


	public List<NewCustomerOrder> loadBatchLoader() throws FileNotFoundException {
		// Load the file from the classpath
		File file = ResourceUtils.getFile("classpath:static/ordersBatch.csv");
		String filePath = file.getAbsolutePath();
		//OrderLoader orderLoader = new OrderLoader();
		List<NewCustomerOrder> orders = loadOrdersFromFile(filePath);

		// Print loaded orders for debugging purposes only
		for (NewCustomerOrder order : orders) {
			System.out.println(order);
		}

		return orders;
	}
	
    // Remove in production 
    public static void main(String[] args) throws FileNotFoundException {
        // Load the file from the classpath
        File file = ResourceUtils.getFile("classpath:static/ordersBatch.csv");
        String filePath = file.getAbsolutePath();
        OrderLoader orderLoader = new OrderLoader();
        List<NewCustomerOrder> orders = orderLoader.loadOrdersFromFile(filePath);
    	
        // Print loaded orders for debugging purposes only
        for (NewCustomerOrder order : orders) {
            System.out.println(order);
        }
    }
}

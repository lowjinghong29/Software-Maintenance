package oop_assignment.service.impl;

import oop_assignment.model.Cart;
import oop_assignment.model.CartItem;
import oop_assignment.model.Customer;
import oop_assignment.model.PricingSummary;
import oop_assignment.model.Sales;
import oop_assignment.repository.SalesRepository;
import oop_assignment.service.SalesService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of SalesService.
 */
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    public SalesServiceImpl(SalesRepository salesRepository) {
        if (salesRepository == null) {
            throw new IllegalArgumentException("SalesRepository cannot be null");
        }
        this.salesRepository = salesRepository;
    }

    @Override
    public void recordSale(Cart cart, PricingSummary pricingSummary, Customer customer) {
        List<Sales> allSales = salesRepository.findAll();
        String customerId = (customer != null) ? customer.getEmail() : null;
        String dateTime = LocalDateTime.now().toString();
        for (CartItem item : cart.getItems()) {
            Sales sale = new Sales(
                    null, // saleId can be null or generated
                    item.getItem().getName(), // productId as name
                    item.getItem().getName(),
                    item.getQuantity(),
                    item.getLineTotal(),
                    dateTime,
                    customerId
            );
            allSales.add(sale);
        }
        salesRepository.saveAll(allSales);
    }

    @Override
    public double getTotalRevenue() {
        List<Sales> allSales = salesRepository.findAll();
        return allSales.stream().mapToDouble(Sales::getLineTotal).sum();
    }

    @Override
    public Map<String, Integer> getTotalQuantityByProductId() {
        List<Sales> allSales = salesRepository.findAll();
        Map<String, Integer> map = new HashMap<>();
        for (Sales sale : allSales) {
            map.put(sale.getProductId(), map.getOrDefault(sale.getProductId(), 0) + sale.getQuantity());
        }
        return map;
    }
}

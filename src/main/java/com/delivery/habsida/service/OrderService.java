package com.delivery.habsida.service;

import com.delivery.habsida.dto.*;
import com.delivery.habsida.entity.*;
import com.delivery.habsida.exception.*;
import com.delivery.habsida.repository.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.delivery.habsida.entity.OrderStatus.CREATED;

@Service
public class OrderService {
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;


    public OrderService(OrderRepository orderRepository, StoreRepository storeRepository, CustomerRepository customerRepository, CustomerAddressRepository customerAddressRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderDTO createOrder(Long storeId, OrderCreateRequest orderCreateRequest) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("Store not found"));
        Customer customer = customerRepository.findById(orderCreateRequest.customerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CustomerAddress customerAddress = customerAddressRepository.findById(orderCreateRequest.customerAddressId())
                .orElseThrow(() -> new CustomerAddressNotFoundException("Customer address not found"));
        if (!customerAddress.getCustomer().getId().equals(customer.getId())) {
            throw new CustomerAddressNotFoundException("Customer address not found");
        }
        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal orderSubtotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : orderCreateRequest.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            if (!product.getStore().getId().equals(storeId)) {
                throw new ProductNotFoundException("Product not found");
            }
            if (product.getStatus() != ProductStatus.AVAILABLE) {
                throw new ProductNotAvailableException("Product not available");
            }
            if (itemRequest.quantity() < product.getMinQuantity() || itemRequest.quantity() > product.getMaxQuantity()) {
                throw new InvalidQuantityException("Quantity must be between: " + product.getMinQuantity() + " and " + product.getMaxQuantity());
            }

            List<Integer> stockResult = productRepository.decreaseStockAndGet(product.getId(), itemRequest.quantity());
            if (stockResult.isEmpty()) {
               throw new InsufficientStockException("Insufficient stock");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(itemRequest.quantity());
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            orderItem.setSubtotal(subtotal);
            orderItems.add(orderItem);
            orderSubtotal = orderSubtotal.add(subtotal);
        }
        BigDecimal deliveryFee = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;
        BigDecimal total = orderSubtotal.add(deliveryFee).subtract(discountTotal);

        Order order = new Order();
        order.setStore(store);
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);
        order.setDeliveryCity(customerAddress.getCity());
        order.setDeliveryStreet(customerAddress.getStreet());
        order.setDeliveryHouse(customerAddress.getHouse());
        order.setDeliveryApartment(customerAddress.getApartment());
        order.setOrderType(orderCreateRequest.type());
        order.setCustomerNote(orderCreateRequest.customerNote());
        order.setOrderStatus(CREATED);
        order.setSubTotal(orderSubtotal);
        order.setDeliveryFee(deliveryFee);
        order.setDiscountTotal(discountTotal);
        order.setTotal(total);
        Long orderNumber = storeRepository.incrementAndGetOrderNumber(storeId).get(0);
        order.setOrderNumber(String.valueOf(orderNumber));
        orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        List<OrderItemDto> itemDtos = orderItems.stream().map(OrderItemDto::from).toList();
        return OrderDTO.from(order, itemDtos);
    }
}

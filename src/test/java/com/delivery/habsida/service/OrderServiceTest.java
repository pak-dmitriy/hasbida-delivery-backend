package com.delivery.habsida.service;

import com.delivery.habsida.dto.OrderCreateRequest;
import com.delivery.habsida.dto.OrderDTO;
import com.delivery.habsida.dto.OrderItemRequest;
import com.delivery.habsida.entity.*;
import com.delivery.habsida.exception.CustomerAddressNotFoundException;
import com.delivery.habsida.exception.CustomerNotFoundException;
import com.delivery.habsida.exception.ProductNotFoundException;
import com.delivery.habsida.exception.StoreNotFoundException;
import com.delivery.habsida.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.delivery.habsida.entity.OrderType.DELIVERY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerAddressRepository customerAddressRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private Store store;
    private Customer customer;
    private CustomerAddress customerAddress;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        order = new Order();
        store = new Store();
        customer = new Customer();
        customerAddress = new CustomerAddress();
        product = new Product();
        orderItem = new OrderItem();

        store.setId(1L);
        customer.setId(1L);
        customerAddress.setId(1L);
        product.setId(1L);
        product.setName("apple");
        product.setPrice(new BigDecimal("10.00"));

    }

    @Test
    void createOrder() {
        when(orderRepository.countByStoreId(1L)).thenReturn(0L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );

        OrderDTO result = orderService.createOrder(1L, request);
        assertEquals(new BigDecimal("20.00"), result.total());
        assertEquals(1, result.orderItems().size());
        assertEquals("apple", result.orderItems().get(0).productName());
    }

    @Test
    void createOrder_shouldThrowException_whenStoreNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );

        assertThrows(StoreNotFoundException.class,
                () -> orderService.createOrder(1L, request));
    }

    @Test
    void createOrder_shouldThrowException_whenCustomerNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );

        assertThrows(CustomerNotFoundException.class,
                () -> orderService.createOrder(1L, request));
    }

    @Test
    void createOrder_shouldThrowException_whenCustomerAddressNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());
        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );
        assertThrows(CustomerAddressNotFoundException.class,
                () -> orderService.createOrder(1L, request));
    }

    @Test
    void createOrder_shouldThrowException_whenProductNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );
        assertThrows(ProductNotFoundException.class,
                () -> orderService.createOrder(1L, request));
    }

    @Test
    void createOrder_shouldGenerateCorrectOrderNumber_whenStoreHasExistingOrders() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.countByStoreId(1L)).thenReturn(5L);
        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );
        OrderDTO result = orderService.createOrder(1L, request);
        assertEquals("6", result.orderNumber());

    }
}
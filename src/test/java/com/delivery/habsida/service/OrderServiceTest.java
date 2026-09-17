package com.delivery.habsida.service;

import com.delivery.habsida.dto.OrderCreateRequest;
import com.delivery.habsida.dto.OrderDTO;
import com.delivery.habsida.dto.OrderItemRequest;
import com.delivery.habsida.entity.*;
import com.delivery.habsida.exception.*;
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


    private Store store;
    private Customer customer;
    private CustomerAddress customerAddress;
    private Product product;


    @BeforeEach
    void setUp() {

        store = new Store();
        customer = new Customer();
        customerAddress = new CustomerAddress();
        product = new Product();


        store.setId(1L);
        customer.setId(1L);
        customerAddress.setId(1L);
        product.setId(1L);
        product.setName("apple");
        product.setPrice(new BigDecimal("10.00"));
        product.setStore(store);
        product.setStatus(ProductStatus.AVAILABLE);
        product.setMaxQuantity(10);
        product.setMinQuantity(1);
        product.setStock(100);
        customerAddress.setCustomer(customer);

    }

    private OrderCreateRequest defaultRequest() {
        return new OrderCreateRequest(
                DELIVERY,
                "Good",
                1L,
                1L,
                List.of(new OrderItemRequest(1L, 2))
        );
    }

    @Test
    void createOrder() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of(98));
        when(storeRepository.incrementAndGetOrderNumber(1L)).thenReturn(List.of(1L));

        OrderDTO result = orderService.createOrder(1L, defaultRequest());

        assertEquals(new BigDecimal("20.00"), result.total());
        assertEquals(1, result.orderItems().size());
        assertEquals("apple", result.orderItems().get(0).productName());
        assertEquals("1", result.orderNumber());

    }

    @Test
    void createOrder_shouldThrowException_whenStoreNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldThrowException_whenCustomerNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldThrowException_whenCustomerAddressNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerAddressNotFoundException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldThrowException_whenProductNotFound() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldGenerateCorrectOrderNumber_whenStoreHasExistingOrders() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of(98));
        when(storeRepository.incrementAndGetOrderNumber(1L)).thenReturn(List.of(6L));

        OrderDTO result = orderService.createOrder(1L, defaultRequest());
        assertEquals("6", result.orderNumber());

    }

    @Test
    void createOrder_shouldThrowException_whenAddressBelongsToDifferentCustomer() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(2L);
        customerAddress.setCustomer(anotherCustomer);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));

        assertThrows(CustomerAddressNotFoundException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldThrowException_whenProductFromDifferentStore() {
        Store anotherStore = new Store();
        anotherStore.setId(2L);
        product.setStore(anotherStore);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ProductNotFoundException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldThrowException_whenProductNotAvailable() {
        product.setStatus(ProductStatus.PAUSED);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ProductNotAvailableException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }

    @Test
    void createOrder_shouldThrowException_whenQuantityOutOfRange() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderCreateRequest request = new OrderCreateRequest(
                DELIVERY, "Good", 1L, 1L,
                List.of(new OrderItemRequest(1L, 99))
        );

        assertThrows(InvalidQuantityException.class,
                () -> orderService.createOrder(1L, request));
    }

    @Test
    void createOrder_shouldThrowException_whenInsufficientStock() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of());

        assertThrows(InsufficientStockException.class,
                () -> orderService.createOrder(1L, defaultRequest()));
    }}
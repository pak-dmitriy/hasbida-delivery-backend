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
import org.springframework.orm.ObjectOptimisticLockingFailureException;

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
    private Order order;


    @BeforeEach
    void setUp() {

        store = new Store();
        customer = new Customer();
        customerAddress = new CustomerAddress();
        product = new Product();
        order = new Order();


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
        order.setId(1L);
        order.setStore(store);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);
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

        assertEquals(new BigDecimal("25.00"), result.total());
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
    }

    @Test
    void acceptOrder_shouldSucceed_whenTransitionIsValid() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of());
        OrderDTO result = orderService.acceptOrder(1L, order.getId());
        assertEquals(OrderStatus.ACCEPTED, result.orderStatus());
    }

    @Test
    void acceptOrder_shouldThrowException_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.acceptOrder(1L, 1L));
    }

    @Test
    void acceptOrder_shouldThrowException_whenOrderBelongsToDifferentStore() {
        Store anotherStore = new Store();
        anotherStore.setId(2L);
        order.setStore(anotherStore);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(OrderNotFoundException.class,
                () -> orderService.acceptOrder(1L, order.getId()));
    }

    @Test
    void acceptOrder_shouldThrowException_whenTransitionIsInvalid() {
        order.setOrderStatus(OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> orderService.acceptOrder(1L, order.getId()));
    }

    @Test
    void rejectOrder_shouldSucceed_whenTransitionIsValid() {
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of());

        OrderDTO result = orderService.rejectOrder(1L, order.getId(), "Out of stock");

        assertEquals(OrderStatus.REJECTED, result.orderStatus());
    }

    @Test
    void rejectOrder_shouldThrowException_whenTransitionIsInvalid() {
        order.setOrderStatus(OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> orderService.rejectOrder(1L, order.getId(), "Out of stock"));
    }

    @Test
    void cancelOrder_shouldSucceed_whenTransitionIsValid() {
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of());

        OrderDTO result = orderService.cancelOrder(1L, order.getId());

        assertEquals(OrderStatus.CANCELLED, result.orderStatus());
    }

    @Test
    void cancelOrder_shouldThrowException_whenTransitionIsInvalid() {
        order.setOrderStatus(OrderStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> orderService.cancelOrder(1L, order.getId()));
    }

    @Test
    void startOrder_shouldSucceed_whenTransitionIsValid() {
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of());

        OrderDTO result = orderService.startOrder(1L, order.getId());

        assertEquals(OrderStatus.IN_PROGRESS, result.orderStatus());
    }

    @Test
    void startOrder_shouldThrowException_whenTransitionIsInvalid() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> orderService.startOrder(1L, order.getId()));
    }

    @Test
    void completeOrder_shouldSucceed_whenTransitionIsValid() {
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of());

        OrderDTO result = orderService.completeOrder(1L, order.getId());

        assertEquals(OrderStatus.COMPLETED, result.orderStatus());
    }

    @Test
    void completeOrder_shouldThrowException_whenTransitionIsInvalid() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusTransitionException.class,
                () -> orderService.completeOrder(1L, order.getId()));
    }

    @Test
    void getNewOrders_shouldReturnOrderList() {
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findByStoreIdAndOrderStatus(1L, OrderStatus.CREATED))
                .thenReturn(List.of(order));
        when(orderItemRepository.findByOrderIdIn(List.of(order.getId())))
                .thenReturn(List.of());

        List<OrderDTO> result = orderService.getNewOrders(1L);

        assertEquals(1, result.size());
        assertEquals(OrderStatus.CREATED, result.get(0).orderStatus());
    }

    @Test
    void cancelOrder_shouldSucceed_whenOrderIsAccepted() {
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(order.getId())).thenReturn(List.of());

        OrderDTO result = orderService.cancelOrder(1L, order.getId());

        assertEquals(OrderStatus.CANCELLED, result.orderStatus());
    }

    @Test
    void acceptOrder_shouldPropagateException_whenVersionConflictOccurs() {
        order.setCustomer(customer);
        order.setCustomerAddress(customerAddress);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order))
                .thenThrow(new ObjectOptimisticLockingFailureException(Order.class, order.getId()));

        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> orderService.acceptOrder(1L, order.getId()));
    }

    @Test
    void createOrder_shouldApplyDiscount_whenProductHasDiscount() {
        product.setDiscountPercent(20);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of(98));
        when(storeRepository.incrementAndGetOrderNumber(1L)).thenReturn(List.of(1L));

        OrderDTO result = orderService.createOrder(1L, defaultRequest());

        assertEquals(new BigDecimal("4.00"), result.discountTotal());
        assertEquals(new BigDecimal("21.00"), result.total());
    }

    @Test
    void createOrder_shouldHaveZeroDiscount_whenProductHasNoDiscount() {

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of(98));
        when(storeRepository.incrementAndGetOrderNumber(1L)).thenReturn(List.of(1L));

        OrderDTO result = orderService.createOrder(1L, defaultRequest());

        assertEquals(new BigDecimal("0.00"), result.discountTotal());
        assertEquals(new BigDecimal("25.00"), result.total());
    }

    @Test
    void createOrder_shouldRoundDiscountToTwoDecimals_whenDivisionIsNotExact() {
        product.setPrice(new BigDecimal("10.01"));
        product.setDiscountPercent(33);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of(98));
        when(storeRepository.incrementAndGetOrderNumber(1L)).thenReturn(List.of(1L));

        OrderDTO result = orderService.createOrder(1L, defaultRequest());

        assertEquals(new BigDecimal("6.60"), result.discountTotal());
    }

    @Test
    void createOrder_shouldApplySameTotalDiscount_regardlessOfItemSplitting() {
        product.setPrice(new BigDecimal("0.01"));
        product.setDiscountPercent(50);

        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerAddressRepository.findById(1L)).thenReturn(Optional.of(customerAddress));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(storeRepository.incrementAndGetOrderNumber(1L)).thenReturn(List.of(1L));

        when(productRepository.decreaseStockAndGet(1L, 2)).thenReturn(List.of(98));
        OrderCreateRequest oneLineRequest = new OrderCreateRequest(
                DELIVERY, "Good", 1L, 1L,
                List.of(new OrderItemRequest(1L, 2))
        );
        OrderDTO oneLineResult = orderService.createOrder(1L, oneLineRequest);

        when(productRepository.decreaseStockAndGet(1L, 1)).thenReturn(List.of(99));
        OrderCreateRequest twoLinesRequest = new OrderCreateRequest(
                DELIVERY, "Good", 1L, 1L,
                List.of(
                        new OrderItemRequest(1L, 1),
                        new OrderItemRequest(1L, 1)
                )
        );
        OrderDTO twoLinesResult = orderService.createOrder(1L, twoLinesRequest);

        assertEquals(new BigDecimal("0.02"), oneLineResult.discountTotal());
        assertEquals(oneLineResult.discountTotal(), twoLinesResult.discountTotal());
    }
}
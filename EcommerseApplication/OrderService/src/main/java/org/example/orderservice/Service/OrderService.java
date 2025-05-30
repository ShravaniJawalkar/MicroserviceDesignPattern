package org.example.orderservice.Service;

import jakarta.validation.constraints.NotNull;
import org.example.orderservice.Dao.*;
import org.example.orderservice.Repository.OrderItemsRepository;
import org.example.orderservice.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${product.service.url}")
    private String productServiceUrl;

    @Transactional
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        ResponseEntity<OrderResponse> response =  validateRequest(orderRequest);
        if(response.getStatusCode() != HttpStatus.OK) {
            return response;
        }
        // Create and save the order
        Orders order = Orders.builder()
                .userId(orderRequest.getUserId())
                .totalAmount(orderRequest.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity())))
                .status(OrderStatus.PENDING)
                .build();
        order = orderRepository.save(order);

        OrderItems orderItems = OrderItems.builder()
                .price(orderRequest.getPrice())
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .totalPrice(orderRequest.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity())))
                .build();

        order.addItem(orderItems);
        orderItems.setOrders(order);
        orderItemsRepository.save(orderItems);
        if (order.getId() == null) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    private ResponseEntity<OrderResponse> validateRequest(OrderRequest orderRequest) {
        // Validate if user exists in UserService
        boolean userExists = validateUser(orderRequest.getUserId());
        if (!userExists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean productAvailable = existsByProductId(orderRequest.getProductId());
        if (!productAvailable) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Get product details from ProductService

        ProductResponse productResponse = getProductDetails(orderRequest.getProductId());
        if(productResponse==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Validate product price and quantity
        if (orderRequest.getPrice().compareTo(BigDecimal.valueOf(productResponse.getPrice())) != 0) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if( orderRequest.getQuantity() <= 0 || orderRequest.getQuantity().compareTo(productResponse.getQuantity()) != 0) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean existsByProductId(@NotNull Long productId) {
        try {
            String url = productServiceUrl + "/api/products/" + productId;
            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }

    }

    private ProductResponse getProductDetails(@NotNull Long productId) {
        try {
            String url = productServiceUrl + "/api/products/" + productId;
            ResponseEntity<ProductResponse> response = restTemplate.getForEntity(url, ProductResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }

    }

    private boolean validateUser(Long userId) {
        try {
            // Example REST call to UserService
            String url = userServiceUrl + "/api/users/" + userId;
            ResponseEntity<UserServiceResponse> response = restTemplate.getForEntity(url, UserServiceResponse.class);
            return response.getStatusCode() == HttpStatus.OK; // User exists if 200 OK is returned.
        } catch (HttpClientErrorException.NotFound e) {
            return false; // User does not exist.
        }
    }

    public ResponseEntity<OrderDetails> getOrderById(Long orderId) {
        Optional<Orders> order = orderRepository.findById(orderId);
        OrderDetails orderResponse = new OrderDetails();
        if (order.isEmpty()) {
            return new ResponseEntity<>(orderResponse, HttpStatus.NOT_FOUND);
        } else {
            order.ifPresent(orders -> {
                orderResponse.setOrderId(orders.getId());
                orderResponse.setUserName(getUserName(orders.getUserId()));
                orderResponse.setTotalAmount(orders.getTotalAmount());
                orderResponse.setStatus(String.valueOf(orders.getStatus()));
                orderResponse.setOrderDetails(orders.getItems().stream()
                        .map(item -> OrderItemsDetails.builder()
                                .itemId(item.getId())
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .toList());
            });

        }
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    private String getUserName(Long userId) {
        String url = userServiceUrl + "/api/users/" + userId;
        ResponseEntity<UserServiceResponse> response = restTemplate.getForEntity(url, UserServiceResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            UserServiceResponse userServiceResponse = response.getBody();
            if (userServiceResponse != null) {
                return userServiceResponse.getUserName();
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with ID " + userId + " does not exist");
            }
        }
        return response.getStatusCode().toString();
    }

    @Transactional
    public String updateOrderStatus(long orderId, String status) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Order with ID " + orderId + " does not exist"));

        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return String.format("Order with ID %d has been updated to status %s", orderId, status);
    }

    public String deleteOrder(long orderId) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        if (orders.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order with ID " + orderId + " does not exist");
        } else {
            orderRepository.deleteById(orderId);
            return String.format("Order with ID %d has been deleted", orderId);
        }
    }

    public ResponseEntity<OrderDetails> getOrderByUserId(long userId) {
        OrderDetails orderResponse = new OrderDetails();
        orderRepository.findByUserId(userId).ifPresentOrElse(order -> {
            Orders typeOrder = (Orders) order;
            orderResponse.setOrderId(typeOrder.getId());
            orderResponse.setUserName(getUserName(typeOrder.getUserId()));
            orderResponse.setTotalAmount(typeOrder.getTotalAmount());
            orderResponse.setStatus(String.valueOf(typeOrder.getStatus()));
            orderResponse.setOrderDetails(typeOrder.getItems().stream()
                    .map(item -> OrderItemsDetails.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .totalPrice(item.getTotalPrice())
                            .build())
                    .toList());
        }, () -> {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order with User ID " + userId + " does not exist");
        });
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }
}

package org.example.orderservice.Controller;

import org.example.orderservice.Dao.OrderDetails;
import org.example.orderservice.Dao.OrderRequest;
import org.example.orderservice.Dao.OrderResponse;
import org.example.orderservice.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
@Autowired
    private OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orders) {
              OrderResponse orderResponse= orderService.createOrder(orders);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable long orderId, @RequestBody String status) {
        String responseStatus = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(responseStatus, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetails> getOrder(@PathVariable long orderId) {
       OrderDetails response = orderService.getOrderById(orderId);
       return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<OrderDetails> getUserOrder(@PathVariable long userId) {
        OrderDetails response = orderService.getOrderByUserId(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable long orderId) {
        String response = orderService.deleteOrder(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.luisalvarez.orderservice.controller;

import com.luisalvarez.orderservice.dto.OrderRequest;
import com.luisalvarez.orderservice.dto.OrderResponse;
import com.luisalvarez.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@Valid @RequestBody OrderRequest orderRequest){
        return orderService.placeOrder(orderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
    }
}

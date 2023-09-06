package com.web_food.library.service.impl;

import com.web_food.library.model.*;
import com.web_food.library.repository.OrderDetailRepository;
import com.web_food.library.repository.OrderRepository;
import com.web_food.library.service.DicountService;
import com.web_food.library.service.OrderService;
import com.web_food.library.service.ShoppingCartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private DicountService dicountService;
    @Autowired
    private OrderDetailRepository detailRepository;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order save(ShoppingCart shoppingCart, String method, String discount) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus("Pending");
        order.setAccept(false);
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(2);
        order.setQuantity(shoppingCart.getTotalItems());
        order.setTotalPrices(shoppingCart.getTotalPrice());
        order.setPaymentMethod(method);
        Discount discount1 = new Discount();
        if(!discount.equals("")){
            discount1 = dicountService.findByCode(discount);
            dicountService.delete(discount1);
            order.setDiscount(discount1);
        }
        order.setDiscount(null);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(CartItem item : shoppingCart.getCartItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            detailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetails(orderDetailList);
        shoppingCartService.deleteAllCart(shoppingCart.getId());

        return orderRepository.save(order);

    }

    @Override
    public void CancelById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order AcceptById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        order.get().setOrderStatus("Success");
        order.get().setAccept(true);
        order.get().setDeliveryDate(new Date());
        return orderRepository.save(order.get());
    }

    @Override
    public Order FailById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        order.get().setOrderStatus("Fail");
        order.get().setAccept(true);
        order.get().setDeliveryDate(new Date());

        return orderRepository.save(order.get());
    }
}

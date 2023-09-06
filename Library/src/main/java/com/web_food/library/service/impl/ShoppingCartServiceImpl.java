package com.web_food.library.service.impl;

import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.CartItem;
import com.web_food.library.model.Customer;
import com.web_food.library.model.Product;
import com.web_food.library.model.ShoppingCart;
import com.web_food.library.repository.CartItemRepository;
import com.web_food.library.repository.ShoppingCartRepository;
import com.web_food.library.service.CartItemService;
import com.web_food.library.service.CustomerService;

import com.web_food.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public ShoppingCart addItemtoCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        if(shoppingCart == null){
            shoppingCart = new ShoppingCart();
        }
        Set<CartItem> cartItemSet = shoppingCart.getCartItems();
        CartItem cartItem = find(cartItemSet, productDto.getId());
        Product product = transfer(productDto);
        int itemQuantity = 0;

        if(cartItemSet == null){
            cartItemSet = new HashSet<>();
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setQuantity(quantity);
                cartItem.setCart(shoppingCart);
                cartItem.setProduct(product);
                cartItem.setUnitPrice(product.getCost());

                cartItemSet.add(cartItem);
                cartItemRepository.save(cartItem);
            }else{
                itemQuantity = cartItem.getQuantity() + quantity;
                if(itemQuantity <= productDto.getCurrentQuantity())
                     cartItem.setQuantity(itemQuantity);
                cartItemRepository.save(cartItem);
            }

        }else{
            if(cartItem == null){
                cartItem = new CartItem();
                cartItem.setQuantity(quantity);
                cartItem.setCart(shoppingCart);
                cartItem.setProduct(product);
                cartItem.setUnitPrice(product.getCost());

                cartItemSet.add(cartItem);
                cartItemRepository.save(cartItem);
            }else{
                itemQuantity = cartItem.getQuantity() + quantity;
                if(itemQuantity <= productDto.getCurrentQuantity())
                    cartItem.setQuantity(itemQuantity);
                cartItemRepository.save(cartItem);
            }
        }

        shoppingCart.setCustomer(customer);
        shoppingCart.setCartItems(cartItemSet);
        shoppingCart.setTotalItems(totalItem(cartItemSet));
        shoppingCart.setTotalPrice(totalPrice(cartItemSet));
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart updateCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        Set<CartItem> cartItemSet = shoppingCart.getCartItems();
        CartItem cartItem = find(cartItemSet,productDto.getId());

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        shoppingCart.setTotalPrice(totalPrice(cartItemSet));
        shoppingCart.setTotalItems(totalItem(cartItemSet));
        shoppingCart.setCartItems(cartItemSet);

        return shoppingCartRepository.save(shoppingCart);

    }

    @Override
    public ShoppingCart deleteCart(ProductDto productDto, int quantity, String username) {
        Customer customer = customerService.findByUsername(username);
        ShoppingCart shoppingCart = customer.getCart();
        Set<CartItem> cartItemSet = shoppingCart.getCartItems();
        CartItem cartItem = find(cartItemSet,productDto.getId());

        cartItemSet.remove(cartItem);
        cartItemRepository.delete(cartItem);

        shoppingCart.setTotalPrice(totalPrice(cartItemSet));
        shoppingCart.setTotalItems(totalItem(cartItemSet));
        shoppingCart.setCartItems(cartItemSet);

        return shoppingCartRepository.save(shoppingCart);

    }

    @Override
    public Optional<ShoppingCart> findById(Long id) {
        return shoppingCartRepository.findById(id);
    }

    @Override
    public ShoppingCart deleteAllCart(Long id) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);

        Set<CartItem> cartItemSet1 = shoppingCart.get().getCartItems();
        Set<CartItem> itemsToDelete = new HashSet<>(cartItemSet1);
        for(CartItem item : itemsToDelete){
            cartItemSet1.remove(item);
            cartItemRepository.delete(item);
            if(cartItemSet1.isEmpty()) break;
        }
        cartItemSet1.clear();

        shoppingCart.get().setTotalPrice(0);
        shoppingCart.get().setTotalItems(0);
        shoppingCart.get().setCustomer(null);
        shoppingCart.get().setCartItems(null);

       return shoppingCartRepository.save(shoppingCart.get());
    }

    public int totalItem(Set<CartItem> cartItemSet){
        int total = 0;
        for(CartItem item : cartItemSet)
            total += item.getQuantity();
        return total;
    }
    private double totalPrice(Set<CartItem> cartItemSet){
        double totalPrice  = 0.0;
        for(CartItem item : cartItemSet)
            totalPrice += item.getUnitPrice()*item.getQuantity();
        return totalPrice;
    }

    private CartItem find(Set<CartItem> cartItemset, Long id){
        CartItem cartItem = null;
        if(cartItemset == null) return null;
        else{
            for(CartItem item : cartItemset){
                if(item.getProduct().getId() == id){
                    cartItem = item;
                }
            }
        }
        return cartItem;
    }
    private Product transfer(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCurrentQuantity(productDto.getCurrentQuantity());
        product.setCost(productDto.getCost());
        product.setSale(productDto.getSale());
        product.setDescription(productDto.getDescription());
        product.setImage(productDto.getImage());
        product.setIs_activated(productDto.getIs_activated());
        product.setIs_deleted(productDto.getIs_deleted());
        product.setCategory(productDto.getCategory());
        return product;
    }
}

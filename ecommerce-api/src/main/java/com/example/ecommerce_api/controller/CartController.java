package com.example.ecommerce_api.controller;

import com.example.ecommerce_api.DTO.CartDTO;
import com.example.ecommerce_api.DTO.CartItemDTO;
import com.example.ecommerce_api.entity.Cart;
import com.example.ecommerce_api.entity.CartItem;
import com.example.ecommerce_api.entity.Product;
import com.example.ecommerce_api.service.CartItemService;
import com.example.ecommerce_api.service.CartService;
import com.example.ecommerce_api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Carts", description = "APIs for managing customer carts and cart items")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    // Constructor
    public CartController(CartItemService cartItemService, CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    // Helper method: map Cart entity → CartDTO
    private CartDTO mapToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getId());
        dto.setCustomerId(cart.getCustomer().getId());
        dto.setCustomerName(cart.getCustomer().getName());

        List<CartItemDTO> items = cart.getItems().stream().map(item -> {
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setPrice(item.getProduct().getPrice());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setTotalPrice(item.getQuantity() * item.getProduct().getPrice());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(items);
        dto.setTotalAmount(items.stream().mapToDouble(CartItemDTO::getTotalPrice).sum());
        return dto;
    }

    @Operation(summary = "Get cart by customer ID")
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCart(@PathVariable Long customerId) {
        Optional<Cart> cart = cartService.getCartByCustomerId(customerId);
        if (cart.isPresent()) {
            CartDTO cartDto = mapToDTO(cart.get());
            return ResponseEntity.ok(cartDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found for customerId: " + customerId);
        }
    }

    @Operation(summary = "Add item to cart")
    @PostMapping("/{customerId}/add/{productId}")
    public ResponseEntity<?> addItem(@PathVariable Long customerId,
                                     @PathVariable Long productId,
                                     @RequestParam(defaultValue = "1") int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than 0");
        }
        try {
            Cart updatedCart = cartService.addItemToCart(customerId, productId, quantity);
            return ResponseEntity.ok(mapToDTO(updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/{customerId}/remove/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable Long customerId,
                                        @PathVariable Long productId) {
        try {
            Cart updatedCart = cartService.removeItemFromCart(customerId, productId);
            return ResponseEntity.ok(mapToDTO(updatedCart));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Clear entire cart")
    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long customerId) {
        boolean cleared = cartService.clearCart(customerId);
        if (cleared) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart not found for customerId: " + customerId);
        }
    }

    @Operation(summary = "Create new cart for a customer")
    @PostMapping("/{customerId}")
    public ResponseEntity<CartDTO> createCart(@PathVariable Long customerId) {
        Cart cart = cartService.createCartForCustomer(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(cart));
    }

    /****** CartItem-related mappings ******/

    @Operation(summary = "Add or update item in cart by cart ID")
    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addItemToCart(@PathVariable Long cartId,
                                           @RequestParam Long productId,
                                           @RequestParam int quantity) {
        Optional<Cart> cartOpt = cartService.findById(cartId);
        Optional<Product> productOpt = productService.findById(productId);

        if (cartOpt.isEmpty() || productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid cart or product ID");
        }

        Cart cart = cartOpt.get();
        Product product = productOpt.get();

        Optional<CartItem> existingItemOpt = cartItemService.findByCartAndProduct(cartId, productId);

        CartItem item;
        if (existingItemOpt.isPresent()) {
            item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
        }

        cartItemService.save(item);

        return ResponseEntity.ok(mapToDTO(cartService.save(cart)));
    }

    @Operation(summary = "Update cart item quantity")
    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartId,
                                            @PathVariable Long itemId,
                                            @RequestParam int quantity) {
        return cartItemService.findById(itemId).map(item -> {
            if (!item.getCart().getId().equals(cartId)) {
                return ResponseEntity.badRequest().body("Item does not belong to this cart");
            }
            item.setQuantity(quantity);
            cartItemService.save(item);
            return ResponseEntity.ok(mapToDTO(item.getCart()));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remove cart item by item ID")
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartId,
                                            @PathVariable Long itemId) {
        return cartItemService.findById(itemId).map(item -> {
            if (!item.getCart().getId().equals(cartId)) {
                return ResponseEntity.badRequest().body("Item does not belong to this cart");
            }
            cartItemService.delete(item);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

package com.icodeap.ecommerce.infrastructure.controller;

import com.icodeap.ecommerce.application.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/user/cart")
@Slf4j
public class CartController {
   private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add-product")
    public String addProductCart(@RequestParam Integer quantity, @RequestParam Integer idProduct, @RequestParam String nameProduct, @RequestParam BigDecimal price){
        cartService.addItemCart(quantity, idProduct, nameProduct, price);
        showCart();


        return "redirect:/home";
    }

    private void showCart() {
        cartService.getItemCarts().forEach(
                itemCart -> log.info("Item cart: {}", itemCart)
        );
    }

    @GetMapping("/get-cart")
    public String getCart(Model model, HttpSession httpSession){
        //log.info("id user desde la variable de session desde getCart: {}",httpSession.getAttribute("iduser").toString());
        showCart();
        model.addAttribute("cart", cartService.getItemCarts());
        model.addAttribute("total",cartService.getTotalCart());
        model.addAttribute("id", httpSession.getAttribute("iduser").toString());
        return "user/cart/cart";
    }

    @GetMapping("/delete-item-cart/{id}")
    public String deleteItemCart(@PathVariable Integer id){
        cartService.removeItemCart(id);
        return "redirect:/user/cart/get-cart";
    }

    @GetMapping("/checkout/paypal")
    public String checkoutWithPaypal() {
        return "redirect:https://www.paypal.com/checkout";
    }

    // Método para manejar la respuesta de PayPal después del pago
    @GetMapping("/paypal/checkout-success")
    public String handlePaypalCheckoutSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpSession session) {
        // Lógica para manejar el éxito del pago en PayPal
        // Aquí puedes actualizar el estado del pedido en tu sistema y redirigir al usuario a una página de confirmación de compra
        return "redirect:/user/cart/get-cart";
    }

}

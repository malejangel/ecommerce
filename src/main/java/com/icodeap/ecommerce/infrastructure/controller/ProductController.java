package com.icodeap.ecommerce.infrastructure.controller;

import com.icodeap.ecommerce.application.service.ProductService;
import com.icodeap.ecommerce.application.service.StockService;
import com.icodeap.ecommerce.domain.Product;
import com.icodeap.ecommerce.domain.Stock;
import com.icodeap.ecommerce.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final StockService stockService;

    public ProductController(ProductService productService, StockService stockService) {
        this.productService = productService;
        this.stockService = stockService;
    }

    @GetMapping("/create")
    public String create(){
        return "admin/products/create";
    }

    @PostMapping("/save-product")
    public String saveProduct(Product product, @RequestParam("img") MultipartFile multipartFile, HttpSession httpSession, RedirectAttributes flash, Model model ) throws IOException {
        log.info("Nombre de producto: {}", product);
        productService.saveProduct(product, multipartFile, httpSession);
        String mensaje = "Producto guardado con exito";
        model.addAttribute("mensaje", mensaje);
        flash.addFlashAttribute("mensaje", mensaje);
        return "redirect:/admin/products/show?success=true";
    }
    @GetMapping("/show")
    public String showProduct(Model model, HttpSession httpSession){
        log.info("id user desde la variable de session: {}",httpSession.getAttribute("iduser").toString());

        User user = new User();
        user.setId(Integer.parseInt(httpSession.getAttribute("iduser").toString()));
        Iterable<Product> products = productService.getProductsByUser(user);
        model.addAttribute("products", products);
        return "admin/products/show";
    }


    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model){
        Product product = productService.getProductById(id);
        log.info("Product obtenido: {}", product);
        model.addAttribute("product",product);
        return "admin/products/edit";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, Model model, RedirectAttributes flash){

      try {
          productService.deleteProductById(id);
      }catch (DataIntegrityViolationException e){

          flash.addFlashAttribute("error","No se puede eliminar este producto, porque tiene una relacion con una compra.");
          return"redirect:/admin/products/show";
      }

      String mensaje = "Producto eliminado con exito";
      model.addAttribute("mensaje", mensaje);
      flash.addFlashAttribute("mensaje", mensaje);
        return "redirect:/admin/products/show?success=true";
    }

    @GetMapping("/product-detail/{id}")
    public String productDetail(@PathVariable Integer id, Model model, HttpSession httpSession){
        List<Stock> stocks = stockService.getStockByProduct(productService.getProductById(id));
        log.info("Id product: {}", id);
        log.info("stock size: {}", stocks.size());
        Integer lastBalance = stocks.get(stocks.size()-1).getBalance();

        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("stock", lastBalance);
        try {
            model.addAttribute("id", httpSession.getAttribute("iduser").toString());
        }catch (Exception e){

        }
        return "user/productdetail";
    }


}

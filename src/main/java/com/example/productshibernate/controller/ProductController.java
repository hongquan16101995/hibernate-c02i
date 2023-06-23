package com.example.productshibernate.controller;

import com.example.productshibernate.model.Product;
import com.example.productshibernate.service.ICRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Value("${path-upload}")
    private String upload;

    @Autowired
    public ICRUDService productService;

    @GetMapping
    public ModelAndView findAll() {
        List<Product> products = productService.findAll();
        ModelAndView modelAndView = new ModelAndView("/list");
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView createForm() {
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Product product) {
        String filename = product.getFile().getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getFile().getBytes(),
                    new File(upload + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setImage(filename);
        productService.create(product);
        return "redirect:/products";
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/update");
        modelAndView.addObject("product", productService.findById(id));
        return modelAndView;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Product product) {
        String filename = product.getFile().getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getFile().getBytes(),
                    new File(upload + filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setImage(filename);
        productService.update(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}

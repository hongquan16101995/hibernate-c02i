package com.example.productshibernate.service;

import com.example.productshibernate.model.Product;

import java.util.List;

public interface ICRUDService {

    List<Product> findAll();

    Product findById(Long id);

    void create(Product product);

    void update(Product product);

    void delete(Long id);
}

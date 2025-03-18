package com.example.demo.mapper;

import com.example.demo.entity.Product;
import com.example.demo.entity.request.ProductRequest;

import org.modelmapper.PropertyMap;

import java.time.LocalDateTime;


public class ProductMapper extends PropertyMap<ProductRequest, Product> {

    @Override
    protected void configure() {
        map().setId(0);
        map().setUpdateAt(LocalDateTime.now());
        map().setCreatedAt(LocalDateTime.now());
    }
}

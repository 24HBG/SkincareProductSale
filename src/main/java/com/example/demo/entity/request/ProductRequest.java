package com.example.demo.entity.request;

import com.example.demo.enums.SkinTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    public String name;
    public String description;
    @Min(value = 0)
    public double price;
    @Min(value = 0)
    public int stock;
    public SkinTypeEnum skinType;
    public BigDecimal rating;
    @NotNull
    public long categoryId;
}

package com.example.demo.entity.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    public long id = 0;
    @NotBlank
    public String name;
    @Min(value = 0)
    public float price;
    @Min(value = 0)
    public int stock;
    //PD00001
    @Pattern(regexp = "PD\\d{5}", message = "Code must be PDxxxxx!")
    @Column(unique = true)
    public String code;
    @NotNull
    public long categoryId;
}

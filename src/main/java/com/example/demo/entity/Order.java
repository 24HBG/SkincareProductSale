package com.example.demo.entity;

import com.example.demo.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id = 0;
    public LocalDateTime createAt;
    public float total;
    public OrderStatusEnum status = OrderStatusEnum.IN_PROCESS;
    @ManyToOne
    @JoinColumn(name = "account_id")
    public Account account;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails = new ArrayList<>();
}

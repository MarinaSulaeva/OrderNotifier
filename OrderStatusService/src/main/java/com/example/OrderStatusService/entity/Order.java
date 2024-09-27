package com.example.OrderStatusService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product")
    private String product;
    @Column(name = "quantity")
    private String quantity;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;


}

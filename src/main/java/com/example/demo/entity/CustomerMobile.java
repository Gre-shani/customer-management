package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "customer_mobile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerMobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15)
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}

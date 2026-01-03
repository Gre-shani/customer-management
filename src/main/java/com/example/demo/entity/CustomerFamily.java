package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "customer_family")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CustomerFamilyId.class)
public class CustomerFamily {

    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_member_id")
    private Customer familyMember;

    @Column(length = 50)
    private String relationship;
}

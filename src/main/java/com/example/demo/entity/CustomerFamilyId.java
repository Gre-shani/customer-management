package com.example.demo.entity;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFamilyId implements Serializable {

    private Long customer;
    private Long familyMember;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerFamilyId)) return false;
        CustomerFamilyId that = (CustomerFamilyId) o;
        return customer.equals(that.customer) &&
               familyMember.equals(that.familyMember);
    }

    @Override
    public int hashCode() {
        return 31 * customer.hashCode() + familyMember.hashCode();
    }
}

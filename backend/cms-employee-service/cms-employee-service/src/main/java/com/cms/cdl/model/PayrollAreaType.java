package com.cms.cdl.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "payrollTypeId"))
public class PayrollAreaType extends BaseEntity {
    private String sapPayrollType;
    @OneToMany(mappedBy = "payrollAreaType", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

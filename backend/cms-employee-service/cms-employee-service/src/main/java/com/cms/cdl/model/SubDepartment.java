package com.cms.cdl.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AttributeOverride(name = "id", column = @Column(name = "subDeptId"))
public class SubDepartment extends BaseEntity {
    private String subDeptName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainDeptId", referencedColumnName = "mainDeptId")
    private MainDepartment mainDepartment;
    @OneToMany(mappedBy = "subDepartment", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

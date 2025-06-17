package com.cms.cdl.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "mainDeptId"))
public class MainDepartment extends BaseEntity {
    private String mainDeptName;
    @OneToMany(mappedBy = "mainDepartment", fetch = FetchType.LAZY)
    private List<Employee> employees;
    @OneToMany(mappedBy = "mainDepartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubDepartment> subDepartmentsList;
}

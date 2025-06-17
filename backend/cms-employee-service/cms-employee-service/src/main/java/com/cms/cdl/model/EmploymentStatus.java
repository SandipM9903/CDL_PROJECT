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
@AttributeOverride(name = "id", column = @Column(name = "employmentStatusId"))
public class EmploymentStatus extends BaseEntity {
    private String employmentStatus;
    private String empStatusShortName;
    @OneToMany(mappedBy = "employmentStatus", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

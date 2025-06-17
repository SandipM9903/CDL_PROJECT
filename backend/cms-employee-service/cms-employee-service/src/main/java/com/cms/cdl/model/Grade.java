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
@AttributeOverride(name = "id", column = @Column(name = "gradeId"))
public class Grade extends BaseEntity {
    private String sapCode;
    private String grade;
    @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

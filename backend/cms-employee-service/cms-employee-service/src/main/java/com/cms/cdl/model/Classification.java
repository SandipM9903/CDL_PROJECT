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
@AttributeOverride(name = "id", column = @Column(name = "classificationId"))
public class Classification extends BaseEntity {
    private String classification;
    private String classificationDefinition;

    @OneToMany(mappedBy = "classification", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

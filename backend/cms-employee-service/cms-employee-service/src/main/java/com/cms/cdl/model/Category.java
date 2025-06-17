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
@AttributeOverride(name = "id", column = @Column(name = "categoryId"))
public class Category extends BaseEntity {
    private String category;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

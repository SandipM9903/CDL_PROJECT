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
@AttributeOverride(name = "id", column = @Column(name = "orgHierarchyId"))
public class OrgHierarchy extends BaseEntity {
    private String orgHierarchy;
    @OneToMany(mappedBy = "orgHierarchy", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

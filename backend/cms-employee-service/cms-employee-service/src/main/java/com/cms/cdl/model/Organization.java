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
@AttributeOverride(name = "id", column = @Column(name = "orgCode"))
public class Organization extends BaseEntity{
    private String organizationName;
    private String sapCode;
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

package com.cms.cdl.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@AttributeOverride(name = "id", column = @Column(name = "buHeadId"))
public class BuHeads extends BaseEntity {
    private String buHeadName;

    @OneToMany(mappedBy = "buHeads", fetch = FetchType.EAGER)
    private List<Employee> employees;
}

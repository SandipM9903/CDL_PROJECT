package com.cms.cdl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AttributeOverride(name = "id", column = @Column(name = "dependentDetailsId"))
public class DependentDetails extends BaseEntity {
    private String dependentName;
    private String dependentRelationship;

    @Column(name="dependent_dob")
    private LocalDate dependentDateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empId", referencedColumnName = "empId")
    private Employee employee;
}

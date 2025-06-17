package com.cms.cdl.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@AttributeOverride(name = "id", column = @Column(name = "hrHierarchyId"))
public class HrHierarchy extends BaseEntity {
    private String role;
    private String locationName;
    private String city;
    private String district;
    private String state;
    private String country;
}

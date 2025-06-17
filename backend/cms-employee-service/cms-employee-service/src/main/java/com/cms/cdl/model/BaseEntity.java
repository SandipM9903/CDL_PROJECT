package com.cms.cdl.model;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDate createdDate;

    @UpdateTimestamp()
    @Column(insertable = false, nullable = true)
    private LocalDate updatedDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private String createdBy;
    private String updatedBy;
}

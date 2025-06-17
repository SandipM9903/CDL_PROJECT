package com.cms.cdl.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AttributeOverride(name = "id", column = @Column(name = "salaryAccDetailsId"))
public class SalaryAccountDetails extends BaseEntity {
    private String bankName;
    private long accountNumber;
    private String nameOnAccount;
    private String ifsc;

    @OneToOne(mappedBy = "salaryAccountDetails", fetch = FetchType.LAZY)
    private Employee employee;
}

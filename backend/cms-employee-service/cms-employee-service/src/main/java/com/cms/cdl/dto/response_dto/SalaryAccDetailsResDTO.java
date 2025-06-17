package com.cms.cdl.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryAccDetailsResDTO {
    private long salaryAccDetailsId;
    private String bankName;
    private long accountNumber;
    private String nameOnAccount;
    private String ifsc;
}

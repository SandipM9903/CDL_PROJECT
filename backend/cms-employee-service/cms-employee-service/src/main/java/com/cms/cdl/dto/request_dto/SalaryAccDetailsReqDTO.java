package com.cms.cdl.dto.request_dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryAccDetailsReqDTO extends BaseEntityReqDTO{
    private String bankName;
    private long accountNumber;
    private String nameOnAccount;
    private String ifsc;
}

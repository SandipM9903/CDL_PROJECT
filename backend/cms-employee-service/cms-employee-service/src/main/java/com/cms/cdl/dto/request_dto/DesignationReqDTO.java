package com.cms.cdl.dto.request_dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DesignationReqDTO extends BaseEntityReqDTO {
    private String designationName;
}

package com.cms.cdl.dto.request_dto;

import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntityReqDTO {
    private String description;
    private String createdBy;
    private String updatedBy;
}

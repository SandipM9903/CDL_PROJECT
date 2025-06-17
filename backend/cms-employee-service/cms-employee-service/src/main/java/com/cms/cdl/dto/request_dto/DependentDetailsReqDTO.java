package com.cms.cdl.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DependentDetailsReqDTO extends BaseEntityReqDTO{
    private String dependentName;
    private String dependentRelationship;
    private String dependentDateOfBirth;
}

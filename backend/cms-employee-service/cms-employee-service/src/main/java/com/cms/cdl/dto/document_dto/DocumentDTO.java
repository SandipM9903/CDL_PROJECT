package com.cms.cdl.dto.document_dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDTO {
    private Long docId;
    private String itemName;
    private String docType;
    private String docPath;
    private String compressed;
    private String empCode;
    private String empGroup;
    private String empOrg;
    private String docTags;
    private String location;
    private String itemType;
    private String createdByRole;
    private LocalDate createdDate;
    private String docCaptions;
    private String docDescription;
}

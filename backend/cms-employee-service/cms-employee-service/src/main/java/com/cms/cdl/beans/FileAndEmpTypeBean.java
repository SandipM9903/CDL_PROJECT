package com.cms.cdl.beans;

import com.cms.cdl.dto.response_dto.EmpHierarchyResDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileAndEmpTypeBean {
    FileAndContentTypeBean fileAndContentTypeBean;
    EmpHierarchyResDTO empHierarchyResDTO;
}

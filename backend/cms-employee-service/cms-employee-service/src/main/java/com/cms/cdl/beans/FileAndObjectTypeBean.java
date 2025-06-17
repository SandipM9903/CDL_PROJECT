package com.cms.cdl.beans;

import com.cms.cdl.dto.response_dto.EmpResDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileAndObjectTypeBean {
    FileAndContentTypeBean fileAndContentTypeBean;
    EmpResDTO empResDTO;
}

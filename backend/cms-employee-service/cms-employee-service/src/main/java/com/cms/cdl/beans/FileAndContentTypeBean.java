package com.cms.cdl.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileAndContentTypeBean {
    private  byte[] file;
    private  String contentType;
}

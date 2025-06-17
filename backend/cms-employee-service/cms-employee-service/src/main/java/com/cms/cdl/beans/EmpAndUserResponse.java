package com.cms.cdl.beans;

import com.cms.cdl.dto.user_dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonDeserialize(using = EmpAndUserResponseDeserializer.class)
public class EmpAndUserResponse {
    private FileAndObjectTypeBean fileAndObjectTypeBean;
    private UserDTO userDTO;
    // Getters and setters

    @JsonProperty("fileAndObjectTypeBean")  // Specify JSON property names if needed
    public FileAndObjectTypeBean getFileAndObjectTypeBean() {
        return fileAndObjectTypeBean;
    }

    public void setFileAndObjectTypeBean(FileAndObjectTypeBean fileAndObjectTypeBean) {
        this.fileAndObjectTypeBean = fileAndObjectTypeBean;
    }

    @JsonProperty("userDTO")  // Specify JSON property names if needed
    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}

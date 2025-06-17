package com.cms.cdl.dto.user_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {
    private long experienceId;
    private String experience;
    private String companyName;
    private String companyAddress;
//    @JsonSerialize(using = CustomDateSerializer.class)
//    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDate dateOfJoining;
//    @JsonSerialize(using = CustomDateSerializer.class)
//    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDate dateOfReliving;
    private String jobTitle;
    private String certification;
}

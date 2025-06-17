package com.cms.cdl.utils;

import com.cms.cdl.dto.user_dto.ExperienceDTO;
import com.cms.cdl.dto.user_dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.Period;
import java.util.List;

@Component
public class UserOperations {
    @Value("${user.fetch.byUserIdAPI}")
    private String userByUserIdAPI;
    @Value("${user.fetch.getAllUserAPI}")
    private String getAllUserAPI;
    @Value("${user.fetch.byLocationAPI}")
    private String userByLocationAPI;
    @Autowired
    WebClient webClient;

    /*
    -- fetch the user details by user id from user service
    */
    public UserDTO getUserByUserId(long userId) {
        Mono<UserDTO> response = webClient.get()
                .uri(userByUserIdAPI + userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UserDTO>() {
                });
        UserDTO userDTO = response.block();
        if (userDTO != null) {
            // fetching old company experience
            String oldCompaniesExp = calculateOldCompanyExp(userDTO);
            userDTO.setTotalExperience(oldCompaniesExp);
            return userDTO;
        } else {
            return null;
        }
    }


    /*
    -- fetch all users from user service
    */
    public List<UserDTO> getAllUsers() {
        Mono<List<UserDTO>> responseList = webClient.get()
                .uri(getAllUserAPI)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {
                });
        List<UserDTO> userDTOList = responseList.block();
        if (userDTOList != null) {
            return userDTOList;
        } else {
            return null;
        }
    }


    /*
    -- fetch list of active users from user service for same location
    */
    public List<UserDTO> getUserByLocation(Long locationId, int page) {
        Mono<List<UserDTO>> response = webClient.get()
                .uri(userByLocationAPI + locationId+ "/" + page)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UserDTO>>() {});
        List<UserDTO> userDTOList = response.block();
        if (userDTOList != null) {
            return userDTOList;
        } else {
            return null;
        }
    }


    /*
    -- calculate old experience
    */
    public String calculateOldCompanyExp(UserDTO userDTO) {
        int totalMonths = 0;
        int totalYears = 0;
        String totalExp = "";
        for (ExperienceDTO experience : userDTO.getExperienceDTOS()) {
            Period period = Period.between(experience.getDateOfJoining(), experience.getDateOfReliving());
            totalYears += period.getYears();
            totalMonths += period.getMonths();
        }
        totalYears += totalMonths / 12;
        totalMonths = totalMonths % 12;
        totalExp = totalYears + " Years " + totalMonths + " Months";
        return totalExp;
    }

}

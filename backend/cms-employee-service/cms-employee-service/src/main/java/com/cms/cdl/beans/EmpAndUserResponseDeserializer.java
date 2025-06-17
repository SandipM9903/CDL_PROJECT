package com.cms.cdl.beans;

import com.cms.cdl.dto.user_dto.UserDTO;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EmpAndUserResponseDeserializer extends StdDeserializer<EmpAndUserResponse> {
    public EmpAndUserResponseDeserializer() {
        super(EmpAndUserResponse.class);
    }
    @Override
    public EmpAndUserResponse deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        // Get the JSON node
        JsonNode node = jp.getCodec().readTree(jp);

        // Deserialize the fields from the JSON
        FileAndObjectTypeBean fileAndObjectTypeBean = parseFileAndObjectTypeBean(node.get("fileAndObjectTypeBean"));
        UserDTO userDTO = parseUserDTO(node.get("userDTO"));

        // Create and return the EmpAndUserResponse object
        return new EmpAndUserResponse(fileAndObjectTypeBean, userDTO);
    }

    private FileAndObjectTypeBean parseFileAndObjectTypeBean(JsonNode node) throws JsonProcessingException {
        if (node == null) {
            return null;
        }

        // Add your logic to parse FileAndObjectTypeBean
        // For simplicity, let's assume it's a simple field, you can modify it to match your structure
        return new ObjectMapper().treeToValue(node, FileAndObjectTypeBean.class);
    }

    private UserDTO parseUserDTO(JsonNode node) throws JsonProcessingException {
        if (node == null) {
            return null;
        }

        // Add your logic to parse UserDTO
        // Similarly, adjust according to your UserDTO structure
        return new ObjectMapper().treeToValue(node, UserDTO.class);
    }
}

package com.cms.cdl.utils;

import com.cms.cdl.beans.FileAndContentTypeBean;
import com.cms.cdl.dto.document_dto.DocumentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class DocumentOperations {
    @Value("${employee.document.fetch.API}")
    private String empDocFetchAPI;

    @Value("${employee.document.post.API}")
    private String empDocPostAPI;

    @Value("${employee.document.upload.directory}")
    private String empDocUploadDirectory;

    @Value("${employee.document.delete.API}")
    private String empDocDeleteAPI;

    @Value("${myDocuments.API}")
    private String myDocsAPI;

    @Value("${employee.document.update.API}")
    private String empDocUpdateAPI;
    @Autowired
    WebClient webClient;

    @CircuitBreaker(name = "documentService", fallbackMethod = "fallbackOpenAndDownloadDocument")
    @TimeLimiter(name = "documentService", fallbackMethod = "fallbackOpenAndDownloadDocument")
    public CompletableFuture<FileAndContentTypeBean> openAndDownloadDocument(long documentId) {
        return webClient.get()
                .uri(empDocFetchAPI + documentId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchangeToMono(response -> response.toEntity(byte[].class))
                .timeout(Duration.ofSeconds(10))
                .map(responseEntity -> {
                    String contentType = responseEntity.getHeaders().getContentType().toString();
                    byte[] fileData = responseEntity.getBody();
                    return new FileAndContentTypeBean(fileData, contentType);
                })
                .toFuture();
    }

    public FileAndContentTypeBean fetchDocument(long documentId) {
        return webClient.get()
                .uri(empDocFetchAPI + documentId)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .exchangeToMono(response -> response.toEntity(byte[].class))
                .timeout(Duration.ofSeconds(10))
                .map(responseEntity -> {
                    String contentType = responseEntity.getHeaders().getContentType().toString();
                    byte[] fileData = responseEntity.getBody();
                    return new FileAndContentTypeBean(fileData, contentType);
                }).block();
    }

    public List<DocumentDTO> uploadDocument(DocumentDTO documentDTO, MultipartFile empProfileImg) throws IOException {
        List<DocumentDTO> docDTOList = new ArrayList<DocumentDTO>();
        ObjectMapper objectMapper = new ObjectMapper();
        String documentDTOJson = null;
        try {
            documentDTOJson = objectMapper.writeValueAsString(documentDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Create HttpEntity for the DocumentDTO JSON
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> jsonEntity = new HttpEntity<>(documentDTOJson, jsonHeaders);

        // Create HttpEntity for the MultipartFile
        HttpHeaders fileHeaders = new HttpHeaders();
        ByteArrayResource fileAsResource = new ByteArrayResource(empProfileImg.getBytes()) {
            @Override
            public String getFilename() {
                return empProfileImg.getOriginalFilename();
            }
        };
        HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileAsResource, fileHeaders);

        // Create multipart form data
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("documentDTO", jsonEntity);
        formData.add("files", fileEntity);

        ResponseEntity<List<DocumentDTO>> documentDTOResponseEntity = webClient.post()
                .uri(empDocPostAPI + empDocUploadDirectory)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<DocumentDTO>>() {
                })
                .block();
        if (documentDTOResponseEntity != null && documentDTOResponseEntity.hasBody()) {
            return documentDTOResponseEntity.getBody();
        } else {
            return null;
        }
    }

    public DocumentDTO updateDocument(DocumentDTO documentDTO, long docId, MultipartFile profileImg, String userRole) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String documentDTOJson = null;
        try {
            documentDTOJson = objectMapper.writeValueAsString(documentDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        // Create HttpEntity for the DocumentDTO JSON
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> jsonEntity = new HttpEntity<>(documentDTOJson, jsonHeaders);

        // Create HttpEntity for the MultipartFile
        HttpHeaders fileHeaders = new HttpHeaders();
        ByteArrayResource fileAsResource = new ByteArrayResource(profileImg.getBytes()) {
            @Override
            public String getFilename() {
                return profileImg.getOriginalFilename();
            }
        };
        HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileAsResource, fileHeaders);

        // Create multipart form data
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("documentDTO", jsonEntity);
        formData.add("file", fileEntity);

        try {
            // Update the existing document using WebClient

            return webClient.put()
                    .uri(empDocUpdateAPI + docId + "/" + userRole + "/" + empDocUploadDirectory )  // Assuming the API endpoint includes the documentId for updates
                    .body(BodyInserters.fromMultipartData(formData))
                    .retrieve()
                    .bodyToMono(DocumentDTO.class)  // Assuming the API returns a String response
                    .block();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DocumentDTO> getMyDocuments(long empId) {
        Mono<List<DocumentDTO>> response = webClient.get()
                .uri(myDocsAPI + empId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        List<DocumentDTO> documentDTOs = response.block();

        if (documentDTOs != null) {
            return documentDTOs;
        } else {
            return null;
        }
    }

    public boolean deleteDocument(long docId) {
        try {
            Boolean response = webClient.delete()
                    .uri(empDocDeleteAPI + docId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .doOnError(error -> {
                        System.err.println("Error occurred: " + error.getMessage());
                    })
                    .block();  // Blocking to get the result synchronously

            if (response != null && response) {
                System.out.println("Delete successful: " + response);
                return true;
            } else {
                System.err.println("Delete failed or returned false");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Exception during delete: " + e.getMessage());
            return false;
        }
    }

    public CompletableFuture<FileAndContentTypeBean> fallbackOpenAndDownloadDocument(long documentId, Throwable throwable) {
        return CompletableFuture.completedFuture(null);
    }

}

package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.config.file.ThumbnailVariant;
import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dto.BannerDto;
import com.bedatasolutions.leaseDrop.dto.FileResponseDto;
import com.bedatasolutions.leaseDrop.services.FileService;
import com.bedatasolutions.leaseDrop.utils.ApiResponse;
import com.bedatasolutions.leaseDrop.utils.ImageWithBannerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping(value = "/api/banners", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class BannerController {
    private final FileService fileService;


    @Value("${app.server.file.root.path}")
    private String FILE_SOURCE;


    @PostMapping
    public ResponseEntity<FileResponseDto> uploadBanner(
            @RequestParam("file") MultipartFile file,
            @RequestParam("duration") Integer duration) {  // Accept duration as part of the request
        String uploadedFileName = "";

        if (file.isEmpty()) {
            log.error("No file selected for upload.");
            return new ResponseEntity<>(new FileResponseDto(null, "No file selected for upload"), HttpStatus.BAD_REQUEST);
        }
        try {
            uploadedFileName = fileService.upload(file, duration);
            return new ResponseEntity<>(new FileResponseDto(uploadedFileName, "File Uploaded Successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            return new ResponseEntity<>(new FileResponseDto(uploadedFileName == "" ? " No File Selected" : null, "Error uploading file"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping(value = "/images", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImage(@RequestParam String type, @RequestParam String path) {
        try {
            // Call service method to retrieve the image
            Resource image = fileService.getImage(type, path);
            if (image != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Set the response type to JPEG
                        .body(image); // Return the image as the response body
            } else {
                return ResponseEntity.notFound().build(); // Return 404 if image is not found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return 500 on error
        }
    }

    public ResponseEntity<ImageWithBannerResponse> getRandomImage(@RequestParam(value = "type", required = false) String fileType) {
        try {
            // If fileType is null or invalid, set it to "L"
            if (fileType == null || !isValidFileType(fileType)) {
                fileType = "L"; // Default value
            }

            // Retrieve a random banner from the service
            BannerDao randomBanner = fileService.getRandomBanner();

            // If a random banner is found
            if (randomBanner != null) {
                // Retrieve the file path from the banner
                String filePath = randomBanner.getFilePath();

                // Call the getImage() method to get the image (it already returns ResponseEntity<Resource>)
                ResponseEntity<Resource> imageResponse = getImage(fileType, filePath);

                // Return a custom response with both the BannerDao, image, and duration
                return ResponseEntity.ok(new ImageWithBannerResponse(randomBanner, imageResponse.getBody()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // No banner found
            }
        } catch (Exception e) {
            log.error("Error fetching random image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Internal error
        }
    }



    // Method to validate if the fileType is valid
    private boolean isValidFileType(String fileType) {
        return fileType.equals(ThumbnailVariant.L.name()) ||
                fileType.equals(ThumbnailVariant.M.name()) ||
                fileType.equals(ThumbnailVariant.O.name());
    }


    // Update a banner (PUT request)


    // Update banner (PUT request)
    @PutMapping()
    public ResponseEntity<ApiResponse<String>> update(@RequestBody @Valid BannerDto bannerDto) {
        String message = fileService.update(bannerDto);
        if (message.contains("successfully")) {
            return ApiResponse.success(message, null);
        } else {
            return ApiResponse.error(message, null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Integer id) {
        String message = fileService.delete(id);
        if (message.contains("successfully")) {
            return ApiResponse.success(message, null);
        } else {
            return ApiResponse.error(message, null);
        }
    }





    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String field,
            @RequestParam(defaultValue = "desc") String direction) {
        try {
            Map<String, Object> result = fileService.getAllImages(page, size, field, direction);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving images", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}



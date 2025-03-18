package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.config.file.ThumbnailVariant;
import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dto.BannerDto;
import com.bedatasolutions.leaseDrop.dto.rest.RestPageResponse;
import com.bedatasolutions.leaseDrop.dto.rest.RestQuery;
import com.bedatasolutions.leaseDrop.services.BannerService;
import com.bedatasolutions.leaseDrop.utils.ApiResponse;
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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/banners", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class BannerController {
    private final BannerService bannerService;


    @Value("${app.server.file.root.path}")
    private String FILE_SOURCE;

   // @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<BannerDto> uploadBanner(
            @RequestParam("file") MultipartFile file,
            @RequestParam("duration") Integer duration) {

        String uploadedFileName = "";

        if (file.isEmpty()) {
            log.error("No file selected for upload.");
            return new ResponseEntity<>(new BannerDto(
                    null,  // duration
                    null,  // createdAt, can be any placeholder value
                    null,  // fileName
                    null,  // fileSize
                    null,  // id
                    null), HttpStatus.BAD_REQUEST);

        }

        try {
            BannerDto response = bannerService.upload(file, duration);  // Now returns complete response
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());




        
            // Handle the error response by providing all required arguments
            return new ResponseEntity<>(new BannerDto(
                    null,  // duration (no value in error)
                    null,  // createdAt (no value in error, can be a placeholder)
                    0,  // duration (handle empty case)
                    null,  // fileSize (no value in error)
                    null,  // id (no value in error)
                    "Error uploading file"
            ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/images", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImage(@RequestParam String type, @RequestParam String path) {
        try {
            // Call service method to retrieve the image
            Resource image = bannerService.getImage(type, path);
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


   // @CrossOrigin(origins = "*")
    @GetMapping("/random-image")
    public ResponseEntity<Map<String, Object>> getRandomImage(@RequestParam(value = "type", required = false) String fileType) {
        try {
            // If fileType is null or invalid, set it to "L"
            if (fileType == null || !isValidFileType(fileType)) {
                fileType = "O"; // Default value
            }

            // Retrieve a random banner from the service
            BannerDao randomBanner = bannerService.getRandomBanner();

            // If a random banner is found
            if (randomBanner != null) {
                // Retrieve the file path from the banner (already Base64 encoded)
                String encodedFilePath = randomBanner.getFilePath();

                // Generate image URL using the same method as in getAllImages()
                String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                        BannerController.class, "getImage", fileType, encodedFilePath
                ).build().toString();

                // Create response object with image URL and duration
                Map<String, Object> response = new HashMap<>();
                response.put("url", imgUrl);
                response.put("duration", randomBanner.getDuration());

                return ResponseEntity.ok(response);
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



    @PutMapping()
    public ResponseEntity<ApiResponse<BannerDto>> update(@RequestBody @Valid BannerDto bannerDto) {
        try {
            // Call service method to update the banner and get the updated data
            BannerDto updatedBanner = bannerService.update(bannerDto);

            // Return success response with the updated banner data
            return ApiResponse.success("Banner updated successfully", updatedBanner);
        } catch (Exception e) {
            // Return error response if update fails
            return ApiResponse.error("Error updating banner: " + e.getMessage(), null);
        }
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean isDeleted = bannerService.delete(id);
        if (isDeleted) {
            // Return HTTP 202 Accepted (successful deletion, processing may still be ongoing)
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            // Return HTTP 404 Not Found if the banner doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public RestPageResponse<BannerDao, BannerDto> getAllBanners(
            @RequestBody(required = false) RestQuery query) {

        // Call the service method to fetch customers with pagination, sorting, and filtering
//        return customerService.getAllCustomers(page, size, new RestSort(field, direction), filters);
//        return customerService.getAllCustomers(query.page().pageNumber(), query.page().size(), query.sort(), query.filter().filters());
        return  bannerService.getAllBanners(query.page(), query.sort(), query.filter());
    }


}



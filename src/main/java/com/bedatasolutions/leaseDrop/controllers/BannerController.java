package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.DocumentDao;
import com.bedatasolutions.leaseDrop.dto.DocumentDto;
import com.bedatasolutions.leaseDrop.dto.FileInfoDto;
import com.bedatasolutions.leaseDrop.dto.FileResponseDto;
import com.bedatasolutions.leaseDrop.services.FileService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

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
        try {
            String uploadedFileName = fileService.upload(file, duration);

            return new ResponseEntity<>(new FileResponseDto(uploadedFileName,"File Uploaded Successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage());
            return new ResponseEntity<>(new FileResponseDto(null,"Error uploading file"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
/*
    @GetMapping
    public ResponseEntity<List<FileInfoDto>> getBanners(@RequestParam String path) {
        //log.info("Received GET request for path: " + path);
        System.out.println(path);
        try {
            List<FileInfoDto> fileList = bannerService.getBanners(path);

            return new ResponseEntity<>(fileList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving banners: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }
*/

/*    @GetMapping("/images/{variant}/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String variant, @PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(FILE_SOURCE, "users", "user_photo", fileName);

            // Check if file exists
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            // Determine content type
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default binary type if unknown
            }

            // Return FileSystemResource as Resource directly
            FileSystemResource resource = new FileSystemResource(imagePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body((Resource) resource); // Explicitly cast to Resource (should not be necessary)

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }*/


//    @GetMapping("/{fileName}")
//    public ResponseEntity<UrlResource> getImageByName(@PathVariable String fileName) {
//        return bannerService.getImageByName(fileName);
//    }



    @GetMapping("/{id}")
    public ResponseEntity<UrlResource>getBanner(@PathVariable Integer id) {

        FileInfoDto fileInfoDto= fileService.getOne(id);
        String fileName=fileInfoDto.name();
        return fileService.getImageByName(fileName);
    }


    @GetMapping("/all")
    public ResponseEntity<List<FileInfoDto>> getAllBanners() {
        try {
            List<FileInfoDto> fileList = fileService.getAll();  // This now retrieves all banners from the DB
            return new ResponseEntity<>(fileList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving banners: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }




    @GetMapping("/banner")
    public ResponseEntity<UrlResource> getRandomBanner(@RequestParam Optional<Integer> id) {
        try {
            FileInfoDto fileDto;
            String imageName;

            if (id.isPresent()) {
                // If ID is provided, fetch the banner by ID
                fileDto = fileService.getOne(id.get());
                imageName=fileDto.name();
                if (fileDto.id() == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if not found
                }
            } else {
                // If ID is not provided, fetch a random banner
                fileDto = fileService.getRandom();
                 imageName=fileDto.name();
                if (fileDto.id() == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }

            return fileService.getImageByName(imageName);

        } catch (Exception e) {
            log.error("Error retrieving banner: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


/*    // Method to update an existing document

        DocumentDao existingDocument = documentRepo.getReferenceById(documentDto.id());

        if (existingDocument.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found with id: " + documentDto.id());
        }
        existingDocument.setActionKey(ActionType.UPDATE);
        DocumentDao updatedDocument=documentRepo.save(dtoToDao(documentDto,existingDocument));
        return daoToDto(updatedDocument);
    }*/

    @Transactional
    @PutMapping("/{id")
    public ResponseEntity<FileResponseDto> update(FileInfoDto fileInfoDto)
    {


    }


    @GetMapping("/{userPath}/{photoPath}/{fileName}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String userPath,
            @PathVariable String photoPath,
            @PathVariable String fileName) {
        try {
            // Construct file path dynamically based on URL parameters
            Path imagePath = Paths.get(FILE_SOURCE, userPath, photoPath, fileName);

            // Check if file exists
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            // Determine content type
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default binary type if unknown
            }

            // Return FileSystemResource as a downloadable resource
            FileSystemResource resource = new FileSystemResource(imagePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body((Resource) resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }





}


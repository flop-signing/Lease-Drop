package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.config.file.FilePath;
import com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils;
import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dto.FileInfoDto;
import com.bedatasolutions.leaseDrop.repo.BannerRepo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class FileService {

    @Value("${app.server.file.root.path}")
    private String FILE_SOURCE;


    private final BannerRepo bannerRepo;

    public FileService(BannerRepo bannerRepo) {
        this.bannerRepo = bannerRepo;
    }


    public String upload(MultipartFile file, Integer duration) throws IOException {
        if (!file.isEmpty()) {
            // Extract the actual file name
            String originalFileName = file.getOriginalFilename();

            // Define the full file storage path
            Path filePath = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get());

            // Process and save the file
            MultipartFileUtils.processFile(file, filePath.toFile().getAbsolutePath(), true);

            // Extract and save only the relative directory path in DB (without FILE_SOURCE)
            String relativeDirectoryPath =  FilePath.USERS.get() +  FilePath.USER_PHOTO.get() ;

            // Save banner info to database
            BannerDao bannerDao = new BannerDao();
            bannerDao.setActionKey(ActionType.CREATE);
            bannerDao.setTitle(originalFileName);
            bannerDao.setDuration(duration);
            bannerDao.setSize(FileUtils.byteCountToDisplaySize(file.getSize()));
            bannerDao.setFilePath(relativeDirectoryPath);

            bannerRepo.save(bannerDao);

            return originalFileName;
        }
        return "No file uploaded";
    }



/*    public List<FileInfoDto> getBanners(String path) throws IOException {
   //     String cmpPath = MultipartFileUtils.urlDecode(path);
        Path filePath = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get());

        List<FileInfoDto> fileInfos = new ArrayList<>();
        try (Stream<Path> filePathStream = Files.walk(filePath, 1)) {
            filePathStream.filter(Files::isRegularFile).forEach(file -> {
                FileInfoDto fileInfo = createFileInfo(file.toFile());
                if (fileInfo != null) {
                    fileInfos.add(fileInfo);
                }
            });
        }
        return fileInfos;
    }*/


    // Retrieve all banners from the database
    public List<FileInfoDto> getAll() throws IOException {
        List<BannerDao> banners = bannerRepo.findAll();  // Fetch all banners from the DB

        List<FileInfoDto> fileInfos = new ArrayList<>();
        for (BannerDao banner : banners) {
            fileInfos.add(convertToDto(banner));  // Convert BannerDao to FileInfoDto
        }

        return fileInfos;
    }




    public FileInfoDto getOne(Integer id) {
        Optional<BannerDao> bannerOpt = bannerRepo.findById(id);
        if (bannerOpt.isPresent()) {
            BannerDao banner = bannerOpt.get();
            return convertToDto(banner);
        }
        return null;  // If not found, return null
    }

    public FileInfoDto getRandom() {
        List<BannerDao> banners = bannerRepo.findAll();
        if (banners.isEmpty()) {
            return null;  // If no banners exist, return null
        }

        // Select a random banner
        Random rand = new Random();
        BannerDao randomBanner = banners.get(rand.nextInt(banners.size()));
        return convertToDto(randomBanner);
    }



    public ResponseEntity<UrlResource> getImageByName(String fileName) {
        try {
            // Construct full image path
            Path imagePath = Paths.get(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get(),fileName);

            // Check if the file exists
            if (!Files.exists(imagePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            //  Declare `Resource` explicitly
            UrlResource resource = new UrlResource(imagePath.toUri());

            // Ensure the resource is readable
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Determine content type
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default binary type if unknown
            }

            // Return the image as a response
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    private FileInfoDto convertToDto(BannerDao banner) {
        try {
            // Retrieve the stored relative directory path
            String relativePath = banner.getFilePath();  // Will be "/users/user_photo/"

            // Construct the full absolute file path
            String absoluteFilePath = FILE_SOURCE + relativePath + MultipartFileUtils.urlEncode(banner.getTitle());

            return FileInfoDto.builder()
                    .id(banner.getId())
                    .name(banner.getTitle())
                    .url(absoluteFilePath)  //  Return full file path in the response
                    .dateTime(banner.getCreatedAt())  // Using createdAt from AuditableEntity
                    .size(banner.getSize())
                    .duration(banner.getDuration())  // Retrieve the duration from the DB
                    .build();
        } catch (Exception e) {
            return null;
        }
    }






}

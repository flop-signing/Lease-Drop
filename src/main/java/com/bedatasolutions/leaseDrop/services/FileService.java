package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.config.file.FilePath;
import com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils;
import com.bedatasolutions.leaseDrop.config.file.ThumbnailVariant;
import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.controllers.BannerController;
import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dto.BannerDto;

import com.bedatasolutions.leaseDrop.dto.FileResponseDto;
import com.bedatasolutions.leaseDrop.repo.BannerRepo;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils.urlDecode;
import static com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils.urlEncode;

@Service
public class FileService {
    @Value("${app.server.file.root.path}")
    private String FILE_SOURCE;

    private static final Logger log = LoggerFactory.getLogger(FileService.class);


    private final BannerRepo bannerRepo;

    public FileService(BannerRepo bannerRepo) {
        this.bannerRepo = bannerRepo;
    }


    @Transactional
    public FileResponseDto upload(MultipartFile file, Integer duration) throws IOException {
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            // Define the full file storage path
            Path filePath = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get());

            // Process and save the file
            MultipartFileUtils.processFile(file, filePath.toFile().getAbsolutePath(), true);

            // Construct the relative file path for accessing the image
            String filePathEE = Path.of(FilePath.USERS.get(), FilePath.USER_PHOTO.get(), originalFileName).toString();
            String encodedFilePath = urlEncode(filePathEE);

            // Save banner info to database
            BannerDao bannerDao = new BannerDao();
            bannerDao.setActionKey(ActionType.CREATE);
            bannerDao.setFileName(originalFileName);
            bannerDao.setFileSize(FileUtils.byteCountToDisplaySize(file.getSize()));
            bannerDao.setDuration(duration);
            bannerDao.setFilePath(encodedFilePath);
            bannerDao.setFileType(ThumbnailVariant.M.name());

            // Save to the repository
            bannerRepo.save(bannerDao);

            // Construct the image URL
            String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                    BannerController.class, "getImage", ThumbnailVariant.M.name(), encodedFilePath
            ).build().toString();

            // Prepare the response
            return new FileResponseDto(
                    duration,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),  // createdAt
                    originalFileName,
                    FileUtils.byteCountToDisplaySize(file.getSize()),  // fileSize
                    bannerDao.getId(),
                    imgUrl
            );
        }
        return new FileResponseDto(
                null,  // duration
                "No date",  // createdAt (placeholder value)
                "No file selected",  // fileName (file not selected)
                null,  // fileSize (no size available)
                null,  // id (no id available)
                "Error uploading file"  // url (error message instead of a URL)
        );
    }


    public Map<String, Object> getAllImages(Integer page, Integer size, String field, String direction) throws Exception {
        // Handle null and invalid input values, set defaults if necessary
        if (page == null || page < 1) page = 1; // Default to page 1
        if (size == null || size <= 0) size = 10; // Default to size 10
        if (field == null || field.trim().isEmpty()) field = "id"; // Default to `id` for sorting
        if (direction == null || direction.trim().isEmpty()) direction = "desc"; // Default to "desc" for sorting

        // Convert `field` to lowercase to handle case insensitivity
        field = field.toLowerCase();

        // Define Date Format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Sort direction (ASC/DESC)
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());

        // Determine Sorting Strategy (DB vs Java)
        List<BannerDao> banners;
        if (field.equals("filesize") || field.equals("filename")) {
            // Sorting by `fileSize` or `fileName` requires manual sorting (fetch all)
            banners = bannerRepo.findAll();
        } else {
            // Use DATABASE SORTING for `fileName`, `createdAt`, and `id`
            String sortField = field.equals("filename") ? "tx_file_name" :
                    field.equals("createdat") ? "createdAt" :
                            field.equals("id") ? "id" : field; // Ensure default is id

            banners = bannerRepo.findAll(Sort.by(sortDirection, sortField));
        }

        // Remove any banners with null `fileName` values before sorting
        banners = banners.stream()
                .filter(b -> b.getFileName() != null && !b.getFileName().trim().isEmpty())
                .collect(Collectors.toList());

        // Manual Sorting for `fileSize` and `fileName`
        if (field.equals("filesize")) {
            Comparator<BannerDao> sizeComparator = Comparator.comparingInt(b -> extractNumericSize(b.getFileSize()));
            if (direction.equalsIgnoreCase("desc")) {
                sizeComparator = sizeComparator.reversed();
            }
            banners.sort(sizeComparator);
        } else if (field.equals("filename")) {
            Comparator<BannerDao> nameComparator = Comparator.comparing(BannerDao::getFileName, String.CASE_INSENSITIVE_ORDER);
            if (direction.equalsIgnoreCase("desc")) {
                nameComparator = nameComparator.reversed();
            }
            banners.sort(nameComparator);
        }

        // Convert BannerDao to response format
        List<Map<String, Object>> fileInfos = new ArrayList<>();
        for (BannerDao banner : banners) {
            try {
                if (banner.getFileName() == null || banner.getFileName().trim().isEmpty()) {
                    log.warn("Skipping banner with null or empty fileName, id: {}", banner.getId());
                    continue;
                }

                // Construct file path safely
                String filePathEE = Path.of(FilePath.USERS.get(), FilePath.USER_PHOTO.get(), banner.getFileName()).toString();

                // Generate image URL
                String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                        BannerController.class, "getImage", ThumbnailVariant.M.name(), urlEncode(filePathEE)
                ).build().toString();

                // Format date using dd-MM-yyyy
                String formattedDate = banner.getCreatedAt() != null ? banner.getCreatedAt().format(dateFormatter) : "N/A";

                // Create response object with correct field names
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("url", imgUrl);
                fileInfo.put("fileName", banner.getFileName());
                fileInfo.put("fileSize", banner.getFileSize() != null ? banner.getFileSize() : "N/A");
                fileInfo.put("duration", banner.getDuration());
                fileInfo.put("id", banner.getId());
                fileInfo.put("createdAt", formattedDate);

                fileInfos.add(fileInfo);
            } catch (Exception e) {
                log.error("Error processing banner with fileName: {}", banner.getFileName(), e);
            }
        }

        // Apply pagination manually since we fetched all data
        int start = Math.min((page - 1) * size, fileInfos.size());
        int end = Math.min(start + size, fileInfos.size());
        List<Map<String, Object>> paginatedList = fileInfos.subList(start, end);

        // Create response structure
        Map<String, Object> response = new HashMap<>();
        response.put("payload", paginatedList);

        // Add page info to "header"
        Map<String, Object> header = new HashMap<>();
        header.put("pageNo", page);
        header.put("totalElements", fileInfos.size());
        header.put("pageSize", size);
        header.put("totalPages", (int) Math.ceil((double) fileInfos.size() / size));

        response.put("header", header);

        return response;
    }


    private int extractNumericSize(String sizeString) {
        if (sizeString == null || !sizeString.contains(" ")) return 0;
        try {
            return Integer.parseInt(sizeString.split(" ")[0]); // Extract number before "KB"
        } catch (NumberFormatException e) {
            return 0; // Return 0 if parsing fails
        }
    }

    public Resource getImage(String type, String path) throws IOException {
        // Decode the URL-encoded file path from Base64
        String cmpPath = urlDecode(path);
        log.info("Decoded file path: {}", cmpPath);

        // Construct the full file path
        Path fileExPath = Path.of(FILE_SOURCE, FilenameUtils.getPath(cmpPath),
                ThumbnailVariant.valueOf(type).path(), FilenameUtils.getName(cmpPath));
        log.info("Accessing file at: {}", fileExPath);

        // Check if the file exists and is readable
        if (Files.exists(fileExPath) && Files.isReadable(fileExPath)) {
            // Create InputStream for the file
            InputStream inputStream = Files.newInputStream(fileExPath);
            return new InputStreamResource(inputStream); // Return the image as a Resource
        } else {
            log.error("File not found or not readable: {}", fileExPath);
            return null; // Return null if the file doesn't exist or is not readable
        }
    }


    // Method to retrieve a random banner
    public BannerDao getRandomBanner() {
        // Retrieve all banners from the database
        List<BannerDao> allBanners = bannerRepo.findAll();

        // If no banners are found, return null
        if (allBanners.isEmpty()) {
            return null;
        }

        // Select a random index and return the corresponding banner
        Random random = new Random();
        int randomIndex = random.nextInt(allBanners.size());
        return allBanners.get(randomIndex);
    }


    @Transactional
    public FileResponseDto update(BannerDto bannerDto) {
        BannerDao existingBanner = bannerRepo.findById(bannerDto.id()).orElse(null);

        if (existingBanner != null) {
            // Update the fields of the existing banner
            existingBanner.setDuration(bannerDto.duration());
            existingBanner.setActionKey(ActionType.UPDATE);

            // Save the updated banner back to the database
            bannerRepo.save(existingBanner);

            // Retrieve the file path from the updated banner (Base64 encoded)
            String encodedFilePath = existingBanner.getFilePath();

            // Generate the image URL using MvcUriComponentsBuilder (same as in getAllImages)
            String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                    BannerController.class, "getImage", ThumbnailVariant.M.name(), encodedFilePath
            ).build().toString();


            // Prepare FileResponseDto with updated banner data
            return new FileResponseDto(
                    existingBanner.getDuration(),
                    existingBanner.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    existingBanner.getFileName(),
                    existingBanner.getFileSize(),
                    existingBanner.getId(),
                    imgUrl
            );
        } else {
            // Handle the case when banner is not found
            throw new IllegalArgumentException("Banner not found with ID: " + bannerDto.id());
        }
    }


    @Transactional
    public boolean delete(Integer id) {
        // Find the banner by ID
        BannerDao banner = bannerRepo.findById(id).orElse(null);

        if (banner != null) {
            // Retrieve the file path from the banner
            String encodedFilePath = banner.getFilePath();
            String filePath = new String(Base64.getDecoder().decode(encodedFilePath));

            // Construct the main file path
            Path mainFilePath = Path.of(FILE_SOURCE, filePath);

// Construct the paths for the subfolders (e.g., temp/100 and temp/200)
            String baseFileName = Path.of(filePath).getFileName().toString(); // Extract the file name (e.g., image.png)
            Path temp100Path = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get(),ThumbnailVariant.L.path(), baseFileName);
            Path temp200Path = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get(),ThumbnailVariant.M.path(), baseFileName);

            try {
                // Delete the file from the main path
                Files.deleteIfExists(mainFilePath);

                // Delete the file from the temp/100 path
                Files.deleteIfExists(temp100Path);

                // Delete the file from the temp/200 path
                Files.deleteIfExists(temp200Path);

            } catch (IOException e) {
                log.error("Failed to delete file from one or more paths", e);
                // Handle the exception as needed
            }

            // Delete the banner from the database
            bannerRepo.deleteById(id);

            return true; // Indicate successful deletion
        } else {
            return false; // Banner not found
        }
    }

}

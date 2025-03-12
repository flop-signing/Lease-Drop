package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.config.file.FilePath;
import com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils;
import com.bedatasolutions.leaseDrop.config.file.ThumbnailVariant;
import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.controllers.BannerController;
import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dto.BannerDto;
import com.bedatasolutions.leaseDrop.dto.FileInfoDto;
import com.bedatasolutions.leaseDrop.repo.BannerRepo;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public String upload(MultipartFile file, Integer duration) throws IOException {
        if (!file.isEmpty()) {

            String originalFileName = file.getOriginalFilename();
            // Define the full file storage path
            Path filePath = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_PHOTO.get());

            // Process and save the file
            MultipartFileUtils.processFile(file, filePath.toFile().getAbsolutePath(), true);



            // Construct the relative file path for accessing the image
            String filePathEE = Path.of(FilePath.USERS.get(), FilePath.USER_PHOTO.get(), originalFileName).toString();
            String encodedFilePath= urlEncode(filePathEE);

////             Generate image URL (assuming you have some method to generate this URL)
//            String imgUrl = MvcUriComponentsBuilder.fromMethodName(
//                    BannerController.class, "getImage", ThumbnailVariant.M.name(), urlEncode(filePathEE)
//            ).build().toString();

            // Save banner info to database
            BannerDao bannerDao = new BannerDao();
            bannerDao.setActionKey(ActionType.CREATE);

            bannerDao.setFileName(originalFileName);
            bannerDao.setFileSize(FileUtils.byteCountToDisplaySize(file.getSize()));
            bannerDao.setDuration(duration);
            bannerDao.setFilePath(encodedFilePath);
            bannerDao.setFileType(ThumbnailVariant.M.name());
//            bannerDao.setImageUrl(imgUrl);

            bannerRepo.save(bannerDao);
            return originalFileName;
        }
        return "No file uploaded";
    }



    public Map<String, Object> getAllImages(Integer page, Integer size, String field, String direction) throws Exception {
        // Handle null and invalid input values
        if (page == null || page < 1) page = 1; // Default to page 1
        if (size == null || size <= 0) size = 10; // Default to size 10
        if (field == null || field.trim().isEmpty()) field = "fileName"; // Default to `fileName`
        if (direction == null || direction.trim().isEmpty()) direction = "desc"; // Default to "desc"

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
            // Use DATABASE SORTING for `fileName` & `createdAt`
            String sortField = field.equals("filename") ? "tx_file_name" : field.equals("createdat") ? "createdAt" : "id";
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

    /**
     * Extracts numeric file size from a string (e.g., "36 KB" -> 36).
     */
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
        String cmpPath = new String(Base64.getDecoder().decode(path));
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
    public String update(BannerDto bannerDto) {
        BannerDao existingBanner = bannerRepo.findById(bannerDto.id()).orElse(null);

        if (existingBanner != null) {
            // Update the fields of the existing banner

            existingBanner.setDuration(bannerDto.duration());


           existingBanner.setActionKey(ActionType.UPDATE);

            // Save the updated banner back to the database
            bannerRepo.save(existingBanner);
            return "Banner updated successfully with ID: " + bannerDto.id();
        } else {
            return "Banner not found with ID: " + bannerDto.id();
        }
    }

    @Transactional
    public String delete(Integer id) {
        if (bannerRepo.existsById(id)) {
            bannerRepo.deleteById(id);  // Deletes the banner
            return "Banner deleted successfully with ID: " + id;
        } else {
            return "Banner not found with ID: " + id;
        }
    }



    // Sorting

    // Sorting Method
/*
    public List<FileInfoDto> getAllImagesWithSorting(String field,String direction) throws Exception {
        // Retrieve all banner records from the database, sorted by the provided field
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        List<BannerDao> banners = bannerRepo.findAll(Sort.by(sortDirection, field));

        // Initialize the list to hold the FileInfoDto objects
        List<FileInfoDto> fileInfos = new ArrayList<>();

        // Process each banner entry
        for (BannerDao banner : banners) {
            try {
                // Check if the fileName is null or empty before proceeding
                if (banner.getFileName() == null || banner.getFileName().isEmpty()) {
                    log.warn("Skipping banner with ID: {} because the file name is null or empty", banner.getId());
                    continue;  // Skip this banner and move to the next
                }

                // Construct the relative file path for accessing images
                String filePathEE = Path.of(FilePath.USERS.get(), FilePath.USER_PHOTO.get(), banner.getFileName()).toString();

                // Generate image URL with M variant (medium thumbnail)
                String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                        BannerController.class, "getImage", ThumbnailVariant.M.name(), urlEncode(filePathEE)
                ).build().toString();

                // Create FileInfoDto with details from BannerDao
                FileInfoDto fileInfo = new FileInfoDto(
                        banner.getId(),
                        imgUrl,
                        banner.getFileName(),
                        banner.getFileSize(),
                        banner.getDuration(),
                        banner.getCreatedAt() // Assuming you want to include createdAt for the image
                );

                fileInfos.add(fileInfo); // Add the FileInfoDto to the list
            } catch (Exception e) {
                log.error("Error processing banner with file name: {}", banner.getFileName(), e);
            }
        }

        return fileInfos; // Return the list of FileInfoDto objects
    }*/



    // Pagination
/*

    public Page<FileInfoDto> getAllImagesWithPagination(int page, int size) throws Exception {
        // Create a Pageable object for pagination (no sorting)
        Pageable pageable = PageRequest.of(page, size);

        // Retrieve a page of banner records from the database (no sorting)
        Page<BannerDao> bannerPage = bannerRepo.findAll(pageable);

        // Initialize the list to hold the FileInfoDto objects
        List<FileInfoDto> fileInfos = new ArrayList<>();

        // Process each banner entry in the page
        for (BannerDao banner : bannerPage.getContent()) {
            try {
                // Check if the fileName is null or empty before proceeding
                if (banner.getFileName() == null || banner.getFileName().isEmpty()) {
        //            log.warn("Skipping banner with ID: {} because the file name is null or empty", banner.getId());
                    continue;  // Skip this banner and move to the next
                }

                // Construct the relative file path for accessing images
                String filePathEE = Path.of(FilePath.USERS.get(), FilePath.USER_PHOTO.get(), banner.getFileName()).toString();

                // Generate image URL with M variant (medium thumbnail)
                String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                        BannerController.class, "getImage", ThumbnailVariant.M.name(), urlEncode(filePathEE)
                ).build().toString();

                // Create FileInfoDto with details from BannerDao
                FileInfoDto fileInfo = new FileInfoDto(
                        banner.getId(),
                        imgUrl,
                        banner.getFileName(),
                        banner.getFileSize(),
                        banner.getDuration(),
                        banner.getCreatedAt() // Assuming you want to include createdAt for the image
                );

                fileInfos.add(fileInfo); // Add the FileInfoDto to the list
            } catch (Exception e) {
                log.error("Error processing banner with file name: {}", banner.getFileName(), e);
            }
        }

        // Return the Page of FileInfoDto objects
        return new PageImpl<>(fileInfos, pageable, bannerPage.getTotalElements());
    }*/


}

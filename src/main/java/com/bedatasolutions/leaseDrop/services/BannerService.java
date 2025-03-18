package com.bedatasolutions.leaseDrop.services;

import com.bedatasolutions.leaseDrop.config.file.FilePath;
import com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils;
import com.bedatasolutions.leaseDrop.config.file.ThumbnailVariant;
import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.controllers.BannerController;
import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dto.BannerDto;
import com.bedatasolutions.leaseDrop.dto.rest.RestPage;
import com.bedatasolutions.leaseDrop.dto.rest.RestPageResponse;
import com.bedatasolutions.leaseDrop.dto.rest.RestSort;
import com.bedatasolutions.leaseDrop.repo.BannerRepo;
import com.bedatasolutions.leaseDrop.utils.ClassMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils.urlDecode;
import static com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils.urlEncode;

@Service
public class BannerService {
    @Value("${app.server.file.root.path}")
    private String FILE_SOURCE;

    private static final Logger log = LoggerFactory.getLogger(BannerService.class);
    private final BannerRepo bannerRepo;
    private final Map<String, Class<?>> COLUMN_TYPE_MAP;


    public BannerService(BannerRepo bannerRepo) {
        this.bannerRepo = bannerRepo;
        this.COLUMN_TYPE_MAP = ClassMapper.buildColumnTypeMap(BannerDao.class);
    }


    @Transactional
    public BannerDto upload(MultipartFile file, Integer duration) throws IOException {
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            // Define the full file storage path
            Path filePath = Path.of(FILE_SOURCE, FilePath.BANNER.get(), FilePath.BANNER_GALLERY.get());

            // Process and save the file
            MultipartFileUtils.processFile(file, filePath.toFile().getAbsolutePath(), true);

            // Construct the relative file path for accessing the image
            String filePathEE = Path.of(FilePath.BANNER.get(),  FilePath.BANNER_GALLERY.get(), originalFileName).toString();
            String encodedFilePath = urlEncode(filePathEE);

            String size = FileUtils.byteCountToDisplaySize(file.getSize());


            // Save banner info to database
            BannerDao bannerDao = new BannerDao();
            bannerDao.setActionKey(ActionType.CREATE);
            bannerDao.setFileName(originalFileName);
            bannerDao.setFileSize(size);
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
            return new BannerDto(
                    bannerDao.getId(),
                    LocalDateTime.now().toLocalDate(),  // createdAt
                    duration,
                    originalFileName,
                    size,  // fileSize
                    imgUrl
            );
        }
        return new BannerDto(
                null,  // duration
                null,  // createdAt (placeholder value)
                0,  // fileName (file not selected)
                null,  // fileSize (no size available)
                null,  // id (no id available)
                "Error uploading file"  // url (error message instead of a URL)
        );
    }


    public RestPageResponse<BannerDao,BannerDto> getAllBanners(RestPage page, RestSort sort,
                                                               Map<String, String> filters) {
        // Define sorting direction
        Sort sortE = sort.direction().equalsIgnoreCase("asc")
                ? Sort.by(sort.field()).ascending() : Sort.by(sort.field()).descending();

        // Create a PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(page.pageNumber()-1, page.size(), sortE);


        // Convert filter values to their appropriate types dynamically
        Map<String, Object> typedFilters = filters.entrySet().stream()
                .filter(entry ->
                        entry.getValue() != null
                                && !entry.getValue().isEmpty()
                                && COLUMN_TYPE_MAP.containsKey(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> ClassMapper.convertValue(entry.getValue(), COLUMN_TYPE_MAP.get(entry.getKey()))
                ));




        // Create the dynamic specification using the filters map
        Specification<BannerDao> spec = ClassMapper.createSpecification(typedFilters);

        // Fetch the customers with pagination and sorting, applying the filters
        Page<BannerDao> bannerPage = bannerRepo.findAll(spec, pageRequest);

        // Convert the BannerDao to BannerDto and add imgUrl to each DTO
        List<BannerDto> bannerDtos = bannerPage.getContent().stream()
                .map(bannerDao -> {
                    BannerDto bannerDto = daoToDto(bannerDao); // Convert DAO to DTO
                    // Get the id and encoded file path from BannerDao
                    Integer id = bannerDto.id();
                    BannerDao banner = bannerRepo.findById(id).orElse(null);
                    if (banner != null) {
                        String encodedFilePath = banner.getFilePath();

                        // Construct the image URL
                        String imgUrl = MvcUriComponentsBuilder.fromMethodName(
                                BannerController.class, "getImage", ThumbnailVariant.M.name(), encodedFilePath
                        ).build().toString();

                        // Set the imgUrl in BannerDto
                        bannerDto= bannerDto.url(imgUrl);
                    }
                    return bannerDto;
                })
                .collect(Collectors.toList());



//        // Log the response
//        log.info("Fetched Banners - Total Banners: {}, Page: {}, Banners: {}",
//                bannerPage.getTotalElements(), page.pageNumber(), bannerDtos);


        return new RestPageResponse<>(bannerDtos, bannerPage);
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
    public BannerDto update(BannerDto bannerDto) {
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
            return new BannerDto(
                    existingBanner.getId(),
                    existingBanner.getCreatedAt().toLocalDate(),
                    existingBanner.getDuration(),
                    existingBanner.getFileName(),
                    existingBanner.getFileSize(),
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
            Path temp100Path = Path.of(FILE_SOURCE, FilePath.BANNER.get(), FilePath.BANNER_GALLERY.get(),ThumbnailVariant.L.path(), baseFileName);
            Path temp200Path = Path.of(FILE_SOURCE, FilePath.BANNER.get(), FilePath.BANNER_GALLERY.get(),ThumbnailVariant.M.path(), baseFileName);

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


    public BannerDto daoToDto(BannerDao bannerDao) {
        return new BannerDto(
                bannerDao.getId(),
                bannerDao.getCreatedAt().toLocalDate(),
                bannerDao.getDuration(),
                bannerDao.getFileName(),
                bannerDao.getFileSize(),
                null

        );
    }

    // Convert DTO to DAO
    public BannerDao dtoToDao(BannerDto bannerDto, BannerDao bannerDao) {

        bannerDao.setDuration(bannerDto.duration());
        bannerDao.setFileName(bannerDto.fileName());
        bannerDao.setFileSize(bannerDto.fileSize());
        bannerDao.setCreatedAt(bannerDto.createdAt().atStartOfDay());
        return bannerDao;
    }


}

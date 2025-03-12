/*
package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.config.file.FilePath;
import com.bedatasolutions.leaseDrop.config.file.ThumbnailVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.bedatasolutions.leaseDrop.config.file.MultipartFileUtils.*;
import static java.util.function.Predicate.not;

@RestController
@RequestMapping(value = "/api/document_server", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FileController {

    @Value("${itbd.server.filesource}")
    protected String FILE_SOURCE;

    @Autowired
    private FileProcessingService fileProcessingService;

    @PostMapping
    public ResponseEntity<FileResponse> uploadDocuments(
            @RequestParam(name = "files") List<MultipartFile> files,
            @RequestParam(name = "groupId") Long groupId
//            , @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        files.stream().filter(not(MultipartFile::isEmpty)).forEach(byt -> {
            Path filePath = Path.of(FILE_SOURCE, FilePath.USERS.get(), groupId.toString(), FilePath.USER_PHOTO.get());
            try {
                processFile(byt, filePath.toFile().getAbsolutePath(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return new ResponseEntity<>(new FileResponse("hell", UUID.randomUUID().toString()), HttpStatus.CREATED);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDocuments(@RequestParam String path) {
        String cmpPath = urlDecode(path);
        Path filePath = Path.of(FILE_SOURCE, FilePath.USERS.get(), FilePath.USER_DOCUMENT.get());
//        Path filePath = Path.of(FILE_SOURCE, FilenameUtils.getPath(cmpPath), ThumbnailVariant.valueOf("LOW").path(), FilenameUtils.getName(cmpPath));
        try {
            List<GallerySourceModel> galleryImgLs = new ArrayList<>();
            try (Stream<Path> dirPath = Files.walk(filePath, 1)) {
                dirPath.filter(Files::isDirectory).forEach(dir -> {
                    try (Stream<Path> filePathE = Files.walk(dir, 1)) {
                        List<FileInfo> fileInfos = filePathE.filter(Files::isRegularFile).map(Path::toFile).map(fileE -> {
                            try {
                                BasicFileAttributes attr = Files.readAttributes(fileE.toPath(), BasicFileAttributes.class);
                                FileTime fileTime = attr.creationTime();
                                LocalDateTime fileCDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(fileTime.toMillis()), ZoneId.systemDefault());

                                String filePathEE = Path.of(FilePath.USERS.get(), fileE.getName(), fileE.getName()).toString();
                                String imgUrl = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getImage", ThumbnailVariant.M.name(), urlEncode(filePathEE)).build().toString();
                                return FileInfo.builder().name(fileE.getName()).url(imgUrl).dateTime(fileCDate)
                                        .size(FileUtils.byteCountToDisplaySize(fileE.length())).build();
                            } catch (IOException e) {
                                log.error("Could not read the file!\nError: {}", e.getMessage());
                            }
                            return null;
                        }).toList();
                        galleryImgLs.add(GallerySourceModel.builder().groupName(dir.toFile().getName()).files(fileInfos).build());
                    } catch (Exception ignored) {
                    }
                });
            }
            return new ResponseEntity<>(galleryImgLs, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Login unsuccessful: [{}]", e.toString());
            return new ResponseEntity<>("", HttpStatus.PRECONDITION_FAILED);
        }
    }

    @GetMapping(value = "/images", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getImage(@RequestParam String type, @RequestParam String path) {
        String cmpPath = urlDecode(path);
        Path fileExPath = Path.of(FILE_SOURCE, FilenameUtils.getPath(cmpPath), ThumbnailVariant.valueOf(type).path(), FilenameUtils.getName(cmpPath));
        return fileProcessingService.fileResponse(fileExPath.toFile());
    }

    @GetMapping(value = "/byte")
    public ResponseEntity<?> getByte(@RequestParam String path)
            throws IOException {
        String cmpPath = urlDecode(path);
        Path fileExPath = Path.of(FILE_SOURCE, cmpPath);
        File file = fileExPath.toFile();
        if (file.exists())
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(FileUtils.readFileToByteArray(file));
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
*/
/*
    public record FileResponse(String fileName, String uuid) {
    }*//*

//
//    @Builder
//    public record GallerySourceModel(
//            String groupName,
//            @JsonIgnore
//            File groupDir,
//            @JsonIgnore
//            FileInfo groupDirInfo,
//            List<FileInfo> files
//    ) {
//    }

*/
/*    @Builder
    @JsonPropertyOrder({ //
            "url", //
            "name", //
            "size", //
            "dateTime",//
    })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FileInfo(
            String url,
            String name,
            String size,
            @JsonIgnore
            String fullPath,
            @JsonIgnore
            String path,
            LocalDateTime dateTime
    ) {
    }*//*

}
*/

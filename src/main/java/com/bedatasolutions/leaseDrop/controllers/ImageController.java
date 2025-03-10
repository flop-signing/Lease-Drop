/*
package com.bedatasolutions.leaseDrop.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ImageController {

    // Folder path inside 'static/photos'
    private final String IMAGE_DIRECTORY = "static/photos";

    @GetMapping("/api/images")
    public List<Map<String, Object>> getImages() {
        List<Map<String, Object>> response = new ArrayList<>();

        // Using ClassPathResource to access files from the resources folder
        try {
            // Get the images folder from the resources
            File folder = new ClassPathResource(IMAGE_DIRECTORY).getFile();
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    // Check if the file is an image (ends with .jpg, .jpeg, or .png)
                    if (file.isFile() && isImageFile(file.getName())) {
                        // Prepare response with relative URL for static content
                        Map<String, Object> imageDetails = Map.of(
                                "imageUrl", "/photos/" + file.getName(),
                                "imageDuration", 120, // Static duration, you can adjust this
                                "imageName", file.getName().substring(0, file.getName().lastIndexOf('.'))
                        );
                        response.add(imageDetails);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle the error appropriately in production
        }

        return response;
    }

    // Helper method to check if the file is a valid image type
    private boolean isImageFile(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
}
*/

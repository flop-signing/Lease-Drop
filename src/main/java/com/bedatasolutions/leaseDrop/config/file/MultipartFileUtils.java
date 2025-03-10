package com.bedatasolutions.leaseDrop.config.file;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

public class MultipartFileUtils {
    public static String urlEncode(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String urlDecode(String value) {
        return new String(Base64.getUrlDecoder().decode(value));
    }

    public static void processFile(MultipartFile byt, String filePath, boolean multipartFileName) throws IOException {
        File file = multipartFileName ? Path.of(filePath, byt.getOriginalFilename()).toFile() : new File(filePath);
        if (Optional.ofNullable(byt.getContentType()).orElse("").startsWith("image/")) {
            for (ThumbnailVariant tv : ThumbnailVariant.values()) {
                imageThumbnail(byt.getInputStream(), file, tv);
            }
        } else
            FileUtils.copyInputStreamToFile(byt.getInputStream(), file);
    }


    private static void imageThumbnail(InputStream byt, File file, ThumbnailVariant tv) throws IOException {
        File output = Path.of(file.getParent(), tv.path(), file.getName()).toFile();
        if (tv == ThumbnailVariant.O) {
            FileUtils.copyInputStreamToFile(byt, output);
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(byt).width(tv.width()).outputFormat(FilenameUtils.getExtension(file.getName())).toOutputStream(outputStream);
            FileUtils.writeByteArrayToFile(output, outputStream.toByteArray());
        }
    }

}

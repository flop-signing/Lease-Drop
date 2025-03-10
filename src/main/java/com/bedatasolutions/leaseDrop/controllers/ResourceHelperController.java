package com.bedatasolutions.leaseDrop.controllers;
import com.bedatasolutions.leaseDrop.constants.db.ReportType;
import com.bedatasolutions.leaseDrop.services.reports.tools.WReportBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
public class ResourceHelperController {

    private final WReportBuilder wReportBuilder;

    @Value("classpath:assets/reports/lease_report.jrxml")
    public Resource reportTemplate;

    public ResourceHelperController(WReportBuilder wReportBuilder) {
        this.wReportBuilder = wReportBuilder;
    }


    @GetMapping("/api/resource")
    public byte[] serveFile(String filePath) throws IOException {
        Path path = new File(new String(Base64.getDecoder().decode(filePath.getBytes(StandardCharsets.UTF_8)))).toPath();
        File file = path.toFile();
        if (file.exists()) {
            return FileUtils.readFileToByteArray(file);
        }
        return null;
    }



    @GetMapping(path = "/api/report")
    public ResponseEntity<?> exportReport(@RequestParam("reportType") ReportType reportType, String checksum, String fileName) throws IOException {
        Map<String, Object> parameterLs = new HashMap<>();
        parameterLs.put("checksum", checksum);
        byte[] reportByte = wReportBuilder.reportBuilder(reportTemplate.getFile().getAbsolutePath(), parameterLs, reportType);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "." + reportType.name().toLowerCase() + "\"")
                .body(reportByte);
    }

}

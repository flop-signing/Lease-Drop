package com.bedatasolutions.leaseDrop.controllers;

import com.bedatasolutions.leaseDrop.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;



    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Endpoint to generate a PDF report.
     */
    @PostMapping("/pdf")
    public ResponseEntity<byte[]> generatePdfReport(@RequestParam String templateFileName, @RequestBody Map<String, Object> parameters) {
        byte[] report = reportService.generatePdfReport(templateFileName, parameters);

        // Returning the PDF as a response to Postman
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.pdf")
                .header("Custom-Message", "PDF generated and saved successfully") // Optional header
                .body(report);
    }

    /**
     * Endpoint to generate an Excel report.
     */
    @PostMapping("/excel")
    public ResponseEntity<byte[]> generateExcelReport(@RequestParam String templateFileName, @RequestBody Map<String, Object> parameters) {
        byte[] report = reportService.generateExcelReport(templateFileName, parameters);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.xlsx")
                .header("Custom-Message", "EXCEL generated and saved successfully") // Optional header
                .body(report);
    }

    /**
     * Endpoint to generate a Word report.
     */
    @PostMapping("/docx")
    public ResponseEntity<byte[]> generateDocxReport(@RequestParam String templateFileName, @RequestBody Map<String, Object> parameters) {
        byte[] report = reportService.generateDocxReport(templateFileName, parameters);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.docx")
                .header("Custom-Message", "DOCX generated and saved successfully") // Optional header
                .body(report);
    }

    /**
     * Endpoint to generate the HTML report.
     */
    @PostMapping("/html")
    public ResponseEntity<String> generateHtmlReport(@RequestParam String templateFileName, @RequestBody Map<String, Object> parameters) {
        String report = reportService.generateHtmlReport(templateFileName, parameters);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.html")
                .header("Custom-Message", "HTML generated and saved successfully") // Optional header
                .body(report);
    }
}

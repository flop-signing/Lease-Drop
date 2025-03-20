package com.bedatasolutions.leaseDrop.services;


import com.bedatasolutions.leaseDrop.constants.db.ReportType;
import com.bedatasolutions.leaseDrop.controllers.ReportController;
import com.bedatasolutions.leaseDrop.services.reports.tools.WReportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class ReportService {

    private final WReportBuilder reportBuilder;
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    // Injecting template path from application.yml
    @Value("${app.server.file.root.path}")
    private String templatePath;

    public ReportService(WReportBuilder reportBuilder) {
        this.reportBuilder = reportBuilder;
    }

    /**
     * Generate a report in PDF format.
     */
//    public byte[] generatePdfReport(String templateFileName, Map<String, Object> parameters) {
//        // Prepending the base template path to the template file name
//        String fullTemplatePath = templatePath + templateFileName;
//        logger.info(fullTemplatePath);
//
//        return reportBuilder.reportBuilder(fullTemplatePath, parameters, ReportType.PDF);
//    }

    /**
     * Generate a PDF report.
     */
    public byte[] generatePdfReport(String templateFileName, Map<String, Object> parameters) {
        // Prepending the base template path to the template file name
        String fullTemplatePath = templatePath + templateFileName;

        // Logging the full path for debugging
        logger.info("Full Template Path: " + fullTemplatePath);

        // Generate the PDF report
        byte[] report = reportBuilder.reportBuilder(fullTemplatePath, parameters, ReportType.PDF);

        // Define the file path where the PDF will be saved
        Path pdfPath = Paths.get(fullTemplatePath.replace(".jrxml", ".pdf"));

        try {
            // Create necessary directories if they don't exist
            Files.createDirectories(pdfPath.getParent());

            // Write the generated PDF to the specified location
            Files.write(pdfPath, report);

            // Log successful PDF saving
            logger.info("PDF saved at: " + pdfPath.toString());
        } catch (IOException e) {
            logger.error("Error while saving PDF file", e);
        }

        // Returning the report as a response for Postman
        return report;
    }

    /**
     * Generate a report in Excel format and save the file.
     */
    public byte[] generateExcelReport(String templateFileName, Map<String, Object> parameters) {
        String fullTemplatePath = templatePath + templateFileName;

        // Generate the Excel report
        byte[] report = reportBuilder.reportBuilder(fullTemplatePath, parameters, ReportType.XLSX);

        // Define the file path where the Excel file will be saved
        Path excelPath = Paths.get(fullTemplatePath.replace(".jrxml", ".xlsx"));

        try {
            // Create necessary directories if they don't exist
            Files.createDirectories(excelPath.getParent());

            // Write the generated Excel to the specified location
            Files.write(excelPath, report);

            // Log successful Excel saving
            logger.info("Excel saved at: " + excelPath.toString());
        } catch (IOException e) {
            logger.error("Error while saving Excel file", e);
        }

        return report;
    }

    /**
     * Generate a report in DOCX format and save the file.
     */
    public byte[] generateDocxReport(String templateFileName, Map<String, Object> parameters) {
        String fullTemplatePath = templatePath + templateFileName;

        // Generate the DOCX report
        byte[] report = reportBuilder.reportBuilder(fullTemplatePath, parameters, ReportType.DOCX);

        // Define the file path where the DOCX file will be saved
        Path docxPath = Paths.get(fullTemplatePath.replace(".jrxml", ".docx"));

        try {
            // Create necessary directories if they don't exist
            Files.createDirectories(docxPath.getParent());

            // Write the generated DOCX to the specified location
            Files.write(docxPath, report);

            // Log successful DOCX saving
            logger.info("DOCX saved at: " + docxPath.toString());
        } catch (IOException e) {
            logger.error("Error while saving DOCX file", e);
        }

        return report;
    }

    /**
     * Generate the HTML representation of the report and save the file.
     */
    public String generateHtmlReport(String templateFileName, Map<String, Object> parameters) {
        String fullTemplatePath = templatePath + templateFileName;

        // Generate the HTML representation of the report
        String report = reportBuilder.getHtml(fullTemplatePath, parameters, "");

        // Define the file path where the HTML file will be saved
        Path htmlPath = Paths.get(fullTemplatePath.replace(".jrxml", ".html"));

        try {
            // Create necessary directories if they don't exist
            Files.createDirectories(htmlPath.getParent());

            // Write the generated HTML to the specified location
            Files.write(htmlPath, report.getBytes());

            // Log successful HTML saving
            logger.info("HTML saved at: " + htmlPath.toString());
        } catch (IOException e) {
            logger.error("Error while saving HTML file", e);
        }

        return report;
    }
}

//package com.bedatasolutions.leaseDrop.controllers;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itbd.docreader.constants.ReportType;
//import com.itbd.docreader.dao.ClassifierDao;
//import com.itbd.docreader.dto.ClassifierDto;
//import com.itbd.docreader.dto.process.RuleDto;
//import com.itbd.docreader.dto.response.AiResponse;
//import com.itbd.docreader.repo.ClassifierRepo;
//import com.itbd.docreader.service.ContractService;
//import com.itbd.docreader.service.reports.WReportBuilder;
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
//public class DbController {
//    private final ClassifierRepo classifierRepo;
//    private final ContractService contractService;
//    private final WReportBuilder wReportBuilder;
//
//    @Value("classpath:prompts/extracted_data.json")
//    public Resource rules;
//
//    @Value("classpath:assets/reports/lease_report.jrxml")
//    public Resource reportTemplate;
//
//    public DbController(ClassifierRepo classifierRepo, ContractService contractService, WReportBuilder wReportBuilder) {
//        this.contractService = contractService;
//        this.classifierRepo = classifierRepo;
//        this.wReportBuilder = wReportBuilder;
//    }
//
//    @PutMapping
//    public void saveDb() throws IOException {
//        String ruleContent = FileUtils.readFileToString(rules.getFile(), StandardCharsets.UTF_8);
//        ObjectMapper mapper = new ObjectMapper();
//        List<RuleDto> classifiers = mapper.readValue(ruleContent, new TypeReference<List<RuleDto>>() {
//        });
//        List<ClassifierDao> classifierLs = classifiers.stream().map(item -> {
//            ClassifierDao classifier = new ClassifierDao();
//            classifier.setName(item.name());
//            classifier.setDescription(item.description());
//            return classifier;
//        }).toList();
////        classifierRepo.saveAll(classifierLs);
//    }
//
//    @GetMapping(path = "/api/categories")
//    public List<ClassifierDto> getClassifiers() {
////        List<ClassifierDto> classifiers = classifierRepo.findAll()
//        List<ClassifierDto> classifiers = classifierRepo.findAllByIsActiveTrue()
//                .stream()
//                .map(item -> new ClassifierDto(item.getId(), item.isActive(), item.getName(), item.getGroupKey(), item.getDescription(), item.getType().name()))
//                .toList();
//        return classifiers;
//    }
//
//    @PostMapping(path = "/api/process_init")
//    public AiResponse startProcess(@RequestParam("document") MultipartFile document, Boolean paidUser) throws IOException {
//        try (InputStream inputStream = document.getInputStream()) {
//            return contractService.start(inputStream.readAllBytes(), document.getOriginalFilename(), paidUser);
//        }
//    }
//
//    @GetMapping(path = "/api/report")
//    public ResponseEntity<?> exportReport(@RequestParam("reportType") ReportType reportType, String checksum, String fileName) throws IOException {
////        String fileName = "hello." + reportType.name().toLowerCase();
//        Map<String, Object> parameterLs = new HashMap<>();
//        parameterLs.put("checksum", checksum);
//        byte[] reportByte = wReportBuilder.reportBuilder(reportTemplate.getFile().getAbsolutePath(), parameterLs, reportType);
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
////                        .body(new UrlResource(file.toURI()));
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "." + reportType.name().toLowerCase() + "\"")
//                .body(reportByte);
//    }
//}

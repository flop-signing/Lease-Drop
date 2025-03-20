//package com.bedatasolutions.leaseDrop.services.reports.tools.reports;
//
//
//import com.itbd.docreader.constants.ReportType;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.design.JasperDesign;
//import net.sf.jasperreports.engine.export.HtmlExporter;
//import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
//import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
//import net.sf.jasperreports.engine.xml.JRXmlLoader;
//import net.sf.jasperreports.export.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.StringWriter;
//import java.nio.file.Path;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class WReportBuilder {
//    private final String fileSource;
//    private final DataSource dataSource;
//
//    //    public WReportBuilder(@Value("${desoft.server.filesource}") String filesource, @Qualifier("dbds1") DataSource dataSource) {
//    public WReportBuilder(@Value("${app.server.file.root.path}") String fileSource, @Autowired DataSource dataSource) {
//        this.fileSource = fileSource;
//        this.dataSource = dataSource;
//    }
//
//    private Map<String, Object> projectParameters() {
//        Map<String, Object> parameters = new HashMap<>();
////        parameters.put("orgName", club.getCompany());
////        parameters.put("orgAddress", club.getHoAddress());
//        return parameters;
//    }
//
//    private JasperReport templateInit(String templatePath) throws JRException {
////        File templateFile = Path.of(fileSource, FilePath.GALLERY.get(), templatePath).toFile();
//        File templateFile = Path.of(templatePath).toFile();
//        JasperDesign jasperDesign = JRXmlLoader.load(templateFile);
//        JasperReport report = JasperCompileManager.compileReport(jasperDesign);
//        return report;
//    }
//
//    public byte[] getByteStream(String templatePath, Map<String, Object> parameterLs, String fileName) {
//        Connection connection = null;
//        try {
//            connection = dataSource.getConnection();
//            parameterLs.putAll(projectParameters());
//            return JasperRunManager.runReportToPdf(templateInit(templatePath), parameterLs, connection);
//        } catch (JRException | SQLException ex) {
//            log.error("Cant print voter list: [{}]", ex.toString());
//        } finally {
//            if (connection != null)
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    log.trace("Unable to close connection: [{}]", e.toString());
//                }
//        }
//        return null;
//    }
//
//    public void reportDialog(String templatePath, String fileName, Map<String, Object> parameterLs, String title) {
//        try (Connection connection = dataSource.getConnection()) {
//            parameterLs.putAll(projectParameters());
//            JasperPrint jasperPrint = JasperFillManager.fillReport(templateInit(Path.of(templatePath, fileName).toFile().getPath()), parameterLs, connection);
////            new JasperViewerFX().viewReport(title, jasperPrint);
//
////            FxControllerAndView<ReportView, BorderPane> meterMVC = fxWeaver.load(ReportView.class);
////            ReportView meterDialog = meterMVC.getController();
////            meterDialog.viewReport(title, jasperPrint);
//        } catch (Exception ex) {
//            log.error("Cant print voter list: [{}]", ex, ex);
//        }
//    }
//
//
//    public String getHtml(String templatePath, Map<String, Object> parameterLs, String fileName) {
//        Connection connection = null;
//        try {
//            connection = dataSource.getConnection();
//            parameterLs.putAll(projectParameters());
//            JasperPrint jasperPrint = JasperFillManager.fillReport(templateInit(templatePath), parameterLs, connection);
////            JasperPrint jasperPrint = JasperFillManager.fillReport(templateInit(templatePath), parameterLs, new JREmptyDataSource());
//
//            StringWriter stringWriter = new StringWriter();
//            HtmlExporter exporter = new HtmlExporter();
//            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//            exporter.setExporterOutput(new SimpleHtmlExporterOutput(stringWriter));
//            exporter.exportReport();
//            String htmlContent = stringWriter.toString();
//            return htmlContent;
//        } catch (JRException | SQLException ex) {
//            log.error("Cant print voter list: [{}]", ex.toString(), ex);
//        } finally {
//            if (connection != null)
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    log.trace("Unable to close connection: [{}]", e.toString());
//                }
//        }
//        return null;
//    }
//
//    public byte[] reportBuilder(String templatePath, Map<String, Object> parameterLs, ReportType format) {
//        Connection connection = null;
//        try {
//            connection = dataSource.getConnection();
//            parameterLs.putAll(projectParameters());
//            JasperPrint jasperPrint = JasperFillManager.fillReport(templateInit(templatePath), parameterLs, connection);
////            String format = "pdf";
//            byte[] reportContent = switch (format) {
//                case ReportType.PDF -> JasperExportManager.exportReportToPdf(jasperPrint);
//                case ReportType.XML -> JasperExportManager.exportReportToXml(jasperPrint).getBytes();
//                case ReportType.XLSX -> exportToXlsx(jasperPrint);
//                case ReportType.DOCX -> exportToDocx(jasperPrint);
//                default -> throw new RuntimeException("Unknown report format");
//            };
//            return reportContent;
//        } catch (JRException | SQLException ex) {
//            log.error("Cant print voter list: [{}]", ex.toString(), ex);
//        } finally {
//            if (connection != null)
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    log.trace("Unable to close connection: [{}]", e.toString());
//                }
//        }
//        return null;
//    }
//
//    /**
//     * Export report to Microsoft Excel file
//     *
//     * @return
//     */
//    public byte[] exportToXlsx(JasperPrint jasperPrint) {
//        try {
//            JRXlsxExporter exporter = new JRXlsxExporter();
//            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//            ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
////            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
//            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
//
//            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
//            configuration.setIgnoreCellBackground(true); // Removes background color
//            configuration.setWhitePageBackground(false); // Ensures white background
//            configuration.setIgnoreGraphics(true); // Ignores graphical elements
//
//            configuration.setOnePagePerSheet(false); // Ensures a single continuous sheet
//            configuration.setIgnorePageMargins(true); // Ignores margins to prevent extra space
//            configuration.setRemoveEmptySpaceBetweenRows(true); // Removes unnecessary gaps
//            configuration.setDetectCellType(true); // Detects cell types (e.g., numbers, text)
//            configuration.setCollapseRowSpan(false); // Keeps row spans intact
//
//            exporter.setConfiguration(configuration);
//            exporter.exportReport();
//            return xlsReport.toByteArray();
//        } catch (JRException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * Export report to Microsoft Excel file
//     *
//     * @return
//     */
//    public byte[] exportToDocx(JasperPrint jasperPrint) {
//        try {
//            JRDocxExporter exporter = new JRDocxExporter();
////            JRXlsxExporter exporter = new JRXlsxExporter();
//            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//            ByteArrayOutputStream docsReport = new ByteArrayOutputStream();
////            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
//            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(docsReport));
//
//            SimpleDocxReportConfiguration configuration = new SimpleDocxReportConfiguration();
//            configuration.setFlexibleRowHeight(true);  // Ensures rows adjust properly
//            configuration.setFramesAsNestedTables(false); // Prevents breaking table structure
//            configuration.setIgnoreHyperlink(true); // Avoids issues with hyperlinks in tables
//
//            exporter.setConfiguration(configuration);
//            exporter.exportReport();
//            return docsReport.toByteArray();
//        } catch (JRException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
////
////    public MyDialog processReportDialog(Integer projectKey, final String templatePath, final String fileName, Map<String, Object> parameterLs, boolean isQueryAccess) {
////        PdfViewer pdfViewer = new PdfViewer();
////        pdfViewer.setSrc(getStreamResource(projectKey, templatePath, parameterLs, fileName));
////        pdfViewer.setSizeFull();
////
////        FormLayout ff = queryPane(projectKey, templatePath, pdfViewer, fileName, isQueryAccess);
////        ff.setSizeFull();
////        SplitLayout splitLayout = new SplitLayout(pdfViewer, ff);
////        splitLayout.setSplitterPosition(85);
////        splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
////        splitLayout.setSizeFull();
////
////        MyDialog dialog = new MyDialog();
////        dialog.setWidth(80, Unit.PERCENTAGE);
////        dialog.setHeight(95, Unit.PERCENTAGE);
////        dialog.setHeaderTitle("Generate Report");
//////        dialog.add(sidePane);
////        dialog.add(splitLayout);
//////        dialog.getFooter().add(new Button(anchor));
////        return dialog;
////    }
////
////
////    private FormLayout queryPane(Integer projectKey, String templatePath, PdfViewer pdfViewer, String fileName, boolean isQueryAccess) {
////        Map<String, Object> parameterLs = new HashMap<>();
////        FormLayout query = new FormLayout();
////
////        Anchor anchor = new Anchor(pdfViewer.getSrc(), "Print");
////        anchor.getElement().setAttribute("target", "_blank");
////        anchor.getStyle().set("text-decoration", "none").set("color", "white");
////
////        Button queryBtn = new Button("Query", click -> {
////            pdfViewer.setSrc(getStreamResource(projectKey, templatePath, parameterLs, fileName));
////            anchor.setHref(pdfViewer.getSrc());
////        });
////        queryBtn.setVisible(isQueryAccess);
////        queryBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
////
////        Button printBtn = new Button(anchor);
////        printBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
////
////        query.add(new HorizontalLayout(queryBtn, printBtn));
////        if (isQueryAccess)
////            try {
////                JRParameter[] fields = templateInit(projectKey, templatePath).getParameters();
////                for (JRParameter field : fields) {
////                    if (field.isForPrompting() && !field.isSystemDefined()) {
////                        Class<?> valueClass = field.getValueClass();
////                        if (valueClass.equals(String.class)) {
////                            TextField textField = new TextField();
////                            textField.setTooltipText("Parameter: " + field.getName());
////                            textField.addValueChangeListener(value -> parameterLs.put(field.getName(), value.getValue()));
////                            query.addFormItem(textField, Objects.nonNull(field.getDescription()) ? field.getDescription() : field.getName());
////                        } else if (valueClass.equals(Integer.class) || valueClass.equals(Long.class)) {
////                            IntegerField integerField = new IntegerField();
////                            integerField.setTooltipText("Parameter: " + field.getName());
////                            integerField.addValueChangeListener(value -> parameterLs.put(field.getName(), value.getValue()));
////                            query.addFormItem(integerField, Objects.nonNull(field.getDescription()) ? field.getDescription() : field.getName());
////                        } else if (valueClass.equals(BigDecimal.class)) {
////                            BigDecimalField decimalField = new BigDecimalField();
////                            decimalField.setTooltipText("Parameter: " + field.getName());
////                            decimalField.addValueChangeListener(value -> parameterLs.put(field.getName(), value.getValue()));
////                            query.addFormItem(decimalField, Objects.nonNull(field.getDescription()) ? field.getDescription() : field.getName());
////                        } else if (valueClass.equals(LocalDateTime.class)) {
////                            DateTimePicker decimalField = new DateTimePicker();
////                            decimalField.setTooltipText("Parameter: " + field.getName());
////                            decimalField.addValueChangeListener(value -> parameterLs.put(field.getName(), value.getValue()));
////                            query.addFormItem(decimalField, Objects.nonNull(field.getDescription()) ? field.getDescription() : field.getName());
////                        } else if (valueClass.equals(LocalDate.class)) {
////                            DatePicker decimalField = new DatePicker();
////                            decimalField.setTooltipText("Parameter: " + field.getName());
////                            decimalField.addValueChangeListener(value -> parameterLs.put(field.getName(), value.getValue()));
////                            query.addFormItem(decimalField, Objects.nonNull(field.getDescription()) ? field.getDescription() : field.getName());
////                        }
////                    }
////                }
////            } catch (JRException e) {
////                log.error("Unable to extract parameters: {}", e.toString());
////            }
////        return query;
////    }
//}

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.viettel.utils;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.Iterator;
//
//import java.util.ResourceBundle;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import net.sf.jxls.transformer.XLSTransformer;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
///**
// *
// * @author Ebaymark
// */
//public class ReportUtil {
//
//    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReportUtil.class);
//
//    public static final String exportReport(HttpServletRequest req, Map bean, String templateFile, String destFile)
//            throws IOException {
//        XLSTransformer transformer = new XLSTransformer();
//        transformer.transformXLS(req.getRealPath(templateFile), bean, req.getRealPath(destFile));
//        String linkFile = req.getContextPath() + destFile;
//        req.setAttribute("downloadLinkPath", linkFile);
//
//        return "reportResult";
//    }
//
//    public static void exportReport(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//
//        Date newDate = new Date();
//        String path = req.getRealPath(templateFile);
//        InputStream is = new BufferedInputStream(new FileInputStream(path));
//        XLSTransformer transformer = new XLSTransformer();
//        HSSFWorkbook hm = transformer.transformXLS(is, bean);
//        res.setContentType("application/vnd.ms-excel");
//
//        res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//        res.setHeader("Content-Type", "application/vnd.ms-excel");
//        hm.write(res.getOutputStream());
//        res.getOutputStream().flush();
//        res.getOutputStream().close();
//    }
//
//    //hieptq update 140515
//    public static String exportReportSaveFileTemp(HttpServletRequest req, Map bean, String templateFile)
//            throws IOException {
//
//        Date newDate = new Date();
//        InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//        XLSTransformer transformer = new XLSTransformer();
//        HSSFWorkbook hm = transformer.transformXLS(is, bean);
//        ResourceBundle rb = ResourceBundle.getBundle("config");
//        String filePath = rb.getString("report_excel_temp");
//        FileOutputStream fop = null;
//        File file;
//        String fullFilePath = filePath+"report_"+newDate.getTime()+".xls";
//        file = new File(fullFilePath);
//        fop = new FileOutputStream(file);
//        if (!file.exists()) {
//            file.createNewFile();
//        }
//        hm.write(fop);
//        fop.close();
//        return fullFilePath;
//    }
//
//    public static void exportReportForMerge(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                int maxleng3 = 0;
//                int maxleng4 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 7));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (1110 * (Math.floor(leng / 13) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 4));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (1110 * (Math.floor(leng2 / 12) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//
//                            HSSFCell cell3 = hfRow.getCell((short) (i + 8));
//                            String cellValue3 = cell3.toString();
//                            int leng3 = cellValue3.length() + 1;
//                            maxleng3 = (int) (1110 * (Math.floor(leng3 / 9) + 1));
//                            if (hfRow.getHeight() < maxleng3 && maxleng < maxleng3 && maxleng2 < maxleng3) {
//                                hfRow.setHeight((short) maxleng3);
//                            }
//                            HSSFCell cell4 = hfRow.getCell((short) (i + 9));
//                            String cellValue4 = cell4.toString();
//                            int leng4 = cellValue4.length() + 1;
//                            maxleng4 = (int) (1110 * (Math.floor(leng4 / 11) + 1));
//                            if (hfRow.getHeight() < maxleng4 && maxleng < maxleng4 && maxleng2 < maxleng4 && maxleng3 < maxleng4) {
//                                hfRow.setHeight((short) maxleng4);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//        //res.getOutputStream().close();
//    }
//
//    public static void exportReportGQVB(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 3));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (1245 * (Math.floor(leng / 27)));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//        //res.getOutputStream().close();
//    }
//
//    public static void exportReportTH(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 2));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (1110 * (Math.floor(leng / 30) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportQHXL(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 2));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (915 * (Math.floor(leng / 18) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 4));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (915 * (Math.floor(leng2 / 27) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportInSoDi(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                int maxleng3 = 0;
//                int maxleng4 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 5));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (420 * (Math.floor(leng / 22) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 6));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (420 * (Math.floor(leng2 / 11) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//
//                            HSSFCell cell3 = hfRow.getCell((short) (i + 7));
//                            String cellValue3 = cell3.toString();
//                            int leng3 = cellValue3.length() + 1;
//                            maxleng3 = (int) (420 * (Math.floor(leng3 / 12) + 1));
//                            if (hfRow.getHeight() < maxleng3 && maxleng < maxleng3 && maxleng2 < maxleng3) {
//                                hfRow.setHeight((short) maxleng3);
//                            }
//
//                            HSSFCell cell4 = hfRow.getCell((short) (i + 8));
//                            String cellValue4 = cell4.toString();
//                            int leng4 = cellValue4.length() + 1;
//                            maxleng4 = (int) (420 * (Math.floor(leng4 / 9) + 1));
//                            if (hfRow.getHeight() < maxleng4 && maxleng < maxleng4 && maxleng2 < maxleng4 && maxleng3 < maxleng4) {
//                                hfRow.setHeight((short) maxleng4);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportLoaiVBDi(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 2));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (750 * (Math.floor(leng / 11) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 4));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (750 * (Math.floor(leng2 / 47) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportSendReceiveDoc(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 2));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (1000 * (Math.floor(leng / 20) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 3));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (1000 * (Math.floor(leng2 / 20) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportCG(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 3));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (650 * (Math.floor(leng / 20) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 4));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (650 * (Math.floor(leng2 / 30) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportForFormBorrow(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                int maxleng3 = 0;
//                int maxleng4 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 2));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (1000 * (Math.floor(leng / 10) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 3));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (1000 * (Math.floor(leng2 / 27) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//
//                            HSSFCell cell3 = hfRow.getCell((short) (i + 5));
//                            String cellValue3 = cell3.toString();
//                            int leng3 = cellValue3.length() + 1;
//                            maxleng3 = (int) (1000 * (Math.floor(leng3 / 15) + 1));
//                            if (hfRow.getHeight() < maxleng3 && maxleng < maxleng3 && maxleng2 < maxleng3) {
//                                hfRow.setHeight((short) maxleng3);
//                            }
//
//                            HSSFCell cell4 = hfRow.getCell((short) (i + 6));
//                            String cellValue4 = cell4.toString();
//                            int leng4 = cellValue4.length() + 1;
//                            maxleng4 = (int) (1000 * (Math.floor(leng4 / 15) + 1));
//                            if (hfRow.getHeight() < maxleng4 && maxleng < maxleng4 && maxleng2 < maxleng4 && maxleng3 < maxleng4) {
//                                hfRow.setHeight((short) maxleng4);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//
//    public static void exportReportForBorrowed(HttpServletRequest req, Map bean, String templateFile, HttpServletResponse res)
//            throws IOException {
//        try {
//            Date newDate = new Date();
//            InputStream is = new BufferedInputStream(new FileInputStream(req.getRealPath(templateFile)));
//            XLSTransformer transformer = new XLSTransformer();
//            HSSFWorkbook hm = transformer.transformXLS(is, bean);
//            res.setContentType("application/vnd.ms-excel");
//            res.setHeader("Content-Disposition", "attachment; filename=report_" + newDate.getTime() + ".xls");
//            res.setHeader("Content-Type", "application/vnd.ms-excel");
//            //res.setHeader("Content-Length", String.valueOf(hm.getBytes().length));
//            HSSFSheet sheet = hm.getSheetAt(0);
//            Iterator it = sheet.rowIterator();
//            String s = "";
//            while (it.hasNext()) {
//                HSSFRow hfRow = (HSSFRow) it.next();
//
//                int maxleng = 0;
//                int maxleng2 = 0;
//                int maxleng3 = 0;
//                int maxleng4 = 0;
//                for (int i = 0; i <= hfRow.getLastCellNum(); i++) {
//                    HSSFCell cell = hfRow.getCell((short) i);
//                    sheet.getColumnWidth((short) i);
//                    if (cell != null) {
//                        String cellValue = cell.toString();
//                        cell.getCellNum();
//                        if (cellValue.equals("&TA1")) {
//                            HSSFCell cell1 = hfRow.getCell((short) (i + 2));
//                            String cellValue1 = cell1.toString();
//                            int leng = cellValue1.length() + 1;
//                            maxleng = (int) (1000 * (Math.floor(leng / 10) + 1));
//                            if (hfRow.getHeight() < maxleng) {
//                                hfRow.setHeight((short) maxleng);
//                            }
//
//                            HSSFCell cell2 = hfRow.getCell((short) (i + 3));
//                            String cellValue2 = cell2.toString();
//                            int leng2 = cellValue2.length() + 1;
//                            maxleng2 = (int) (1000 * (Math.floor(leng2 / 27) + 1));
//                            if (hfRow.getHeight() < maxleng2 && maxleng < maxleng2) {
//                                hfRow.setHeight((short) maxleng2);
//                            }
//
//                            HSSFCell cell3 = hfRow.getCell((short) (i + 4));
//                            String cellValue3 = cell3.toString();
//                            int leng3 = cellValue3.length() + 1;
//                            maxleng3 = (int) (1000 * (Math.floor(leng3 / 15) + 1));
//                            if (hfRow.getHeight() < maxleng3 && maxleng < maxleng3 && maxleng2 < maxleng3) {
//                                hfRow.setHeight((short) maxleng3);
//                            }
//                        }
//                    }
//                }
//            }
//            hm.write(res.getOutputStream());
//            //res.setContentLength(hm.getBytes().length);
//            res.getOutputStream().flush();
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//}

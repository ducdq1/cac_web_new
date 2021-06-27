///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.viettel.voffice.DAO.Document;
//
//import com.viettel.utils.Constants;
//import com.viettel.utils.ConvertFileUtils;
//import com.viettel.voffice.BO.Document.Attachs;
//import com.viettel.core.base.DAO.BaseGenericForwardComposer;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import org.zkforge.ckez.CKeditor;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Execution;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.select.annotation.WireVariable;
//import org.zkoss.zul.Iframe;
//import org.zkoss.util.media.AMedia;
//
///**
// *
// * @author giangpn
// */
//@SuppressWarnings({"rawtypes", "serial"})
//public class AttachContentController extends BaseGenericForwardComposer {
//
//    //<editor-fold defaultstate="collapsed" desc="declare controls,variables">
//    @WireVariable
//    CKeditor ckContentAttach;
//    Iframe iframeView;
//    //</editor-fold>
//
//    public AttachContentController() {
//        super();
//    }
//
//    @Override
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//        this.self.setAttribute("controller", this, false);
//        Execution execution = Executions.getCurrent();
//        Attachs att = (Attachs) execution.getArg().get("attachs");
//        if (att != null) {
//            String path = att.getAttachPath() + "\\" + att.getAttachName();
//            File f = new File(path);
//            if (f.exists()) {
//                try {
//                    File htmlFile = new File(att.getAttachPath() + "\\" + att.getAttachName() + ".html");
//                    if (!htmlFile.exists()) {
//                        ConvertFileUtils.docxToHtmlFile(path, att.getAttachName() + ".html");
//                    } 
//                    ByteArrayInputStream mediais = new ByteArrayInputStream(
//                            Files.readAllBytes(FileSystems.getDefault().getPath(att.getAttachPath(),
//                            att.getAttachName() + ".html")));
//                    AMedia amedia = new AMedia(att.getAttachName() + ".html", "html", "text/HTML", mediais);
//                    iframeView.setContent(amedia);
//
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//            } else {
//                showNotify("File không còn tồn tại trên hệ thống!");
//            }
//        }
//    }
//    //<editor-fold defaultstate="collapsed" desc="load data">
//    //</editor-fold>
//}

package com.viettel.voffice.DAO.Ycnk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;

/**
 *
 * @author Linhdx
 */
public class AttachCRUDController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int SAVE = 1;
    private final int SAVE_CLOSE = 2;
    private final int SAVE_COPY = 3;

    @Wire
    private Textbox tbAttachCode, tbAttachName;

    @Wire
    private Label lbTopWarning, lbBottomWarning;
    // Ý kiến lãnh đạo
    @Wire
    private Window windowCRUDAttach;
    private Window parentWindow;
    private Attachs attach;
    private String crudMode;
    @SuppressWarnings("rawtypes")
    private ListModel model;

    // Tep noi dung
    @Wire
    private Vlayout flist;
    @Wire
    private Button btnAttach;
    private List<Media> listMedia;
    private Long ycnkFileId;
    // Attach file
    private List<Attachs> listFileAttach;

    /**
     * linhdx Ham bat dau truoc khi load trang
     *
     * @param page
     * @param parent
     * @param compInfo
     * @return
     */
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
        parentWindow = (Window) arguments.get("parentWindow");
        crudMode = (String) arguments.get("CRUDMode");
        ycnkFileId = (Long) arguments.get("ycnkFileId");
        switch (crudMode) {
            case "CREATE":// Tao moi van ban
                attach = new Attachs();
                break;
            case "UPDATE":// Sua van ban
                attach = (Attachs) arguments.get("attach");
                break;
        }
        attach.setObjectId(ycnkFileId);
        attach.setAttachCat(Constants.OBJECT_TYPE.YCNK_FILE);
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    /**
     * linhdx Ham thuc hien sau khi load form xong
     */
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        listMedia = new ArrayList();
        loadFileAttach(listFileAttach);

    }

    /**
     * linhdx Ham tao doi tuong tu form de luu lai
     *
     * @return
     */
    private Attachs createAttachs() {
        attach.setAttachCode(tbAttachCode.getValue());
        attach.setAttachName(tbAttachName.getValue());
        attach.setIsActive(Constants.Status.ACTIVE);
        return attach;
    }

    private void resetObject() {
        attach = new Attachs();

    }

    private void resetForm() {
        crudMode = "CREATE";
        tbAttachCode.setValue("");
        tbAttachName.setText("");

    }

    /**
     * Validate du lieu
     *
     * @return
     */
    private boolean isValidatedData() {

        if (tbAttachCode.getText().matches("\\s*")) {
            showWarningMessage("Mã file không thể để trống");
            tbAttachCode.focus();
            return false;
        }

        if (tbAttachName.getText().matches("\\s*")) {
            showWarningMessage("Tên file không thể để trống");
            tbAttachName.focus();
            return false;
        }

        return true;
    }

    /**
     * Hien thi canh bao
     *
     * @param message
     */
    protected void showWarningMessage(String message) {
        lbTopWarning.setValue(message);
        lbBottomWarning.setValue(message);
    }

    /**
     * Tao canh bao
     */
    protected void clearWarningMessage() {
        lbTopWarning.setValue("");
        lbBottomWarning.setValue("");
    }

    /**
     * Xu ly phim tat
     *
     * @param keyCode
     */
    public void keyEventHandle(int keyCode) {
        switch (keyCode) {
            case KeyEvent.F6:
                onSave(SAVE);
                break;
            case KeyEvent.F7:
                onSave(SAVE_CLOSE);
                break;
            case KeyEvent.F8:
                onSave(SAVE_COPY);
                break;
        }
    }

    /**
     * linhdx Xu ly su kien luu
     *
     * @param typeSave
     */
    @Listen("onClick = #btnSave, .saveClose")
    public void onSave(int typeSave) {
        clearWarningMessage();
        if (!isValidatedData()) {
            return;
        }

        try {
            if (null != crudMode) {
//                attachDAOHE.saveOrUpdate(attach);
                switch (crudMode) {
                    case "CREATE": {
                        showNotification(String.format(
                                Constants.Notification.SAVE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.ATTACH),
                                Constants.Notification.INFO);
                        break;
                    }
                    case "UPDATE":
                        showNotification(String.format(
                                Constants.Notification.UPDATE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.ATTACH),
                                Constants.Notification.INFO);
                        break;

                }
                //attach = createAttachs();
                // save attach file
                for (Object media : listMedia) {
                    try {
                        saveFileAttach((Media) media, ycnkFileId, null);
                    } catch (IOException ex) {
                        Logger.getLogger(AttachCRUDController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            Events.sendEvent("onGetAttach", parentWindow, attach);
            switch (typeSave) {
                case SAVE:
                    resetForm();
                    break;
                case SAVE_CLOSE:
                    resetForm();
                    onClose();
                    //windowCRUDProduct.onClose();

                    break;
                case SAVE_COPY:
                    break;
            }
        } catch (WrongValueException e) {
        	LogUtils.addLog(e);
            if (null != crudMode) {
                switch (crudMode) {
                    case "CREATE":
                        showNotification(String.format(
                                Constants.Notification.SAVE_ERROR, Constants.DOCUMENT_TYPE_NAME.ATTACH),
                                Constants.Notification.ERROR);
                        break;
                    case "UPDATE":
                        showNotification(String.format(
                                Constants.Notification.UPDATE_ERROR, Constants.DOCUMENT_TYPE_NAME.ATTACH),
                                Constants.Notification.ERROR);
                        break;
                }
            }
        }
    }

    /**
     * Dong cua so khi o dang popup
     */
    @Listen("onClose = #windowCRUDAttach")
    public void onClose() {
        windowCRUDAttach.onClose();
        Events.sendEvent("onVisible", parentWindow, null);
    }

    @Listen("onUpload = #btnAttach")
    public void onUpload(UploadEvent event)  throws UnsupportedEncodingException {
        // final Media media = event.getMedia();
        final Media[] medias = event.getMedias();
        for (int i = 0; i < medias.length; i++) {
            final Media media = medias[i];

            String extFile = FileUtil.getFileExtention(media.getName());
			if (!FileUtil.validFileType(extFile.replace(".", ""))) {
                showNotification("Định dạng file không được phép tải lên",
                        Constants.Notification.WARNING);
                continue;
            }

            // luu file vao danh sach file
            listMedia.add(media);

            // layout hien thi ten file va nut "Xóa"
            final Hlayout hl = new Hlayout();
            hl.setSpacing("6px");
            hl.setClass("newFile");
            hl.appendChild(new Label(media.getName()));
            hl.addEventListener(Events.ON_CLICK,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            //todo:download file
                            Filedownload.save(media, null);
//                            Attachs att = media;
//                            media.
                            //Attachs att = lbAttachs.getSelectedItem().getValue();
//                            String path = att.getAttachPath() + File.separator + att.getAttachId();
//                            File f = new File(path);
//                            if (f.exists()) {
//                                File tempFile = FileUtil.createTempFile(f, att.getAttachName());
//                                Filedownload.save(media, null);
//                            } else {
//                                showNotification("File không còn tồn tại trên hệ thống");
//                            }
                        }
                    });
            A rm = new A("Xóa");
            rm.addEventListener(Events.ON_CLICK,
                    new org.zkoss.zk.ui.event.EventListener() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            hl.detach();
                            // xoa file khoi danh sach file
                            listMedia.remove(media);
                        }
                    });
            hl.appendChild(rm);
            flist.appendChild(hl);
        }
    }

    private void loadFileAttach(List<Attachs> listFileAttach) {
        if (listFileAttach != null) {
            for (final Attachs attach : listFileAttach) {
                // layout hien thi ten file va nut "Xoa"
                final Hlayout hl = new Hlayout();
                hl.setSpacing("6px");
                hl.setClass("newFile");
                hl.appendChild(new Label(attach.getAttachName()));
                A rm = new A("Xóa");
                rm.addEventListener(Events.ON_CLICK,
                        new org.zkoss.zk.ui.event.EventListener() {
                            @Override
                            public void onEvent(Event event) throws Exception {
                                hl.detach();
								// Xoa file khoi he thong file
                                // tam thoi khong can

                                // Set attach deactive
                                attach.setIsActive(Constants.Status.INACTIVE);
                                AttachDAOHE daoHE = new AttachDAOHE();
                                daoHE.saveOrUpdate(attach);
                            }
                        });
                hl.appendChild(rm);
                flist.appendChild(hl);
            }
        }
    }

    private void saveFileAttach(Media media, Long ycnkFileId, String mode) throws IOException {
        // Neu ung dung chua co avatar thi return
        if (media == null) {
            return;
        }

        AttachDAOHE attachDAOHE = new AttachDAOHE();
        // Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
        // target)
        String folderPath = Executions.getCurrent().getDesktop().getWebApp()
                .getRealPath(Constants.UPLOAD.ATTACH_PATH);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            attach.setAttachPath(folderPath + "\\");
            attach.setAttachName(media.getName());
            attach.setIsActive(Constants.Status.ACTIVE);
            attach.setObjectId(ycnkFileId);
            attach.setAttachCat(Constants.OBJECT_TYPE.YCNK_FILE);// van

            attach.setAttachCode(tbAttachCode.getValue());
            attach.setAttachUrl(tbAttachName.getValue());
            
            attach.setIsActive(Constants.Status.ACTIVE);
            // ban
            // den
            attachDAOHE.saveOrUpdate(attach);

            //
            File f = new File(folderPath + "\\" + attach.getAttachId());

            if (f.exists()) {
            } else {
                f.createNewFile();
            }

            // save to hard disk and database
            inputStream = media.getStreamData();
            outputStream = null;
            outputStream = new FileOutputStream(f);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException ex) {
           LogUtils.addLog(ex);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public ListModel getModel() {
        return model;
    }

    public void setModel(ListModel model) {
        this.model = model;
    }

    public Attachs getAttach() {
        return attach;
    }

    public void setAttach(Attachs attach) {
        this.attach = attach;
    }

}

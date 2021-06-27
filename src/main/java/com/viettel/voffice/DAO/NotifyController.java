package com.viettel.voffice.DAO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.NotifyDAOHE;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
/**
 *
 * @author ChucHV
 */
public class NotifyController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 6500094886399688830L;
    @Wire
    private Listbox lbProcess;
    @Wire
    private Textbox tbcomment;
    private Long objectId;
    private Long objectType;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        objectId = (Long) Executions.getCurrent().getArg().get("objectId");
        objectType = (Long) Executions.getCurrent().getArg().get("objectType");
        EventQueue eq = EventQueues.lookup("onLoadListNotify",
                EventQueues.DESKTOP, true);
        eq.subscribe(new EventListener() {

            @Override
            public void onEvent(Event event) throws Exception {
                onLoadListNotify();
            }
        });
        onLoadListNotify();
    }

    @Listen("onLoadListNotify = #windowNotify")
    public void onLoadListNotify() {
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        List<Notify> data = notifyDAOHE.getNotifyByObject(objectId, objectType,
                getUserId(), getDeptId());
        ListModelList modelList = new ListModelList(data);
        lbProcess.setModel(modelList);
    }

    @Listen("onClick = #btnSendNotify")
    public void onSendNotify() {
        /*
         * Luu notification
         */
        String comment = tbcomment.getText();
        if (comment.matches("\\s*")) {
            showNotification("Nội dung ý kiến không được để trống",
                    Constants.Notification.WARNING);
            return;
        }
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        notifyDAOHE.saveOrUpdate(createNotify(comment));
        onLoadListNotify();

        tbcomment.setValue("");
        tbcomment.focus();
    }

    @Listen("onOK=#tbcomment")
    public void onEnterComment() {
        onSendNotify();
    }

    public String getDefaultImgPath() {
        String path = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/Share/avatar/")
                + File.separator + "default-avatar.png";
        return path;
    }

    public AImage getAvatar(Notify notify) throws IOException {
        AImage aImage;
        String avatarPath;
        String folderPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(ResourceBundleUtil.getString("dir_avartar"));
        String defaultPath = getDefaultImgPath();

        UserDAOHE userDAOHE = new UserDAOHE();
        Users user = userDAOHE.getUserById(notify.getSendUserId());
        if (user == null) {
            avatarPath = defaultPath;
        } else {
            if (user.getAvartarPath() == null) {
                avatarPath = defaultPath;
            } else {
                File file = new File(folderPath + "\\" + user.getUserId());
                if (file.exists()) {
                    avatarPath = folderPath + "\\" + user.getUserId();
                } else {
                    // load default avatar
                    avatarPath = defaultPath;
                }
            }
        }
        aImage = new AImage(avatarPath);
        return aImage;
    }

    @Listen("onDownloadAttach = #lbProcess")
    public void onDownloadAttach(Event event) throws FileNotFoundException {
        Notify notify = (Notify) event.getData();
        AttachDAOHE adhe = new AttachDAOHE();
        List<Attachs> lstAttachs = adhe.getByObjectIdAndType(
                notify.getNotifyId(), Constants.OBJECT_TYPE.NOTIFY);
        if (lstAttachs == null || lstAttachs.isEmpty()) {
            showNotification("Không có file đính kèm",
                    Constants.Notification.INFO);
        } else if (lstAttachs.size() == 1) {
            String path = lstAttachs.get(0).getAttachPath()
                    + lstAttachs.get(0).getAttachId();
            File f = new File(path);
            if (f.exists()) {
                Filedownload.save(lstAttachs.get(0).getAttachPath()
                        + lstAttachs.get(0).getAttachId(), null, lstAttachs.get(0).getAttachName());
            } else {
                showNotification("File không còn tồn tại trên hệ thống",
                        Constants.Notification.INFO);
            }
        } else {
            HashMap<String, Object> args = new HashMap<String, Object>();
            args.put("objectId", notify.getNotifyId());
            args.put("objectType", Constants.OBJECT_TYPE.NOTIFY);
            Window wnd = (Window) Executions.createComponents(
                    "/Pages/common/downloadSelect.zul", null, args);
            wnd.doModal();
        }
    }

    @Listen("onUpload = #imgSendAttach")
    public void onUploadAttach(UploadEvent event) throws UnsupportedEncodingException {
        Media media = event.getMedia();
        String extFile = FileUtil.getFileExtention(media.getName());
		if (!FileUtil.validFileType(extFile.replace(".", ""))) {
            showNotification("File không đúng định dạng!",
                    Constants.Notification.INFO);
            return;
        }
        try {
            NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
            Notify nf = createNotify("Gửi tệp: " + media.getName());
            notifyDAOHE.saveOrUpdate(nf);
            saveFileAttach(media, nf);
            onLoadListNotify();
            tbcomment.setValue("");
            tbcomment.focus();
        } catch (NullPointerException|IOException e) {
        	LogUtils.addLog(e);
            showNotification("Gửi tệp đính kèm lỗi!",
                    Constants.Notification.INFO);
        }

    }

    @Listen("onClick=#deleteMenu")
    public void onDeleteNotify() {
        if (lbProcess.getSelectedItem() == null) {
            showNotification("Bạn chưa chọn ý kiến để xóa");
            return;
        }
        Notify notify = lbProcess.getSelectedItem().getValue();
        if (notify.getSendUserId() != null
                && notify.getSendUserId().equals(getUserId())) {
            NotifyDAOHE ndhe = new NotifyDAOHE();
            ndhe.delete(notify);
            onLoadListNotify();
        } else {
            showNotification("Bạn không được phép xóa ý kiến này");
        }
    }

    private Notify createNotify(String comment) {
        UserToken user = (UserToken) Sessions.getCurrent(true).getAttribute(
                "userToken");
        Notify notify = new Notify();
        notify.setObjectId(objectId);
        notify.setObjectType(objectType);
        notify.setContent(comment);
        notify.setSendUserId(user.getUserId());
        notify.setSendUserName(user.getUserFullName());
        notify.setSendTime(new Date());
        notify.setStatus(Constants.Status.ACTIVE);
        return notify;
    }

    private Attachs saveFileAttach(Media media, Notify notify)
            throws IOException {
        // Neu ung dung chua co avatar thi return
        if (media == null) {
            return null;
        }

        AttachDAOHE attachDAOHE = new AttachDAOHE();
        // Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
        // target)
//        String folderPath = Executions.getCurrent().getDesktop().getWebApp()
//                .getRealPath(Constants.UPLOAD.ATTACH_PATH);
        String folderPath = ResourceBundleUtil.getString("dir_upload");
        FileUtil.mkdirs(folderPath);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Attachs attach = null;
        boolean saveSuccess = true;
        try {
            attach = new Attachs();
            attach.setAttachPath(folderPath + "\\");
            attach.setAttachName(media.getName());
            attach.setIsActive(Constants.Status.ACTIVE);
            attach.setObjectId(notify.getNotifyId());
            attach.setAttachCat(Constants.OBJECT_TYPE.NOTIFY);
            attachDAOHE.saveOrUpdate(attach);
            File fd = new File(folderPath);
            if (!fd.exists()) {
                fd.mkdirs();
            }
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
            if (attach.getAttachId() != null) {
                NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
                notify.setAttachId(attach.getAttachId());
                notifyDAOHE.saveOrUpdate(notify);
            }

        } catch (IOException ex) {
        	LogUtils.addLog(ex);
            saveSuccess = false;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        if (saveSuccess) {
            return attach;
        } else {
            return null;
        }
    }
}

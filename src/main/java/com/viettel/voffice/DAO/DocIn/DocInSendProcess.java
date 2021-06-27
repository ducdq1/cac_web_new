/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.NotifyDAOHE;
import com.viettel.core.workflow.DAO.ProcessDAOHE;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

/**
 *
 * @author ChucHV
 */
public class DocInSendProcess extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Wire
    protected Groupbox grbDeadline;
    @Wire
    protected Datebox dbDeadline;
    @Wire
    protected Caption ctDeadline, cbProcessContent;
    @Wire
    protected Label lbBottomWarning, lbTopWarning;
    @Wire
    protected Window windowComment;
    @Wire
    protected Textbox tbcomment;

    @Wire
    protected Include incSelectObjectsToSendProcess;
    @Wire("#incSelectObjectsToSendProcess #wdListObjectsToSend #lbNodeDeptUser")
    protected Listbox lbNodeDeptUser;
    @Wire("#incSelectObjectsToSendProcess #wdListObjectsToSend #cbListNDU")
    protected Caption cbListNDU;
    @Wire("#incSelectObjectsToSendProcess #wdListObjectsToSend #grbListNDU")
    protected Groupbox grbListNDU;
    @Wire("#incSelectObjectsToSendProcess #wdListObjectsToSend #lhDelete")
    protected Listheader lhDelete;
    @Wire("#incSelectObjectsToSendProcess #wdListObjectsToSend")
    protected Window wdListObjectsToSend;

    // Attach file
    @Wire
    protected Vlayout flist;
    protected List<Media> listMedia;
    @Wire
    protected Button btnSend, btnChoose;

    protected Window windowParent;
    protected Process process;// process dang duoc xu li
    protected String actionName;
    protected Long actionType;
    protected DocumentReceive documentReceive;

    protected List<NodeToNode> listNodeToNode;
    protected List<NodeDeptUser> listAvailableNDU;
    protected List<Long> listUserToSend;
    protected List<Long> listDeptToSend;

    @Listen("onChange = #dbDeadline")
    public void onChangeDeadline() {
        if (documentReceive.getDeadlineByDate() != null) {
            if (dbDeadline.getValue()
                    .after(documentReceive.getDeadlineByDate())) {
                showNotification(
                        "Hạn xử lí không được sau Hạn trả lời của văn bản",
                        Constants.Notification.WARNING);
                dbDeadline.setValue(documentReceive.getDeadlineByDate());
            }
        }
    }

    protected Notify sendNotify(
            com.viettel.core.workflow.BO.Process process,
            List<Long> listDeptToSend, List<Long> listUserToSend) {
        NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
        Notify notify = new Notify();
        notify.setObjectId(process.getObjectId());
        notify.setObjectType(process.getObjectType());
        notify.setContent(tbcomment.getText());
        notify.setSendUserId(getUserId());
        notify.setSendUserName(getUserFullName());
        notify.setSendTime(new Date());
        notify.setStatus(Constants.Status.ACTIVE);

        if (listDeptToSend != null && !listDeptToSend.isEmpty()) {
            StringBuilder builder = new StringBuilder(";");
            for (Long deptId : listDeptToSend) {
                builder.append(deptId).append(";");
            }
            notify.setMultiDept(builder.toString());
        }

        if (listUserToSend != null && !listUserToSend.isEmpty()) {
            StringBuilder builder = new StringBuilder(";");
            for (Long userId : listUserToSend) {
                builder.append(userId).append(";");
            }
            notify.setMultiUser(builder.toString());
        }

        notifyDAOHE.saveOrUpdate(notify);

        EventQueue eq = EventQueues.lookup("onLoadListNotify",
                EventQueues.DESKTOP, true);
        eq.publish(new Event("NO NAME"));

        return notify;
    }

    protected Attachs saveFileAttach(Media media, Notify notify)
            throws IOException {
        // Neu ung dung chua co avatar thi return
        if (media == null) {
            return null;
        }

        AttachDAOHE attachDAOHE = new AttachDAOHE();
        // Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
        // target)
        String folderPath = Executions.getCurrent().getDesktop().getWebApp()
                .getRealPath(Constants.UPLOAD.ATTACH_PATH);

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
            saveSuccess = false;
            LogUtils.addLog(ex);
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

    // Xu li su kien khi user upload file
    @Listen("onUpload = #btnAttach")
    public void onUpload(UploadEvent event) throws UnsupportedEncodingException{
        final Media[] medias = event.getMedias();
        for (final Media media : medias) {

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

    public List<NodeDeptUser> convertListitemToListNDU(
            Collection<Listitem> items) {
        List<NodeDeptUser> listNDU = new ArrayList<>();
        for (Listitem item : items) {
            listNDU.add((NodeDeptUser) item.getValue());
        }
        return listNDU;
    }

    /**
     * Là những listitem không bị disable (chưa được gửi)
     * @return 
     */
    public List<NodeDeptUser> getListChoosedNDU() {
        List list = new ArrayList<>();
        if (lbNodeDeptUser != null) {
            for (Listitem item : lbNodeDeptUser.getItems()) {
                if (!item.isDisabled()) {
                    list.add((NodeDeptUser) item.getValue());
                }
            }
        }
        return list;
    }

    public String isListNDUValidatedToSend(List<NodeDeptUser> listNDU) {
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        return processDAOHE.isListNDUValidatedToSend(listNDU);
    }

    public void renderSentListitem(List<Process> listSentProcess, Listitem item) {
        if (listSentProcess != null) {
            NodeDeptUser ndu = item.getValue();
            for (Process p : listSentProcess) {
                if (Objects.equals(ndu.getDeptId(), (p.getReceiveGroupId()))
                        && Objects
                        .equals(ndu.getUserId(), p.getReceiveUserId())
                        && !Constants.PROCESS_STATUS.RETURN.equals(p
                                .getStatus())
                        && !Constants.PROCESS_STATUS.RETRIEVE.equals(p
                                .getStatus())) {
                    ndu.setProcessType(p.getProcessType());
                    item.setValue(ndu);
                    item.setDisabled(true);
                }
            }
        }
    }

    /**
     * hoangnv28 Ngoài những listitem đã được chọn, còn có những process đã được
     * gửi, và những process có thể trùng với listitem, đã đến trường hợp gửi
     * nhiều lần cho cùng 1 người. Giải pháp: render các process đã được gửi
     * trước. Những listitem đã chọn để gửi, cái nào trùng với process đã gửi
     * thì loại bỏ, cái nào ko trùng thì giữ lại. Từ đó mới kiểm tra tính hợp lệ
     * của listitem (1 xử lý chính,..)
     * http://10.60.15.33/mantis/view.php?id=197095
     *
     * @param processCurrent
     * @param listChoosedNDU
     * @return
     */
    public List<NodeDeptUser> getListNDUToSend(Process processCurrent,
            List<NodeDeptUser> listChoosedNDU) {
        ProcessDAOHE processDAOHE = new ProcessDAOHE();
        List<Process> listSentProcess = processDAOHE
                .getSentProcess(processCurrent);
        List<NodeDeptUser> listSentNDU = processDAOHE
                .convertProcessToNDU(listSentProcess);
        List<NodeDeptUser> listNDU = new ArrayList<>();
        if (listSentNDU != null && !listSentNDU.isEmpty()) {
            for (NodeDeptUser sentNDU : listSentNDU) {
                for (NodeDeptUser choosedNDU : listChoosedNDU) {
                    if (!(Objects.equals(sentNDU.getDeptId(),
                            choosedNDU.getDeptId()) && Objects.equals(
                                    sentNDU.getUserId(), choosedNDU.getUserId()))) {
                        listNDU.add(choosedNDU);
                    }
                }
            }
            listNDU.addAll(listSentNDU);
        } else {
            listNDU.addAll(listChoosedNDU);
        }
        return listNDU;
    }
}

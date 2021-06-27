//package com.viettel.voffice.DAO;
//
//import com.viettel.core.base.DAO.BaseComposer;
//import com.viettel.utils.Constants;
//import com.viettel.utils.FileUtil;
//import com.viettel.utils.ResourceBundleUtil;
//import com.viettel.core.user.model.UserToken;
//import com.viettel.voffice.BO.Document.Attachs;
//import com.viettel.voffice.BO.Home.Notify;
//import com.viettel.core.user.BO.Users;
//import com.viettel.voffice.DAOHE.AttachDAOHE;
//import com.viettel.voffice.DAOHE.NotifyDAOHE;
//import com.viettel.core.user.DAO.UserDAOHE;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//
//import org.zkoss.image.AImage;
//import org.zkoss.util.media.Media;
//import org.zkoss.zk.ui.Component;
//import org.zkoss.zk.ui.Executions;
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zk.ui.event.EventQueue;
//import org.zkoss.zk.ui.event.EventQueues;
//import org.zkoss.zk.ui.event.UploadEvent;
//import org.zkoss.zk.ui.select.annotation.Listen;
//import org.zkoss.zk.ui.select.annotation.Wire;
//import org.zkoss.zul.Filedownload;
//import org.zkoss.zul.Html;
//import org.zkoss.zul.Label;
//import org.zkoss.zul.ListModelList;
//import org.zkoss.zul.Listbox;
//import org.zkoss.zul.Textbox;
//import org.zkoss.zul.Window;
//
///**
// *
// * @author huantn1
// */
//public class NotifyDetailViewController extends BaseComposer {
//
//	/**
//     *
//     */
//	private static final long serialVersionUID = 6500094886399688830L;
//	@Wire
//	private Label labelTitle,labelDeptName,labelUserName,labelSendTime;
//	@Wire
//	private Html labelContent;
//	private Notify notifyItem;
//        private NotifyDAOHE notifyDAOHE;
//        
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public void doAfterCompose(Component comp) throws Exception {
//		super.doAfterCompose(comp);
//		notifyItem = (Notify) Executions.getCurrent().getArg().get("notify");
//                if(notifyItem != null){
//                    notifyDAOHE = new NotifyDAOHE();
//                    notifyItem = notifyDAOHE.findById(notifyItem.getNotifyId());
//                    Users user = (new UserDAOHE()).findById(notifyItem.getUserId());
//                    labelUserName.setValue(user.getFullName());
//                    labelDeptName.setValue(user.getDeptName());
//                    labelTitle.setValue(notifyItem.getTitle());
//                    labelContent.setContent(notifyItem.getContent());
//                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(notifyItem.getSendTime().toString());
//                    labelSendTime.setValue(new SimpleDateFormat("dd-MM-yyyy").format(date));
//                    
//                }
//	}
//
//}

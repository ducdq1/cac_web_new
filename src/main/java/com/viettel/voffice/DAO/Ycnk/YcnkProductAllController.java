package com.viettel.voffice.DAO.Ycnk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Ycnk.YcnkFile;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.YcnkFileDAOHE;
import com.viettel.voffice.model.DocumentProcess;
import com.viettel.voffice.model.YcnkFileSearchModel;

/**
 *
 * @author ChucHV
 */
public class YcnkProductAllController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Paging userPagingTop, userPagingBottom;
	@Wire
	private Listbox lbListProduct;
	@Wire
	private Window ycnkProductAll;

	private int _totalSize = 0;
	// private DocInPagingListModel model;

	private YcnkFileSearchModel lastSearchModel;


	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		//onSearch();
	}



	/*
	 * isPaging: neu chuyen page thi khong can lay _totalSize
	 */
	private void reloadModel(Long fileId,boolean isPaging) {

//		YcnkProductDAOHE ycnkProductDAOHE = new YcnkProductDAOHE();
//		List listYcnkProduct = ycnkProductDAOHE.getYcnkProductList(fileId, userPagingTop.getActivePage(),
//				userPagingTop.getPageSize(), false);
//
//		if (!isPaging) {
//			List result = ycnkProductDAOHE.getYcnkProductList(fileId,userPagingTop.getActivePage(),
//					userPagingTop.getPageSize(), true);
//			if (result.isEmpty()) {
//				_totalSize = 0;
//			} else {
//				_totalSize = ((Long) result.get(0)).intValue();
//			}
//		}
//		userPagingTop.setTotalSize(_totalSize);
//		userPagingBottom.setTotalSize(_totalSize);
//                //catList = new BindingListModelList<>(listYcnkFile, true);
//		lbListProduct.setModel(new BindingListModelList<>(listYcnkProduct, true));

	}

	@Listen("onAfterRender = #lbListProduct")
	public void onAfterRender() {
		List<Listitem> listitems = lbListProduct.getItems();
		for (Listitem item : listitems) {
			
		}
	}

	@Listen("onPaging = #userPagingTop, #userPagingBottom")
	public void onPaging(Event event) {
		final PagingEvent pe = (PagingEvent) event;
		if (userPagingTop.equals(pe.getTarget())) {
			userPagingBottom.setActivePage(userPagingTop.getActivePage());
		} else {
			userPagingTop.setActivePage(userPagingBottom.getActivePage());
		}
		//reloadModel(getDeptId(), lastSearchModel, true);
	}

	/*
	 * Xu li su kien tim kiem simple
	 */
//	@Listen("onSearchFullText = #ycnkProductAll")
//	public void onSearchFullText(Event event) {
//		String data = event.getData().toString();
//		Gson gson = new Gson();
//		ToolbarModel model = gson.fromJson(data, ToolbarModel.class);
//		if (model != null) {
//			reloadModel(getDeptId(), lastSearchModel, false);
//		}
//	}

	

	

//	@Listen("onClick = #incSearchFullForm #btnSearch")
//	public void onSearch() {
//		lastSearchModel = new YcnkFileSearchModel();
//		try {
//			reloadModel(getDeptId(), lastSearchModel, false);
//		} catch (Exception e) {
//                    
//			LogUtils.addLog(e.getMessage());
//		}
//	}

	@Listen("onOpenCreate=#ycnkProductAll")
	public void onOpenCreate() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("CRUDMode", "CREATE");
		arguments.put("parentWindow", ycnkProductAll);
                
		Window window = createWindow("windowCRUDDocIn",
				"/Pages/ycnk/ycnkProductCRUD.zul", arguments,
				Window.EMBEDDED);
		window.setMode(Window.Mode.EMBEDDED);
		ycnkProductAll.setVisible(false);
	}

	@Listen("onOpenView = #lbListProduct")
	public void onOpenView(Event event) {
		YcnkFile ycnkFile = (YcnkFile) event.getData();
                createWindowDocInView(ycnkFile.getFileId(), Constants.DOCUMENT_MENU.ALL,
					ycnkProductAll);
			ycnkProductAll.setVisible(false);
                        

	}

	@Listen("onSelectedProcess = #ycnkProductAll")
	public void onSelectedProcess(Event event) {
		// <editor-fold defaultstate="collapsed" desc="Compiled Code">
//		createWindowDocInView(process.getObjectId(),
//				Constants.DOCUMENT_MENU.ALL, ycnkProductAll, process);
		ycnkProductAll.setVisible(false);
		// </editor-fold>
	}

	private void createWindowDocInView(Long ycnkFileId, int menuType,
			Window parentWindow) {
		HashMap<String, Object> arguments =new HashMap<String, Object>();
		arguments.put("ycnkFileId", ycnkFileId);
		arguments.put("menuType", menuType);
		arguments.put("parentWindow", parentWindow);
//		arguments.put("processId", process.getProcessId());
		createWindow("windowDocInView", "/Pages/ycnk/viewYcnk.zul",
				arguments, Window.EMBEDDED);
	}

	@Listen("onDownloadAttach=#lbListProduct")
	public void onDownloadAttach(Event event) throws FileNotFoundException {
		DocumentProcess docProcess = (DocumentProcess) event.getData();
		AttachDAOHE adhe = new AttachDAOHE();
		List<Attachs> lstAttachs = adhe.getByObjectIdAndType(docProcess
				.getDocumentReceive().getDocumentReceiveId(),
				Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
		if (lstAttachs == null || lstAttachs.isEmpty()) {
			showNotification("Không có file đính kèm",
					Constants.Notification.INFO);
		} else if (lstAttachs.size() == 1) {
			String path = lstAttachs.get(0).getAttachPath() + File.separator
					+ lstAttachs.get(0).getAttachId();
			File f = new File(path);
			if (f.exists()) {
				Filedownload.save(lstAttachs.get(0).getAttachPath()
						+ lstAttachs.get(0).getAttachId(), null, lstAttachs
						.get(0).getAttachName());
			} else {
				showNotification("File không còn tồn tại trên hệ thống",
						Constants.Notification.INFO);
			}
		} else {
			HashMap<String, Object> args =new HashMap<String, Object>();
			args.put("objectId", docProcess.getDocumentReceive()
					.getDocumentReceiveId());
			args.put("objectType", Constants.OBJECT_TYPE.DOCUMENT_RECEIVE);
			createWindow("downloadWnd", "/Pages/common/downloadSelect.zul",
					args, Window.MODAL);
		}
	}

	@Listen("onSave = #ycnkProductAll")
	public void onSave(Event event) {
		ycnkProductAll.setVisible(true);
	}

	// Khi cac window Create, Update, View dong thi gui su kien hien thi
	// windowDocIn len
	// Con cac su kien save, update thi da xu li hien thi windowDocIn trong
	// phuong thuc onSave
	@Listen("onVisible = #ycnkProductAll")
	public void onVisible() {
		//reloadModel(getDeptId(), lastSearchModel, false);
		ycnkProductAll.setVisible(true);
	}
        @Listen("onOpenUpdate = #lbListProduct")
	public void onOpenUpdate(Event event) {
		YcnkFile ycnkFile = (YcnkFile) event.getData();
		HashMap<String,Object> arguments =new HashMap<String, Object>();
		arguments.put("ycnkFileId", ycnkFile.getFileId());
		arguments.put("CRUDMode", "UPDATE");
		arguments.put("parentWindow", ycnkProductAll);
		createWindow("windowCRUDDocIn", "/Pages/ycnk/ycnkFileCRUD.zul",
				arguments, Window.EMBEDDED);
		ycnkProductAll.setVisible(false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onDelete = #lbListProduct")
	public void onDelete(Event event) {
		// <editor-fold defaultstate="collapsed" desc="Compiled Code">
		final YcnkFile ycnkFile = (YcnkFile) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM,
				Constants.DOCUMENT_TYPE_NAME.FILE);

		Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event event) {
						if (null != event.getName()) {
							switch (event.getName()) {
							case Messagebox.ON_OK:
								// OK is clicked
								try {
                                                                        YcnkFileDAOHE ycnkFileDAOHE = new YcnkFileDAOHE();
                                                                        ycnkFileDAOHE.delete(ycnkFile);
									//onSearch();
									showNotification(String.format(Constants.Notification.DELETE_SUCCESS,Constants.DOCUMENT_TYPE_NAME.FILE),Constants.Notification.INFO);
								} catch (NullPointerException e) {
									showNotification(String.format(Constants.Notification.DELETE_ERROR,Constants.DOCUMENT_TYPE_NAME.FILE),Constants.Notification.ERROR);
									LogUtils.addLog(e);
								} finally {
								}
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
					}
				});
		// </editor-fold>
	}
        
        
        
	public boolean isCRUDProductMenu() {
		return true;
	}
        public boolean isAbleToDeleteProduct(YcnkFile ycnkFile) {
		return true;
	}

	public boolean isAbleToModifyProduct(YcnkFile ycnkFile) {
		return true;
	}
}

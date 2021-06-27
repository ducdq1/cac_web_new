package com.viettel.module.phamarcy.controller;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;

import com.viettel.core.base.DAO.AttachDAO;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.module.phamarcy.BO.PhaRecieving;
import com.viettel.module.phamarcy.DAO.Document.PhaRecievingDAO;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;

@SuppressWarnings("serial")
public class PhamarcyViewAdditionIncludeController extends BaseComposer {

	private Long fileId;
	@Wire
	private Listbox lbDispatch;// danh sach tu choi
	@Wire
	private Listbox lbTypeAttachFile, lbAttachmentsTypeOther;// loai tep dinh
																// kem
	@Wire
	private Div divTypeOther;
	@SuppressWarnings("rawtypes")
	private List lstDispatch;
	private Long PHA_FILE_TYPE;
	private Long fileType;

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		PHA_FILE_TYPE = new WorkflowAPI().getProcedureTypeIdByCode(Constants.CATEGORY_TYPE.PHAMARCY_OBJECT);
		return super.doBeforeCompose(page, parent, compInfo);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
		fileId = (Long) arguments.get("id");
		fileType = (Long) arguments.get("fileType");
		PhaRecievingDAO phaRecievingDAO = new PhaRecievingDAO();
		lstDispatch = phaRecievingDAO.findAllActiveBySystemFileId(fileId);
		PhaRecieving phaRecieving = (PhaRecieving) lstDispatch.get(0);
		if (lstDispatch.size() > 0) {
			if (!fileType.equals(PHA_FILE_TYPE)) {
				divTypeOther.setVisible(true);

				String rejectAttachOtherFile = phaRecieving.getRejectOTherAttachFile();
				// lay danh sach categoryID cua loai tep dinh kem
				if (rejectAttachOtherFile != null && rejectAttachOtherFile.trim().length() > 0) {
					int index = rejectAttachOtherFile.lastIndexOf(",");
					if (index >= 0) {
						rejectAttachOtherFile = rejectAttachOtherFile.substring(0, index);
						List<Category> lstCategory = new CategoryDAOHE().findCategoryById(rejectAttachOtherFile);
						lbAttachmentsTypeOther.setModel(new ListModelArray(lstCategory));
					}
				}
				
			}

			
			String rejectAttachFile = phaRecieving.getRejectAttachFile();
			// lay danh sach categoryID cua loai tep dinh kem
			if (rejectAttachFile != null && rejectAttachFile.trim().length() > 0) {
				int index = rejectAttachFile.lastIndexOf(",");
				if (index >= 0) {
					rejectAttachFile = rejectAttachFile.substring(0, index);
					List<Category> lstCategory = new CategoryDAOHE().findCategoryById(rejectAttachFile);
					lbTypeAttachFile.setModel(new ListModelArray(lstCategory));
				}
			}
		}
		
		lbDispatch.setModel(new ListModelArray(lstDispatch));
	}

	@Listen("onDownloadFile = #lbDispatch")
	public void onDownloadFile(Event event) throws FileNotFoundException {
		PhaRecieving obj = (PhaRecieving) event.getData();
		Long objectID = obj.getSystemFileId();
		AttachDAOHE attDAOHE = new AttachDAOHE();
		// hieptq update 060715
		try {
			List<Attachs> lst = attDAOHE.getByObjectIdAndType(objectID, Constants.OBJECT_TYPE.PHAMARCY_SDBS_DISPATH_VT);
			if (lst.size() > 0) {
				Attachs att = lst.get(0);
				AttachDAO attDAO = new AttachDAO();
				attDAO.downloadFileAttachOriginal(att);
			}
		} catch (FileNotFoundException fileNotFoundException) {
			LogUtils.addLog(fileNotFoundException);
		}
	}

	public int CheckCVTC(Long fileId) {
		int check = 0;
		AttachDAOHE attDAOHE = new AttachDAOHE();
		List<Attachs> lst = attDAOHE.getByObjectIdAndType(fileId, Constants.OBJECT_TYPE.PHAMARCY_SDBS_DISPATH_VT);
		if (lst.size() > 0) {
			check = 1;
		}
		return check;
	}

}

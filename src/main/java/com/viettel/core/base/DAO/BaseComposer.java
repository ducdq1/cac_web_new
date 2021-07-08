/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.DAO;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.xel.fn.CommonFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.viettel.core.base.model.UserModel;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.user.DAO.RoleUserDeptDAOHE;
import com.viettel.core.user.model.DeptNode;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.WorkflowAPI;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.PhamarcyDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;

/**
 *
 * @author HaVM2
 */
public class BaseComposer extends SelectorComposer<Component> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2593028752703160467L;
	private Boolean isFileClerk = null;
	private UserModel userModel;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// Display title
		this.getPage().setTitle(getLabel("title"));
	}

	public UserToken getToken() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
		return tk;

	}

	public Long getUserId() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
		return tk.getUserId();
	}

	public String getUserFullName() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
		return tk.getUserFullName();
	}

	public String getUserName() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
		return tk.getUserName();
	}

	public String getIpClient() {
		Session ssCurrent = Sessions.getCurrent();
		return ssCurrent.getRemoteAddr();
	}

	public Long getDeptId() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
		return tk.getDeptId();
	}

	public String getDeptName() {
		UserToken tk = (UserToken) Sessions.getCurrent(true).getAttribute("userToken");
		return tk.getDeptName();
	}

	@SuppressWarnings("rawtypes")
	public Window createWindow(String id, String url, HashMap arg, int mode) {
		Window window = null;
		try {
			System.out.println("Pases: "+url);
			if (this.getPage().hasFellow(id)) {
				window = (Window) this.getPage().getFellow(id);
				window.setVisible(true);
			} else {
				Div div = (Div) Path.getComponent("/bodyContent");
				window = (Window) Executions.createComponents(url, div, arg);
				window.setMode(mode);
				switch (mode) {
				case Window.EMBEDDED:
					window.doEmbedded();
					break;
				case Window.HIGHLIGHTED:
					window.doHighlighted();
					break;
				case Window.MODAL:
					window.doModal();
					break;
				case Window.OVERLAPPED:
					window.doOverlapped();
					break;
				case Window.POPUP:
					window.doPopup();
					break;
				}
			}
		} catch (Exception ex) {
			showDialogWithNoEvent(ex.getMessage(), "Lỗi");
			LogUtils.addLog(ex);
			System.out.println("Err: "+ex);
		}
		return window;
	}

	public boolean isFileClerk() {
		if (isFileClerk != null) {
			return isFileClerk;
		} else {
			RoleUserDeptDAOHE rolesDAOHE = new RoleUserDeptDAOHE();
			isFileClerk = rolesDAOHE.isFileClerk(getUserId(), getDeptId());
			return isFileClerk;
		}
	}

	public void showNotification(String message, String type, int time) {
		Clients.showNotification(message, type, null, "top_center", time);
	}

	public void showNotification(String message, String type) {
		Clients.showNotification(message, type, null, "top_center", 3000);
	}

	public void showNotification(String message) {
		Clients.showNotification(message, Constants.Notification.ERROR, null, "top_center", 3000);
	}

	public void showSuccessNotification(String message) {
		Clients.showNotification(message, Constants.Notification.INFO, null, "top_center", 1000);
	}

	public void doExpandTree(Collection<Treeitem> ti, List<Long> parentIds, Long idSelected, Tree treeDept) {
		for (Treeitem tt : ti) {
			DeptNode dn = (DeptNode) tt.getValue();
			Long deptId = dn.getId();
			if (parentIds.contains(deptId)) {
				if (idSelected.equals(deptId)) {
					treeDept.setSelectedItem(tt);
					tt.setOpen(true);
				} else {
					tt.setOpen(true);
					doExpandTree(tt.getTreechildren().getItems(), parentIds, idSelected, treeDept);
				}
				break;
			}
		}
	}

	public Date getSysDate() {
		return new Date();
	}

	public String convertDateToString(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (date == null) {
			return "";
		}
		return dateFormat.format(date);
	}

	public String getLabelRt(String key) {
		try {
			return ResourceBundleUtil.getString(key, getConfigLanguageFile());
		} catch (Exception ex) {
			LogUtils.addLog(ex);

		}
		return "";
	}

	public String getLabelCos(String key) {
		return getLabelCos(key, "");
	}

	public String getLabelCos(String key, String defaultValue) {
		try {
			return ResourceBundleUtil.getString(key, "language_COSMETIC_vi");
		} catch (Exception e) {
			LogUtils.addLog(e);
		}
		return defaultValue;
	}

	public String getLabelIns(String key) {
		return getLabelIns(key, "");
	}

	public String getLabelIns(String key, String defaultValue) {
		try {
			return ResourceBundleUtil.getString(key, "language_INSPECT_vi");
		} catch (Exception ex) {
			LogUtils.addLog(ex);
		}
		return defaultValue;
	}

	/**
	 * Override this method to change language file
	 *
	 * @author giangnh20
	 * @return
	 */
	public String getConfigLanguageFile() {
		return "language_XNN_vi";
	}

	/**
	 * Method get string value from language file
	 *
	 * @author giangnh20
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getLabelName(String key, String defaultValue) {
		return getString(key, null, defaultValue);
	}

	/**
	 * Method get string value from language file
	 *
	 * @author giangnh20
	 * @param key
	 * @return
	 */
	public String getLabelName(String key) {
		return getString(key, null, "");
	}

	public String getString(String key, String languageFile, String defaultValue) {
		if (languageFile == null || "".equals(languageFile)) {
			languageFile = getConfigLanguageFile();
		}
		ResourceBundle bundle = ResourceBundle.getBundle(languageFile);
		return bundle.getString(key);
	}

	public String getString(String key, String languageFile) {
		return getString(key, languageFile, "");
	}

	public String convertDateTimeToString(Date date) {
		if (date != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
			return dateFormat.format(date);
		}
		return "";
	}

	/**
	 * @author giangnh20 Ham cat chuoi & them ... vao sau.
	 * @param input
	 * @param maxLength
	 * @param force
	 * @return
	 */
	public String cutString(String input, int maxLength, Boolean force) {
		StringBuilder sb = new StringBuilder();
		if (input != null && (input.length() > maxLength)) {
			if (force || !input.contains(" ")) {
				sb.append(input.substring(0, maxLength - 1));
			} else {
				int i = maxLength - 1;
				while (input.charAt(i) != ' ' && input.charAt(i) != '.' && input.charAt(i) != ','
						&& i < input.length() - 1) {
					i++;
				}
				sb.append(input.substring(0, i));
			}
			sb.append("...");
			return sb.toString();
		}
		return (input == null) ? "" : input;
	}

	public String getFormatDate(Date mDate) {
		String sTemp = "";
		if (mDate != null) {
			sTemp = CommonFns.formatDate(mDate, "dd/MM/yyyy");
		}
		return sTemp;

	}

	public String getFormatDateTime(Date mDate) {
		String sTemp = "";
		if (mDate != null) {
			sTemp = CommonFns.formatDate(mDate, "dd_MM_yyyy");
		}
		return sTemp;

	}

	public String getFormatHoursAndeDate(Date mDate) {
		String sTemp = "";
		if (mDate != null) {
			sTemp = CommonFns.formatDate(mDate, "HH:mm dd/MM/yyyy");
		}
		return sTemp;

	}

	public String cutString(String input, int maxLength) {
		return cutString(input, maxLength, false);
	}

	/**
	 * Lưu lại evenListener được lắng nghe bởi textbox Khi đóng cửa sổ window
	 * phải remove listener này đi tránh duplicate evenlistener.
	 *
	 */
	private EventListener currentEvenListener;

	/**
	 * Hien thi popup danh sach cac ky tu dac biet Cach dung: them
	 * <button label="Symbol" onClick=
	 * "$composer.openSpecialWnd(window, tbBrandName)"/> vao trong file zul.
	 *
	 * @author giangnh20
	 * @param parentWindow
	 * @param textBox
	 */
	@SuppressWarnings("unchecked")
	public void openSpecialWnd(final Window parentWindow, final Textbox textBox) {
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("parentWindow", parentWindow);
		args.put("textBox", textBox);
		createWindow("specialSymbolWnd", "/Pages/module/cosmetic/specialSymbol.zul", args, Window.OVERLAPPED);

		if (textBox != null && parentWindow != null) {

			textBox.addEventListener("onAddSymbol", currentEvenListener = new EventListener() {

				@Override
				public void onEvent(Event e) throws Exception {
					if (textBox != null && e.getData() != null) {
						StringBuilder sb = new StringBuilder(textBox.getValue());
						sb.append(e.getData().toString());
						textBox.setValue(sb.toString());
					}
				}
			});

			parentWindow.addEventListener("onSymbolWndClose", new EventListener() {

				@Override
				public void onEvent(Event t) throws Exception {
					textBox.removeEventListener("onAddSymbol", currentEvenListener);
				}

			});
		}

	}

	/**
	 * Clients Notification Position Reference at
	 * http://books.zkoss.org/wiki/ZK_Developer%27s_Reference/UI_Patterns/
	 * Useful_Java_Utilities#showNotification
	 */
	public enum NOTIFICATION_POSITION {

		TOP_LEFT("top_left"), TOP_CENTER("top_center"), TOP_RIGHT("top_right"), MIDDLE_LEFT(
				"middle_left"), MIDDLE_CENTER("middle_center"), MIDDLE_RIGHT("middle_right"), BOTTOM_LEFT(
						"bottom_left"), BOTTOM_CENTER("bottom_center"), BOTTOM_RIGHT("bottom_right");

		private final String value;

		private NOTIFICATION_POSITION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * Lay gia tri Textbox & trim gia tri
	 *
	 * @param tb
	 * @return
	 */
	public String textBoxGetValue(Textbox tb) {
		if (tb != null) {
			try {
				if (tb.getValue() != null) {
					return tb.getValue().trim();
				}
			} catch (WrongValueException e) {
				LogUtils.addLog(e);
			}
			return tb.getValue();
		}
		return null;
	}

	/**
	 * Lay gia tri file properties
	 *
	 * @author giangnh20
	 * @param key
	 * @param file
	 * @return
	 */
	public String getConfigValue(String key, String file) {
		return getConfigValue(key, file, "");
	}

	/**
	 * Lay gia tri file properties co defaultValue
	 *
	 * @author giangnh20
	 * @param key
	 * @param file
	 * @param defaultValue
	 * @return
	 */
	public String getConfigValue(String key, String file, String defaultValue) {
		ResourceBundle rb = ResourceBundle.getBundle(file);
		return rb.getString(key);
	}

	/**
	 * Ham formatNumber voi pattern ex: ##,###,###.00 se format cac so thanh
	 * chuoi co 2 so sau dau phay ex: #.# doi voi so co dinh dang bat ky
	 *
	 * @author giangnh20
	 * @param number
	 * @param pattern
	 * @param dotToComma
	 * @return
	 */
	public String formatNumber(Number number, String pattern, Boolean dotToComma) {
		return StringUtils.formatNumber(pattern, number, dotToComma);
	}

	public static String formatNumber(Number number, String pattern) {
		if (pattern == null) {
			pattern = "###,###,###.####";
		}
		return StringUtils.formatNumber(pattern, number, true);
	}

	public String convertStringToNumber(String value) {

		if (value == null || value == "") {
			return "";
		}	
		
		value=value.replace(".", "");
		value=value.replace(",", "");
		
		
		Long f=0L;
		try {
			 f = Long.valueOf(value);
		} catch (Exception e) {
			return "";
		}
		
		return formatNumber(f,null);
	}

	/**
	 * Helper call replace string in zul file
	 *
	 * @author giangnh20
	 * @param input
	 * @param target
	 * @param replacement
	 * @return
	 */
	public String replaceString(String input, String target, String replacement) {
		if (input != null && !"".equals(input) && target != null && !"".equals(target) && replacement != null) {
			return input.replace(target, replacement);
		}
		return input;
	}

	public Long getLongFromString(String value) {
		try {value=value.replace(".", "").replace(",", "");
			return Long.parseLong(value);
		} catch (NumberFormatException | NullPointerException e) {
			LogUtils.addLog(e);
		}
		return null;
	}
	
	public String getPriceFromString(String value) {
		try {
			
			String temp=value.replace(".", "").replace(",", "");
			return formatNumber(Long.parseLong(temp), "###,###");
		} catch (NumberFormatException | NullPointerException e) {
			LogUtils.addLog(e);
		}
		return value;
	}

	public Double getDoubleFromString(String value) {
		try {
			if(value!=null){
				value=value.replace(",", ".");
			}
			return Double.valueOf(value);
		} catch (NumberFormatException | NullPointerException e) {
			LogUtils.addLog(e);
		}
		return null;
	}

	
	public String getStringFromLong(Long value) {
		try {
			return value.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public String getStringFromDouble(Double value) {
		try {
			String result=value.toString();
			if(result.contains(".0")){
				int temp=value.intValue();
				if(value.equals(Double.valueOf(temp))){
					return String.valueOf(temp);
				}
			}
			return value.toString().replace(".", ",");
		} catch (Exception e) {
		}
		return "";
	}

	public boolean validateTextBox(Textbox tb) {
		 
		try{
			if (tb.getText().trim().length() == 0) {
				tb.setText("");
				String message = getLabel("require_input");
				tb.setErrorMessage(message);
				tb.setFocus(true);
				return false;
			}
		}catch(Exception e){
			
		}
		 

		return true;
	}

	public boolean validateMaxlenghtTextbox(Textbox tb) {
		String value = tb.getText();
		int maxlenght = tb.getMaxlength();

		if (maxlenght > 0 && value.length() > maxlenght) {
			String message = getLabel("maxlenght_validate");
			tb.setErrorMessage(message);
			tb.setFocus(true);
			return false;
		}

		return true;
	}

	public boolean validateIntBox(Intbox tb) {
		if (tb.getText().trim().length() == 0) {
			String message = getLabel("require_input");
			tb.setErrorMessage(message);
			tb.setFocus(true);
			return false;
		}
		return validateMaxlenghtIntBox(tb);
	}

	public boolean validateMaxlenghtIntBox(Intbox tb) {
		String value = tb.getText();
		int maxlenght = tb.getMaxlength();
		if (maxlenght > 0 && value.getBytes().length > maxlenght) {
			String message = getLabel("maxlenght_validate");
			tb.setErrorMessage(message);
			tb.setFocus(true);
			return false;
		}
		return true;
	}

	public boolean validateComboBox(Combobox db) {
		if (db.getText().trim().length() == 0) {
			String message = getLabel("require_input");
			db.setErrorMessage(message);
			db.setFocus(true);
			return false;
		}
		return validateMaxlenghtComboBox(db);
	}

	public boolean validateMaxlenghtComboBox(Combobox tb) {
		String value = tb.getText();
		int maxlenght = tb.getMaxlength();
		if (maxlenght > 0 && value.length() > maxlenght) {
			String message = getLabel("maxlenght_validate");
			tb.setErrorMessage(message);
			tb.setFocus(true);
			return false;
		}
		return true;
	}

	/**
	 * lay value tu file language_vi.properties
	 * 
	 * @param key
	 * @return
	 */
	public String getLabel(String key) {
		try {
			return ResourceBundleUtil.getString(key, "language_vi");
		} catch (Exception e) {
			LogUtils.addLog(e);
		}
		return "";
	}

	/**
	 * hien thi dialog confirm
	 */
	public void showDialogConfirm(String message, String title, EventListener<ClickEvent> clickListener) {
		if (title == null) {
			title = getLabel("confirm");
		}
		Messagebox.show(message, title, new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
				new String[] { getLabel("title_button_OK"), getLabel("title_button_cancel") }, Messagebox.QUESTION,
				null, clickListener);
	}

	/**
	 * hien thi dialog confirm
	 */
	public void showDialogWithNoEvent(String message, String title) {
		if (title == null) {
			title = getLabel("confirm");
		}
		Messagebox.show(message, title, new Messagebox.Button[] { Messagebox.Button.OK },
				new String[] { getLabel("title_button_OK") }, Messagebox.EXCLAMATION, null, null);
	}

	/**
	 * Tuannt40 Thuc hien xu ly 1 ho so khi chon nhieu va thao tac
	 */
	public void processOneDocument(Long fileId) {
		// Thuc hien dong y tu choi 1 ho so
		List<VPhaFileMedicine> lstFiles = new ArrayList<VPhaFileMedicine>();
		PhamarcyDao phamarcyDao = new PhamarcyDao();
		VPhaFileMedicine vFileCosFile = phamarcyDao.findViewBySystemFileId(fileId);
		lstFiles.add(vFileCosFile);
		HashMap<String, Object> data = new HashMap<String, Object>();
		List<com.viettel.core.workflow.BO.Process> listProcessCurrent = new ArrayList<com.viettel.core.workflow.BO.Process>();
		List<Long> listDocId = new ArrayList<Long>();
		List<Long> listDocType = new ArrayList<Long>();
		for (VPhaFileMedicine app : lstFiles) {
			// them list process
			listProcessCurrent.add((WorkflowAPI.getInstance().getCurrentProcess(app.getSystemFileId(),
					app.getFileType(), app.getStatus(), getUserId())));
			listDocId.add(app.getSystemFileId());
			listDocType.add(app.getFileType());
		}
		// Lay 1 ho so de xu ly 1
		data.put("type", Constants.PAYMENT.PAYMENT_KTXACNHAN.toString());
		data.put("listProcess", listProcessCurrent);
		data.put("listDocId", listDocId);
		data.put("listDocType", listDocType);
		HttpSession session = (HttpSession) (Executions.getCurrent()).getDesktop().getSession().getNativeSession();
		session.setAttribute("key", data);

	}

	public String getStatus(Long status) {
		return WorkflowAPI.getStatusName(status);
	}

	/**
	 * lay dead line so ngay lam viec
	 * 
	 * @param startDate
	 * @param numberDate
	 * @return
	 */
	public Date getDeadLine(long startDate, int numberDate) {
		int numberDateWork = 0;// 6 ngay lam viec
		while (numberDateWork < numberDate) {
			startDate += (24 * 60 * 60 * 1000);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(startDate));
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (day != Calendar.SATURDAY && day != Calendar.SUNDAY) {
				numberDateWork++;
			}
		}

		return new Date(startDate);
	}

	/**
	 * hien thi ho so goc
	 * 
	 * @author ducdq1
	 * @param fileId
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void fillFinalFileListbox(Long fileId, Listbox finalFileListbox) {
		AttachDAOHE rDaoHe = new AttachDAOHE();
		List<Attachs> lstAttach = rDaoHe.getByObjectIdAndType(fileId, Constants.OBJECT_TYPE.PHAMARCY_HO_SO_GOC);
		if (lstAttach != null && lstAttach.size() > 0) {
			new AttachDAOHE().flushListObject(lstAttach);
			if (lstAttach.size() > 1) {
				for (int i = 0; i < lstAttach.size(); i++) {
					if (i < lstAttach.size() - 1) {
						lstAttach.get(i).setCreatorName(getLabel("root_file1"));
					} else {
						lstAttach.get(i).setCreatorName(getLabel("root_file"));
					}
				}
			} else {
				lstAttach.get(0).setCreatorName(getLabel("root_file"));
			}
		}
		finalFileListbox.setModel(new ListModelArray(lstAttach));
	}

	/**
	 * down load file dinh kem cua xac nhan quang cao
	 * 
	 * @param fileTypeAD
	 */
	public void onDownloadFile(Long attachId) {
		AttachDAOHE attDAOHE = new AttachDAOHE();
		Attachs att = attDAOHE.findById(attachId);
		AttachDAO attDAO = new AttachDAO();
		attDAO.viewCollectionOnBrowser(att);

	}

	public String getStringFromData(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}

	public String getXuatxu(Integer xuatXuType) {
		if (xuatXuType == 0) {
			return getLabel("trong_nuoc");
		}
		if (xuatXuType == 1) {
			return getLabel("nuoc_ngoai");
		}
		return "";
	}

	public String getMedicineType(Integer medicineType) {
		if (medicineType == 0) {
			return getLabel("duoc_lieu");
		}
		if (medicineType == 1) {
			return getLabel("tan_duoc");
		}
		return "";
	}

	/**
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 3, 2016
	 * @updatedDate: Feb 3, 2016
	 * @return:void
	 */
	public void downloadAttach(Long attachId) throws FileNotFoundException {
		if (attachId != null) {
			AttachDAOHE attDAOHE = new AttachDAOHE();
			Attachs att = attDAOHE.findByAttachId(attachId);
			new AttachDAO().viewPdfOnBrowser(att);
		}
	}

	/**
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 3, 2016
	 * @updatedDate: Feb 3, 2016
	 * @return:String
	 */
	public String getBussinessCode(Long businessTypeId) {
		Category cat = new CategoryDAOHE().findById(businessTypeId);
		if (cat != null) {
			return cat.getCode();
		} else {
			return "";
		}
	}

}

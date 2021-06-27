package com.viettel.module.phamarcy.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.model.UserToken;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.QuotationDetailDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class DanhSachBaoGiaController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbMaNVBH,tbKhacHang,tbSDT,tbDiaChi;
	@Wire
	private Datebox dbFromDay,dbToDay;
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbListSP;
	@Wire
	private Window danhSachBaoGia;
	@Wire
	private Label lbTongtien;
	@Wire
	private Combobox cbTrangThai;
	PhamarcyFileModel lastSearchModel;
	private List<QuotationDetail> lstQuotationDetail;
	private List<Quotation> lstQuotation;
	private List<Product> lstProductsCombobox;
	ListModelArray lstModel;
	int indexUpdate;// cap nhat sp bao gia
	QuotationDetail quotationDetailUpdate;
	
	Quotation quotation = new Quotation();
	
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		lstQuotationDetail = new ArrayList<>();

		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		HttpSession httpSession = req.getSession(true);
		UserToken userToken =(UserToken) httpSession.getAttribute("userToken");
		if(userToken != null){
			tbMaNVBH.setText(userToken.getUserName());
			//tbTenNVBH.setText(userToken.getUserFullName());
		}
		cbTrangThai.setSelectedIndex(0);
		tbMaNVBH.setReadonly(true);
		
		Calendar c = Calendar.getInstance();   // this takes current date
	    c.set(Calendar.DAY_OF_MONTH, 1);
	    dbFromDay.setValue(c.getTime());
	    
		onSearch();
		
	}


	@Listen("onClick = #btnSearch")
	public void onSearch() {
		//lastSearchModel.setProductCode(tbMaSP.getText().trim());
		// lastSearchModel.setProductName(tbTenSP.getText().trim());
		lastSearchModel.setUserName(tbMaNVBH.getText().trim());
		lastSearchModel.setQuotationUserFullName(null);
		lastSearchModel.setTenHK(tbKhacHang.getText().trim());
		lastSearchModel.setSoDT(tbSDT.getText().trim());
		lastSearchModel.setFromDate(dbFromDay.getValue());
		lastSearchModel.setToDate(dbToDay.getValue());
		lastSearchModel.setDiaChi(tbDiaChi.getText().trim());
		lastSearchModel.setTrangThai(Integer.valueOf(cbTrangThai.getSelectedItem().getValue().toString()));
		try {
			userPagingBottom.setActivePage(0);
			reloadModel(lastSearchModel);
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
		}
	}
	
	
	@Listen("onClick = #btnSave")
	public void onSave() {
		try{
		if(tbMaNVBH.getValue().isEmpty()){
			tbKhacHang.setErrorMessage("Mã nhân viên không được để trống");
			return;
		}
		
		if(tbKhacHang.getValue().isEmpty()){
			tbKhacHang.setErrorMessage("Tên khách hàng không được để trống");
			return;
		}
		
		if(tbDiaChi.getValue().isEmpty()){
			tbKhacHang.setErrorMessage("Địa chỉ khách hàng không được để trống");
			return;
		}
		 
		if(quotation.getQuotationNumber() == null){
			quotation.setQuotationNumber(new QuotationDao().getAutoPhaFileCode());
		}
		
		quotation.setCreateDate(new Date());
		quotation.setModifyDate(new Date());
		quotation.setQuotationDate(new Date());
		quotation.setCreateUserCode(tbMaNVBH.getText().trim());
		quotation.setCusName(tbKhacHang.getText().trim());
		quotation.setCusAddress(tbDiaChi.getText().trim());
		quotation.setCusPhone(tbSDT.getText().trim());
		quotation.setCreateUserFullNameSearch(new BaseGenericForwardComposer().removeVietnameseChar(quotation.getCreateUserFullName()));
		
	    new QuotationDao().saveOrUpdate(quotation);
		Long quotationId = quotation.getQuotationID();

		for (QuotationDetail obj : lstQuotationDetail) {
			obj.setQuotationId(quotationId);
			new QuotationDetailDao().saveOrUpdate(obj);
		}
		
		showSuccessNotification("Lưu báo giá thành công");
		}catch(Exception e){
			showNotification("Lưu không thành công \n"+e.getMessage());
		}
	}
	
	
	@Listen("onClick = #btnThemSP")
	public void addListItem() {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", danhSachBaoGia);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_sp.zul", arguments, Window.MODAL);
	}

	@Listen("onOK=#danhSachBaoGia")
	public void onEnter() {
	 onSearch();
	}

	/**
	 * Lay danh sach tim kiem
	 * 
	 * @param searchModel
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void reloadModel(PhamarcyFileModel searchModel) {
		try {
			int take = userPagingBottom.getPageSize();
			int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
			QuotationDao dao = new QuotationDao();
			PagingListModel plm = dao.findFilesByReceiverAndDeptId(searchModel, start, take);

			userPagingBottom.setTotalSize(plm.getCount());
			if (plm.getCount() == 0) {
				userPagingBottom.setVisible(false);
			} else {
				userPagingBottom.setVisible(true);
			}

			lstQuotation = plm.getLstReturn();
			lstModel = new ListModelArray<>(lstQuotation);
			lbListSP.setModel(lstModel);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
			LogUtils.addLog(ex);
		}
	}

	@Listen("onPaging =  #userPagingBottom")
	public void onPaging(Event event) {
		reloadModel(lastSearchModel);
	}

	/**
	 * Open view xem chi tiet
	 * 
	 * @param event
	 */
	@Listen("onOpenView =#incList #lbList")
	public void onOpenView(Event event) {
		VPhaFileMedicine obj = (VPhaFileMedicine) event.getData();

		createWindowView(obj.getFileId(), obj.getSystemFileId(), Constants.DOCUMENT_MENU.ALL, danhSachBaoGia);

	}

	/**
	 * Tao view xem chi tiet
	 * 
	 * @param id
	 * @param systemFileId
	 * @param menuType
	 * @param parentWindow
	 */
	private void createWindowView(Long id, Long systemFileId, int menuType, Window parentWindow) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("id", id);
		createWindow("windowView", "/Pages/module/phamarcy/generalview/phamarcyView.zul", arguments, Window.EMBEDDED);
	}

	@Listen("onClick = #incSearchFullForm #btnReset")
	public void onReset() {
		// tbTenSP.setText("");
	}


	@Listen("onReload =#danhSachBaoGia")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	@Listen("onOpenUpdate = #lbListSP")
	public void onOpenUpdate(Event event) {
		Quotation obj = (Quotation) event.getData();
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parrent", danhSachBaoGia);
		arguments.put("quotation", obj);
		createWindow("windowView", "/Pages/module/phamarcy/tao_bao_gia.zul", arguments, Window.MODAL);
	}

	@Listen("onDelete = #lbListSP")
	public void onDelete(Event event) {
		final Quotation obj = (Quotation) event.getData();
		if(obj.getStatus() !=0){
			showNotification("Không thể xóa báo giá này");
			return;
		}
		String message =   "Bạn muốn xóa báo giá " + "\""  
				+ obj.getQuotationNumber() + "\" này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					new QuotationDao().delete(obj);
					lstQuotation.remove(obj);
					lstModel = new ListModelArray(lstQuotation);
					lbListSP.setModel(lstModel);
					showNotification("Xóa báo giá thành công", Constants.Notification.INFO, 2000);
				}
			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onClick = #btnChonSP")
	public void onChonSp(Event event) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", danhSachBaoGia);
		arguments.put("lstQuotationDetail", lstQuotationDetail);
		createWindow("windowView", "/Pages/module/phamarcy/bao_gia_chon_sp.zul", arguments, Window.MODAL);

	}

}

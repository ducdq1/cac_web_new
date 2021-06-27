package com.viettel.module.phamarcy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.module.phamarcy.DAO.PhaMedicine.AreaDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.CustomerDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.StreetDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class AddKHController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Listbox lbTenDuong, lbTrangThai, lbLayHang, tbArea;
	@Wire
	private Textbox tbTenKH, tbDiaChi, tbYKienKH, tbDT, tbAssignName, tbTienDo,tbNVKhaoSat;
	@Wire
	private Textbox tbDaGoiDien;
	@Wire
	private Datebox dbBuyDay, dbCreateDate;
	@Wire
	private Window resetPassDlg;
	private Window parent;
	private Street street;
	private Customer cus;
	boolean isUpdate = false;
	Boolean isImport;
	@Wire
	private Checkbox cbGiaCao,cbThaiDoPV;
	List<Area> lstArea;
	List<Street> lstStreet;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		dbBuyDay.setValue(null);
		lstArea = new StreetDao().getListArea();
		ListModelList mlstArea = new ListModelList(lstArea);
		tbArea.setModel(mlstArea);

		lstStreet = new StreetDao().getAllListStreetByArea();
		ListModelList mStreet = new ListModelList(lstStreet);
		lbTenDuong.setModel(mStreet);

		List<Category> lstTrangThai = new ArrayList<>();
		Category category = new Category();
		category.setName(getLabel("sai_sdt"));
		category.setCategoryId(0L);
		lstTrangThai.add(category);

		category = new Category();
		category.setName(getLabel("sai_dia_chi"));
		category.setCategoryId(1L);
		lstTrangThai.add(category);

		category = new Category();
		category.setName(getLabel("xong_roi"));
		category.setCategoryId(2L);
		lstTrangThai.add(category);

		category = new Category();
		category.setName(getLabel("da_qt"));
		category.setCategoryId(3L);
		lstTrangThai.add(category);
		
		ListModelList lstModelTrangThai = new ListModelList(lstTrangThai);
		lbTrangThai.setModel(lstModelTrangThai);

		List<Category> lstbLayHang = new ArrayList<>();
		category = new Category();
		category.setName(getLabel("chua_lay_hang"));
		category.setCategoryId(0L);
		lstbLayHang.add(category);

		category = new Category();

		category.setName(getLabel("da_lay_hang"));
		category.setCategoryId(1L);
		lstbLayHang.add(category);
		
		category = new Category();

		category.setName(getLabel("ko_lay_hang"));
		category.setCategoryId(2L);
		lstbLayHang.add(category);
		

		ListModelList lstModelLayHang = new ListModelList(lstbLayHang);
		lbLayHang.setModel(lstModelLayHang);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		cus = (Customer) arguments.get("Customer");
		dbCreateDate.setValue(new Date());
		if (cus != null) {
			viewData();
			isUpdate = true;
			resetPassDlg.setTitle("Cập nhật khách hàng");
		}
	}

	private void viewData() {
		tbTenKH.setText(cus.getName());
		tbDiaChi.setText(cus.getAddress());
		tbDT.setText(cus.getPhone());
		tbAssignName.setText(cus.getAssignName());
		tbDaGoiDien.setText(cus.getNumCall());
		dbBuyDay.setValue(cus.getBuyDate());
		tbTienDo.setText(cus.getProcess());
		dbCreateDate.setValue(cus.getCreateDate());
		tbYKienKH.setText(cus.getContent());
		tbNVKhaoSat.setText(cus.getSurveyName());
		if(cus.getReasion()!=null){
			String reasion=cus.getReasion();
			
			if(reasion.contains("giacao")){
				cbGiaCao.setChecked(true);
			}
			
			if(reasion.contains("thaidopv")){
				cbThaiDoPV.setChecked(true);
			}			
			 
		}
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		tbDT.clearErrorMessage();
		tbDiaChi.clearErrorMessage();

		if (lbTenDuong.getSelectedIndex() == -1) {
			showNotification("Bạn chưa chọn tên đường", Constants.Notification.WARNING);
			return;
		}

		if (!validateTextBox(tbTenKH)) {
			return;
		}

		if (!validateTextBox(tbDiaChi)) {
			return;
		}

		if (!validateTextBox(tbDT)) {
			return;
		}

		if (cus == null) {
			cus = new Customer();

		}

		cus.setCreateDate(dbCreateDate.getValue());

		Street str = new Street();
		str.setStreetId((Long) lbTenDuong.getSelectedItem().getValue());
		Customer toCheck = new Customer();
		toCheck.setCustomerId(cus.getCustomerId());
		toCheck.setPhone(tbDT.getText().trim());
		toCheck.setAddress(tbDiaChi.getText().trim());
		toCheck.setStreet(str);
		toCheck.setStatus(lbTrangThai.getSelectedIndex());
		toCheck.setAssignName(tbAssignName.getText().trim());

		Customer cusCheck = new CustomerDao().checkExistCusTomer(toCheck);
		if (cusCheck != null) {
			if (cusCheck.getPhone().equals(toCheck.getPhone())) {
				tbDT.setErrorMessage("Số điện thoại đã tồn tại");
				return;
			} else if (toCheck.getStreet() != null && cusCheck.getStreet() != null
					&& toCheck.getStreet().getStreetId().equals(cusCheck.getStreet().getStreetId())
					&& toCheck.getAddress().toLowerCase().equals(cusCheck.getAddress().toLowerCase())) {
				tbDiaChi.setErrorMessage("Địa chỉ này đã tồn tại");
				return;
			}
		}

		cus.setName(tbTenKH.getText().trim());
		String nameSearch = new BaseGenericForwardComposer().removeVietnameseChar(cus.getName());
		cus.setNameSearch(nameSearch);

		cus.setPhone(tbDT.getText().trim());
		cus.setAddress(tbDiaChi.getText().trim());
		cus.setStreet(str);
		cus.setStatus(lbTrangThai.getSelectedIndex());
		cus.setAssignName(tbAssignName.getText().trim());
		cus.setIsBuy(lbLayHang.getSelectedIndex());
		cus.setBuyDate(dbBuyDay.getValue());
		cus.setProcess(tbTienDo.getText().trim());
		cus.setContent(tbYKienKH.getText().toString());
		cus.setSurveyName(tbNVKhaoSat.getText().toString());
		cus.setNumCall(tbDaGoiDien.getText().toString());

		String lyDoKhongLayHang ="";
		if(cbGiaCao.isChecked()){
			lyDoKhongLayHang+="giacao;";
		}
		
		if(cbThaiDoPV.isChecked()){
			lyDoKhongLayHang+="thaidopv;";
		}
		
		cus.setReasion(lyDoKhongLayHang);
		if(lyDoKhongLayHang.length()>0){
			cus.setIsBuy(2);	
		}
		
		new CustomerDao().saveOrUpdate(cus);

		String message = "Thêm mới khách hàng thành công.";
		if (isUpdate) {
			message = "Cập nhật khách hàng thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 1000);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", street);
		Events.sendEvent("onRefreshALL", parent, map);
		resetPassDlg.onClose();
	}

	/**
	 * set chon item 0 trong combo loai tep dinh kem
	 */
	public void getStreetSelectedIndexInModel() {
		if (isUpdate) {
			int index = 0;
			int select = 0;
			for (Street street : lstStreet) {
				if (street.getStreetId().equals(cus.getStreet().getStreetId())) {
					lbTenDuong.setSelectedIndex(index);
					select = index;
					break;
				}
				index++;
			}
			if (!lstStreet.isEmpty()) {
				lbTenDuong.setSelectedIndex(select);
			}

		} else {
			if (!lstStreet.isEmpty()) {
				lbTenDuong.setSelectedIndex(0);
			}

		}

	}

	public void getLayHangSelectedIndexInModel() {
		if (isUpdate) {
			if (cus != null && cus.getIsBuy() != null) {
				lbLayHang.setSelectedIndex(cus.getIsBuy());
			} else {
				lbLayHang.setSelectedIndex(0);
			}
		} else {
			lbLayHang.setSelectedIndex(0);
		}
	}

	public void onSelectArea() {
		Long areaId = (Long) tbArea.getSelectedItem().getValue();
		lstStreet = new StreetDao().getListStreetByArea(areaId);
		ListModelList mStreet = new ListModelList(lstStreet);
		lbTenDuong.setModel(mStreet);

	}

	/**
	 * set chon item 0 trong combo loai tep dinh kem
	 */
	public void getAreaIndexInModel() {
		if (isUpdate) {
			int index = 0;
			int select = 0;
			for (Area area : lstArea) {
				if (cus.getStreet().getArea() != null
						&& area.getAreaId().equals(cus.getStreet().getArea().getAreaId())) {
					tbArea.setSelectedIndex(index);
					select = index;
					break;
				}
				index++;
			}

			if (!lstArea.isEmpty()) {
				tbArea.setSelectedIndex(select);
			}

			int trangThai = cus.getStatus();
			lbTrangThai.setSelectedIndex(trangThai);

		} else {

			if (!lstArea.isEmpty()) {
				tbArea.setSelectedIndex(0);
			}
			lbTrangThai.setSelectedIndex(0);
		}

		onSelectArea();
	}

}

package com.viettel.module.phamarcy.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.EventHandler;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModels;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.module.phamarcy.BO.Area;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.GiayDan;
import com.viettel.module.phamarcy.BO.Street;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.AreaDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.CustomerDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.GiayDanDao;
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
	private Listbox lbNhanVien, lbNgayNhanGiay;
	@Wire
	private Combobox lbTenDuong;
	@Wire
	private Textbox tbTenKH, tbDiaChi, tbYKienKH, tbDT, tbTienDo, tbNhanVienKiemTra, tbQuanLyXacNhan;
	@Wire
	private Label soLuongGiayDan;
	@Wire
	private Radiogroup rdgGapChuNha,rdgKiemTraThongTin;
	@Wire
	private Datebox dbNgayNhap, dbNgayDan;
	 
	@Wire
	private Window resetPassDlg;
	private Window parent;
	private Street street;
	private Customer cus;
	boolean isUpdate = false;
	Boolean isImport;
	List<Area> lstArea;
	List<Street> lstStreet;
	List<String> lstNhanVien;
	boolean firstLoad = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		lstStreet = new StreetDao().getAllListStreetByArea();

		Comparator comparator = new Comparator() {
			public int compare(Object text, Object item) {
				String val = text.toString();
				String itemText = ((Street) item).getStreetName();
				return itemText.toLowerCase().contains(val.toLowerCase()) ? 0 : 1;
			}
		};

		loadNhanVien();

		lbTenDuong.setModel(ListModels.toListSubModel(new ListModelList(lstStreet), comparator, 20));
		if (lstStreet.size() == 1) {
			lbTenDuong.setValue(lstStreet.get(0).getStreetName());
		}

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		cus = (Customer) arguments.get("Customer");
		dbNgayNhap.setValue(new Date());

		if (cus != null) {
			firstLoad = true;
			viewData();			
			isUpdate = true;
			resetPassDlg.setTitle("Cập nhật khách hàng");
		}
	}

	private void loadNhanVien() {
		lstNhanVien = new GiayDanDao().findNhanVien("").getLstReturn();
		ListModelList lstModelLayHang = new ListModelList(lstNhanVien);
		lbNhanVien.setModel(lstModelLayHang);
	}

	private void viewData() {
		tbTenKH.setText(cus.getName());
		tbDiaChi.setText(cus.getHouseNum());
		tbDT.setText(cus.getPhone());
		tbNhanVienKiemTra.setText(cus.getNhanVienKiemTra());
		tbQuanLyXacNhan.setText(cus.getXacNhanQuanLy());
		rdgGapChuNha.setSelectedIndex(cus.getGapChuNha().intValue());
		tbTienDo.setText(cus.getTienDoKhiDanGiay());
		dbNgayDan.setValue(cus.getNgayDiDan());
		dbNgayNhap.setValue(cus.getNgayNhapPM());
		rdgKiemTraThongTin.setSelectedIndex(cus.getKetQuaKiemTra().intValue());
		 
		for (Street street : lstStreet) {
			if (street.getStreetId().equals(cus.getStreet().getStreetId())) {
				lbTenDuong.setValue(street.getStreetName());
				break;
			}
		}
		
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		tbDT.clearErrorMessage();
		tbDiaChi.clearErrorMessage();

		if (lbNhanVien.getSelectedItem() == null) {
			showNotification("Bạn chưa chọn Nhân viên", Constants.Notification.WARNING);
			return;
		}
		
		
		if (lbNgayNhanGiay.getSelectedIndex() < 0) {
			showNotification("Bạn chưa chọn Ngày nhận giấy", Constants.Notification.WARNING);
			return;
		}

		if (lbTenDuong.getSelectedItem() == null || lbTenDuong.getValue() == null || lbTenDuong.getValue().isEmpty()) {
			lbTenDuong.setErrorMessage("Tên đường không hợp lệ");
			return;
		}

		/*if (!validateTextBox(tbTenKH)) {
			return;
		}*/

		if (!validateTextBox(tbDiaChi)) {
			return;
		}

		/*if (!validateTextBox(tbDT)) {
			return;
		}*/

		if (cus == null) {
			cus = new Customer();

		}

		Street str = new Street();
		str.setStreetId((Long) lbTenDuong.getSelectedItem().getValue());
		Customer toCheck = new Customer();
		toCheck.setCustomerId(cus.getCustomerId());
		toCheck.setPhone(tbDT.getText().trim());
		toCheck.setAddress(String.format("%s %s",tbDiaChi.getText().trim(),lbTenDuong.getValue()));
		toCheck.setStreet(str);

		Customer cusCheck = new CustomerDao().checkExistCusTomer(toCheck);
		if (cusCheck != null) {
			if (cusCheck.getPhone()!=null && cusCheck.getPhone().equals(toCheck.getPhone())) {
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
		cus.setPhone(tbDT.getText().trim());
		cus.setHouseNum(tbDiaChi.getText().trim());
		cus.setStreet(str);
		cus.setAddress(String.format("%s %s",tbDiaChi.getText().trim(),lbTenDuong.getValue()));
		cus.setGapChuNha(Long.valueOf(rdgGapChuNha.getSelectedIndex()));

		cus.setKetQuaKiemTra(Long.valueOf(rdgKiemTraThongTin.getSelectedIndex()));
		
		cus.setNgayDiDan(dbNgayDan.getValue());
		cus.setNgayNhapPM(dbNgayNhap.getValue());
		cus.setNhanVienDanGiay(lbNhanVien.getSelectedItem().getLabel());
		cus.setNhanVienKiemTra(tbNhanVienKiemTra.getValue());
		cus.setXacNhanQuanLy(tbQuanLyXacNhan.getValue());
		cus.setTienDoKhiDanGiay(tbTienDo.getValue());

		int selected = lbNgayNhanGiay.getSelectedIndex();
		List<Listitem> items = lbNgayNhanGiay.getItems();

		GiayDan giayDan = (GiayDan) items.get(selected).getValue();
		cus.setGiayDanId(giayDan.getId());

		new CustomerDao().saveOrUpdate(cus);

		String message = "Thêm mới khách hàng thành công.";
		if (isUpdate) {
			message = "Cập nhật khách hàng thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 1000);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", street);
		Events.sendEvent("onRefreshALL", parent, map);
		if (isUpdate) {
			resetPassDlg.onClose();
		}else{
			cus = null;
			lbTenDuong.setValue("");
			tbTenKH.setText("");
			tbDiaChi.setText("");
			tbDT.setText("");
			tbNhanVienKiemTra.setText("");
			tbQuanLyXacNhan.setText("");
			rdgGapChuNha.setSelectedIndex(-1);
			tbTienDo.setText("");
			dbNgayDan.setValue(null);
			dbNgayNhap.setValue(null);
			rdgKiemTraThongTin.setSelectedIndex(-1);
			
		}
	}

 

	public void getSelectedIndexInModel() {
		if (isUpdate) {
			int index = 0;
			final Long giayDanId = cus.getGiayDanId();
			GiayDan giayDan = new GiayDanDao().findById(giayDanId);
			String nhanVien = giayDan.getNhanVien();

			for (String nv : lstNhanVien) {
				if (nv.equals(nhanVien)) {
					lbNhanVien.setSelectedIndex(index);
					final List<GiayDan> lstGiayDan = new GiayDanDao().findGiayDan(nhanVien).getLstReturn();
					ListModelList lstModelLayHang = new ListModelList(lstGiayDan);
					lbNgayNhanGiay.setModel(lstModelLayHang);
					soLuongGiayDan.setValue("");
					lbNgayNhanGiay.addEventListener("onAfterRender", new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (firstLoad) {
								int index = 0;
								for (GiayDan gD : lstGiayDan) {
									if (gD.getId().equals(giayDanId)) {
										lbNgayNhanGiay.setSelectedIndex(index);
										soLuongGiayDan.setValue(gD.getSoLuong().toString());
										break;
									}
									index++;
								}
							}
							
							firstLoad = false;

						}
					});
					break;
				}

				index++;
			}
		}

		
	}

	public void onLbNhanVienSelect() {
		String nhanVien = (String) lbNhanVien.getSelectedItem().getValue();
		List<GiayDan> lstGiayDan = new GiayDanDao().findGiayDan(nhanVien).getLstReturn();
		ListModelList lstModelLayHang = new ListModelList(lstGiayDan);
		lbNgayNhanGiay.setModel(lstModelLayHang);
		soLuongGiayDan.setValue("");

		// if (lstGiayDan.size() == 1) {
		// lbNgayNhanGiay.setSelectedIndex(0);
		// soLuongGiayDan.setValue(lstGiayDan.get(0).getSoLuong().toString());
		// }

	}

	public void onGiayDanSelect() {
		GiayDan giayDan = (GiayDan) lbNgayNhanGiay.getSelectedItem().getValue();
		soLuongGiayDan.setValue(getStringFromLong(giayDan.getSoLuong()));
	}

	@Listen("onClick = #btnThemGiayDan")
	public void btnThemGiayDan() {

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", resetPassDlg);

		createWindow("windowView", "/Pages/module/phamarcy/them_giay_dan.zul", arguments, Window.MODAL);

	}

	@Listen("onAddUnit = #resetPassDlg")
	public void onAddUnit(Event event) {
		loadNhanVien();
	}

}

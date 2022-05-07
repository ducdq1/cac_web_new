package com.viettel.module.phamarcy.controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModels;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.viettel.core.base.DAO.AttachDAO;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.user.model.UserToken;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.ThauTho;
import com.viettel.module.phamarcy.BO.ThauThoKH;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ThauThoDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ThauThoKHDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.UnitDao;
import com.viettel.utils.Constants;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.ws.ProductService;
import com.viettel.ws.bo.HangHoaBO;

/**
 *
 * @author tuannt40
 */
public class AddThauThoKHController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbTenKH, tbNVPT, tbNoiDungTienHoaHong, diaChiDoiTac, tbDT, tbDiaChiThiCong, tbTienHoaHong;
	@Wire
	private Label lbDSP;
	@Wire
	private Datebox dbCreateDate, dbNgayGoiKH,dbNgayTangQua, dbNgayTangHoaHong, dbNgayTangQuaCuoiNam;
	@Wire
	private Combobox cbNhomDoiTac, cbLoaiQua, cbLoaiQuaCuoiNam;
	@Wire
	private org.zkoss.zul.Image QRCode;
	// @Wire
	// private Textbox tbHinhAnh, tbDonViTinh;
	@Wire
	private Listbox lbListImages;
	@Wire
	Canvas cvs;
	@Wire
	private Radiogroup rdg, rdgProductType;
	@Wire
	private Window createDlg;
	private Window parent;
	private ThauThoKH thauThoKH;
	boolean isUpdate = false;

	Boolean isCopy;

	String createUser = "";
	Long createUserId;

	public static final Long LOAI_DOI_TAC = 1L;
	public static final Long LOAI_QUA = 2L;
	public static final Long LOAI_QUA_CUOI_NAM = 3L;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		thauThoKH = (ThauThoKH) arguments.get("thauThoKH");
		parent = (Window) arguments.get("parent");

		if (thauThoKH != null) {
			viewData();
			isUpdate = true;
			createDlg.setTitle("Cập nhật khách hàng");
		}

		loadComboboxData();

		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		HttpSession httpSession = req.getSession(true);
		UserToken userToken = (UserToken) httpSession.getAttribute("userToken");
		createUser = "";
		if (userToken != null) {
			createUser += userToken.getUserName();
			createUser += " - ";
			createUser += userToken.getUserFullName();
			createUserId = userToken.getUserId();
		}

	}

	private void loadComboboxData() {
		initCombobox(cbNhomDoiTac, LOAI_DOI_TAC);
		initCombobox(cbLoaiQua, LOAI_QUA);
		initCombobox(cbLoaiQuaCuoiNam, LOAI_QUA_CUOI_NAM);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCombobox(Combobox combobox, Long type) {

		combobox.getItems().clear();
		List<ThauTho> units = new ThauThoDao().findAll(type, -1, -1).getLstReturn();

		Comparator comparator = new Comparator() {
			public int compare(Object text, Object item) {
				String val = text.toString();

				String itemText = ((ThauTho) item).getTen();
				return itemText.toLowerCase().contains(val.toLowerCase()) ? 0 : 1;
			}
		};

		combobox.setModel(ListModels.toListSubModel(new ListModelList(units), comparator, 20));

	}

	private void viewData() throws IOException {
		tbTenKH.setText(thauThoKH.getTen());
		tbDiaChiThiCong.setText(thauThoKH.getDiaChiThiCong());
		tbDT.setText(thauThoKH.getSdt());
		dbCreateDate.setValue(thauThoKH.getNgayNhap());
		tbTienHoaHong.setText(
				thauThoKH.getTienHoahong() != null ? formatNumber(thauThoKH.getTienHoahong(), "###,###,###.####") : "");
		dbNgayTangHoaHong.setValue(thauThoKH.getNgayTangHH());
		cbLoaiQua.setText(thauThoKH.getQua());
		dbNgayTangQua.setValue(thauThoKH.getNgayTangQua());
		dbNgayGoiKH.setValue(thauThoKH.getNgayGoiKH());
		cbLoaiQuaCuoiNam.setText(thauThoKH.getQuaCN());
		dbNgayTangQuaCuoiNam.setValue(thauThoKH.getNgayTangQuaCN());
		cbNhomDoiTac.setValue(thauThoKH.getNhomThauTho());
		diaChiDoiTac.setValue(thauThoKH.getDiaChi());
		tbNoiDungTienHoaHong.setValue(thauThoKH.getNoiDunHoaHong());
		cbNhomDoiTac.setValue(thauThoKH.getNhomThauTho());
		tbNVPT.setValue(thauThoKH.getNhanVienPhuTrach());
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		if (!validateTextBox(tbNVPT)) {
			return;
		}
		
		if (!validateNhomDoiTac() || !validateLoaiQua(cbLoaiQua) || !validateLoaiQua(cbLoaiQuaCuoiNam)) {
			return;
		}

		if (!validateTextBox(tbTenKH)) {
			return;
		}

		if (!validateTextBox(tbDT)) {
			return;
		}

		if (dbCreateDate.getValue() == null) {
			dbCreateDate.setErrorMessage("Trường bắt buộc nhập");
			return;
		}

		if (thauThoKH == null) {
			thauThoKH = new ThauThoKH();
		}

		thauThoKH.setTen(tbTenKH.getText().trim());
		thauThoKH.setDiaChi(diaChiDoiTac.getText().trim());
		thauThoKH.setDiaChiThiCong(tbDiaChiThiCong.getText().trim());
		thauThoKH.setSdt(tbDT.getText().trim());
		thauThoKH.setNgayNhap(dbCreateDate.getValue());
		thauThoKH.setTienHoahong(getLongFromString(tbTienHoaHong.getText().trim().replace(".", "")));
		thauThoKH.setNgayTangHH(dbNgayTangHoaHong.getValue());
		thauThoKH.setQua(cbLoaiQua.getText().trim());
		thauThoKH.setNgayTangQua(dbNgayTangQua.getValue());
		thauThoKH.setQuaCN(cbLoaiQuaCuoiNam.getText().trim());
		thauThoKH.setNgayTangQuaCN(dbNgayTangQuaCuoiNam.getValue());
		thauThoKH.setNhomThauTho(cbNhomDoiTac.getValue());
		thauThoKH.setNguoiNhap(createUser);
		thauThoKH.setUserId(createUserId);
		thauThoKH.setNoiDunHoaHong(tbNoiDungTienHoaHong.getText().trim());
		thauThoKH.setNhanVienPhuTrach(tbNVPT.getText().trim());
		thauThoKH.setNgayGoiKH(dbNgayGoiKH.getValue());
		
		new ThauThoKHDao().saveOrUpdate(thauThoKH);

		showNotification("Lưu thông tin thành công", Constants.Notification.INFO, 1500);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("thauThoKH", thauThoKH);
		Events.sendEvent("onRefreshALL", parent, map);
		createDlg.onClose();
	}

	private boolean validateNhomDoiTac() {
		String xuatXu = cbNhomDoiTac.getValue();
		boolean valid = false;

		for (Comboitem comboitem : cbNhomDoiTac.getItems()) {
			if (xuatXu.equals(comboitem.getLabel())) {
				valid = true;
			}
		}

		if (!valid) {
			cbNhomDoiTac.setErrorMessage("Nhóm đối tác không hợp lệ");
		}

		return valid;
	}

	private boolean validateLoaiQua(Combobox combobox) {
		String xuatXu = combobox.getValue();
		boolean valid = false;
		if (xuatXu.isEmpty()) {
			return true;
		}

		for (Comboitem comboitem : combobox.getItems()) {
			if (xuatXu.equals(comboitem.getLabel())) {
				valid = true;
			}
		}

		if (!valid) {
			combobox.setErrorMessage("Dữ liệu không hợp lệ");
		}

		return valid;
	}

	@Listen("onClick = #btnThemDoiTac")
	public void themTenSP() {
		themGiaTri(LOAI_DOI_TAC);
	}

	@Listen("onClick = #btnThemLoaiQua")
	public void themTenLoaiQua() {
		themGiaTri(LOAI_QUA);
	}

	@Listen("onClick = #btnThemLoaiQuaCuoiNam")
	public void themTenLoaiQuaCuoiNam() {
		themGiaTri(LOAI_QUA_CUOI_NAM);
	}

	private void themGiaTri(Long type) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", createDlg);
		arguments.put("type", type);
		createWindow("windowView", "/Pages/module/phamarcy/them_danh_muc_doi_tac.zul", arguments, Window.MODAL);

	}

	@SuppressWarnings("unchecked")
	@Listen("onAddUnit = #createDlg")
	public void onAddUnit(Event event) {
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		Long type = (Long) arguments.get("type");
		String value = (String) arguments.get("value");
		Comboitem item = new Comboitem();
		item.setLabel(value);

		if (type == LOAI_DOI_TAC) {
			initCombobox(cbNhomDoiTac, type);
		} else if (type == LOAI_QUA) {
			initCombobox(cbLoaiQua, type);
		} else if (type == LOAI_QUA_CUOI_NAM) {
			initCombobox(cbLoaiQuaCuoiNam, type);
		}
	}

}

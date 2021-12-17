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
	private Textbox tbMaSP, tbMaHangHoa, tbMaSPDaiLy, tbGiaNhapKM, tbGiaBLKM, tbGiaDL, tbGiaDLKM;
	@Wire
	private Label lbDSP;
	@Wire
	private Combobox cbNhomDoiTac;
	@Wire
	private Textbox tbGia, tbGiaNhap, tbDSP;
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
	private ThauThoKH product;
	boolean isUpdate = false;

	public static final int TYPE_DVT = 0;
	public static final int TYPE_KICK_THUOC = 1;
	public static final int TYPE_XUAT_XU = 2;
	public static final int TYPE_NOI_SX = 3;
	public static final int TYPE_TINH_NANG = 4;
	public static final int TYPE_TRONG_LUONG_THUNG = 5;
	public static final int TYPE_DONG_GOI_VIEN_THUNG = 6;
	public static final int TYPE_THONG_SO_KT = 7;
	public static final int TYPE_TEN_SP = 8;
	public static final int TYPE_BAO_HANH = 9;
	public static final int TYPE_MAU_SAC = 10;
	int productType;// 0: thiet bi, 1 Gach
	Boolean isCopy;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		product = (ThauThoKH) arguments.get("thauthoKH");
		parent = (Window) arguments.get("parent");

		/*
		 * if (product != null) { viewData(); isUpdate = true;
		 * createDlg.setTitle("Cập nhật sản phẩm"); }
		 */
		
		loadComboboxData();

	}

	private void loadComboboxData() {
		initCombobox();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCombobox() {
		 
		cbNhomDoiTac.getItems().clear();
		List<ThauTho> units = new ThauThoDao().findAll(null, -1, -1).getLstReturn();

		Comparator comparator = new Comparator() {
			public int compare(Object text, Object item) {
				String val = text.toString();
				String itemText = ((Unit) item).getValue();
				return itemText.toLowerCase().contains(val.toLowerCase()) ? 0 : 1;
			}
		};

		cbNhomDoiTac.setModel(ListModels.toListSubModel(new ListModelList(units), comparator, 20));
		if (units.size() == 1) {
			cbNhomDoiTac.setValue(units.get(0).getTen());
		}
	}

	private void viewData() throws IOException {

	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		if (!validateTextBox(tbMaHangHoa)) {
			return;
		}

		if (!validateTextBox(tbMaSP)) {
			return;
		}

		/*
		 * if (!validateTextBox(tbTenSP)) { return; }
		 */
		if (!validate()) {
			return;
		}

		showNotification("Lưu thông tin thành công", Constants.Notification.INFO, 1500);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		Events.sendEvent("onRefreshALL", parent, map);
		createDlg.onClose();
	}

	private boolean validate() {

		if (!validateCombobox()) {
			return false;
		}

		return true;
	}

	private boolean validateCombobox() {
		String xuatXu = cbNhomDoiTac.getValue();
		boolean valid = false;
		if (xuatXu.isEmpty()) {
			return true;
		}

		for (Comboitem comboitem : cbNhomDoiTac.getItems()) {
			if (xuatXu.equals(comboitem.getLabel())) {
				valid = true;
			}
		}

		if (!valid) {
			cbNhomDoiTac.setErrorMessage("Nhóm đối tác không hợp lệ");
			showNotification("Nhóm đối tác không hợp lệ", Constants.Notification.WARNING, 1000);
		}

		return valid;
	}

	@Listen("onClick = #btnThemDoiTac")
	public void themTenSP() {
		themGiaTri(TYPE_TEN_SP);
	}

	private void themGiaTri(int type) {
		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", createDlg);
		arguments.put("type", type);
		int producType;
		if (rdgProductType.getSelectedIndex() == 0 || rdgProductType.getSelectedIndex() == 1) {
			producType = 0;
		} else {
			producType = 1;
		}
		arguments.put("productType", producType);
		createWindow("windowView", "/Pages/module/phamarcy/them_danh_muc.zul", arguments, Window.MODAL);

	}

	@SuppressWarnings("unchecked")
	@Listen("onAddUnit = #createDlg")
	public void onAddUnit(Event event) {
		initCombobox();
	}

 
}

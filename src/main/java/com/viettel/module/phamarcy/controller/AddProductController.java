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
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
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
public class AddProductController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbMaSP, tbMaHangHoa, tbMaSPDaiLy, tbGiaNhapKM, tbGiaBLKM, tbGiaDL, tbGiaDLKM;
	@Wire
	private Label lbDSP;
	@Wire
	private Combobox cbXuatXu, cbKichThuoc, cbTinhNang, cbDonViTinh, cbTrongLuongThung, cbDongGoi, cbNoiSanXuat,
			cbThongSoKyThuat, cbTenSP, cbColor, cbBaoHanh;
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
	private Product product;
	boolean isUpdate = false;
	Boolean isImport;
	private List<Media> medias;
	private List<Attachs> attachs;
	private List<Attachs> attachsDelete;

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
		attachs = new ArrayList<Attachs>();
		attachsDelete = new ArrayList<Attachs>();
		super.doBeforeComposeChildren(comp);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		product = (Product) arguments.get("product");
		parent = (Window) arguments.get("parent");
		isCopy = (Boolean) arguments.get("isCopy");

		if (isCopy == null) {
			isCopy = false;
		}

		if (product != null) {
			viewData();
			isUpdate = true;
			createDlg.setTitle("Cập nhật sản phẩm");
		}

		loadComboboxData();

	}

	private void loadComboboxData() {
		initCombobox(TYPE_TEN_SP, cbTenSP);
		initCombobox(TYPE_DONG_GOI_VIEN_THUNG, cbDongGoi);
		initCombobox(TYPE_DVT, cbDonViTinh);
		initCombobox(TYPE_KICK_THUOC, cbKichThuoc);
		initCombobox(TYPE_NOI_SX, cbNoiSanXuat);
		initCombobox(TYPE_TINH_NANG, cbTinhNang);
		initCombobox(TYPE_TRONG_LUONG_THUNG, cbTrongLuongThung);
		initCombobox(TYPE_XUAT_XU, cbXuatXu);
		initCombobox(TYPE_THONG_SO_KT, cbThongSoKyThuat);
		initCombobox(TYPE_BAO_HANH, cbBaoHanh);
		initCombobox(TYPE_MAU_SAC, cbColor);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCombobox(int type, Combobox combobox) {
		if (rdgProductType.getSelectedIndex() == 0 || rdgProductType.getSelectedIndex() == 1) {
			productType = 0;
		} else {
			productType = 1;
		}
		List<Unit> units = new UnitDao().findUnit("", type, productType).getLstReturn();
		combobox.getItems().clear();

		Comparator comparator = new Comparator() {
			public int compare(Object text, Object item) {
				String val = text.toString();
				String itemText = ((Unit) item).getValue();
				return itemText.toLowerCase().contains(val.toLowerCase()) ? 0 : 1;
			}
		};

		combobox.setModel(ListModels.toListSubModel(new ListModelList(units), comparator, 20));
		if (units.size() == 1) {
			combobox.setValue(units.get(0).getValue());
		}
	}

	@Listen("onChange=#tbMaSP")
	public void onProductCodeChange() throws IOException {
		byte[] data = getQRCodeImage(tbMaSP.getText());
		if (data != null) {
			QRCode.setContent(new AImage("qrcode", data));
		}
	}

	@Listen("onClick = #btnSaveImage")
	public void onSaveImage() {
		Filedownload.save(QRCode.getContent().getByteData(), "application/png", tbMaSP.getText());
	}

	@Listen("onAfterRender=#lbListImages")
	public void results_onAfterRender() throws IOException {
		List<Listitem> listitems = lbListImages.getItems();
		for (Listitem item : listitems) {
			Attachs media = item.getValue();
			Listcell cell = (Listcell) item.getChildren().get(2);
			org.zkoss.zul.Image image = (org.zkoss.zul.Image) cell.getFirstChild();
			if (media.getAttachId() != null) {
				String dir_upload = ResourceBundleUtil.getString("dir_upload");
				File file = new File(dir_upload + media.getFullPathFile());
				if (file.exists()) {
					image.setContent(new org.zkoss.image.AImage(file));
				}
			} else {
				image.setContent(media.getContent());
			}
		}

	}

	private void viewData() throws IOException {
		tbMaHangHoa.setValue(product.getMaHangHoa());
		cbTenSP.setValue(product.getProductName());
		tbMaSP.setText(product.getProductCode());
		tbMaSPDaiLy.setText(product.getMaDaiLy());

		tbGiaNhapKM.setText(product.getPriceNHAPKM());
		tbGiaDL.setText(product.getPriceDL());
		tbGiaNhap.setText(product.getPrice());
		tbGia.setText(product.getSalePrice());// gia ban le
		tbGiaDLKM.setText(product.getPriceDLKM());
		tbGiaBLKM.setText(product.getPriceBLKM());

		cbBaoHanh.setValue(product.getWarranty());

		cbXuatXu.setValue(product.getMadeIn());
		rdg.setSelectedIndex(product.getVat() == null ? 0 : product.getVat().intValue());
		rdgProductType.setSelectedIndex(product.getProductType() == null ? 0 : product.getProductType().intValue());
		cbKichThuoc.setValue(product.getSize());
		cbTinhNang.setValue(product.getFeature());
		cbDongGoi.setValue(product.getDongGoi());
		cbNoiSanXuat.setValue(product.getNoiSanXuat());
		cbTrongLuongThung.setValue(product.getTrongLuongThung());
		cbDonViTinh.setValue(product.getUnit());
		cbColor.setValue(product.getColor());
		cbThongSoKyThuat.setValue(product.getThongSoKT());

		if (isCopy!=null && !isCopy) {
			attachs = new AttachDAOHE().findAllAttachByAttachCodeAndAttachTye(Constants.OBJECT_TYPE.CAC_IMAGE,
					product.getProductId());
			ListModelArray lstModel = new ListModelArray(attachs);
			lbListImages.setModel(lstModel);
		}

		byte[] data = getQRCodeImage(product.getProductCode());
		if (data != null) {
			QRCode.setContent(new AImage("qrcode", data));
		}

		if (product.getProductType() != null && product.getProductType().intValue() >= 2) {
			tbDSP.setText(product.getDsp());
			tbDSP.setVisible(true);
			lbDSP.setVisible(true);
		} else {
			tbDSP.setVisible(false);
			lbDSP.setVisible(false);
		}

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

		getProduct();

		if (isImport == null) {
			new ProductDao().saveOrUpdate(product);
		}

		String pathImage = saveImages(product.getProductId());
		if (pathImage != null) {
			product.setImage(pathImage);
			new ProductDao().saveOrUpdate(product);
		}

		try {
			//saveImageThongSoKT();
		} catch (Exception e) {
			showNotification("Không thể lưu hình ảnh thông số kỹ thuật", Constants.Notification.ERROR, 1500);
			return;
		}

		String message = "Thêm mới sản phẩm thành công.";
		if (isUpdate) {
			message = "Cập nhật sản phẩm thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 1500);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("product", product);
		Events.sendEvent("onRefreshALL", parent, map);
		createDlg.onClose();
	}

	private void getProduct() {
		if (product == null || isCopy) {
			product = new Product();
		}

		product.setMaHangHoa(tbMaHangHoa.getText().trim());
		product.setProductName(cbTenSP.getValue());
		product.setProductCode(tbMaSP.getText().toUpperCase());
		// product.setPrice(getLongFromString(tbGiaNhap.getText().trim()));

		product.setPrice(getPriceFromString(tbGiaNhap.getText().toString()));
		product.setSalePrice(getPriceFromString(tbGia.getText()));
		product.setPriceNHAPKM(getPriceFromString(tbGiaNhapKM.getText()));
		product.setPriceDL(getPriceFromString(tbGiaDL.getText()));
		product.setPriceDLKM(getPriceFromString(tbGiaDLKM.getText()));
		product.setPriceBLKM(getPriceFromString(tbGiaBLKM.getText()));

		product.setMaDaiLy(tbMaSPDaiLy.getText().trim());
		product.setSize(cbKichThuoc.getValue().toString());
		product.setFeature(cbTinhNang.getValue());
		product.setThongSoKT(cbThongSoKyThuat.getValue());

		int index = rdgProductType.getSelectedIndex();

		product.setProductType(Long.valueOf(rdgProductType.getSelectedIndex()));
		product.setVat(Long.valueOf(rdg.getSelectedIndex()));
		product.setWarranty(cbBaoHanh.getValue());
		product.setMadeIn(cbXuatXu.getValue());
		product.setDongGoi(cbDongGoi.getValue());
		product.setTrongLuongThung(cbTrongLuongThung.getValue());
		product.setNoiSanXuat(cbNoiSanXuat.getValue());
		product.setUnit(cbDonViTinh.getValue());
		product.setCreateDate(new Date());
		product.setColor(cbColor.getValue().toString());
		if (index >= 2) {
			product.setDsp(tbDSP.getText());
		}

		HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		HttpSession httpSession = req.getSession(true);
		UserToken userToken = (UserToken) httpSession.getAttribute("userToken");
		if (userToken != null) {
			product.setCreateUser(userToken.getUserName() + " - " + userToken.getUserFullName());
		}

		// String productNameSearch = new
		// BaseGenericForwardComposer().removeVietnameseChar(product.getProductName());
		// product.setProductNameSearch(productNameSearch);

	}

	private boolean validate() {

		if (product == null || (isCopy != null && isCopy)) {
			product = new Product();
		}

		if (new ProductDao().checkExistMaHangHoa(tbMaHangHoa.getText().trim().toLowerCase(), product.getProductId())) {
			tbMaHangHoa.setErrorMessage("Mã hàng hóa này đã tồn tại. Vui lòng nhập mã khác");
			return false;
		}

		/*HangHoaBO hangHoa =  ProductService.layThongTinTonKho(tbMaHangHoa.getText().trim());
		if (hangHoa != null) {
			if (hangHoa.getSo_luong() == null && hangHoa.getsError() != null) {
				tbMaHangHoa.setErrorMessage(hangHoa.getsError());
				return false;
			}
		}*/

		if (new ProductDao().checkExistProductCode(tbMaSP.getText().trim().toLowerCase(), product.getProductId())) {
			tbMaSP.setErrorMessage("Mã sản phẩm này đã tồn tại. Vui lòng nhập mã khác");
			return false;
		}

		if (!validateCombobox(cbTenSP, "Tên sản phẩm")) {
			return false;
		}
		if (!validateCombobox(cbXuatXu, "Xuất xứ")) {
			return false;
		}

		if (!validateCombobox(cbTinhNang, "Tính năng")) {
			return false;
		}

		if (!validateCombobox(cbKichThuoc, "Kích thước")) {
			return false;
		}

		if (cbDonViTinh.getValue() == null || cbDonViTinh.getValue().isEmpty()) {
			showNotification("Chưa nhập Đơn Vị Tính", Constants.Notification.WARNING, 1000);
			return false;
		}
		if (!validateCombobox(cbDonViTinh, "Đơn vị tính")) {
			return false;
		}

		if (!validateCombobox(cbTrongLuongThung, "Trọng lượng thùng")) {
			return false;
		}
		if (!validateCombobox(cbDongGoi, "Đóng gói viên/thùng")) {
			return false;
		}
		if (!validateCombobox(cbNoiSanXuat, "Nơi sản xuất")) {
			return false;
		}
		if (!validateCombobox(cbThongSoKyThuat, "Thông số kỹ thuật")) {
			return false;
		}
		if (!validateCombobox(cbBaoHanh, "Thông tin bảo hành")) {
			return false;
		}
		if (!validateCombobox(cbColor, "Mô tả màu sắc")) {
			return false;
		}
		return true;
	}

	private boolean validateCombobox(Combobox cb, String message) {
		String xuatXu = cb.getValue();
		boolean valid = false;
		if (xuatXu.isEmpty()) {
			return true;
		}
		for (Comboitem comboitem : cb.getItems()) {
			if (xuatXu.equals(comboitem.getLabel())) {
				valid = true;
			}
		}
		if (!valid) {
			showNotification(message + " không hợp lệ", Constants.Notification.WARNING, 1000);
		}
		return valid;
	}

	private String saveImages(Long objectId) {
		AttachDAO adhe = new AttachDAO();
		Attachs attach = null;
		for (Attachs att : attachs) {
			try {
				if (att.getAttachId() == null) {
					attach = adhe.saveFileAttach(att.getContent(), objectId, Constants.OBJECT_TYPE.CAC_IMAGE, null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		AttachDAOHE dao = new AttachDAOHE();
		for (Attachs att : attachsDelete) {
			att.setIsActive(0L);
			dao.saveOrUpdate(att);
		}

		if (attach != null) {
			return attach.getFullPathFile();
		}

		return null;
	}

	@Listen("onUpload = #btnUpload")
	public void handle(UploadEvent evt) throws IOException {
		Media[] mediasUpload = evt.getMedias();
		for (Media media : mediasUpload) {
			if (media instanceof org.zkoss.image.Image) {
				Attachs att = new Attachs();
				att.setAttachName(media.getName());
				att.setContent((Image) media);
				attachs.add(att);
			}
		}

		ListModelArray lstModel = new ListModelArray(attachs);
		lbListImages.setModel(lstModel);
	}

	@Listen("onDeleteImage = #lbListImages")
	public void onDeleteImage(Event evt) throws IOException {
		Long index = (Long) evt.getData();
		index = index - 1;
		Attachs att = attachs.get(index.intValue());

		if (att.getAttachId() != null) {
			attachsDelete.add(att);
		}
		attachs.remove(index.intValue());
		ListModelArray lstModel = new ListModelArray(attachs);
		lbListImages.setModel(lstModel);

	}

	private byte[] getQRCodeImage(String text) {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix;
		try {
			bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 550, 550);

			ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
			byte[] pngData = pngOutputStream.toByteArray();
			return pngData;
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Listen("onClick = #btnThemXuatXu")
	public void themXuatXu() {
		themGiaTri(TYPE_XUAT_XU);
	}

	@Listen("onClick = #btnThemTinhNang")
	public void themTinhNang() {
		themGiaTri(TYPE_TINH_NANG);
	}

	@Listen("onClick = #btnThemKichThuoc")
	public void themKichThuoc() {
		themGiaTri(TYPE_KICK_THUOC);
	}

	@Listen("onClick = #btnThemDonViTinh")
	public void themDonViTinh() {
		themGiaTri(TYPE_DVT);
	}

	@Listen("onClick = #btnThemTrongLuongThung")
	public void themTrongLuongThung() {
		themGiaTri(TYPE_TRONG_LUONG_THUNG);
	}

	@Listen("onClick = #btnThemNoiSanXuat")
	public void themNoiSanXuat() {
		themGiaTri(TYPE_NOI_SX);
	}

	@Listen("onClick = #btnThemDongGoi")
	public void themDongGoi() {
		themGiaTri(TYPE_DONG_GOI_VIEN_THUNG);
	}

	@Listen("onClick = #btnThemThongSoKT")
	public void themThongSoKT() {
		themGiaTri(TYPE_THONG_SO_KT);
	}

	@Listen("onClick = #btnThemMauSac")
	public void themMauSac() {
		themGiaTri(TYPE_MAU_SAC);
	}

	@Listen("onClick = #btnThemBaoHanh")
	public void themBaoHanh() {
		themGiaTri(TYPE_BAO_HANH);
	}

	@Listen("onClick = #btnThemTenSP")
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
		HashMap<String, Object> arguments = (HashMap<String, Object>) event.getData();
		int type = (int) arguments.get("type");
		String value = (String) arguments.get("value");
		Comboitem item = new Comboitem();
		item.setLabel(value);

		if (type == AddProductController.TYPE_XUAT_XU) {
			initCombobox(type, cbXuatXu);
		} else if (type == AddProductController.TYPE_DVT) {
			initCombobox(type, cbDonViTinh);
		} else if (type == AddProductController.TYPE_DONG_GOI_VIEN_THUNG) {
			initCombobox(type, cbDongGoi);
		} else if (type == AddProductController.TYPE_KICK_THUOC) {
			initCombobox(type, cbKichThuoc);
		} else if (type == AddProductController.TYPE_NOI_SX) {
			initCombobox(type, cbNoiSanXuat);
		} else if (type == AddProductController.TYPE_TINH_NANG) {
			initCombobox(type, cbTinhNang);
		} else if (type == AddProductController.TYPE_TRONG_LUONG_THUNG) {
			initCombobox(type, cbTrongLuongThung);
		} else if (type == AddProductController.TYPE_THONG_SO_KT) {
			initCombobox(type, cbThongSoKyThuat);
		} else if (type == AddProductController.TYPE_TEN_SP) {
			initCombobox(type, cbTenSP);
		} else if (type == AddProductController.TYPE_BAO_HANH) {
			initCombobox(type, cbBaoHanh);
		} else if (type == AddProductController.TYPE_MAU_SAC) {
			initCombobox(type, cbColor);
		}

	}

	@SuppressWarnings("deprecation")
	public File createImage(Product product, boolean isTemp) throws IOException {
		int index = rdgProductType.getSelectedIndex();

		if (index == 0 || index == 1) {
			return createThietBiImage(isTemp);
		}

		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path = request.getRealPath("/WEB-INF/template/thong_so_kt_gach.png");

		File inPut = new File(path);

		final BufferedImage image = ImageIO.read(inPut);

		Graphics g = image.getGraphics();
		g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		g.setColor(Color.black);
		String maSP = product.getProductCode();
		String dsp = tbDSP.getText().toString();
		if (dsp.length() > 0) {
			String dspArray[] = dsp.split("-");
			if (dspArray.length > 1) {
				if (maSP.length() > 4) {
					String prefix = maSP.substring(0, 4);
					String lastFix = maSP.substring(4, maSP.length());
					maSP = prefix + dspArray[0] + lastFix + dspArray[1];
				}
			} else {
				// maSP = dspArray[0] + maSP;
				if (maSP.length() > 4) {
					String prefix = maSP.substring(0, 4);
					String lastFix = maSP.substring(4, maSP.length());
					maSP = prefix + dspArray[0] + lastFix;
				}
			}
		} else {
			maSP = product.getProductCode();
		}

		g.drawString(StringUtils.getValue(product.getProductName()), 190, 50);
		g.drawString(StringUtils.getValue(maSP), 190, 80);
		g.drawString(StringUtils.getValue(product.getSize()), 190, 115);
		// g.drawString(StringUtils.getValue(product.getColor()), 190, 145);

		String colorArray[] = StringUtils.getValue(product.getColor()).split(";");
		if (colorArray.length > 1) {
			int y = 145;
			g.drawString(colorArray[0].trim(), 190, 145);
			for (int i = 1; i < colorArray.length; i++) {
				g.drawString(colorArray[i].trim(), 190, y += 20);
				if (i == 2) {
					break;
				}
			}
		} else {
			g.drawString(StringUtils.getValue(product.getColor()), 190, 145);
		}

		// g.drawString("Màu sắc 2 ", 190, 165);
		// g.drawString("Màu sắc 3 ", 190, 185);
		// g.drawString(StringUtils.getValue(product.getFeature()), 190, 215);
		// g.drawString("Tính năng 2", 190, 235);
		// g.drawString("Tính năng 3", 190, 255);

		String featureArray[] = StringUtils.getValue(product.getFeature()).split(";");
		if (featureArray.length > 1) {
			int y = 215;
			g.drawString(featureArray[0].trim(), 190, 215);
			for (int i = 1; i < featureArray.length; i++) {
				g.drawString(featureArray[i].trim(), 190, y += 20);
				if (i == 2) {
					break;
				}
			}
		} else {
			g.drawString(StringUtils.getValue(product.getFeature()), 190, 215);
		}

		g.drawString(StringUtils.getValue(product.getThongSoKT()), 190, 280);
		g.drawString(StringUtils.getValue(product.getTrongLuongThung()), 190, 315);
		g.drawString(StringUtils.getValue(product.getDongGoi()), 190, 350);
		g.drawString(StringUtils.getValue(product.getNoiSanXuat()), 190, 380);
		g.dispose();
		File outPut = null;
		if (isTemp) {
			outPut = new File(ResourceBundleUtil.getString("dir_upload") + "/" + Constants.OBJECT_TYPE_STR.IMAGE_STR
					+ "/temp/TSKT_" + new Date().getTime() + ".png");
		} else {
			outPut = new File(ResourceBundleUtil.getString("dir_upload") + "/" + Constants.OBJECT_TYPE_STR.IMAGE_STR
					+ "/TSKT_" + new Date().getTime() + ".png");
		}

		//ImageIO.write(image, "png", outPut);
		System.out.println("Done");
		return outPut;
	}

	@SuppressWarnings("deprecation")
	private File createThietBiImage(boolean isTemp) throws IOException {
		HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
		String path = request.getRealPath("/WEB-INF/template/thong_so_kt_thietbi.png");

		File inPut = new File(path);

		final BufferedImage image = ImageIO.read(inPut);

		Graphics g = image.getGraphics();
		g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		g.setColor(Color.black);
		g.drawString(StringUtils.getValue(product.getProductName()), 220, 30);
		g.drawString(StringUtils.getValue(product.getProductCode()), 220, 63);
		g.drawString(StringUtils.getValue(product.getNoiSanXuat()), 220, 325);
		g.drawString(StringUtils.getValue(product.getWarranty()), 220, 360);

		// g.drawString(StringUtils.getValue(product.getFeature()), 220, 100);

		String featureArray[] = StringUtils.getValue(product.getFeature()).split(";");
		if (featureArray.length > 1) {
			int y = 100;
			g.drawString(featureArray[0].trim(), 220, 100);
			for (int i = 1; i < featureArray.length; i++) {
				g.drawString(featureArray[i].trim(), 220, y += 25);
				if (i == 3) {
					break;
				}
			}
		} else {
			g.drawString(StringUtils.getValue(product.getFeature()), 220, 100);
		}

		String tsktArray[] = StringUtils.getValue(product.getThongSoKT()).split(";");
		if (tsktArray.length > 1) {
			int y = 210;
			g.drawString(tsktArray[0].trim(), 220, 210);
			for (int i = 1; i < tsktArray.length; i++) {
				g.drawString(tsktArray[i].trim(), 220, y += 25);
				if (i == 3) {
					break;
				}
			}
		} else {
			g.drawString(StringUtils.getValue(product.getThongSoKT()), 220, 210);
		}

		g.dispose();

		File outPut = null;
		if (isTemp) {
			outPut = new File(ResourceBundleUtil.getString("dir_upload") + "/" + Constants.OBJECT_TYPE_STR.IMAGE_STR
					+ "/temp/TSKT_" + new Date().getTime() + ".png");
		} else {
			outPut = new File(ResourceBundleUtil.getString("dir_upload") + "/" + Constants.OBJECT_TYPE_STR.IMAGE_STR
					+ "/TSKT_" + new Date().getTime() + ".png");
		}

		ImageIO.write(image, "png", outPut);
		System.out.println("Done");
		return outPut;
	}

	public static void main(String[] args) throws IOException {
		// createImage("Mít tờ lép");
		File inPut = new File("E:\\DATA\\SOFT\\tomcat7\\tomcat7 - Copy\\webapps\\temp.png");

		final BufferedImage image = ImageIO.read(inPut);

		Graphics g = image.getGraphics();
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		g.setColor(Color.black);
		g.drawString("Tên sản phẩm", 190, 50);
		g.drawString("Mã sản phẩm", 190, 80);
		g.drawString("Kích thước", 190, 115);
		g.drawString("Màu sắc 1 ", 190, 145);
		g.drawString("Màu sắc 2 ", 190, 165);
		g.drawString("Màu sắc 3 ", 190, 185);
		g.drawString("Tính năng 1", 190, 215);
		g.drawString("Tính năng 2", 190, 235);
		g.drawString("Tính năng 3", 190, 255);
		g.drawString("Tính năng 3", 190, 255);
		g.drawString("Thông số kỹ thuật", 190, 285);
		g.drawString("Trọng lượng", 190, 315);
		g.drawString("Đóng gói", 190, 350);
		g.drawString("Nơi sản xuất", 190, 380);
		g.dispose();
		File outPut = new File(
				"E:\\DATA\\SOFT\\tomcat7\\tomcat7 - Copy\\webapps\\images\\temp\\" + new Date().getTime() + ".png");

		ImageIO.write(image, "png", outPut);
		System.out.println("Done");
	}

	private void saveImageThongSoKT() throws IOException {
		if (attachs != null) {
			for (Attachs att : attachs) {
				if (Constants.OBJECT_TYPE.CAC_IMAGE_TSKT.equals(att.getAttachType())) {
					new AttachDAOHE().delete(att);
					String dir_upload = ResourceBundleUtil.getString("dir_upload");
					File file = new File(dir_upload + att.getFullPathFile());
					try {
						if (file.exists()) {
							file.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		File f = createImage(product, false);
		Attachs attach = new Attachs();
		attach.setAttachName(f.getName());
		attach.setIsActive(Constants.Status.ACTIVE);
		attach.setObjectId(product.getProductId());
		attach.setCreatorId(getUserId());
		attach.setCreatorName("");
		attach.setDateCreate(new Date());
		attach.setModifierId(getUserId());
		attach.setDateModify(new Date());
		attach.setAttachCat(Constants.OBJECT_TYPE.CAC_IMAGE);// van ban den
		attach.setAttachType(Constants.OBJECT_TYPE.CAC_IMAGE_TSKT);
		attach.setAttachPath("/" + Constants.OBJECT_TYPE_STR.IMAGE_STR + "/");
		new AttachDAOHE().saveOrUpdate(attach);

	}

	@Listen("onClick = #btnViewFile")
	public void viewImageFile() {
		try {
			getProduct();
			File f = createImage(product, true);

			String port = (Executions.getCurrent().getServerPort() == 80) ? ""
					: (":" + Executions.getCurrent().getServerPort());
			String url = Executions.getCurrent().getScheme() + "://" + Executions.getCurrent().getServerName() + port;// +
																														// Executions.getCurrent().getDesktop().getRequestPath();
			url += "/images/temp/" + f.getName();
			Executions.getCurrent().sendRedirect(url, "_blank");
			// Filedownload.save(f, "application/application/png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Listen("onCheck =#rdgProductType")
	public void onCheck() {
		loadComboboxData();
	}
}

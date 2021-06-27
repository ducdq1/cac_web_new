/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.phamarcy.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportFileDAO;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.utils.Constants;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;

/**
 *
 * @author ducdq1 Import du lieu tu file
 */
public class importDataController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8335569097012109019L;
	@Wire
	private Vlayout flist;
	private List<Media> listMedia;
	@Wire
	private Listbox lbList;
	@Wire
	private Label lbError, lbCount;
	private Product objUpdate;
	private List<Product> lstImport;
	private List<String> lstProductCode;
	@Wire
	private Window phamarcyAll;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		listMedia = new ArrayList();
		lstImport = new ArrayList<Product>();
		lstProductCode = new ProductDao().getAllProductCode();
	}

	/**
	 * upload file
	 * 
	 * @param event
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onUpload = #btnAttach")
	public void onUpload(UploadEvent event) throws UnsupportedEncodingException {
		listMedia.clear();
		final Media[] medias = event.getMedias();
		for (final Media media : medias) {
			String extFile = FileUtil.getFileExtention(media.getName());
			if (!"xlsx".equals(extFile.toLowerCase())) {
				showNotification("Định dạng file không được phép tải lên, chỉ cho phép định dạng .xlsx",
						Constants.Notification.WARNING);
				continue;
			}

			// luu file vao danh sach file
			listMedia.add(media);
			flist.getChildren().clear();
			// layout hien thi ten file va nut "Xóa";
			final Hlayout hl = new Hlayout();
			hl.setSpacing("6px");
			hl.setClass("z-valign-left");
			Label lb = new Label(media.getName());
			lb.setMaxlength(50);
			hl.appendChild(lb);
			A rm = new A("Xóa");
			rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					hl.detach();
					listMedia.remove(media);
				}
			});
			hl.appendChild(rm);
			flist.appendChild(hl);

			XSSFWorkbook wb = onCreateFromImportExcel();
			importData(wb);
		}
	}

	@Listen("onClick=#btnImport")
	public void onImport() {

		if (listMedia == null || listMedia.isEmpty()) {
			showNotification("Chọn tệp tải lên trước khi thêm mới!", Constants.Notification.WARNING);
			return;
		}

		try {

			if (lstImport == null || lstImport.isEmpty()) {
				showNotification("Không có dữ liệu để nhập .Vui lòng kiểm tra lại!", Constants.Notification.WARNING,
						3000);
				return;
			}

			for (Product priceDeclareDataImport : lstImport) {
				new ProductDao().saveOrUpdate(priceDeclareDataImport);

			}

			showNotification(String.format("Nhập dữ liệu thành công %s sản phẩm!", lstImport.size()),
					Constants.Notification.INFO, 3000);
		} catch (Exception e) {
			showNotification(String.format("Có  lỗi xãy ra khi lưu dữ liệu!", lstImport.size()),
					Constants.Notification.ERROR, 3000);
		}

	}

	/**
	 * import Gia CIF hai quan
	 * 
	 * @param wb
	 */
	private void importData(XSSFWorkbook wb) {
		int j = 0;
		try {
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				j = 2;

				XSSFSheet sheet = wb.getSheetAt(i);

				String error = "";
				while (sheet.getRow(j) != null && sheet.getRow(j).getCell(1) != null
						&& !"".equals(sheet.getRow(j).getCell(1).toString().trim())) {
					String code = sheet.getRow(j).getCell(1).toString().trim();

					if (lstProductCode.contains(code)) {
						error += code + " , ";
						j++;
						continue;
					}

					Product item = new Product();

					item.setProductCode(code);
					item.setQuotationName(sheet.getRow(j).getCell(2).toString().trim());
					item.setProductName(sheet.getRow(j).getCell(3).toString().trim());
					String productNameSearch = new BaseGenericForwardComposer()
							.removeVietnameseChar(item.getProductName());
					item.setProductNameSearch(productNameSearch);
					item.setUnit(sheet.getRow(j).getCell(4).toString().trim());

					String price = sheet.getRow(j).getCell(5).getRawValue();

					try {
						if (price != null && !price.isEmpty()) {
							item.setPrice(Long.valueOf(price));
						}

					} catch (Exception e) {
						error += " Giá sản phẩm " + code + " lỗi ";
						item.setPrice(0L);
					}

					lstImport.add(item);
					lstProductCode.add(code);

					j++;
				}

				if (!error.isEmpty()) {
					lbError.setValue("Mã đơn hàng bị trùng: " + error);
				}

				// luu xuong DB

				reloadListView(lstImport);
				lbCount.setValue("Tổng cộng: " + lstImport.size() + " sản phẩm");
			}
		} catch (Exception ex) {
			showDialogWithNoEvent(
					String.format("Lỗi dữ liệu nhập không đúng định dạng ở dòng thứ %s.Vui lòng kiểm tra lại!", j + 1),
					"Lỗi");
		}

	}

	/**
	 * create upload file
	 * 
	 * @param event
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public XSSFWorkbook onCreateFromImportExcel() {
		XSSFWorkbook wb = null;
		Media media = listMedia.get(0);
		InputStream inputStream = null;
		try {
			inputStream = media.getStreamData();
			wb = new XSSFWorkbook(inputStream);
		} catch (Exception ex) {
			LogUtils.addLog(ex);
			showNotification("File Excel nhập không đúng biểu mẫu. Xin vui lòng kiểm tra lại!",
					Constants.Notification.ERROR, 3000);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LogUtils.addLog(e);
				showNotification("File Excel nhập không đúng biểu mẫu. Xin vui lòng kiểm tra lại!",
						Constants.Notification.ERROR, 3000);
			}
		}

		return wb;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void reloadListView(List<Product> lstProducts) {
		if (lstProducts == null) {
			lstProducts = new ArrayList<>();
		}
		ListModelArray lstModel = new ListModelArray(lstProducts);
		lbList.setModel(lstModel);
	}

	@Listen("onClick=#btnPrint")
	public void onPrint() {
		List<Product> lstProduct = new ProductDao().getAllProduct();

		if (lstProduct.isEmpty()) {
			showNotification("Không có sản phẩm nào để xem", Constants.Notification.ERROR, 3000);
			return;
		}
		for (Product product : lstProduct) {
			product.getPriceString();
		}

		new ExportFileDAO().exportKiemTraHangNhap(lstProduct);
	}

	@Listen("onClick=#btnDelete")
	public void onDelete() {
		if (lstImport == null || lstImport.isEmpty()) {
			showDialogWithNoEvent("Không có sản phẩm nào để xóa ", "Thông báo");
			return;
		}
		String message = "Bạn muốn xóa tất cả sản phẩm vừa nhập? ";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					try {

						for (Product obj : lstImport) {
							if (obj.getProductId() != null) {
								new ProductDao().deleteById(obj.getProductId());
							}
						}

						if (lstImport != null) {
							lstImport.clear();
						}

						if (lstProductCode != null) {
							lstProductCode.clear();
							lstProductCode = new ProductDao().getAllProductCode();
						}

						reloadListView(lstImport);

						showNotification("Xóa dữ liệu thành công!", Constants.Notification.INFO, 3000);
					} catch (Exception e) {
						showNotification("Có lỗi xãy ra!", Constants.Notification.ERROR, 3000);
					}

				}

			}
		};

		showDialogConfirm(message, null, clickListener);

	}

	@Listen("onOpenUpdate = #lbList")
	public void onOpenUpdate(Event event) {
		objUpdate = (Product) event.getData();

		HashMap<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("parent", phamarcyAll);
		arguments.put("product", objUpdate);
		arguments.put("isImport", true);
		createWindow("windowView", "/Pages/module/phamarcy/addProduct.zul", arguments, Window.MODAL);

	}

	@Listen("onDelete = #lbList")
	public void onDelete(Event event) {
		final Product obj = (Product) event.getData();
		String message = String.format(Constants.Notification.DELETE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.FILE)
				+ " này?";
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {

			public void onEvent(ClickEvent event) throws Exception {

				if (Messagebox.Button.YES.equals(event.getButton())) {
					if (obj.getProductId() != null) {
						new ProductDao().delete(obj);
					}

					lstImport.remove(obj);
					showNotification("Xóa sản phẩm thành công", Constants.Notification.INFO, 2000);
					reloadListView(lstImport);
				}

			}
		};
		showDialogConfirm(message, null, clickListener);
	}

	@Listen("onRefreshALL =#phamarcyAll")
	public void onRefreshAll(Event event) {
		HashMap<String, Object> map = (HashMap<String, Object>) event.getData();
		objUpdate = (Product) map.get("product");
		reloadListView(lstImport);
	}

}

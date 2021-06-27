package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import com.viettel.convert.service.PdfDocxFile;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.DAO.BaseGenericForwardComposer;
import com.viettel.module.phamarcy.BO.Customer;
import com.viettel.module.phamarcy.BO.Order;
import com.viettel.module.phamarcy.BO.OrderDetail;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Quotation;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.VOrderDetail;
import com.viettel.module.phamarcy.utils.EncryptionUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.WordExportUtils;

/**
 *
 * @author ChucHV
 */
public class ExportFileDAO extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 500897541201458273L;
	private String BAO_GIA_CTY = "/WEB-INF/template/BAO_GIA.docx";
	private String BAO_GIA_KHACH_LE = "/WEB-INF/template/BAO_GIA_KHACH_LE.docx";
	private String DON_HANG = "/WEB-INF/template/DON_HANG.docx";
	private String DOANH_THU = "/WEB-INF/template/DOANH_THU.docx";
	private String KIEM_TRA_HANG_NHAP = "/WEB-INF/template/KIEM_TRA_HANG_NHAP.docx";
	private String KIEM_TRA_DIA_CHI_KH = "/WEB-INF/template/KIEM_TRA_DIA_CHI_KH.docx";
	private String KIEM_TRA_DIA_CHI_KH_DA_LAY_HANG = "/WEB-INF/template/KIEM_TRA_DIA_CHI_KH_DA_LAY_HANG.docx";
	private String KIEM_TRA_DIA_CHI_KH_KO_LAY_HANG = "/WEB-INF/template/KIEM_TRA_DIA_CHI_KH_KO_LAY_HANG.docx";
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

	}

	/**
	 *
	 * @param fileModel
	 * @param allowDownload
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	public PdfDocxFile exportBaoGia(Quotation quotation, List<QuotationDetail> lstQuotationDetail, int isView) {
		PdfDocxFile outputStreamPdf = null;
		try {
			Date mDate = quotation.getQuotationDate();

			String date = String.valueOf(mDate.getDate());
			String month = String.valueOf(mDate.getMonth() + 1);
			String year = String.valueOf(mDate.getYear() + 1900);

			if (month.length() == 1) {
				month = "0" + month;
			}

			List<QuotationDetail> lstTemp = new ArrayList<>();
			BigDecimal total = new BigDecimal(0);
			if (lstQuotationDetail != null && !lstQuotationDetail.isEmpty()) {
				String pattern = "###,###,###";
				for (QuotationDetail quotationDetail : lstQuotationDetail) {
					QuotationDetail obj = new QuotationDetail();

					obj.setPriceString(formatNumber(quotationDetail.getPrice(), pattern));
					obj.setAmountStr(formatNumber(quotationDetail.getAmount(), "###,###,###.####"));
					obj.setValueString(formatNumber(quotationDetail.getValue(), pattern));
					obj.setProductName(quotationDetail.getProductName());
					obj.setUnit(quotationDetail.getUnit());

					if (isView == 1) {
						obj.setProductName(quotationDetail.getName());
					}

					BigDecimal value = quotationDetail.getValue();

					if (value != null) {
						total = total.add(value);
					}

					lstTemp.add(obj);
				}
			}

			WordExportUtils wU = new WordExportUtils();
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String path = "";
			if (quotation.getType().longValue() == 0) {
				path = request.getRealPath(BAO_GIA_CTY);
			} else {
				path = request.getRealPath(BAO_GIA_KHACH_LE);
			}

			// Neu co dinh nghia bieu mau thi lay tu dinh nghia

			FileInputStream fileTemplate = new FileInputStream(new File(path));
			WordprocessingMLPackage wmp = WordprocessingMLPackage.load(fileTemplate);
			WordExportUtils.resolveFragmentText(wmp);
			wU.replacePlaceholder(wmp, "" + quotation.getQuotationNumber(), "MaBG");

			String sdt = quotation.getCompanyPhone();
			if (sdt != null && !sdt.isEmpty()) {
				wU.replacePlaceholder(wmp,
						"Kính gửi: " + quotation.getCompanyName() + " - SĐT: " + quotation.getCompanyPhone(),
						"companyName");
			} else {
				wU.replacePlaceholder(wmp, "Kính gửi: " + quotation.getCompanyName(), "companyName");
			}

			wU.replacePlaceholder(wmp, "Kính gửi: " + quotation.getCompanyName(), "companyName");

			wU.replacePlaceholder(wmp,
					"Địa chỉ: " + (quotation.getCompanyAdd() == null ? "" : quotation.getCompanyAdd()), "companyAdd");
			wU.replacePlaceholder(wmp, "Đà Nẵng, ngày " + date + " tháng " + month + " năm " + year, "ngaybaogia");

			wU.replacePlaceholder(wmp, quotation.getNote() == null ? "" : quotation.getNote(), "GhiChu");

			if (quotation.getExpireDate() != null) {
				mDate = quotation.getExpireDate();
				date = String.valueOf(mDate.getDate());
				month = String.valueOf(mDate.getMonth() + 1);
				year = String.valueOf(mDate.getYear() + 1900);
				if (month.length() == 1) {
					month = "0" + month;
				}
				wU.replacePlaceholder(wmp, date + " tháng " + month + " năm " + year, "ngayhieuluc");
			} else {
				wU.replacePlaceholder(wmp, ".../.../... ", "ngayhieuluc");
			}

			wU.replacePlaceholder(wmp, formatNumber(total, "###,###,###"), "$total");
			wU.replaceTable(wmp, 0, lstTemp);
			String outPutPath = wU.writePDFToStream(
					new BaseGenericForwardComposer().removeVietnameseChar(quotation.getCompanyName()), wmp, true);
			if (!outPutPath.isEmpty()) {
				viewPdfOnBrowser(outPutPath);
			}
		} catch (Docx4JException | JAXBException | IOException en) {
			LogUtils.addLog(en);
		}
		return outputStreamPdf;
	}

	/**
	 *
	 * @param fileModel
	 * @param allowDownload
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	public boolean exportDonHang(Order quotation, List<OrderDetail> lstQuotationDetail, int isView) {
		try {
			Date mDate = quotation.getOrderDate();
			String date = String.valueOf(mDate.getDate());
			String month = String.valueOf(mDate.getMonth() + 1);
			String year = String.valueOf(mDate.getYear() + 1900);

			if (month.length() == 1) {
				month = "0" + month;
			}

			List<OrderDetail> lstTemp = new ArrayList<>();
			BigDecimal total = new BigDecimal(0);

			if (lstQuotationDetail != null && !lstQuotationDetail.isEmpty()) {
				String pattern = "###,###,###";
				for (OrderDetail quotationDetail : lstQuotationDetail) {
					OrderDetail obj = new OrderDetail();
					obj.setAmount(quotationDetail.getAmount());
					obj.setPrice(quotationDetail.getPrice());
					obj.setPriceString(formatNumber(quotationDetail.getPrice(), pattern));
					obj.setSl(formatNumber(quotationDetail.getAmount(), "###,###,###.####"));
					obj.getValueString();
					obj.setProductName(quotationDetail.getProductName());
					obj.setUnit(quotationDetail.getUnit());
					obj.setNote(quotationDetail.getNote());

					if (isView == 1) {
						obj.setProductName(quotationDetail.getName());
					}

					BigDecimal value = quotationDetail.getValue();

					if (value != null) {
						total = total.add(value);
					}

					lstTemp.add(obj);
				}
			}

			WordExportUtils wU = new WordExportUtils();
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String path = request.getRealPath(DON_HANG);

			// Neu co dinh nghia bieu mau thi lay tu dinh nghia

			FileInputStream fileTemplate = new FileInputStream(new File(path));
			WordprocessingMLPackage wmp = WordprocessingMLPackage.load(fileTemplate);
			WordExportUtils.resolveFragmentText(wmp);
			wU.replacePlaceholder(wmp, "" + quotation.getOrderNumber(), "SoCT");

			wU.replacePlaceholder(wmp, "Ngày " + date + " tháng " + month + " năm " + year, "NgayXuat");
			wU.replacePlaceholder(wmp, quotation.getNote() == null ? "" : quotation.getNote(), "GhiChu");
			wU.replacePlaceholder(wmp, formatNumber(total, "###,###,###"), "$total");

			wU.replacePlaceholder(wmp, quotation.getCompanyName(), "companyName");
			wU.replacePlaceholder(wmp, quotation.getCompanyAdd(), "companyAdd");
			wU.replacePlaceholder(wmp, quotation.getCompanyPhone(), "SoDienThoai");

			wU.replaceTable(wmp, 1, lstTemp);

			String outPutPath = wU.writePDFToStream(
					new BaseGenericForwardComposer().removeVietnameseChar(quotation.getCompanyName()), wmp, true);
			if (!outPutPath.isEmpty()) {
				viewPdfOnBrowser(outPutPath);
			}
			return true;
		} catch (Exception en) {
			showDialogWithNoEvent(en.getMessage(), "Lỗi");
			LogUtils.addLog(en);
		}
		return false;
	}

	/**
	 *
	 * @param fileModel
	 * @param allowDownload
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	public boolean exportDoanhThu(List<VOrderDetail> lstQuotationDetail, Date fromDate, Date toDate,
			BigDecimal sodudauky, BigDecimal phatsinh, BigDecimal soducuoiky) {
		try {

			WordExportUtils wU = new WordExportUtils();
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String path = request.getRealPath(DOANH_THU);

			// Neu co dinh nghia bieu mau thi lay tu dinh nghia

			FileInputStream fileTemplate = new FileInputStream(new File(path));
			WordprocessingMLPackage wmp = WordprocessingMLPackage.load(fileTemplate);
			WordExportUtils.resolveFragmentText(wmp);
			String datetodate = String.format("Từ ngày %s đến ngày %s", getFormatDate(fromDate), getFormatDate(toDate));
			wU.replacePlaceholder(wmp, datetodate, "$Date");

			BigDecimal tongsono = new BigDecimal(0);
			BigDecimal tongsoban = new BigDecimal(0);
			for (VOrderDetail vOrderDetail : lstQuotationDetail) {
				vOrderDetail.getTodate();
				vOrderDetail.getTotalString();
				vOrderDetail.getOdate();
				vOrderDetail.getCompanyNameFullName();
				vOrderDetail.getTotalString();
				vOrderDetail.getDiendai();
				if (vOrderDetail.getType().equals(0L)) {// ban
					if (vOrderDetail.getTotal() != null)
						try {
							tongsoban = tongsoban.add(new BigDecimal(vOrderDetail.getTotal()));
						} catch (Exception e) {

						}

				} else {
					try {
						tongsono = tongsono.add(new BigDecimal(vOrderDetail.getTotal()));
					} catch (Exception e) {

					}
				}

			}

			wU.replaceTableDoanhThu(wmp, 0, lstQuotationDetail);

			wU.replacePlaceholder(wmp, formatNumber(sodudauky, "###,###,###"), "SoDuDauKy");

			wU.replacePlaceholder(wmp, formatNumber(soducuoiky, "###,###,###"), "SoDuCuoiKy");

			wU.replacePlaceholder(wmp, formatNumber(tongsono, "###,###,###"), "tongsono");

			wU.replacePlaceholder(wmp, formatNumber(tongsoban, "###,###,###"), "tongsoban");

			Date mDate = new Date();
			String date = String.valueOf(mDate.getDate());
			String month = String.valueOf(mDate.getMonth() + 1);
			String year = String.valueOf(mDate.getYear() + 1900);

			wU.replacePlaceholder(wmp, String.format("Ngày %s tháng %s năm %s", date, month, year), "ngaylapbang");

			String outPutPath = wU.writePDFToStream(
					"DT_" + new BaseGenericForwardComposer().removeVietnameseChar(datetodate.replace("/", "_")), wmp,
					true);

			if (!outPutPath.isEmpty()) {
				viewPdfOnBrowser(outPutPath);
			} else {
				return false;
			}

			return true;
		} catch (Exception en) {
			showDialogWithNoEvent(en.getMessage(), "Lỗi");
			LogUtils.addLog(en);
		}
		return false;
	}

	/**
	 *
	 * @param fileModel
	 * @param allowDownload
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	public boolean exportKiemTraHangNhap(List<Product> lstQuotationDetail ) {
		try {

			WordExportUtils wU = new WordExportUtils();
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String path = request.getRealPath(KIEM_TRA_HANG_NHAP);

			// Neu co dinh nghia bieu mau thi lay tu dinh nghia

			FileInputStream fileTemplate = new FileInputStream(new File(path));
			WordprocessingMLPackage wmp = WordprocessingMLPackage.load(fileTemplate);
			WordExportUtils.resolveFragmentText(wmp);
			
			 

			wU.replaceTableKT(wmp, 0, lstQuotationDetail);

			String outPutPath = wU.writePDFToStream("KIEM_TRA_HANG_NHAP", wmp,
					true);

			if (!outPutPath.isEmpty()) {
				viewPdfOnBrowser(outPutPath);
			} else {
				return false;
			}

			return true;
		} catch (Exception en) {
			showDialogWithNoEvent(en.getMessage(), "Lỗi");
			LogUtils.addLog(en);
		}
		return false;
	}
	
	public boolean exportDiaChiKH(List<Customer> lstCustomers,int layHang ) {
		try {

			WordExportUtils wU = new WordExportUtils();
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			String path;
			if(layHang==1||layHang==0){
				 path = request.getRealPath(KIEM_TRA_DIA_CHI_KH_DA_LAY_HANG);
			}else if(layHang==2){
				 path = request.getRealPath(KIEM_TRA_DIA_CHI_KH_KO_LAY_HANG);
			}else{
			  path = request.getRealPath(KIEM_TRA_DIA_CHI_KH);
			}

			FileInputStream fileTemplate = new FileInputStream(new File(path));
			WordprocessingMLPackage wmp = WordprocessingMLPackage.load(fileTemplate);
			WordExportUtils.resolveFragmentText(wmp);
			

			wU.replaceTableKT(wmp, 0, lstCustomers);

			String outPutPath = wU.writePDFToStream("KIEM_TRA_DIA_CHI_KH", wmp,
					true);

			if (!outPutPath.isEmpty()) {
				viewPdfOnBrowser(outPutPath);
			} else {
				return false;
			}

			return true;
		} catch (Exception en) {
			showDialogWithNoEvent(en.getMessage(), "Lỗi");
			LogUtils.addLog(en);
		}
		return false;
	}

	/**
	 * xem truoc cong van
	 * 
	 * @author ducdq1
	 * @param path
	 */
	public void viewPdfOnBrowser(String path) {
		File f = new File(path);
		if (f.exists()) {
			String URL;
			URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
			URL = URL + EncryptionUtil.encode(path);
			// URL = URL + (path);
			String link = String.format(
					"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')", URL);
			Clients.evalJavaScript(link);
		}
	}

}

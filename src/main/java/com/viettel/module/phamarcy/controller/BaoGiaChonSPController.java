package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModels;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.QuotationDetail;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.utils.Constants;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;

/**
 *
 * @author tuannt40
 */
public class BaoGiaChonSPController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox tbSoLuong, tbGhiChu;
	@Wire
	private Label lbTenSP, lbDonViTinh;
	@Wire
	private Checkbox cbHinhAnh;
	@Wire
	private Combobox cbMaSP;
	@Wire
	private Paging userPagingBottom;
	@Wire
	private Listbox lbListImages;

	@Wire
	private Window WD_bao_gia_chon_sp;
	private Product selectedProduct;

	private QuotationDetail quotationDetail;

	PhamarcyFileModel lastSearchModel;
	private List<Product> lstProducts;
	List<QuotationDetail> lstQuotationDetail;
	int indexUpdate;// cap nhat sp bao gia
	Window parent;
	private boolean isUpdate = false;

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();

		parent = (Window) arguments.get("parent");
		quotationDetail = (QuotationDetail) arguments.get("quotationDetail");
		lstQuotationDetail = (List<QuotationDetail>) arguments.get("lstQuotationDetail");
		ProductDao dao = new ProductDao();
		PagingListModel plm = dao.findProducts(new PhamarcyFileModel(), -2, 10);

		List<Product> lstProductsCombobox = plm.getLstReturn();

		//cbMaSP.setModel(new ListModelArray(lstProductsCombobox));
		Comparator comparator = new Comparator() {
			public int compare(Object text, Object item) {
				String val = text.toString();
				String itemText = ((Product) item).getProductCode();
				if(itemText ==null){
					return 1;
				}
				return itemText.toLowerCase().contains(val.toLowerCase()) ? 0 : 1;
			}
		};

		cbMaSP.setModel(ListModels.toListSubModel(new ListModelList(lstProductsCombobox), comparator, 30));
		
		cbMaSP.addEventListener("onChange", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				lbTenSP.setValue("");
				lbDonViTinh.setValue("");
				selectedProduct = null;

				List<Attachs> attachs = new ArrayList<>();
				ListModelArray lstModel = new ListModelArray(attachs);
				lbListImages.setModel(lstModel);

				if (cbMaSP.getSelectedItem() != null) {
					Product p = (Product) cbMaSP.getSelectedItem().getValue();
					if (p != null) {
						quotationDetail.setProductId(p.getProductId());
						lbTenSP.setValue(p.getProductName());
						lbDonViTinh.setValue(p.getUnit());
						selectedProduct = p;
						if (cbHinhAnh.isChecked()) {
							lbListImages.setVisible(true);
							getImages();
						} else {
							lbListImages.setVisible(false);
						}
					}
				}
			}

		});

		cbHinhAnh.addEventListener("onCheck", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (cbHinhAnh.isChecked() && selectedProduct != null) {
					lbListImages.setVisible(true);
					getImages();
				} else {
					lbListImages.setVisible(false);
				}

			}
		});

		if (quotationDetail == null) {
			isUpdate = false;
			quotationDetail = new QuotationDetail();
		} else {
			isUpdate = true;
//			selectedProduct = new Product();
//			selectedProduct.setProductId(quotationDetail.getProductId());
//			selectedProduct.setProductName(quotationDetail.getProductName());
//			selectedProduct.setProductCode(quotationDetail.getProductCode());
//			selectedProduct.setUnit(quotationDetail.getUnit());
//			selectedProduct.setProductType(productType);
			for (Product product : lstProductsCombobox) {
				if(product.getProductId().equals(quotationDetail.getProductId())){
					selectedProduct = product;
					break;
				}
			}
			
			lbDonViTinh.setValue(quotationDetail.getUnit());
			lbTenSP.setValue(quotationDetail.getProductName());
			tbGhiChu.setValue(quotationDetail.getNote());

			Double amount = quotationDetail.getAmount();
			if (amount!=null&& Double.compare(amount, Double.parseDouble(String.valueOf(amount.intValue()))) == 0) {
				tbSoLuong.setValue(String.valueOf(amount.intValue()));
			}else{
				tbSoLuong.setValue(quotationDetail.getAmount().toString());
			}

			if (quotationDetail.getAttachId() != null) {
				// selectedProduct = new Product();
				lbListImages.setVisible(true);
				getImages();
				cbHinhAnh.setChecked(true);
			}

			cbMaSP.setValue(quotationDetail.getProductCode());

			for (QuotationDetail detail : lstQuotationDetail) {
				if (detail.getProductCode().equals(quotationDetail.getProductCode())) {
					break;
				}
				indexUpdate++;
			}
		}
	}

	public void getImages() {
		List<Attachs> attachs = new AttachDAOHE().findAllAttachByAttachCodeAndAttachTye(Constants.OBJECT_TYPE.CAC_IMAGE,
				quotationDetail.getProductId());
		ListModelArray lstModel = new ListModelArray(attachs);
		lstModel.setMultiple(false);
		lbListImages.setModel(lstModel);
	}

	@Listen("onAfterRender=#lbListImages")
	public void results_onAfterRender() throws IOException {
		List<Listitem> listitems = lbListImages.getItems();
		for (Listitem item : listitems) {
			Attachs media = item.getValue();
			Listcell cell = (Listcell) item.getChildren().get(1);
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

			if (quotationDetail.getAttachId() != null && media.getAttachId().equals(quotationDetail.getAttachId())) {
				lbListImages.setSelectedItem(item);
				// break;
			}
		}

	}

	@Listen("onOK=#phamarcyAll")
	public void onEnter() {

	}

	@Listen("onClick = #btnChonSP")
	public void onAdd() {
		if(cbMaSP.getValue() == null || cbMaSP.getValue().isEmpty() ||cbMaSP.getSelectedItem() == null){
			cbMaSP.setErrorMessage("Sản phẩm không hợp lệ");
			return;
		}
		Double amount = null;
		try {
			amount = Double.parseDouble(tbSoLuong.getText().toString());
		} catch (Exception e) {
			amount = null;
		}

		if (amount == null) {
			tbSoLuong.setErrorMessage("Số lượng không hợp lệ");
			return;
		}
		
		if(selectedProduct == null || selectedProduct.getProductType()== null){
			cbMaSP.setErrorMessage(String.format("Sản phẩm không hợp lệ. Vuil lòng chọn lại"));
			return;
		}

		if (selectedProduct != null) {

			if (isUpdate) {
				for (int i = 0; i < lstQuotationDetail.size(); i++) {
					if (selectedProduct.getProductCode().equals(lstQuotationDetail.get(i).getProductCode())) {
						if (i != indexUpdate) {
//							showNotification(String.format("Sản phẩm %s đã tồn tại trong báo giá",
//									selectedProduct.getProductCode()));
							cbMaSP.setErrorMessage(String.format("Sản phẩm đã tồn tại trong báo giá"));
							return;
						}
					}
				}
				createQuotationDetail(amount);

			} else {
				for (int i = 0; i < lstQuotationDetail.size(); i++) {
					if (selectedProduct.getProductCode().equals(lstQuotationDetail.get(i).getProductCode())) {
						cbMaSP.setErrorMessage(String.format("Sản phẩm đã tồn tại trong báo giá" ));
						return;
					}
				}
				createQuotationDetail(amount);
				lstQuotationDetail.add(quotationDetail);
			}

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("product", selectedProduct);
			map.put("quotationDetail", quotationDetail);
			map.put("lstQuotationDetail", lstQuotationDetail);
			map.put("isUpdate", isUpdate);
			Events.sendEvent("onAddProduct", parent, map);

			cbMaSP.setValue("");
			tbSoLuong.setValue("");
			List<Attachs> attachs = new ArrayList<>();
			ListModelArray lstModel = new ListModelArray(attachs);
			lbListImages.setModel(lstModel);
			lbDonViTinh.setValue("");
			lbTenSP.setValue("");
			tbGhiChu.setValue("");
			tbSoLuong.setText("");
			quotationDetail = new QuotationDetail();
			isUpdate = false;
			cbMaSP.setDisabled(false);
		}

	}

	private void createQuotationDetail(Double amount) {
		quotationDetail.setProductId(selectedProduct.getProductId());
		quotationDetail.setProductCode(selectedProduct.getProductCode());
		
		quotationDetail.setUnit(selectedProduct.getUnit());
		quotationDetail.setAmount(amount);
		quotationDetail.setNote(tbGhiChu.getText().trim());

		if (cbHinhAnh.isChecked() &&lbListImages.isVisible() && !lbListImages.getItems().isEmpty() && lbListImages.getSelectedItem() != null) {
			Attachs media = lbListImages.getSelectedItem().getValue();
			if(media !=null){
				quotationDetail.setAttachId(media.getAttachId());
				quotationDetail.setImage(media);
			}else{
				quotationDetail.setAttachId(null);
				quotationDetail.setImage(null);
			}
		}else{
			quotationDetail.setAttachId(null);
			quotationDetail.setImage(null);
		}
		 
		//thiet bi
		if (selectedProduct.getProductType().intValue() == 0 || selectedProduct.getProductType().intValue() == 1){
			quotationDetail.setProductName(selectedProduct.getProductName() + " "+selectedProduct.getProductCode());
		}else{//gach
			quotationDetail.setProductName("("+selectedProduct.getSize() + ") "+selectedProduct.getProductCode());
		} 
		
	}

}

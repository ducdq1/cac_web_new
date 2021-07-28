package com.viettel.module.phamarcy.controller;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.BO.ProductCategory;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductCategoryDao;
import com.viettel.utils.Constants;

/**
 *
 * @author tuannt40
 */
public class ThemDanhMucSPController extends BaseComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6105342511808925036L;
	@Wire
	private Textbox tbTenDM,tbNoiDung,tbCode,tbImageUrl;
	
	@Wire
	private Radiogroup rdgLoaiSP,rdgLoaiSoSanh;
	@Wire
	private Window resetPassDlg;
	private Window parent;
	private ProductCategory area;
	boolean isUpdate = false;

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		HashMap<String, Object> arguments = (HashMap<String, Object>) Executions.getCurrent().getArg();
		parent = (Window) arguments.get("parent");
		area = (ProductCategory) arguments.get("productCategory");
		if (area != null) {
			viewData();
			isUpdate = true;
			resetPassDlg.setTitle("Cập nhật danh mục");
		}
	}

	private void viewData() {
		tbTenDM.setText(area.getName());
		tbNoiDung.setText(area.getDescription());
		rdgLoaiSP.setSelectedIndex(area.getType() !=null ? area.getType().intValue(): 0);
		tbCode.setText(area.getCode());
		tbImageUrl.setValue(area.getImageUrl());
		rdgLoaiSoSanh.setSelectedIndex(area.getSelectType() !=null ? area.getSelectType().intValue(): 0 );
	}

	@Listen("onClick = #btnSave")
	public void onSave() {
		 
		if (!validateTextBox(tbTenDM)) {
			return;
		}		
		 
		if (area == null) {
			area = new ProductCategory();
		}
		
		area.setName(tbTenDM.getText().trim());
		area.setDescription(tbNoiDung.getText().trim());
		area.setIsActive(1L);
		area.setType(Long.valueOf(rdgLoaiSP.getSelectedIndex()));
		area.setCode(tbCode.getText().trim());
		area.setImageUrl(tbImageUrl.getValue());
		area.setSelectType(Long.valueOf(rdgLoaiSoSanh.getSelectedIndex()));
		
		new ProductCategoryDao().saveOrUpdate(area);

		String message = "Thêm mới danh mục thành công.";
		if (isUpdate) {
			message = "Cập nhật danh mục thành công.";
		}

		showNotification(message, Constants.Notification.INFO, 2000);

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("productCategory", area);
		Events.sendEvent("onRefreshALL", parent, map);
		resetPassDlg.onClose();
	}
}

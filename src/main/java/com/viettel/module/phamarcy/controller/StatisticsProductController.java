package com.viettel.module.phamarcy.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModels;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.phamarcy.BO.PhamarcyFileModel;
import com.viettel.module.phamarcy.BO.Product;
import com.viettel.module.phamarcy.BO.Unit;
import com.viettel.module.phamarcy.BO.VPhaFileMedicine;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ExportExcell;
import com.viettel.module.phamarcy.DAO.PhaMedicine.ProductDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.StatisticsDao;
import com.viettel.module.phamarcy.DAO.PhaMedicine.UnitDao;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;

/**
 *
 * @author tuannt40
 */
public class StatisticsProductController extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire("#incSearchFullForm #fullSearchGbx") // Form search
	private Groupbox fullSearchGbx;
	@Wire("#incSearchFullForm #tbMaSP") // Ma ho so
	private Textbox tbMaSP;
	@Wire("#incSearchFullForm #cbNV") 
	private Combobox cbNV; 
	// @Wire("#incSearchFullForm #tbTenSP") // Ten thuoc
	// private Textbox tbTenSP;
	@Wire
	private Paging userPagingBottom;
	@Wire("#incListSP #lbListSP")
	private Listbox lbListSP;
	@Wire
	private Window phamarcyAll;
	@Wire
	private Label lbTongtien;
	@Wire("#incSearchFullForm #cbLoaiSP")
	private Combobox cbLoaiSP;
	@Wire("#incSearchFullForm #dbFromDay")
	private Datebox dbFromDay;
	@Wire("#incSearchFullForm #dbToDay")
	private Datebox dbToDay;
	PhamarcyFileModel lastSearchModel;
	private List<Product> lstProducts;
	int indexUpdate;// cap nhat sp bao gia

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		lastSearchModel = new PhamarcyFileModel();
		super.doBeforeComposeChildren(comp);

	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initCombobox();
		onSearch();
		
	}

	@Listen("onClick = #incSearchFullForm #btnSearch")
	public void onSearch() {
		lastSearchModel.setProductCode(tbMaSP.getText().trim());
		// lastSearchModel.setProductName(tbTenSP.getText().trim());
		int index = cbLoaiSP.getSelectedIndex();
		lastSearchModel.setProductType(index);
		lastSearchModel.setFromDate(dbFromDay.getValue());
		lastSearchModel.setToDate(dbToDay.getValue());
		if(!cbNV.getValue().trim().isEmpty()){
			lastSearchModel.setUserName(cbNV.getValue());
		}else{
			lastSearchModel.setUserName(null);
		}
		
		try {
			userPagingBottom.setActivePage(0);
			reloadModel(lastSearchModel);
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
		}
	}

	@Listen("onOK=#phamarcyAll")
	public void onEnter() {
		onSearch();
	}

	private void initCombobox() {
		 
		//List<Unit> units = new UnitDao().findUnit("", type, productType).getLstReturn();
		List<Users> users = new UserDAOHE().getListByType(2L);
		cbNV.getItems().clear();

		Comparator comparator = new Comparator() {
			public int compare(Object text, Object item) {
				String val = text.toString();
				String itemText = ((Users) item).getUserName();
				return itemText.toLowerCase().contains(val.toLowerCase()) ? 0 : 1;
			}
		};

		cbNV.setModel(ListModels.toListSubModel(new ListModelList(users), comparator, 20));
		 
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
			StatisticsDao dao = new StatisticsDao();
			PagingListModel plm;
			
			if(searchModel.getUserName()!=null){
				plm = dao.findProductsByUser(searchModel, start, take);
			}else{
				 plm = dao.findProducts(searchModel, start, take);
			}
			userPagingBottom.setTotalSize(plm.getCount());
			if (plm.getCount() == 0) {
				userPagingBottom.setVisible(false);
			} else {
				userPagingBottom.setVisible(true);
			}

			lstProducts = plm.getLstReturn();

			ListModelArray lstModel = new ListModelArray(lstProducts);
			lstModel.setMultiple(true);
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
 

	 
	@Listen("onClick = #incSearchFullForm #btnReset")
	public void onReset() {
		tbMaSP.setText("");
		// tbTenSP.setText("");
	}


	 
	@Listen("onRefreshALL =#phamarcyAll")
	public void onRefreshAll(Event event) {
		reloadModel(lastSearchModel);
	}

	  
	
}

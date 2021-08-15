/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.AttachDAO;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.BO.Place;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.sys.DAO.PlaceDAOHE;
import com.viettel.core.sys.DAO.RegisterDAOHE;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_XNN;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.utils.StringUtils;
import com.viettel.utils.ValidatorUtil;
import com.viettel.voffice.BO.Business;
import com.viettel.voffice.BO.Register;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BusinessDAOHE;
import com.viettel.voffice.model.PublicFunctionModel;

/**
 *
 * @author HaVM2
 */
public class UserViewController extends BaseComposer {

    @Wire
    Label lbFullName, lbUserName, lbEmail,
            lbTelephone, lbDeptName, lbStaffCode, lbIDNumber, lbBirthday, lbPosition, lbGender;
//    @Wire
//    Image avatar;
    @Wire
    Listbox lstTinh, lstHuyen, lstXa, lstLoaiHinhDN, lstChucVu;
    @Wire
    Textbox txtPassword, txtPasswordNew, txtPasswordConfirm, txtTenDayDu, txtTenDangNhap, txtSoDiDong, txtUserEmail,
            txtTenTiengViet, txtTenTiengAnh, txtTenVietTat, txtMaSoThue, txtDKKD, txtDiaChi, txtDienThoai, txtEmailDoanhNghiep,
            txtFax, txtWebsite, txtNamThanhLap, txtCoQuanChuQuan, tbOfficeName, tbOfficeAddress, tbOfficePhone, txtNumberLicensesInMedicines,
            txtNumberLicensesEstablishedOffices, txtNumberCertificate;
    @Wire
    Window userViewDlg;
    @Wire
    Div businessType, divAvatar, SystemType, divmain;
    @Wire
    Image avatar;
	@Wire
	private Button btnAttach_RegisterBusinessCertificate,btnAttach_EstablishedOfficesCertificate,btnAttach_ActivitiesCertificate;
    
	@Wire
	private Vlayout flist,flist10,flist11;
	
	
	private static final int KEY_REGISTER_BUSINESS = 1;
	private static final int KEY_ESTABLISHEDOFFICESE = 2;
	private static final int KEY_ACTIVITIES = 3;
	private List<Media> listMediaRegisterBusinessCertificate;
	private List<Media> listMediaEstablishedOfficesCertificate;
	private List<Media> listMediaActivitiesCertificate;
	private static final List<String> LIST_ATTACH_TYPE = new ArrayList<String>(Arrays.asList("pdf", "octet-stream","binary/octet-stream","application/pdf"));
	private Media media;
    private Business bus;
    Long userId;
    Long businessId;
    Long businessIdTemp;

    //TrungMHV add: 03/02/2016
    private Attachs attachNoCert;
    private Attachs attachNoCertVNOffice;
    private Attachs attachNoCertInMedicines;
    
    
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
    	listMediaRegisterBusinessCertificate = new ArrayList<Media>();
		listMediaEstablishedOfficesCertificate = new ArrayList<Media>();
		listMediaActivitiesCertificate = new ArrayList<Media>();
		
		 Users u = (Users) Executions.getCurrent().getArg().get("user");
	     if(u!= null && u.getBusinessId()!=null){
	    	 BusinessDAOHE busdao = new BusinessDAOHE();
	    	 businessIdTemp = u.getBusinessId();
	         Business bus = busdao.findById(businessIdTemp);
	        
	    	//get file Attach
	         getAttachs(bus.getBusinessTypeId()); 
	     }
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadItemForEdit();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadItemForEdit() {
        Users u = (Users) Executions.getCurrent().getArg().get("user");
        if (u == null) {
            return;
        }
        
        if (StringUtils.isNotNullNotEmptyNotWhiteSpace(u.getAvartarPath())){
        	avatar.setSrc(ResourceBundleUtil.getString("dir_avartar") + u.getUserId() + "?time=" + new Date().getTime());
		} else {
			avatar.setSrc(ResourceBundleUtil.getString("dir_avartar") + "default-avatar.png");
		}
        
        userId = u.getUserId();
        lbFullName.setValue(u.getFullName() == null ? "" : u.getFullName().trim());
        lbUserName.setValue(u.getUserName() == null ? "" : u.getUserName().trim());
        if (u.getEmail() == null) {
            lbEmail.setValue("");
        } else {
            lbEmail.setValue(u.getEmail().trim());
        }
        lbTelephone.setValue(u.getTelephone() == null ? "" : u.getTelephone().trim());
        lbDeptName.setValue(u.getDeptName() == null ? "" : u.getDeptName());
        lbStaffCode.setValue(u.getStaffCode() == null ? "" : u.getStaffCode().trim());
        if (u.getIdNumber() != null) {
            lbIDNumber.setValue(u.getIdNumber().trim());
        }
        if (u.getBirthday() != null) {
            lbBirthday.setValue(DateTimeUtils.convertDateToString(u.getBirthday()));
        }
        lbPosition.setValue(u.getPosName());
        if (u.getGender() == null) {
            //
            // Chua chon gioi tinh
            //
            lbGender.setValue("");
        } else if (u.getGender() == 1l) {
            lbGender.setValue("Nam");
        } else {
            lbGender.setValue("Nữ");
        }

        CategoryDAOHE cdhe = new CategoryDAOHE();
        PlaceDAOHE phe = new PlaceDAOHE();
        List lstProvince = phe.findPlaceSearchBycode(Constants_XNN.PLACE.PROVINCE, Constants.PLACE.VNCODE);
        ListModelArray lstModelProvince = new ListModelArray(lstProvince);
        lstTinh.setModel(lstModelProvince);
        lstTinh.renderAll();
        lstTinh.setSelectedIndex(0);

        List lstObjects = cdhe.getSelectCategoryByType(Constants_XNN.CATEGORY_TYPE.BUSINESS_TYPE, "value");
        ListModelArray lstPosition = new ListModelArray(lstObjects);
        lstLoaiHinhDN.setModel(lstPosition);
        lstLoaiHinhDN.renderAll();
        lstLoaiHinhDN.setSelectedIndex(0);

        lstObjects = cdhe.getSelectCategoryByType(Constants_XNN.CATEGORY_TYPE.USER_TYPE, "value");
        lstPosition = new ListModelArray(lstObjects);
        lstChucVu.setModel(lstPosition);
        lstChucVu.renderAll();
        lstChucVu.setSelectedIndex(0);

        if (userId != null) {
            txtTenDayDu.setValue(u.getFullName() == null ? "" : u.getFullName().trim());
            txtTenDangNhap.setValue(u.getUserName() == null ? "" : u.getUserName().trim());
            txtSoDiDong.setValue(u.getTelephone() == null ? "" : u.getTelephone().trim());
            txtUserEmail.setValue(u.getEmail() == null ? "" : u.getEmail().trim());
            if (u.getPosId() != null) {
                for (int i = 0; i < lstChucVu.getListModel().getSize(); i++) {
                    Category ct = (Category) lstChucVu.getListModel().getElementAt(i);
                    if (u.getPosId().equals(ct.getCategoryId())) {
                        lstChucVu.setSelectedIndex(i);
                        break;
                    }
                }
            }
            businessId = u.getBusinessId();
            BusinessDAOHE busdao = new BusinessDAOHE();
            Business bus = busdao.findById(businessId);

            if (businessId != null) {
                if (bus.getBusinessTypeId() != null) {
                    for (int i = 0; i < lstLoaiHinhDN.getListModel().getSize(); i++) {
                        Category ct = (Category) lstLoaiHinhDN.getListModel().getElementAt(i);
                        if (bus.getBusinessTypeId().equals(ct.getCategoryId())) {
                            lstLoaiHinhDN.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                if (bus.getBusinessProvince() != null) {

                    for (int i = 0; i < lstTinh.getListModel().getSize(); i++) {
                        Place ct = (Place) lstTinh.getListModel().getElementAt(i);
                        if (bus.getBusinessProvince().equals(ct.getPlaceId().toString())) {
                            lstTinh.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    //list huyen
                    List lstdistrict = phe.findAllPlaceSearch(Constants_XNN.PLACE.DISTRICT, Long.parseLong(bus.getBusinessProvince()));
                    ListModelArray lstModelDistrict = new ListModelArray(lstdistrict);
                    lstHuyen.setModel(lstModelDistrict);
                    lstHuyen.renderAll();
                    if (bus.getBusinessDistrict() != null) {
                        for (int i = 0; i < lstHuyen.getListModel().getSize(); i++) {
                            Place ct = (Place) lstHuyen.getListModel().getElementAt(i);
                            if (bus.getBusinessDistrict().equals(ct.getPlaceId().toString())) {
                                lstHuyen.setSelectedIndex(i);
                                break;
                            }
                        }
                    }

                    //list xa
                    List lsttown = phe.findAllPlaceSearch(Constants_XNN.PLACE.VILLAGE, Long.parseLong(bus.getBusinessDistrict()));
                    ListModelArray lstModelTown = new ListModelArray(lsttown);
                    lstXa.setModel(lstModelTown);
                    lstXa.renderAll();
                    
                    if (bus.getBusinessTown() != null) {
                        for (int i = 0; i < lstXa.getListModel().getSize(); i++) {
                            Place ct = (Place) lstXa.getListModel().getElementAt(i);
                            if (bus.getBusinessTown().equals(ct.getPlaceId().toString())) {
                                lstXa.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }
                else {
                	// //list huyen
            		List lstdistrict = phe.findAllPlaceSearch(Constants_XNN.PLACE.DISTRICT,-1L);
            		ListModelArray lstModelDistrict = new ListModelArray(lstdistrict);
            		lstHuyen.setModel(lstModelDistrict);
            		lstHuyen.renderAll();
            		lstHuyen.setSelectedIndex(0);
            		// list xa
            		List lsttown = phe.findAllPlaceSearch(Constants_XNN.PLACE.VILLAGE, -1L);
            		ListModelArray lstModelTown = new ListModelArray(lsttown);
            		lstXa.setModel(lstModelTown);
            		lstXa.renderAll();
            		lstXa.setSelectedIndex(0);
                }

                txtTenTiengViet.setValue(bus.getBusinessName() == null ? "" : bus.getBusinessName().trim());
                txtTenTiengAnh.setValue(bus.getBusinessNameEng() == null ? "" : bus.getBusinessNameEng().trim());
                txtTenVietTat.setValue(bus.getBusinessNameAlias() == null ? "" : bus.getBusinessNameAlias().trim());
                txtDKKD.setValue(bus.getBusinessLicense() == null ? "" : bus.getBusinessLicense().trim());
                txtDiaChi.setValue(bus.getBusinessAddress() == null ? "" : bus.getBusinessAddress().trim());
                txtDienThoai.setValue(bus.getBusinessTelephone() == null ? "" : bus.getBusinessTelephone().trim());
                txtFax.setValue(bus.getBusinessFax() == null ? "" : bus.getBusinessFax().trim());
                txtEmailDoanhNghiep.setValue(bus.getManageEmail() == null ? "" : bus.getManageEmail().trim());
                txtMaSoThue.setValue(bus.getBusinessTaxCode() == null ? "" : bus.getBusinessTaxCode().trim());
                txtWebsite.setValue(bus.getBusinessWebsite() == null ? "" : bus.getBusinessWebsite().trim());
                txtNamThanhLap.setValue(bus.getBusinessEstablishYear() == null ? "" : bus.getBusinessEstablishYear().trim());
                txtCoQuanChuQuan.setValue(bus.getGoverningBody() == null ? "" : bus.getGoverningBody().trim());
                tbOfficeName.setValue(bus.getOfficeVNName() == null ? "" : bus.getOfficeVNName().trim());
                tbOfficeAddress.setValue(bus.getOfficeVNAddress() == null ? "" : bus.getOfficeVNAddress().trim());
                tbOfficePhone.setValue(bus.getOfficeVNPhoneNumber() == null ? "" : bus.getOfficeVNPhoneNumber().trim());
                txtNumberCertificate.setValue(bus.getNumberCertificate() == null ? "" : bus.getNumberCertificate().trim());
                txtNumberLicensesEstablishedOffices.setValue(bus.getNumberLicensesOfficesVn() == null ? "" : bus.getNumberLicensesOfficesVn().trim());
                txtNumberLicensesInMedicines.setValue(bus.getNumberLicensesInMedicines() == null ? "" : bus.getNumberLicensesInMedicines().trim());
            }
            if (u.getUserType() == Constants_XNN.USER_TYPE.ENTERPRISE_USER) {
                divAvatar.setVisible(false);
                SystemType.setVisible(false);
            } else {
                businessType.setVisible(false);
                divmain.setWidth("450px");
                userViewDlg.setWidth("600px");
            }
            
        
        }
    }

    public Register getRegisterByTaxCode(){
    	RegisterDAOHE regisDAO = new RegisterDAOHE();
    	BusinessDAOHE busdao = new BusinessDAOHE();
        Business bus = busdao.findById(businessIdTemp);
    	Register  register = null;
    	Long businessTypeId = bus.getBusinessTypeId();
    	if (!businessTypeId.equals(Constants.COMBOBOX_HEADER_VALUE)) {
			Category cat = new CategoryDAOHE().findById(businessTypeId);
			if (cat != null) {
				if (cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES)) {
					//MST
					register = regisDAO.findByTaxCodeWithActiveAndUpdateAccept(bus.getBusinessTaxCode());
				}
				else if (cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES)) {
					if(StringUtils.isNotNullNotEmptyNotWhiteSpace(bus.getNumberLicensesOfficesVn())){
		    			//Co Giay phep thanh lap van phong dai dien tai Viet Nam
		    			register = regisDAO.findByTaxCodeAndOfficeWithActiveAndUpdateAccept(bus.getBusinessTaxCode(), bus.getNumberLicensesOfficesVn());
		    			
		    		} else {
		    			register = regisDAO.findByMedicineCerWithActiveAndUpdateAccept(bus.getNumberLicensesInMedicines());
		    		}
				}
			}
		}
    	
    	return register;
    }
    
    
    /**
     * get attachment of account
     * @author: trungmhv
     * @modifier trungmhv
     * @param: 
     * @createdDate: Feb 3, 2016
     * @updatedDate: Feb 3, 2016
     * @return:void
     */
    private void getAttachs(Long businessTypeId){
    	
    	AttachDAOHE attachDAOHE = new AttachDAOHE();
    	 
         if (Constants.BUSINESS_TYPE.LOCAL_BUSINESSES.equals(getBussinessCode(businessTypeId)))
         {
         	attachNoCert = attachDAOHE.getLastAttach(
         			getRegisterByTaxCode().getRegisterId(),
             		Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE,
             		businessTypeId);
         } else if(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES.equals(getBussinessCode(businessTypeId))){
         	attachNoCertVNOffice = attachDAOHE.getLastAttach(
         			getRegisterByTaxCode().getRegisterId(),
             		Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN,
             		businessTypeId);
         	
         	attachNoCertInMedicines = attachDAOHE.getLastAttach(
         			getRegisterByTaxCode().getRegisterId(),
             		Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES,
             		businessTypeId);
         }  
    }
    
    @Listen("onClick=#onDownloadFileNoCertificate")
	public void onDownloadFileNoCertificate()
			throws FileNotFoundException {
    	if(attachNoCert != null){
    		downloadAttach(attachNoCert.getAttachId());
    	}
	}
    
    @Listen("onClick=#onDownloadFileLicensesEstablishedOffices")
   	public void onDownloadFileLicensesEstablishedOffices()
   			throws FileNotFoundException {
    	if(attachNoCertVNOffice != null){
    		downloadAttach(attachNoCertVNOffice.getAttachId());
    	}
   	}
    
    @Listen("onClick=#onDownloadFileNoLicensesInMedicines")
   	public void onDownloadFileNoLicensesInMedicines()
   			throws FileNotFoundException {
    	if(attachNoCertInMedicines != null){
    		downloadAttach(attachNoCertInMedicines.getAttachId());
    	}
   	}
    
    
    @Listen("onAfterRender = #lstLoaiHinhDN")
	public void onAfterRenderLoaiHinh() {
    	handleEventSelectBusinessType();
	}
    
    @Listen("onSelect = #lstLoaiHinhDN")
	public void onSelectBusinessType() {
    	txtNumberLicensesEstablishedOffices.setText("");
		txtNumberLicensesInMedicines.setText("");
		txtNumberCertificate.setText("");
		lstXa.setSelectedIndex(0);
		lstHuyen.setSelectedIndex(0);
		lstTinh.setSelectedIndex(0);
    	handleEventSelectBusinessType();
	}
    
    private void handleEventSelectBusinessType() {
    	setDefaultUIBusinessType();
		Listitem item = lstLoaiHinhDN.getSelectedItem();
		if (item != null) {
			Long value = (Long)item.getValue();
			if (!value.equals(Constants.COMBOBOX_HEADER_VALUE)) {
				Category cat = new CategoryDAOHE().findById(value);
				if (cat != null && cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES))
					selectLocalBusiness();
				else if (cat != null && cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES))
					selectForeignBusiness();
			}
		}
    }
    
    private void setDefaultUIBusinessType() {
    	lstLoaiHinhDN.setDisabled(true);
    	txtNumberCertificate.setDisabled(true);
    	txtNumberLicensesEstablishedOffices.setDisabled(true);
		txtNumberLicensesInMedicines.setDisabled(true);
		txtMaSoThue.setDisabled(true);
		txtTenDangNhap.setDisabled(true);
		lstTinh.setDisabled(true);
		lstHuyen.setDisabled(true);
		lstXa.setDisabled(true);
		btnAttach_RegisterBusinessCertificate.setVisible(false);
		btnAttach_EstablishedOfficesCertificate.setVisible(false);
		btnAttach_ActivitiesCertificate.setVisible(false);
		flist.setVisible(false);
		flist10.setVisible(false);
		flist11.setVisible(false);
		clearAllFileAttach();
    }
    
    private void selectLocalBusiness() {
		lstTinh.setDisabled(false);
		lstHuyen.setDisabled(false);
		lstXa.setDisabled(false);
		btnAttach_RegisterBusinessCertificate.setVisible(true);
		btnAttach_EstablishedOfficesCertificate.setVisible(false);
		btnAttach_ActivitiesCertificate.setVisible(false);
		flist.setVisible(true);
		flist10.setVisible(false);
		flist11.setVisible(false);
    }
    
    private void selectForeignBusiness() {
		btnAttach_RegisterBusinessCertificate.setVisible(false);
		if(txtNumberLicensesEstablishedOffices.getValue() != null && !txtNumberLicensesEstablishedOffices.getValue().trim().isEmpty() ){
			btnAttach_EstablishedOfficesCertificate.setVisible(true);
		} else {
			btnAttach_EstablishedOfficesCertificate.setVisible(false);
		}
		if(txtNumberLicensesInMedicines.getValue() != null && !txtNumberLicensesInMedicines.getValue().trim().isEmpty() ){
			btnAttach_ActivitiesCertificate.setVisible(true);
		} else{
			btnAttach_ActivitiesCertificate.setVisible(false);
		}
		
		flist.setVisible(false);
		flist10.setVisible(true);
		flist11.setVisible(true);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void onSelectProvince() {
        if (lstTinh.getSelectedItem() != null) {
            int idx = lstTinh.getSelectedItem().getIndex();
            PlaceDAOHE phe = new PlaceDAOHE();
            if (idx > 0l) {
                Long id = (Long) lstTinh.getSelectedItem().getValue();
                List lstProvince = phe.findAllPlaceSearch(Constants_XNN.PLACE.DISTRICT, id);
                ListModelArray lstModelProvince = new ListModelArray(lstProvince);
                lstHuyen.setModel(lstModelProvince);
                lstHuyen.renderAll();
                lstXa.renderAll();
                lstXa.setSelectedIndex(0);
                lstHuyen.setSelectedIndex(0);
            } //an nut chon
            else {
                List lstProvince = phe.findAllPlaceSearch(Constants_XNN.PLACE.DISTRICT, -1L);
                ListModelArray lstModelProvince = new ListModelArray(lstProvince);
                lstHuyen.setModel(lstModelProvince);
                lstHuyen.renderAll();

                List lstTown = phe.findAllPlaceSearch(Constants_XNN.PLACE.VILLAGE, -1L);
                ListModelArray lstModelXa = new ListModelArray(lstTown);
                lstXa.setModel(lstModelXa);
                lstXa.renderAll();
                lstXa.setSelectedIndex(0);
                lstHuyen.setSelectedIndex(0);
            }
        }
    }

    public void onSelectDistrict() {
        if (lstHuyen.getSelectedItem() != null) {
            int idx = lstHuyen.getSelectedItem().getIndex();
            PlaceDAOHE phe = new PlaceDAOHE();
            if (idx > 0l) {
                Long id = (Long) lstHuyen.getSelectedItem().getValue();
                List lstProvince = phe.findAllPlaceSearch(Constants_XNN.PLACE.VILLAGE, id);
                ListModelArray lstModelProvince = new ListModelArray(lstProvince);
                lstXa.setModel(lstModelProvince);
                lstXa.renderAll();
                lstXa.setSelectedIndex(0);
            } else {
                List lstProvince = phe.findAllPlaceSearch(Constants_XNN.PLACE.VILLAGE, -1L);
                ListModelArray lstModelProvince = new ListModelArray(lstProvince);
                lstXa.setModel(lstModelProvince);
                lstXa.renderAll();
                lstXa.setSelectedIndex(0);
            }
        }
    }

//    @Listen("onClick=#btnSaveInfo")
    public void Save() {
        //Cap nhat bang business
        try {
            if (isValidatedData()) {
                BusinessDAOHE busdao = new BusinessDAOHE();
                Business bus = busdao.findById(businessId);
                bus.setBusinessName(txtTenTiengViet.getValue());
                bus.setBusinessNameEng(txtTenTiengAnh.getValue());
                bus.setBusinessNameAlias(txtTenVietTat.getValue());
                bus.setBusinessLicense(txtDKKD.getValue());
                bus.setBusinessAddress(txtDiaChi.getValue());
                bus.setBusinessTelephone(txtDienThoai.getValue());
                bus.setBusinessFax(txtFax.getValue());
                bus.setManageEmail(txtEmailDoanhNghiep.getValue());
                bus.setBusinessWebsite(txtWebsite.getValue());
                bus.setBusinessEstablishYear(txtNamThanhLap.getValue());
                bus.setGoverningBody(txtCoQuanChuQuan.getValue());
                bus.setBusinessProvince(lstTinh.getSelectedItem().getValue().toString());
                bus.setBusinessDistrict(lstHuyen.getSelectedItem().getValue().toString());
                bus.setBusinessTown(lstXa.getSelectedItem().getValue().toString());
                bus.setBusinessTypeId(Long.parseLong(lstLoaiHinhDN.getSelectedItem().getValue().toString()));
                bus.setBusinessTypeName(lstLoaiHinhDN.getSelectedItem().getValue().toString());
                bus.setOfficeVNName(tbOfficeName.getValue());
                bus.setOfficeVNAddress(tbOfficeAddress.getValue());
                bus.setOfficeVNPhoneNumber(tbOfficePhone.getValue());
                bus.setNumberCertificate(txtNumberCertificate.getText());
                bus.setNumberLicensesInMedicines(txtNumberLicensesInMedicines.getText());
                bus.setNumberLicensesOfficesVn(txtNumberLicensesEstablishedOffices.getText());
                busdao.saveOrUpdate(bus);
                showSuccessNotification("Cập nhật thành công");
                userViewDlg.detach();
            }
        } catch (Exception ex) {
            LogUtils.addLog(ex);
            showNotification("Cập nhật không thành công");
        }
    }

  
    /**
     * Gui de nghi cap nhat thong tin doang nghiep, luu xuong bang register
     */
    @Listen("onClick=#btnSaveInfo")
    public void saveBusinessInfoToRegister(){
    	if(isValidatedData()){
    	RegisterDAOHE regisDAO = new RegisterDAOHE();
    	BusinessDAOHE busdao = new BusinessDAOHE();
        Business bus = busdao.findById(businessId);
    	Register  register = null;
    	Long businessTypeId = (Long)lstLoaiHinhDN.getSelectedItem().getValue();
    	if (!businessTypeId.equals(Constants.COMBOBOX_HEADER_VALUE)) {
			Category cat = new CategoryDAOHE().findById(businessTypeId);
			if (cat != null) {
				if (cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES)) {
					//MST
					register = regisDAO.findByTaxCode(bus.getBusinessTaxCode());
				}
				else if (cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES)) {
					if(StringUtils.isNotNullNotEmptyNotWhiteSpace(bus.getNumberLicensesOfficesVn())){
		    			//Co Giay phep thanh lap van phong dai dien tai Viet Nam
		    			register = regisDAO.findByTaxCodeAndOfficeCertificate(bus.getBusinessTaxCode(), bus.getNumberLicensesOfficesVn());
		    			
		    		} else {
		    			register = regisDAO.findByMedicineCertificate(bus.getNumberLicensesInMedicines());
		    		}
				}
			}
		}
	  
	    //Them du lieu vao register tu bang Users
        UserDAOHE userdao = new UserDAOHE();
        Users users = userdao.findById(userId);
        if(users!= null){
        register.setUserType(String.valueOf(users.getUserType()));
        register.setUserEmail(users.getEmail());
        register.setUserFullName(users.getFullName());
        register.setUserMobile(users.getTelephone());
        register.setUserTelephone(users.getTelephone());
        }
        
	    //Them du lieu vao register tu bang Business
        if(bus!=null){
        	register.setBusinessTaxCode(bus.getBusinessTaxCode());
        	register.setBusinessTypeId(bus.getBusinessTypeId());
        	register.setBusinessTypeName(bus.getBusinessTypeName());
        	register.setNumberCertificate(bus.getNumberCertificate());
        	register.setNumberLicensesInMedicines(bus.getNumberLicensesInMedicines());
        	register.setNumberLicensesOfficesVn(bus.getNumberLicensesOfficesVn());
        }
        
	    //Cap nhat tu giao dien
		
		if (!businessTypeId.equals(Constants.COMBOBOX_HEADER_VALUE)) {
			Category cat = new CategoryDAOHE().findById(businessTypeId);
			if (cat != null) {
				if (cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES)) {
					register.setBusinessProvince(lstTinh.getSelectedItem().getValue().toString());
					register.setBusinessDistrict(lstHuyen.getSelectedItem().getValue().toString());
					register.setBusinessTown(lstXa.getSelectedItem().getValue().toString());
				}
//				else if (cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES)) {
//					register.setNumberLicensesInMedicines(txtNumberLicensesInMedicines.getText());
//					register.setNumberLicensesOfficesVn(txtNumberLicensesEstablishedOffices.getText());
//				}
			}
		}
	
		register.setBusinessNameVi(txtTenTiengViet.getValue().trim());
	
		if (txtTenTiengAnh.getValue() != null) {
			register.setBusinessNameEng(txtTenTiengAnh.getValue().trim());
		}
		if (txtTenVietTat.getValue() != null) {
			register.setBusinessNameAlias(txtTenVietTat.getValue()
					.trim());
		}
		
		register.setBusinessLicense(txtDKKD.getValue().trim());
		
		if (txtCoQuanChuQuan.getValue() != null) {
			register.setGoverningBody(txtCoQuanChuQuan.getValue().trim());
		}
	
			register.setBusinessAdd(txtDiaChi.getValue().trim());
			register.setBusinessTelephone(txtDienThoai.getValue().trim());
			register.setManageEmail(txtEmailDoanhNghiep.getValue().trim());
		
		if (txtFax.getValue() != null) {
			register.setBusinessFax(txtFax.getValue().trim());
		}
		
		if (txtWebsite.getValue() != null) {
			register.setBusinessWebsite(txtWebsite.getValue().trim());
		}
		
		if (txtNamThanhLap.getValue() != null) {
			register.setBusinessEstablishYear(txtNamThanhLap.getValue().trim());
		}
		
		if (tbOfficeName.getValue() != null) {
			register.setOfficeVNName(tbOfficeName.getValue().trim());
		}

		if (tbOfficeAddress.getValue() != null) {
			register.setOfficeVNAddress(tbOfficeAddress.getValue().trim());
		}

		if (tbOfficePhone.getValue() != null) {
			register.setOfficeVNPhoneNumber(tbOfficePhone.getValue().trim());
		}
		
		register.setDateCreate(new Date());
		//trang thai cho cap nhat
		register.setStatus(Constants.Status.UPDATEPENDING);
		try{
		
		regisDAO.saveOrUpdate(register);
		saveFileAttach(register.getRegisterId());
		showSuccessNotification("Gửi đề nghị cập nhật thành công!");
		}
		catch(Exception ex)
		{
			LogUtils.addLog(ex);
			showNotification("Lỗi cập nhật thông tin!");
		}
    	}
    }
    
    private void saveFileAttach(long registerID){
		AttachDAO attachDao = new AttachDAO();
		Long businessTypeId = (Long)lstLoaiHinhDN.getSelectedItem().getValue();
		Category cat = new CategoryDAOHE().findById(businessTypeId);
		if (cat != null
			&& cat.getCode() != null 
			&& cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES)){
			try {
				AttachDAOHE attachDAO = new AttachDAOHE();
				attachDAO.deleteAttach(registerID, Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE, null);
				
				if(listMediaRegisterBusinessCertificate.size()> 0){
				/*attachDao.saveMedicineFileAttach(listMediaRegisterBusinessCertificate.get(0)
												, false
												,registerID 
												,Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE
												,cat.getCategoryId()
												, null);*/
				
				new AttachDAO().saveFileAttachRegister(listMediaRegisterBusinessCertificate.get(0), registerID,
						Constants.OBJECT_TYPE.REGISTER, Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE,
						"File dinh kem khi dang ki");
				
				} else {
					if(attachNoCert!=null){
					//lay file cu
					Attachs attachOther =  attachNoCert.copyAttachsWithNoId();
					attachOther.setObjectId(registerID);
					attachDAO.saveOrUpdate(attachOther);
					}
					
				}
			} catch (IOException e) {
				LogUtils.addLog(e);
			}
			
		}
		else if(cat != null
				&& cat.getCode() != null 
				&& cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES))
		{
			try {
				AttachDAOHE attachDAOHE =new AttachDAOHE();
				attachDAOHE.deleteAttach(registerID, Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN, null);
				attachDAOHE.deleteAttach(registerID, Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES, null);
				
				if(listMediaEstablishedOfficesCertificate.size() > 0){
					new AttachDAO().saveFileAttachRegister(listMediaEstablishedOfficesCertificate.get(0), registerID,
							Constants.OBJECT_TYPE.REGISTER, Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN,
							"File dinh kem khi dang ki");
				} else {
					if(attachNoCertVNOffice != null){
						Attachs attachVNOfficeOther = attachNoCertVNOffice.copyAttachsWithNoId();
						attachVNOfficeOther.setObjectId(registerID);
						attachDAOHE.saveOrUpdate(attachVNOfficeOther);
					}
				}
				if(listMediaActivitiesCertificate.size() > 0){
					new AttachDAO().saveFileAttachRegister(listMediaActivitiesCertificate.get(0), registerID,
							Constants.OBJECT_TYPE.REGISTER, Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES,
							"File dinh kem khi dang ki");
				} else {
					if(attachNoCertInMedicines != null){
						Attachs attachMedicineOther = attachNoCertInMedicines.copyAttachsWithNoId();
						attachMedicineOther.setObjectId(registerID);
						attachDAOHE.saveOrUpdate(attachMedicineOther);
					}
				}
				
				
				
			} catch (IOException e) {
				LogUtils.addLog(e);
			}
		}		
	}
    
    /**
	 * Clear all file attach
	 */
	private void clearAllFileAttach(){
		removeLayoutFileRegisterBusiness();
		removeLayoutFileEstablishedOffices();
		removeLayoutFileActivities();
		
		listMediaRegisterBusinessCertificate.clear();
		listMediaEstablishedOfficesCertificate.clear();
		listMediaActivitiesCertificate.clear();
	}	
	
	/**
	 * Save media file Attach
	 * @param media
	 * @param typecertificate
	 */
	public void saveMediaFileAttach(Media media, int typecertificate) {
		switch (typecertificate) {
		case KEY_REGISTER_BUSINESS:
			listMediaRegisterBusinessCertificate.clear();
			listMediaRegisterBusinessCertificate.add(media);
			break;
		case KEY_ESTABLISHEDOFFICESE:
			listMediaEstablishedOfficesCertificate.clear();
			listMediaEstablishedOfficesCertificate.add(media);
			break;
		case KEY_ACTIVITIES:
			listMediaActivitiesCertificate.clear();
			listMediaActivitiesCertificate.add(media);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Save media file Attach
	 * @param media
	 * @param typecertificate
	 */
	public void deleteMediaFileAttach(Media media, int typecertificate) {
		switch (typecertificate) {
		case KEY_REGISTER_BUSINESS:
			listMediaRegisterBusinessCertificate.remove(media);
			break;
		case KEY_ESTABLISHEDOFFICESE:
			listMediaEstablishedOfficesCertificate.remove(media);
			break;
		case KEY_ACTIVITIES:
			listMediaActivitiesCertificate.remove(media);
			break;
		default:
			break;
		}
	}
	
	private void removeLayoutFileRegisterBusiness(){
		if(flist.getChildren().size()>0){
			flist.removeChild(flist.getFirstChild());
		}
	}
	
	private void removeLayoutFileEstablishedOffices(){
		if(flist10.getChildren().size() > 0){
			flist10.removeChild(flist10.getFirstChild());
		}
	}
	
	private void removeLayoutFileActivities(){
		if(flist11.getChildren().size() > 0){
			flist11.removeChild(flist11.getFirstChild());
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Hlayout generateLayoutFileMedia(final Media media, final int typeCertificate)
	{
		String fileName = media.getName();
		if(fileName.length() > 10){
			fileName = fileName.substring(0, 7);
			fileName += "...";
		}
		
		final Hlayout hl = new Hlayout();
		hl.setSpacing("6px");
		hl.setClass("newFile");
		hl.appendChild(new Label(fileName));
		A rm = new A("Xóa");
		rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				hl.detach();
				deleteMediaFileAttach(media, typeCertificate);
			}
		});
		hl.appendChild(rm);
		
		return hl;
	}
	
	private void addFlishFile(Media media,int typeCertificate)
	{
		switch (typeCertificate) {
		case KEY_REGISTER_BUSINESS:
			removeLayoutFileRegisterBusiness();
			final Hlayout hl = generateLayoutFileMedia(media, typeCertificate);
			flist.appendChild(hl);
			break;
		case KEY_ESTABLISHEDOFFICESE:
			removeLayoutFileEstablishedOffices();
			final Hlayout hl10 = generateLayoutFileMedia(media, typeCertificate);
			flist10.appendChild(hl10);
			break;
		case KEY_ACTIVITIES:
			removeLayoutFileActivities();
			final Hlayout hl11 = generateLayoutFileMedia(media, typeCertificate);
			flist11.appendChild(hl11);
			break;
		default:
			break;
		}
	}
	
	@Listen("onUpload = #btnAttach_RegisterBusinessCertificate")
	public void onUploadRegisterBusinessCertificate(UploadEvent event) throws UnsupportedEncodingException {
		System.out.println("Upload RegisterBusinessCertificate...");
		implementUploadFileAttach(event, KEY_REGISTER_BUSINESS);
	}
    
	@Listen("onUpload = #btnAttach_EstablishedOfficesCertificate")
	public void onUploadEstablishedOfficesCertificate(UploadEvent event) throws UnsupportedEncodingException {
		System.out.println("Upload EstablishedOfficesCertificate...");
		implementUploadFileAttach(event, KEY_ESTABLISHEDOFFICESE);
	}

	@Listen("onUpload = #btnAttach_ActivitiesCertificate")
	public void onUploadActivitiesCertificate(UploadEvent event) throws UnsupportedEncodingException {
		System.out.println("Upload RegisterBusinessCertificate...");
		implementUploadFileAttach(event, KEY_ACTIVITIES);
	}
	
	/**
	 * Implement upload file attach
	 * @param event
	 * @param typeCertificate
	 */
	private void implementUploadFileAttach(UploadEvent event, int typeCertificate) {
		final Media[] medias = event.getMedias();
		String warning = "";

		// Limit size
		Long limitSize = Long.parseLong("5242880");
		Long limitSizeMB = limitSize / 1024 / 1024;

		for (final Media media : medias) {
			Long mediaSize = Integer.valueOf(media.getByteData().length).longValue();
			if (mediaSize > limitSize) {
				showNotification(String.format(
						"Dung lượng tệp đính kèm không vượt quá 5MB!",
						limitSizeMB), Clients.NOTIFICATION_TYPE_INFO, 4000);
				return;
			}

			String check = checkValidFileType(media);
			if (check.length() == 0) {
				saveMediaFileAttach(media, typeCertificate);
			} else {
				warning += check;
				continue;
			}
			
			// Create layout show file Name
			addFlishFile(media, typeCertificate);
			
			System.out.println("Upload RegisterBusinessCertificate: Success !");
		}
		if (warning.length() > 0) {
			showNotification(String.format("Định dạnh tệp không được phép tải lên: %s", warning),
					Constants.Notification.WARNING, 3000);
			System.out.println("Upload RegisterBusinessCertificate: Error !");
		}
	}
	
	/**
	 * Check valid file type
	 * 
	 * @param media
	 * @return
	 */
	private String checkValidFileType(Media media) {
		String extFile = media.getContentType();
		if (!LIST_ATTACH_TYPE.contains(extFile)) {
			try {
				String[] parts = extFile.split("\\.");
				int abc = parts.length;
				String lastFile = parts[abc - 1];
				if (!LIST_ATTACH_TYPE.contains(lastFile)) {
					return "</br>" + media.getName();
				}
			} catch (Exception e) {
				LogUtils.addLog(e);
				return "</br>" + media.getName();
			}
		}
		return "";
	}
	
    private boolean isValidatedData() {
        String errorMsg;
             
        if (PublicFunctionModel.validateBlank(txtTenTiengViet.getText())) {
            showNotification("Tên tiếng việt không thể để trống!");
            txtTenTiengViet.focus();
            return false;
        }
        
        if (!lstTinh.isDisabled() && "-1".equals(lstTinh.getSelectedItem().getValue().toString())) {
            showNotification("Bạn phải chọn Tỉnh/Thành phố!");
            lstTinh.focus();
            return false;
        }
        if (lstHuyen.getSelectedItem() != null) {
            int idx = lstHuyen.getSelectedItem().getIndex();
            if (idx <= 0 && !lstHuyen.isDisabled()) {
                showNotification("Bạn phải chọn Quận/Huyện!");
                lstHuyen.focus();
                return false;
            }
        } else {
            showNotification("Bạn phải chọn Quận/Huyện!");
            lstHuyen.focus();
            return false;
        }

        if (!lstXa.isDisabled() && "-1".equals(lstXa.getSelectedItem().getValue().toString())) {
            showNotification("Bạn phải chọn Xã/Phường/Thị trấn!");
            lstXa.focus();
            return false;
        }
        if (txtDiaChi.getText().matches("\\s*")) {
            showNotification("Địa chỉ chi tiết không thể để trống!");
            txtDiaChi.focus();
            return false;
        }
        if (txtNamThanhLap.getText() != null && !txtNamThanhLap.getText().trim().isEmpty()) {
            if (PublicFunctionModel.validateNumber(txtNamThanhLap.getText().trim())) {
                showNotification("Năm thành lập phải là số!");
                txtNamThanhLap.focus();
                return false;
            }
        }
        if (txtNamThanhLap.getText() != null && !txtNamThanhLap.getText().trim().isEmpty() ) {
            Integer year = Calendar.getInstance().get(Calendar.YEAR);
            try {
                if (Integer.parseInt(txtNamThanhLap.getText().trim()) > year) {
                    showNotification("Năm thành lập không thể lớn năm hiện tại");
                    txtNamThanhLap.focus();
                    return false;
                }
            } catch(WrongValueException e) {
                LogUtils.addLog(e);
                showNotification("Năm thành lập phải là kiểu số không lớn hơn năm hiện tại");
                txtNamThanhLap.focus();
                return false;
            }
        }
        
        if ((errorMsg = ValidatorUtil.validateTextbox(txtDienThoai, true, 31, ValidatorUtil.PATTERN_CHECK_PHONENUMBER, ValidatorUtil.PHONENUMBER_MIN_LENGTH)) != null) {
            showNotification(String.format(errorMsg, "Điện thoại doanh nghiệp"));
            txtDienThoai.focus();
            return false;
        }
        
        if ((errorMsg = ValidatorUtil.validateTextbox(txtFax, false, 31, ValidatorUtil.PATTERN_CHECK_PHONENUMBER, ValidatorUtil.PHONENUMBER_MIN_LENGTH)) != null) {
            showNotification(String.format(errorMsg, "Số Fax doanh nghiệp"));
            txtFax.focus();
            return false;
        }
        
        if ((errorMsg = ValidatorUtil.validateTextbox(txtEmailDoanhNghiep, true, 50, ValidatorUtil.PATTERN_CHECK_EMAIL)) != null) {
            showNotification(String.format(errorMsg, "Email doanh nghiệp"));
            txtEmailDoanhNghiep.focus();
            return false;
        }
        
        if ((errorMsg = ValidatorUtil.validateTextbox(tbOfficePhone, false,  ValidatorUtil.PHONENUMBER_MAX_LENGTH, ValidatorUtil.PATTERN_CHECK_PHONENUMBER, ValidatorUtil.PHONENUMBER_MIN_LENGTH)) != null) {
            showNotification(String.format(errorMsg, "Điện thoại của văn phòng đại diện hoặc người liên lạc ở Việt Nam"));
            tbOfficePhone.focus();
            return false;
        }
        
        return true;
    }

    /**
     * Lay ten file dinh kem
     * @author: trungmhv
     * @modifier trungmhv
     * @param: 
     * @createdDate: Feb 3, 2016
     * @updatedDate: Feb 3, 2016
     * @return:String
     */
    public String getAttachNameNumberCertificate(){
    	return getAttachName(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE);
    }
    
    public String getAttachLicenseOfficeVN(){
    	return getAttachName(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN);
    }
    
    public String getAttachNameLicenseInMedicines(){
    	return getAttachName(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES);
    }
    
    @Listen("onClick=#btnSaveUserInfo")
    public void saveUserInfo(){
    	if(isValidateUserInfo()){
    		try{   
    			
                 //cap nhat user
                 UserDAOHE userdao = new UserDAOHE();
                 Users users = userdao.findById(userId);
                 if(users!= null){
                	 users.setFullName(txtTenDayDu.getValue().trim());
                     users.setTelephone(txtSoDiDong.getValue().trim());
                     users.setEmail(txtUserEmail.getValue().trim());
                     users.setPosId(Long.parseLong(lstChucVu.getSelectedItem().getValue().toString()));
                     users.setPosName(lstChucVu.getSelectedItem().getLabel().toString());
                     userdao.saveOrUpdate(users); 
                 }
                 showSuccessNotification("Cập nhật thành công!");   
    		}
    		catch(Exception e){
    			LogUtils.addLog(e);
    			showNotification("Cập nhật không thành công!");
    		}
    	}
    }
    /**
     * Validate User Info
     * @return
     */
    private boolean isValidateUserInfo(){
    
    	String errorMsg;
    	
    	//Ten Day Du
    	if (txtTenDayDu.getText().matches("\\s*")) {
    		showNotification("Họ và tên người đại diện không thể để trống!");
			txtTenDayDu.focus();
			return false;
		}
		if (txtTenDayDu.getText().trim().length() > 250) {
			showNotification("Họ và tên người đại diện không được lớn hơn 250 kí tự!");
			txtTenDayDu.focus();
			return false;
		}
		
		//Chuc vu
		if ("-1".equals(lstChucVu.getSelectedItem().getValue().toString())) {
			showNotification("Bạn phải chọn chức vụ người đại diện!");
			lstChucVu.focus();
			return false;
		}
    	
		//So dien thoai
		if ((errorMsg = ValidatorUtil.validateTextbox(txtSoDiDong, true, 15,
				ValidatorUtil.PATTERN_CHECK_PHONENUMBER)) != null) {
			showNotification(String.format(errorMsg, "Số di động người đại diện"));
			txtSoDiDong.focus();
			return false;
		}
		
		//Email		
        if ((errorMsg = ValidatorUtil.validateTextbox(txtUserEmail, true, 50, ValidatorUtil.PATTERN_CHECK_EMAIL,0)) != null) {
            showNotification(String.format(errorMsg, "Email người dùng"));
            txtUserEmail.focus();
            return false;
        }

    	return true;
    }
    
    private void updatePassword() {
        try {
            UserDAOHE udhe = new UserDAOHE();
            udhe.updatePassword(userId, txtPasswordNew.getValue(),false);
            showSuccessNotification("Cập nhật thành công");
            userViewDlg.detach();
            Users u = udhe.findById(userId);
             
        } catch (Exception en) {
            LogUtils.addLog(en);
            showNotification("Cập nhật không thành công");
        }
    }

    @Listen("onClick=#btnSavePassword")
    public void savePassword() {
        try {
            if (txtPassword.getValue().trim().length() == 0) {
                showNotification("Chưa nhập mật khẩu cũ");
                txtPassword.focus();
                return;
            }

            UserDAOHE udhe = new UserDAOHE();
            Users u = udhe.findById(userId);
            String strLogin = udhe.checkLogin(u.getUserName(), txtPassword.getValue());
            if (!"".equals(strLogin)) {
                showNotification("Mật khẩu cũ không chính xác");
                txtPassword.focus();
                return;
            }
            if (txtPasswordNew.getValue().trim().length() == 0) {
                showNotification("Chưa nhập mật khẩu mới", Constants.Notification.ERROR);
                txtPasswordNew.focus();
                return;
            }
            if (!txtPasswordNew.getValue().equals(txtPasswordConfirm.getValue())) {
                showNotification("Mật khẩu gõ lại không trùng với mật khẩu mới", Constants.Notification.ERROR);
                txtPassword.focus();
            } else {
                String password = txtPasswordNew.getValue().trim();
                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                    Messagebox.show("Mật khẩu nhập không phải là mật khẩu mạnh, bạn có muốn tiếp tục",
                            "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                            Messagebox.QUESTION,
                            new org.zkoss.zk.ui.event.EventListener() {
                                @Override
                                public void onEvent(Event e) {
                                    if (null != e.getName()) {
                                        switch (e.getName()) {
                                            case Messagebox.ON_OK:
                                                updatePassword();
                                                break;
                                            case Messagebox.ON_CANCEL:
                                                break;
                                        }
                                    }
                                }
                            });

                } else {
                    updatePassword();
                }
            }
        } catch (Exception ex) {
            LogUtils.addLog(ex);
            showNotification("Cập nhật không thành công");
        }
    }

    @Listen("onUpload = #btnUpload")
    public void handle(UploadEvent evt) throws IOException {
        media = evt.getMedia();
        if (media instanceof org.zkoss.image.Image) {
            if (avatar != null) {
                avatar.setContent((org.zkoss.image.Image) media);
                InputStream fis = avatar.getContent().getStreamData();
                AttachDAOHE adhe = new AttachDAOHE();
                adhe.saveAvatar(fis, userId.toString());
            }
        } else {
            Messagebox.show("Không phải ảnh: " + media, "Lỗi", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public String getLabel(String key) {
        try {
            return ResourceBundleUtil.getString(key, "language_XNN_vi");
        } catch (UnsupportedEncodingException ex) {
        }
        return "";
    }
    
    /**
     * Get Attach Name
     * @param bussinessTypeId
     * @return
     */
    private String getAttachName(Long bussinessTypeId){
    	String result = "";
    	if(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE == bussinessTypeId){
    		result = (attachNoCert != null ? attachNoCert.getAttachName() :  "");
		} else if (Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN == bussinessTypeId){
			result = (attachNoCertVNOffice != null ? attachNoCertVNOffice.getAttachName() :  "");
		} else if (Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES == bussinessTypeId){
			result = (attachNoCertInMedicines != null ? attachNoCertInMedicines.getAttachName() :  "");
		}
    	result = result.trim();
    	if(result.length() > 10){
    		result = result.substring(0, 7);
    		result= result + "...";
    	}
    	return result;
    }
    
}

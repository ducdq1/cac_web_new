package com.viettel.voffice.DAO.Ycnk;



import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.Ycnk.YcnkProduct;
import com.viettel.voffice.DAOHE.YcnkProductDAOHE;

/**
 *
 * @author Linhdx
 */
public class YcnkProductCRUDController extends BaseComposer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int SAVE = 1;
    private final int SAVE_CLOSE = 2;
    private final int SAVE_COPY = 3;
    
    @Wire
    private Textbox tbProductCode, tbProductName, tbProductDescriptions,tbProductTypeCode, 
            tbProductSienceName,tbProductFacilities,
            tbProductAddress,tbPakageTypeCode,
            tbNationalCode,tbTotal,
            tbTotalUnitCode,tbNetweight,
            tbNetweightUnitCode,tbConfirmAnnounceNo,tbStorePlace,tbTransport,tbPurpose,tbCheckLicense,tbCheckAddress,
            tbCheckMethod,tbBaseUnit,tbCurrencyCode,tbVolume,
            tbVolumeUnitCode,tbPakage
            ;
    @Wire
    private Datebox  dbConfirmAnnounceDate,dbCheckTime,dbCreatedDate,dbExpiredDate;

    @Wire
    private Label lbTopWarning, lbBottomWarning;
    // Ý kiến lãnh đạo
    @Wire
    private Window windowCRUDProduct;
    private Window parentWindow;
    private YcnkProduct ycnkProduct;
    private String crudMode;
    @SuppressWarnings("rawtypes")
    private ListModel model;


    /**
     * linhdx Ham bat dau truoc khi load trang
     *
     * @param page
     * @param parent
     * @param compInfo
     * @return
     */
    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent,
            ComponentInfo compInfo) {
        HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
        parentWindow = (Window) arguments.get("parentWindow");
        crudMode = (String) arguments.get("CRUDMode");
        Long ycnkFileId = (Long) arguments.get("ycnkFileId");
		switch (crudMode) {
		case "CREATE":// Tao moi van ban
			ycnkProduct = new YcnkProduct();
			break;
		case "UPDATE":// Sua van ban
                    ycnkProduct = (YcnkProduct) arguments.get("ycnkProduct");;
			break;
                }
        ycnkProduct.setFileId(ycnkFileId);
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    /**
     * linhdx Ham thuc hien sau khi load form xong
     */
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }
   


    /**
     * linhdx Ham tao doi tuong tu form de luu lai
     *
     * @return
     */
    private YcnkProduct createYcnkProduct() {
        ycnkProduct.setProductCode(tbProductCode.getValue());
        ycnkProduct.setProductName(tbProductName.getValue());
        ycnkProduct.setProductDescriptions(tbProductDescriptions.getValue());
        ycnkProduct.setProductTypeCode(tbProductTypeCode.getValue());
        ycnkProduct.setProductSienceName(tbProductSienceName.getValue()); 
        ycnkProduct.setProductFacilities(tbProductFacilities.getValue());  
        ycnkProduct.setProductAddress(tbProductAddress.getValue());  
        ycnkProduct.setPakageTypeCode(tbPakageTypeCode.getValue());  
        ycnkProduct.setNationalCode(tbNationalCode.getValue());
        //ycnkProduct.setTotal(tbTotal.getValue());
        //ycnkProduct.setTotalUnitCode(tbTotalUnitCode.getValue());
       //ycnkProduct.setNetweight(tbNetweight.getValue());         
        ycnkProduct.setNetweightUnitCode(tbNetweightUnitCode.getValue()); 
         ycnkProduct.setConfirmAnnounceNo(tbConfirmAnnounceNo.getValue()); 
         
         ycnkProduct.setStorePlace(tbStorePlace.getValue()); 
         ycnkProduct.setTransport(tbTransport.getValue()); 
         ycnkProduct.setPurpose(tbPurpose.getValue()); 
         ycnkProduct.setCheckLicense(tbCheckLicense.getValue()); 
         ycnkProduct.setCheckAddress(tbCheckAddress.getValue()); 
         ycnkProduct.setCheckMethod(tbCheckMethod.getValue()); 
         //ycnkProduct.setBaseUnit(tbBaseUnit.getValue()); 
         ycnkProduct.setStorePlace(tbStorePlace.getValue()); 
         ycnkProduct.setPurpose(tbPurpose.getValue()); 
         ycnkProduct.setCurrencyCode(tbCurrencyCode.getValue()); 
         ycnkProduct.setVolume(tbVolume.getValue());
         ycnkProduct.setVolumeUnitCode(tbVolumeUnitCode.getValue());
         ycnkProduct.setConfirmAnnounceDate(dbConfirmAnnounceDate.getValue());
         ycnkProduct.setCheckTime(dbCheckTime.getValue());
         ycnkProduct.setCreatedDate(dbCreatedDate.getValue());
         ycnkProduct.setExpiredDate(dbExpiredDate.getValue());
        return ycnkProduct;
    }

 

 

    private void resetObject() {
        ycnkProduct = new YcnkProduct();

    }

    private void resetForm() {
        crudMode = "CREATE";
        tbProductCode.setValue("");
        tbProductName.setText("");
 

    }

    /**
     * Validate du lieu
     *
     * @return
     */
    private boolean isValidatedData() {

        if (tbProductCode.getText().matches("\\s*")) {
                showWarningMessage("Mã sản phẩm không thể để trống");
                tbProductCode.focus();
                return false;
        }

        if (tbProductName.getText().matches("\\s*")) {
                showWarningMessage("Nơi cấp không thể để trống");
                tbProductName.focus();
                return false;
        }
        
     
        return true;
    }

    /**
     * Hien thi canh bao
     *
     * @param message
     */
    protected void showWarningMessage(String message) {
        lbTopWarning.setValue(message);
        lbBottomWarning.setValue(message);
    }

    /**
     * Tao canh bao
     */
    protected void clearWarningMessage() {
        lbTopWarning.setValue("");
        lbBottomWarning.setValue("");
    }

 

    /**
     * Xu ly phim tat
     *
     * @param keyCode
     */
    public void keyEventHandle(int keyCode) {
        switch (keyCode) {
            case KeyEvent.F6:
                onSave(SAVE);
                break;
            case KeyEvent.F7:
                onSave(SAVE_CLOSE);
                break;
            case KeyEvent.F8:
                onSave(SAVE_COPY);
                break;
        }
    }

    /**
     * linhdx Xu ly su kien luu
     *
     * @param typeSave
     */
    @Listen("onClick = #btnSave, .saveClose")
    public void onSave(int typeSave) {
        clearWarningMessage();
        if (!isValidatedData()) {
            return;
        }

        try {
            if (null != crudMode) {
                YcnkProductDAOHE ycnkProductDAOHE=new YcnkProductDAOHE();
                ycnkProduct = createYcnkProduct();
                ycnkProductDAOHE.saveOrUpdate(ycnkProduct);
                switch (crudMode) {
                    case "CREATE": {
                        showNotification(String.format(
                                Constants.Notification.SAVE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.FILE),
                                Constants.Notification.INFO);
                        break;
                    }
                    case "UPDATE":
                        showNotification(String.format(
                                Constants.Notification.UPDATE_SUCCESS, Constants.DOCUMENT_TYPE_NAME.FILE),
                                Constants.Notification.INFO);
                        break;

                }

            }

           
            Events.sendEvent("onGetProduct", parentWindow, ycnkProduct);
            switch (typeSave) {
                case SAVE:
                    resetForm();
                    break;
                case SAVE_CLOSE:
                    resetForm();
                    onClose();
                    //windowCRUDProduct.onClose();
                    
                    break;    
                case SAVE_COPY:
                    break;
            }
        } catch (WrongValueException e) {
        	LogUtils.addLog(e);
            if (null != crudMode) {
                switch (crudMode) {
                    case "CREATE":
                        showNotification(String.format(
                                Constants.Notification.SAVE_ERROR, Constants.DOCUMENT_TYPE_NAME.FILE),
                                Constants.Notification.ERROR);
                        break;
                    case "UPDATE":
                        showNotification(String.format(
                                Constants.Notification.UPDATE_ERROR, Constants.DOCUMENT_TYPE_NAME.FILE),
                                Constants.Notification.ERROR);
                        break;
                }
            }
        }
    }

  

    /**
     * Dong cua so khi o dang popup
     */
    @Listen("onClose = #windowCRUDProduct")
    public void onClose() {
        windowCRUDProduct.onClose();
        Events.sendEvent("onVisible", parentWindow, null);
    }
    
    



    public ListModel getModel() {
        return model;
    }

    public void setModel(ListModel model) {
        this.model = model;
    }

    public YcnkProduct getYcnkProduct() {
        return ycnkProduct;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.DAO.RegisterDAOHE;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_XNN;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Register;
import com.viettel.voffice.model.SearchModel;

/**
 *
 * @author HaVM2
 */
public class UserApproveController extends BaseComposer {

    @Wire
    private Window userApprove;
    @Wire
    private Paging userPagingBottom;
    @Wire
    private Listbox lbList;
    @Wire
    private Textbox txtsTenDoanhNghiep, txtsMaSoThue, txtsNguoiDangKy, txtsEmail;
    @Wire
    private Listbox cbsTrangThai;

    private SearchModel lastSearchModel;

    Register searchForm;

    @Override
    public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
        return super.doBeforeCompose(page, parent, compInfo);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        ListModel infos = new ListModelArray(
                new String[][]{
                    {"-2", "--Chọn--"},
                    {Constants_XNN.Status.ACTIVE.toString(), Constants_XNN.Status.PHEDUYET},
                    {Constants_XNN.Status.DELETE.toString(), Constants_XNN.Status.TUCHOI},
                    {Constants_XNN.Status.INACTIVE.toString(), Constants_XNN.Status.CHUAPHEDUYET},});
        //ListModelArray lstModelSub = new ListModelArray(lstSub);
        cbsTrangThai.setModel(infos);
        onSearch();
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() {
        searchForm = new Register();

        if (!txtsTenDoanhNghiep.getText().isEmpty()) {
            searchForm.setBusinessNameVi(txtsTenDoanhNghiep.getText());
        }
        if (!txtsMaSoThue.getText().isEmpty()) {
            searchForm.setBusinessTaxCode(txtsMaSoThue.getText());
        }
        if (!txtsNguoiDangKy.getText().isEmpty()) {
            searchForm.setUserFullName(txtsNguoiDangKy.getText());
        }
        if (!txtsEmail.getText().isEmpty()) {
            searchForm.setManageEmail(txtsEmail.getText());
        }
        if (cbsTrangThai.getSelectedItem() != null) {
            String subId = cbsTrangThai.getSelectedItem().getValue();
            searchForm.setStatus(Long.parseLong(subId));
        }//mac dinh lay trang thai chua duyet
        else {
            searchForm.setStatus(Constants.Status.INACTIVE);
        }
        //Fill danh sach loai danh muc

        userPagingBottom.setActivePage(0);
        reloadModel();
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        reloadModel();
    }

    @Listen("onOpenUpdate = #lbList")
    public void onOpenUpdate(Event event) {
        Register obj = (Register) event.getData();
        HashMap<String, Object> arguments =new HashMap<String, Object>();
        arguments.put("id", obj.getRegisterId());
        arguments.put("CRUDMode", "CREATE");
        arguments.put("parentWindow", userApprove);
        createWindow("windowCRUDRapidTest", "/Pages/admin/user/userBusinessCreate.zul",
                arguments, Window.EMBEDDED);
        userApprove.setVisible(false);

    }

    private void reloadModel() {
        RegisterDAOHE objhe = new RegisterDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();

        PagingListModel plm = objhe.search(searchForm, start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lbList.setModel(lstModel);
    }

    public String getStatus(String sStatus) {
        if (sStatus.equals(Constants.Status.ACTIVE.toString())) {
            return Constants_XNN.Status.PHEDUYET;
        } else if (sStatus.equals(Constants.Status.DELETE.toString())) {
            return Constants_XNN.Status.TUCHOI;
        } else {
            return Constants_XNN.Status.CHUAPHEDUYET;
        }
    }

    @Listen("onVisible = #userApprove")
    public void onVisible() {
        reloadModel();
        userApprove.setVisible(true);
    }

    @Listen("onClose = #userApprove")
    public void onClose() {
        reloadModel();
        userApprove.setVisible(true);
    }

    public String getLabel(String key) {
        try {
            return ResourceBundleUtil.getString(key, "language_XNN_vi");
        } catch (UnsupportedEncodingException ex) {
        	LogUtils.addLog(ex);
        }
        return "";
    }

}

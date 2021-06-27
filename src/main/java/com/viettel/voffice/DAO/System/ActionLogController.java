/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import java.io.IOException;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.core.sys.BO.ActionLog;
import com.viettel.core.sys.DAO.ActionLogDAOHE;
import com.viettel.utils.LogUtils;

/**
 *
 * @author HaVM2
 */
public class ActionLogController extends BaseComposer {

    @Wire
    Textbox txtUserName, txtObjectTitle;
    ListModelArray lstUser;
    @Wire
    Listbox lstItems, lbModun, lbActionType;
    @Wire
    Datebox dbFromDate, dbToDate;
    @Wire
    Paging userPagingBottom;
    ActionLog searchForm;
    Long userId;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        userPagingBottom.setActivePage(0);
        userId = (Long) Executions.getCurrent().getArg().get("userId");
        if (userId == null) {
            dbFromDate.setValue(new Date());
            dbToDate.setValue(new Date());
        }
        onSearch();
    }

    private void fillDataToList() {
        ActionLogDAOHE ldhe = new ActionLogDAOHE();
        int take = userPagingBottom.getPageSize();
        int start = userPagingBottom.getActivePage() * userPagingBottom.getPageSize();
        searchForm.setUserId(userId);
        PagingListModel plm = ldhe.searchLog(searchForm, dbFromDate.getValue(), dbToDate.getValue(), start, take);
        userPagingBottom.setTotalSize(plm.getCount());
        if (plm.getCount() == 0) {
            userPagingBottom.setVisible(false);
        } else {
            userPagingBottom.setVisible(true);
        }

        ListModelArray lstModel = new ListModelArray(plm.getLstReturn());
        lstItems.setModel(lstModel);
    }

    @Listen("onPaging = #userPagingBottom")
    public void onPaging(Event event) {
        fillDataToList();
    }

    @Listen("onClick=#btnSearch")
    public void onSearch() throws IOException {
        searchForm = new ActionLog();
        searchForm.setUserName(txtUserName.getValue());
        searchForm.setModun(Long.parseLong((String) lbModun.getSelectedItem().getValue()));
        searchForm.setActionType(Long.parseLong((String) lbActionType.getSelectedItem().getValue()));
        searchForm.setObjectTitle(txtObjectTitle.getValue());
        if (("admin").equals(getUserName())) {
        } else {
            searchForm.setDeptId(getDeptId());
        }

        fillDataToList();
    }

    @Listen("onUndo=#lstItems")
    public void onUndo() throws IOException {
        Listitem item = lstItems.getSelectedItem();
        ActionLog u = item.getValue();
        LogUtils.undoLog(u.getActionLogId());
        onSearch();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.CustomControl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;
/**
 *
 * @author Administrator
 */
import com.viettel.voffice.BO.Document.OutsideOffice;
import com.viettel.voffice.DAOHE.OutsideOfficeDAOHE;

@SuppressWarnings({"rawtypes", "serial"})
public class CustomChosen extends BaseGenericForwardComposer {

    private Div flist;
    private Combobox cmbOutsideDepartment;
    private List<OutsideOffice> lstAllOutsideOffice;
    private List<OutsideOffice> lstOutsideOfficeSelected;
    private Textbox txtOutsideDepartmentId, txtOutsideDepartmentName;

    public CustomChosen() {
        super();
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        this.self.setAttribute("controller", this, false);
        Execution executtion = Executions.getCurrent();
        String outsideIds = (String) executtion.getArg().get("outsideIds");
        String outsideNames = (String) executtion.getArg().get("outsideNames");
        String idOfDisplayNameComp = (String) Executions.getCurrent().getArg().get("idOfDisplayNameComp");
        txtOutsideDepartmentName.setValue(idOfDisplayNameComp);

        OutsideOfficeDAOHE odhe = new OutsideOfficeDAOHE();
        List<OutsideOffice> listOutsideOffice = odhe.findAllByDept(getDeptId());
        lstAllOutsideOffice = new ArrayList();
        lstAllOutsideOffice.addAll(listOutsideOffice);
        cmbOutsideDepartment.setVisible(false);
        lstOutsideOfficeSelected = new ArrayList();
        loadOutSide(lstOutsideOfficeSelected);

        if (outsideIds != null && !outsideIds.isEmpty()) {
            txtOutsideDepartmentId.setValue(outsideIds);
        }
        if (outsideNames != null && !outsideNames.isEmpty()) {
            if (outsideNames.contains(";")) {
                String[] osNameArr = outsideNames.split(";");
                for (String os : osNameArr) {
                    if (findInList(os) != null) {
                        addToSelect(os);
                    } else {
                        addToSelect(os);
                    }
                }
            } else {
                if (findInList(outsideNames) != null) {
                    addToSelect(outsideNames);
                } else {
                    addToSelect(outsideNames);
                }
            }
        }
    }

    private void loadOutSide(List<OutsideOffice> lstSelected) {
        cmbOutsideDepartment.getItems().clear();
        List<OutsideOffice> lst = new ArrayList();
        for (OutsideOffice oA : lstAllOutsideOffice) {
            if (!lstSelected.contains(oA)) {
                lst.add(oA);
            }
        }
        if (lst.size() > 0) {
            for (OutsideOffice d : lst) {
                Comboitem ci = new Comboitem();
                ci.setValue(d.getOfficeId());
                ci.setLabel(d.getOfficeName());
                cmbOutsideDepartment.getItems().add(ci);
            }
        }
    }

    public void onOK$cmbOutsideDepartment(Event event) {
        String ci = cmbOutsideDepartment.getValue();
        if (!ci.isEmpty()) {
            addToSelect(ci);
        }
        cmbOutsideDepartment.setVisible(true);
        cmbOutsideDepartment.setValue("");
        cmbOutsideDepartment.setFocus(true);
    }

    public void onBlur$cmbOutsideDepartment() {
        String ci = cmbOutsideDepartment.getValue();
        if (!ci.isEmpty()) {
            addToSelect(ci);
        }
        cmbOutsideDepartment.setVisible(false);
        cmbOutsideDepartment.setValue("");
        txtOutsideDepartmentName.setVisible(true);
    }

    public void onClick$cmbOutsideDepartment() {
        Comboitem ci = cmbOutsideDepartment.getSelectedItem();
        if (ci != null && !ci.getLabel().isEmpty()) {
            addToSelect(ci.getLabel());
        }
    }

    public void onClick$imgDeleleAll() {
        if (flist.getChildren().size() > 0) {
            List childFlist = flist.getChildren();
            if (childFlist.size() > 0) {
                Iterator<Component> iter = childFlist.iterator();
                while (iter.hasNext()) {
                    iter.remove();
                }
            }
            List<OutsideOffice> lst = new ArrayList();
            lstOutsideOfficeSelected = new ArrayList();
            loadOutSide(lst);
        }
        setValueToTexbox();
    }

    private void addToSelect(final String strInput) {
        OutsideOffice op = findInSelectedList(strInput);
        //
        // neu da co  roi thi ko add vao nua
        //
        if (op != null) {
            return;
        }
        final Hlayout hl = new Hlayout();
        hl.setSpacing("6px");
        hl.setClass("newFile");
        hl.appendChild(new Label(strInput));
        Image rm = new Image("/Share/img/icon/close.gif");
        rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                hl.detach();
                OutsideOffice o = findInSelectedList(strInput);
                if (o != null) {
                    lstOutsideOfficeSelected.remove(o);
                }
                loadOutSide(lstOutsideOfficeSelected);

                setValueToTexbox();
            }
        });
        hl.appendChild(rm);
        flist.appendChild(hl);
        OutsideOffice o = findInList(strInput);
        if(o == null){
            o = new OutsideOffice();
            o.setOfficeName(strInput);
        }
        lstOutsideOfficeSelected.add(o);
        loadOutSide(lstOutsideOfficeSelected);
        cmbOutsideDepartment.setVisible(false);
        cmbOutsideDepartment.setValue("");
        setValueToTexbox();
    }

    public void onClick$flist() {
        cmbOutsideDepartment.setVisible(true);
        cmbOutsideDepartment.setFocus(true);
    }

    public void setLstAllOutsideOffice(List<OutsideOffice> lstAllOutsideOffice) {
        this.lstAllOutsideOffice = lstAllOutsideOffice;
    }

    public List<OutsideOffice> getLstAllOutsideOffice() {
        return lstAllOutsideOffice;
    }

    private OutsideOffice findInList(Long id) {
        OutsideOffice result = null;
        if (lstAllOutsideOffice.size() > 0) {
            for (OutsideOffice o : lstAllOutsideOffice) {
                if (o.getOfficeId().equals(id)) {
                    result = o;
                    break;
                }
            }
        }
        return result;
    }

    private OutsideOffice findInList(String name) {
        OutsideOffice result = null;
        if (lstAllOutsideOffice.size() > 0) {
            for (OutsideOffice o : lstAllOutsideOffice) {
                if (o.getOfficeName().toLowerCase().equals(name.toLowerCase().trim())) {
                    result = o;
                    break;
                }
            }
        }
        return result;
    }

    private OutsideOffice findInSelectedList(String name) {
        OutsideOffice result = null;
        if (lstOutsideOfficeSelected.size() > 0) {
            for (OutsideOffice o : lstOutsideOfficeSelected) {
                if (o.getOfficeName().toLowerCase().equals(name.toLowerCase().trim())) {
                    result = o;
                    break;
                }
            }
        }
        return result;
    }

    private void setValueToTexbox() {
        Textbox txtDeptName = (Textbox) Path.getComponent(txtOutsideDepartmentName.getValue());
        if (lstOutsideOfficeSelected.size() > 0) {
            String ids = "";
            String names = "";
            for (OutsideOffice o : lstOutsideOfficeSelected) {
                if (o.getOfficeId() != null) {
                    ids += ids.isEmpty() ? o.getOfficeId() : ";" + o.getOfficeId();
                }
                if (!o.getOfficeName().isEmpty()) {
                    names += names.isEmpty() ? o.getOfficeName() : ";" + o.getOfficeName();
                }
            }
            if (!ids.isEmpty()) {
                txtOutsideDepartmentId.setValue(ids);
            }
            if (!names.isEmpty()) {
                txtDeptName.setValue(names);
            }
        } else {
            txtDeptName.setValue("");
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.files.DAO;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author linhdx
 */
public class FilesCreateController extends BaseComposer {/*

    @Wire
    Listbox lbProcedure, lbDept;
    @Wire
    Window filesCreateWnd;

    // label for validating data
    @Wire
    private Label lbTopWarning, lbBottomWarning;

    public FilesCreateController() {
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadInfoToForm();

    }

    public void loadInfoToForm() {
        CategoryDAOHE cdhe = new CategoryDAOHE();
        List lstProcedure = cdhe.findAllCategory(Constants.CATEGORY_TYPE.PROCEDURE);
        ListModelArray lstModelProcedure = new ListModelArray(lstProcedure);
        lbProcedure.setModel(lstModelProcedure);
        lbProcedure.renderAll();
        lbProcedure.setSelectedIndex(0);
        onSelectProcedure();
//        FlowDAOHE fhe = new FlowDAOHE();
//        List<Department> lstDept = fhe.initDept();
//        ListModelArray lstModelDept = new ListModelArray(lstDept);
//        lbDept.setModel(lstModelDept);
//        lbDept.renderAll();
//        lbDept.setSelectedIndex(0);
    }

    public void onSelectProcedure() {
        if (lbProcedure.getSelectedItem() != null) {
            //Get Dept from flow
            Long procedureId = (Long) lbProcedure.getSelectedItem().getValue();
            FlowDAOHE fhe = new FlowDAOHE();
            List<Department> lstDept = fhe.getListDeptFromProcedureId(procedureId);
            ListModelArray lstModelDept = new ListModelArray(lstDept);
            lbDept.setModel(lstModelDept);
            lbDept.renderAll();
            lbDept.setSelectedIndex(0);
        }
    }

    @Listen("onClick=#btnNext")
    public void onNext() throws IOException {
        HashMap<String, Object> args  = new HashMap<String, Object>();
        if (lbProcedure.getSelectedItem() != null) {
            Long procedureId = (Long) lbProcedure.getSelectedItem().getValue();
            args.put("procedureId", procedureId);
        }
        if (lbDept.getSelectedItem() != null) {
            Long deptId = (Long) lbDept.getSelectedItem().getValue();
            args.put("deptId", deptId);
        }
        args.put("CRUDMode", "CREATE");
        args.put("parentWindow", filesCreateWnd);

        createWindow("windowCRUDRapidTest", "/Pages/files/filesRapidTestChoose.zul", args, Window.EMBEDDED);
        //createWindow("windowCRUDRapidTest", "/Pages/rapidTest/rapidTestCRUD.zul", args, Window.EMBEDDED);
        filesCreateWnd.setVisible(false);
//        
//        Window window = (Window) Executions.createComponents(
//                "/Pages/rapidTest/rapidTestCRUD.zul", null, args);
//        window.doModal();
    }

    @Listen("onVisible = #filesCreateWnd")
    public void onVisible() {
        filesCreateWnd.setVisible(true);
    }

    *//**
     * Hien thi canh bao
     *
     * @param message
     *//*
    protected void showWarningMessage(String message) {
        lbTopWarning.setValue(message);
        lbBottomWarning.setValue(message);
    }

*/}

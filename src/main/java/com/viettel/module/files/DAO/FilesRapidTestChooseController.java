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
public class FilesRapidTestChooseController extends BaseComposer {/*

    @Wire
    Window filesRapidTestChooseWnd;
    private Window parentWindow;
    Long deptId, procedureId;

    // label for validating data
    @Wire
    private Label lbTopWarning, lbBottomWarning;

    public FilesRapidTestChooseController() {
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        loadInfoToForm();

    }

    public void loadInfoToForm() {
        HashMap<String, Object> arguments = (HashMap<String,Object>) Executions.getCurrent().getArg();
        parentWindow = (Window) arguments.get("parentWindow");
        deptId = (Long) arguments.get("deptId");
        procedureId = (Long) arguments.get("procedureId");
    }

    public void createNew() throws IOException {
        HashMap<String, Object> args  = new HashMap<String, Object>();
        args.put("documentTypeCode", Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_TAOMOI);
        setFowardPage(args);
    }
    
    public void changeRequest() throws IOException {
        HashMap<String, Object> args  = new HashMap<String, Object>();
        args.put("documentTypeCode", Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_BOSUNG);
        setFowardPage(args);
    }
    
    public void extendRequest() throws IOException {
        HashMap<String, Object> args  = new HashMap<String, Object>();
        args.put("documentTypeCode", Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_GIAHAN);
        setFowardPage(args);
    }
    public void setFowardPage(HashMap<String, Object> args ){
        args.put("procedureId", procedureId);
        args.put("deptId", deptId);
        args.put("CRUDMode", "CREATE");
        args.put("parentWindow", filesRapidTestChooseWnd);

        createWindow("windowCRUDRapidTest", "/Pages/rapidTest/rapidTestCRUD.zul", args, Window.EMBEDDED);
        filesRapidTestChooseWnd.setVisible(false);
    }
    

    @Listen("onVisible = #filesRapidTestChooseWnd")
    public void onVisible() {
        filesRapidTestChooseWnd.setVisible(true);
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

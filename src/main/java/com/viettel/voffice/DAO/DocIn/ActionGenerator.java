/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.DocIn;

import java.util.HashMap;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.workflow.BO.Process;
import com.viettel.utils.Constants;
import com.viettel.voffice.BO.Document.DocumentPublish;
import com.viettel.voffice.BO.Document.DocumentReceive;
import com.viettel.voffice.DAOHE.DocumentReceiveDAOHE;

/**
 *
 * @author hoangnv28
 */
public class ActionGenerator {

    private BaseComposer composer;

    public ActionGenerator(BaseComposer composer) {
        this.composer = composer;
    }

    public EventListener generateEventForAction(final String action, final Window window,
            final DocumentReceive docIn, final Process process) {
        EventListener el;

        if (null != action) {
            switch (action) {
                case Constants.ACTION.NAME.PUT_IN_BOOK:
                    //Chuyển cả bì, VT có quyền chỉnh sửa tất cả nội dung VB
                    if (docIn.getSendPacking() == 1L) {
                        el = new EventListener() {
                            @Override
                            public void onEvent(Event t) throws Exception {
                                HashMap<String, Object> arguments = new HashMap<String, Object> ();
                                arguments.put("documentReceiveId",
                                        docIn.getDocumentReceiveId());
                                arguments.put("windowParent", window);
                                arguments.put("CRUDMode", "PUT_IN_BOOK");
                                arguments.put("process", process);
                                window.setVisible(false);
                                composer.createWindow("windowCRUDDocIn",
                                        "/Pages/document/docIn/docInCRUD.zul",
                                        arguments, Window.EMBEDDED);
                            }
                        };
                    } else {
                        el = new EventListener() {
                            @Override
                            public void onEvent(Event t) throws Exception {
                                HashMap<String, Object> arguments = new HashMap<String, Object> ();
                                arguments.put("documentReceiveId",
                                        docIn.getDocumentReceiveId());
                                arguments.put("windowParent", window);
                                arguments.put("processId",
                                        process.getProcessId());
                                window.setVisible(false);
                                composer.createWindow("windowPutInBook",
                                        "/Pages/document/docIn/putInBook.zul",
                                        arguments, Window.EMBEDDED);
                            }
                        };
                    }
                    break;
                case Constants.ACTION.NAME.RETURN:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> arguments = new HashMap<String, Object>() ;
                            arguments.put("documentReceiveId", docIn.getDocumentReceiveId());
                            arguments.put("processId", process.getProcessId());
                            arguments.put("windowParent", window);
                            arguments.put("composer", new DocInReturnComposer());
                            composer.createWindow("windowReturn", "/Pages/document/docIn/subForm/sendProcess.zul", arguments, Window.MODAL);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.RETRIEVE:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> arguments = new HashMap<String, Object> ();
                            arguments.put("process", process);
                            arguments.put("windowParent", window);
                            composer.createWindow("windowRetrieve",
                                    "/Pages/document/docIn/subForm/retrieve.zul", arguments,
                                    Window.MODAL);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.SUMPLEMENT:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> data = new HashMap<String, Object> ();
                            data.put("documentReceiveId",
                                    docIn.getDocumentReceiveId());
                            data.put("process", process);
                            data.put("windowParent", window);
                            data.put("composer", new DocInAdditionController());
                            composer.createWindow(
                                    "windowComment",
                                    "/Pages/document/docIn/subForm/sendProcess.zul",
                                    data, Window.MODAL);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.CREATE_FILE:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> arguments = new HashMap<String, Object> ();
                            arguments.put("documentReceiveId", docIn.getDocumentReceiveId());
                            arguments.put("parentWindow", window);
                            composer.createWindow(
                                    "fileCreateWnd",
                                    "/Pages/file/fileCreateWnd.zul",
                                    arguments, Window.EMBEDDED);
                            window.setVisible(false);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.CREATE_DRAFT:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> arguments = new HashMap<String, Object> ();
                            arguments.put("selectedRecord",
                                    new DocumentPublish());
                            arguments.put("recordMode", "Create");
                            arguments.put("processId",
                                    process.getProcessId());
                            arguments
                                    .put("documentReceiveId",
                                            docIn.getDocumentReceiveId());
                            arguments.put("parentWindow",
                                    window);
                            composer.createWindow(
                                    "docCRUD",
                                    "/Pages/document/docOut/insertOrupdate.zul",
                                    arguments, Window.EMBEDDED);
                            window.setVisible(false);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.GET_OPINION:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> arguments = new HashMap<String, Object> ();
                            arguments.put("documentReceiveId",
                                    docIn.getDocumentReceiveId());
                            arguments.put("process", process);
                            arguments.put("actionName", action);
                            arguments.put("actionType",
                                    Constants.NODE_ASSOCIATE_TYPE.NORMAL);
                            arguments.put("windowParent", window);
                            arguments.put("composer",
                                    new DocInSendProcessFirstTimeController());
                            arguments.put("processType",
                                    Constants.PROCESS_TYPE.COMMENT);
                            composer.createWindow(
                                    "windowComment",
                                    "/Pages/document/docIn/subForm/sendProcess.zul",
                                    arguments, Window.MODAL);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.GIVE_OPINION:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> arguments = new HashMap<String, Object> ();
                            arguments.put("documentReceiveId",
                                    docIn.getDocumentReceiveId());
                            arguments.put("process", process);
                            arguments.put("actionName", action);
                            arguments.put("windowParent", window);
                            arguments.put("composer",
                                    new DocInSendProcessFirstTimeController());
                            composer.createWindow(
                                    "windowComment",
                                    "/Pages/document/docIn/subForm/sendProcess.zul",
                                    arguments, Window.MODAL);
                        }
                    };
                    break;
                case Constants.ACTION.NAME.TRANSFORM_TO_PROCESS:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            Messagebox.show("Bạn có muốn chuyển về xử lý", "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                                        @Override
                                        public void onEvent(Event event) {
                                            switch (event.getName()) {
                                                case Messagebox.ON_OK:
                                                    DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
                                                    docReceiveDAOHE.transformToProcess(process, composer);
                                                    Events.sendEvent("onAfterSendProcess", window, null);
                                                    break;
                                                default:
                                                    //do nothing
                                                    break;
                                            }
                                        }

                                    });
                        }
                    };
                    break;
                case Constants.ACTION.NAME.TRANSFORM_TO_RECEIVE_TO_KNOW:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            Messagebox.show("Bạn có muốn chuyển về nhận để biết", "Xác nhận", Messagebox.OK | Messagebox.CANCEL,
                                    Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
                                        @Override
                                        public void onEvent(Event event) {
                                            switch (event.getName()) {
                                                case Messagebox.ON_OK:
                                                    DocumentReceiveDAOHE docReceiveDAOHE = new DocumentReceiveDAOHE();
                                                    docReceiveDAOHE.transformToReceiveToKnow(process.getProcessId(),
                                                            process.getProcessType(), process.getParentId(), composer);
                                                    Events.sendEvent("onAfterSendProcess", window, null);
                                                    break;
                                                default:
                                                    //do nothing
                                                    break;
                                            }
                                        }

                                    });
                        }
                    };
                    break;
                default:
                    el = new EventListener() {
                        @Override
                        public void onEvent(Event t) throws Exception {
                            HashMap<String, Object> data = new HashMap<String, Object> ();
                            data.put("documentReceiveId",
                                    docIn.getDocumentReceiveId());
                            data.put("process", process);
                            data.put("actionName", action);
                            data.put("actionType", Constants.NODE_ASSOCIATE_TYPE.NORMAL);
                            data.put("windowParent", window);
                            data.put("composer", new DocInSendProcessFirstTimeController());
                            composer.createWindow(
                                    "windowComment",
                                    "/Pages/document/docIn/subForm/sendProcess.zul",
                                    data, Window.MODAL);
                        }
                    };
                    break;
            }
            return el;
        }
        return null;
    }
}

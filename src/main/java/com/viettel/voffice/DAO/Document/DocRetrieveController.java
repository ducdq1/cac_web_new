/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.Document;

import com.viettel.core.base.DAO.BaseGenericForwardComposer;

/**
 *
 * @author giangpn
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class DocRetrieveController extends BaseGenericForwardComposer {/*

	@WireVariable
	private String recordMode;
	Listbox lstDeptUser;
	Window retrieveWin;
	CKeditor ckDocumentContent;
	protected Listbox lboxStatus;
	protected Textbox txtNote;
	private Window parentWindow;
	private DocumentPublish docSelected;
	private AnnotateDataBinder binder;
	private Users userSelected;
	private Department deptSelected;
	private Long flowId;
	private Long nodeId;
	private Process processCurrent;// process dang duoc xu li

	public DocRetrieveController() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.self.setAttribute("controller", this, false);
		Execution execution = Executions.getCurrent();
		setRecordMode((String) execution.getArg().get("recordMode"));
		setDocSelected((DocumentPublish) execution.getArg().get(
				"selectedRecord"));
		setParentWindow((Window) execution.getArg().get("parentWindow"));
		setFlowId((Long) execution.getArg().get("flowId"));
		setNodeId((Long) execution.getArg().get("nodeId"));
		processCurrent = (Process) execution.getArg().get("processCurrent");

		ProcessDAOHE phe = new ProcessDAOHE();
		List lstData = phe.getChildProcess(getUserId(),
				docSelected.getDocumentPublishId());
		List result = new ArrayList();
		if (!lstData.isEmpty()) {
			// chi lay 1 process do hien tai chi gui duy nhat 1 nguoi

			Process p = (Process) lstData.get(0);
			UserDAOHE uDaoHe = new UserDAOHE();
			Users u = uDaoHe.getUserById(p.getReceiveUserId());
			p.setReceiveUser(u.getFullName());
			p.setPositionName(u.getPosName());
			result.add(p);
		}
		ListModelArray lstModel = new ListModelArray(result);
		// lstModel.setMultiple(true);
		lstDeptUser.setModel(lstModel);
	}

	public void onCreate$transferWin(Event event) {
		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute(
				"binder", true);
		this.binder.loadAll();
	}

	@SuppressWarnings("unchecked")
	public void onClick$btnRetrieve(Event ev) {
		binder.saveAll();
		HashMap<String, Object> args =HashMap<String, Object>();
		final Set<Listitem> listSelectedItem = lstDeptUser.getSelectedItems();

		if (listSelectedItem.isEmpty()) {
			Messagebox.show("Bạn chưa chọn đơn vị xử lý");
		} else {
			int count = 0;
			List<Process> list = new ArrayList();
			for (Listitem selectedItem : listSelectedItem) {
				Process item = (Process) selectedItem.getValue();
				item.setNote(txtNote.getValue());

				if (item.getStatus().equals(Constants.PROCESS_STATUS.NEW)) {
					item.setIsActive(Constants.Status.INACTIVE);
				}
				item.setStatus(Constants.PROCESS_STATUS.RETRIEVE);
				list.add(item);
				count++;
			}

		 
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				processDAOHE.saveOrUpdate(list);

				if (count == lstDeptUser.getItemCount()) {
					if (processCurrent != null) {
						// chuyen trang thai process hien tai thanh cho xu ly
						processCurrent.setStatus(Constants.PROCESS_STATUS.READ);
						processDAOHE.saveOrUpdate(processCurrent);
					} else {
						// truong hop vb do chuyen vien thu hoi(tro ve du thao)
						DocumentDAOHE docDaoHe = new DocumentDAOHE();
						docSelected.setStatus(Constants.DOCUMENT_STATUS.NEW);
						docDaoHe.update(docSelected);
					}
				}
				Events.sendEvent(new Event("onSaveRetrieve", parentWindow, args));
				retrieveWin.onClose();
				showNotify("Thu hồi văn bản thành công!");
			 
		}
	}

	public String getRecordMode() {
		return recordMode;
	}

	public void setRecordMode(String recordMode) {
		this.recordMode = recordMode;
	}

	public void setDocSelected(DocumentPublish docSelected) {
		this.docSelected = docSelected;
	}

	public DocumentPublish getDocSelected() {
		return docSelected;
	}

	public void setUserSelected(Users userSelected) {
		this.userSelected = userSelected;
	}

	public Users getUserSelected() {
		return userSelected;
	}

	public Window getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Window parentWindow) {
		this.parentWindow = parentWindow;
	}

	public void setDeptSelected(Department deptSelected) {
		this.deptSelected = deptSelected;
	}

	public Department getDeptSelected() {
		return deptSelected;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public AbstractTreeModel getTreeModel() {
		AbstractTreeModel deptUserTreeModel = new DeptTreeModel();
		deptUserTreeModel.setMultiple(true);
		return deptUserTreeModel;
	}
*/}

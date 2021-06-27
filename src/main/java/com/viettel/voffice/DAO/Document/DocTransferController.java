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
public class DocTransferController extends BaseGenericForwardComposer {/*

	@WireVariable
	private String recordMode;
	Listbox lstDeptUser;
	Window transferWin;
	CKeditor ckDocumentContent;
	protected Listbox lboxStatus;
	protected Textbox txtNote;
	private Window parentWindow;
	private DocumentPublish docSelected;
	private AnnotateDataBinder binder;
	private Users userSelected;
	private Department deptSelected;
	private Long flowId;
	private NodeToNode action;
	private Process processCurrent;// process dang duoc xu li
	private Vlayout flist;
	private List<Media> listMedia;
	Button btnAttach;
	private ListModelArray dataDeptUser;

	public DocTransferController() {
		super();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.self.setAttribute("controller", this, false);
		Execution execution = Executions.getCurrent();
		setRecordMode((String) execution.getArg().get("recordMode"));
		setDocSelected((DocumentPublish) execution.getArg().get("selectedRecord"));
		setParentWindow((Window) execution.getArg().get("parentWindow"));
		setAction((NodeToNode) execution.getArg().get("action"));
		processCurrent = (Process) execution.getArg().get("processCurrent");

		FlowDAOHE fdhe = new FlowDAOHE();
		List lstData = fdhe.getFullNodeActor(action.getNextId(), getDeptId());
		if (lstData != null) {
			List lstUser = new ArrayList();

			String checkBeforeSend = ResourceBundleUtil.getString("check_before_send");

			if (action.getAction().toLowerCase().equals("trả lại") || "1L".equals(checkBeforeSend)) {
				// Loc danh sach nguoi da tham gia luong de tra lai
				ProcessDAOHE processDAOHE = new ProcessDAOHE();
				List lstProcess = processDAOHE.getListProcessDocPublishId(docSelected.getDocumentPublishId());
				if (lstProcess != null) {
					List<Long> lstSendUserId = new ArrayList();
					int count = 0;
					for (Object o : lstProcess) {
						Process p = (Process) o;
						lstSendUserId.add(p.getSendUserId());
					}

					for (Object o : lstData) {
						NodeDeptUser ndu = (NodeDeptUser) o;
						if (lstSendUserId.contains(ndu.getUserId())) {
							lstUser.add(o);
						} else {
							count++;
						}
					}
					if (count == lstData.size()) {
						lstUser = lstData;
					}
				} else {
					lstUser = lstData;
				}
			} else {
				lstUser = lstData;
			}

			// truong hop ko co cau hinh xu ly chinh,gan mac dinh xu ly chinh
			// cho user
			for (Object o : lstUser) {
				NodeDeptUser ndu = (NodeDeptUser) o;
				if (ndu.getProcessType() == null) {
					ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
				} else {
					if (!ndu.getProcessType().equals(Constants.PROCESS_TYPE.MAIN)
							&& !ndu.getProcessType().equals(Constants.PROCESS_TYPE.RECEIVE_TO_KNOW)) {
						ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
					}
				}
			}

			ListModelArray lstModel = new ListModelArray(lstUser);
			setDataDeptUser(lstModel);
			lstDeptUser.setModel(lstModel);
			if (lstUser.size() > 0) {
				// lstDeptUser.setSelectedItem(lstDeptUser.getItemAtIndex(0));
			}
		} else {
			// lstDeptUser.setVisible(false);
		}
		listMedia = new ArrayList<>();
	}

	public void onCreate$transferWin(Event event) {
		this.binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
		this.binder.loadAll();
	}

	public void onClick$btnTransfer(Event ev) throws IOException {
		binder.saveAll();
		HashMap<String, Object> args =HashMap<String, Object>();
		final Set<Listitem> listSelectedItem = lstDeptUser.getSelectedItems();

		//
		// truong hop ket thuc xu ly,kiem tra nodeId co la node ket thuc
		//
		NodeDAOHE nDaoHe = new NodeDAOHE();
		Node nodeNext = nDaoHe.getById("nodeId", action.getNextId());
		if (nodeNext == null) {
			return;
		}
		ProcessDAOHE processDAOHE = new ProcessDAOHE();
		if (nodeNext.getType().equals(Constants.NODE_TYPE.NODE_TYPE_FINISH)) {
			processCurrent.setStatus(Constants.PROCESS_STATUS.DID);
			Notify nf = sendNotify(processCurrent, docSelected, getUserToken(), txtNote.getValue());
			processDAOHE.saveOrUpdate(processCurrent);
			if (!listMedia.isEmpty()) {
				AttachDAO att = new AttachDAO();
				att.saveFileAttach(listMedia.get(0), nf.getNotifyId(), Constants.OBJECT_TYPE.NOTIFY, null);
			}
			args.put("actionResult", "Success");
			Events.sendEvent(new Event("onSaved", parentWindow, args));
			transferWin.onClose();
			//
			// kiem tra cac huong xu ly, neu co cung trang thai DID thi ket thuc
			// vb
			//

		} else {
			if (listSelectedItem.isEmpty()) {
				Messagebox.show("Bạn chưa chọn đơn vị xử lý");
			} else {

				if (!validateTransfer()) {
					return;
				}
				List<com.viettel.core.workflow.BO.Process> pros = new ArrayList();
				for (Listitem selectedItem : listSelectedItem) {

					NodeDeptUser item = (NodeDeptUser) selectedItem.getValue();
					Process obj = getProcessForSave(processCurrent, docSelected, action, item, getUserToken(),
							txtNote.getValue());

					if (!item.getUserId().equals(docSelected.getCreatorId())
							|| !action.getAction().toLowerCase().equals("trả lại")) {
						//
						// truong hop gui tra lai cho nguoi du thao, khong tao
						// process
						//
						pros.add(obj);
					} else {
						docSelected.setStatus(Constants.DOCUMENT_STATUS.NEW);

					}

					//
					// kiem tra node nay co quy trinh con hay khong
					//
					FlowDAOHE fdhe = new FlowDAOHE();
					Long nodeToSend;
					// Cho nay sau sua lai:
					// Constants.OBJECT_TYPE.DOCUMENT_PUBLISH
					// Flow fs = fdhe.getDeptFlow(obj.getReceiveGroupId(), 301L,
					// null);
					// Flow fsc = fdhe.getDeptFlow(obj.getSendGroupId(), 301L,
					// null);
					List listfs = fdhe.getDeptFlow(obj.getReceiveGroupId(), 301L, null);
					List listfsc = fdhe.getDeptFlow(obj.getSendGroupId(), 301L, null);
					Flow fs = listfs == null ? null : (Flow) listfs.get(0);
					Flow fsc = listfsc == null ? null : (Flow) listfsc.get(0);
					if (fs == null) {
						//
						// truong hop khong co quy trình con: kiem tra nextNode
						// la transparent hay khong
						//
						nodeToSend = obj.getNodeId();
					} else {
						//
						// cho process nay chay theo quy trinh con cua don vi
						// lay flow cua don vi con, kiem tra co nextAction
						// transparent hay khong
						if (!java.util.Objects.equals(fsc.getFlowId(), fs.getFlowId())) {
							// nodeToSend =
							// fdhe.getFirstNodeOfFlow(fs.getFlowId());\
							nodeToSend = (Long) listfs.get(1);
						} else {
							nodeToSend = obj.getNodeId();
						}
					}
					List<Process> proToSend = getProcessTransparent(obj, nodeToSend, txtNote.getValue(), true);
					pros.addAll(proToSend);
				}

				if (processCurrent != null) {
					processCurrent.setStatus(Constants.PROCESS_STATUS.DID);
					processCurrent.setActionName("Đã " + action.getAction().toLowerCase());
					processDAOHE.saveOrUpdate(processCurrent);

				}
				processDAOHE.saveOrUpdate(pros);

				Notify nf = sendNotify(processCurrent, docSelected, getUserToken(), txtNote.getValue());

				if (!listMedia.isEmpty()) {
					AttachDAO att = new AttachDAO();
					for (Media m : listMedia) {
						// saveFileAttach(m, nf.getNotifyId(),
						// Constants.OBJECT_TYPE.NOTIFY);
						att.saveFileAttachNotify(m, nf);
					}
				}

				// Cap nhat trang thai van ban tu du thao - sang da gui
				DocumentDAOHE docDaoHe = new DocumentDAOHE();
				if (action.getAction().toLowerCase().equals("phê duyệt")) {
					docSelected.setStatus(Constants.DOCUMENT_STATUS.APPROVAL);
				} else {
					if (!action.getAction().toLowerCase().equals("trả lại")) {
						docSelected.setStatus(Constants.DOCUMENT_STATUS.SEND_COORDINATE);
					}
				}

				if (docDaoHe.onUpdate(docSelected)) {
					if (action.getAction().toLowerCase().equals("trả lại")) {
						showNotify("Gửi trả lại thành công!");
					} else {
						showNotify("Chuyển xử lý thành công!");
					}

					args.put("actionResult", "Success");
					Events.sendEvent(new Event("onSaved", parentWindow, args));
					transferWin.onClose();
				} else {
					showErrorNotify("Chuyển xử lý lỗi!");
				}

			}
		}
	}

	public void onChangeProcessType$lstDeptUser(ForwardEvent evt) {
		Event origin = Events.getRealOrigin(evt);
		Image btn = (Image) origin.getTarget();
		Listitem litem = (Listitem) btn.getParent().getParent();

		NodeDeptUser ndu = (NodeDeptUser) litem.getValue();
		final Set<Listitem> listSelectedItem = lstDeptUser.getSelectedItems();

		if (ndu.getProcessType() == null) {
			ndu.setProcessType(0l);
		} else {
			if (ndu.getProcessType() < 3l) {
				ndu.setProcessType(ndu.getProcessType() + 1);
			} else {
				ndu.setProcessType(null);
			}
		}

		lstDeptUser.setModel(getDataDeptUser());
	}

	public void onUpload$btnAttach(UploadEvent event) throws UnsupportedEncodingException {
		final Media media = event.getMedia();
		String extFile = FileUtil.getFileExtention(media.getName());
		if (!FileUtil.validFileType(extFile.replace(".", ""))) {
			showErrorNotify("File không đúng định dạng!");
			return;
		}
		// luu file vao danh sach file
		listMedia.add(media);

		// layout hien thi ten file va nut "Xóa"
		final Hlayout hl = new Hlayout();
		hl.setSpacing("6px");
		hl.setClass("newFile");
		hl.appendChild(new Label(media.getName()));
		A rm = new A("Xóa");
		rm.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				hl.detach();
				// xoa file khoi danh sach file
				listMedia.remove(media);
				if (listMedia.isEmpty()) {
					// btnAttach.setVisible(true);
				}
			}
		});
		hl.appendChild(rm);
		flist.appendChild(hl);
		// btnAttach.setVisible(false);
	}

	public AbstractTreeModel getTreeModel() {
		AbstractTreeModel deptUserTreeModel = new DeptTreeModel();
		deptUserTreeModel.setMultiple(true);
		return deptUserTreeModel;
	}

	// <editor-fold defaultstate="collapsed" desc="public set,get">
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

	public void setAction(NodeToNode action) {
		this.action = action;
	}

	public NodeToNode getAction() {
		return action;
	}

	public void setDataDeptUser(ListModelArray dataDeptUser) {
		this.dataDeptUser = dataDeptUser;
	}

	public ListModelArray getDataDeptUser() {
		return dataDeptUser;
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="private method">

	private Process getProcessForSave(Process current, DocumentPublish doc, NodeToNode action, NodeDeptUser item,
			UserToken u, String comment) {
		Process obj = new Process();
		Date now = new Date();
		obj.setNodeId(action.getNextId());
		obj.setNote(comment);
		obj.setSendDate(now);
		obj.setSendUserId(u.getUserId());
		obj.setSendUser(u.getUserFullName());
		obj.setSendGroupId(u.getDeptId());
		obj.setSendGroup(u.getDeptName());
		obj.setReceiveUserId(item.getUserId());
		obj.setReceiveUser(item.getFullName());
		obj.setReceiveGroupId(item.getDeptId());
		obj.setReceiveGroup(item.getDeptName());
		obj.setIsActive(Constants.Status.ACTIVE);
		obj.setStatus(Constants.PROCESS_STATUS.NEW);
		String actionName = "";
		if (action.getAction() == null) {
			actionName = "Đã chuyển";
		} else {
			if (action.getAction().toLowerCase().equals("trả lại")) {
				actionName = "Bị trả lại";
			} else {
				actionName = "Chờ xử lý";
			}
		}

		obj.setActionName(actionName);
		if (current != null && current.getProcessId() != null) {
			obj.setObjectId(current.getObjectId());
			obj.setObjectType(current.getObjectType());
			obj.setOrderProcess(current.getOrderProcess() + 1);
			obj.setActionType(action.getType());
			if (!java.util.Objects.equals(current.getProcessType(), Constants.PROCESS_TYPE.MAIN)) {
				obj.setProcessType(Constants.PROCESS_TYPE.COOPERATE);
			} else {
				obj.setProcessType(item.getProcessType() == null ? 0L : item.getProcessType());
			}
			obj.setParentId(current.getProcessId());
		} else {
			obj.setObjectId(doc.getDocumentPublishId());
			obj.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
			obj.setActionType(Constants.NODE_ASSOCIATE_TYPE.NORMAL);
			obj.setOrderProcess(1L);
			obj.setProcessType(item.getProcessType() == null ? 0L : item.getProcessType());
		}
		// Notify nf = getNotifyForSave(current, doc, item, u, comment);
		// obj.setNotify(nf);
		return obj;
	}

	private List<Process> getProcessTransparent(Process obj, Long nodeTarget, String comment, boolean notTransparent) {
		List<Process> pros = new ArrayList();
		FlowDAOHE fdhe = new FlowDAOHE();
		NodeToNodeDAOHE ntnDAOHE = new NodeToNodeDAOHE();
		List<NodeToNode> actTransparent = ntnDAOHE.getNextAction(nodeTarget, Constants.NODE_ASSOCIATE_TYPE.ALL);
		if (!actTransparent.isEmpty()) {
			for (NodeToNode node : actTransparent) {
				if (node.getType().equals(Constants.NODE_ASSOCIATE_TYPE.TRANSPARENT)) {
					// clone process de cc cho cac ong nay
					List lstData = fdhe.getFullNodeActor(node.getNextId(), obj.getSendGroupId());
					if (!lstData.isEmpty() && lstData != null) {
						for (Object lstData1 : lstData) {
							NodeDeptUser itemCC = (NodeDeptUser) lstData1;
							Process objCC = getProcessForSave(obj, docSelected, node, itemCC, getUserToken(), comment);
							if (obj.getReceiveUserId() != null) {
								objCC.setSendUserId(obj.getReceiveUserId());
								objCC.setSendUser(obj.getReceiveUser());
							} else {
								objCC.setSendUserId(obj.getSendUserId());
								objCC.setSendUser(obj.getSendUser());
							}
							if (objCC.getNotify() != null) {
								Notify notifyCC = objCC.getNotify();
								notifyCC.setSendUserId(obj.getReceiveUserId());
								notifyCC.setSendUserName(obj.getReceiveUser());
							}
							pros.add(objCC);
						}
					}
				}
				if (notTransparent) {
					// Xử lý sau khi chỉnh lại luồng con
					// Gửi đến node ban đầu khi không có luồng tự động chuyển
				}
			}
		}

		return pros;
	}

	private Notify sendNotify(Process current, DocumentPublish doc, UserToken u, String comment) {
		NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
		Notify nf = new Notify();
		Date now = new Date();
		if (current != null) {
			nf.setObjectId(current.getObjectId());
			nf.setObjectType(current.getObjectType());
		} else {

			nf.setObjectId(doc.getDocumentPublishId());
			nf.setObjectType(Constants.OBJECT_TYPE.DOCUMENT_PUBLISH);
		}
		//
		// thong tin notify
		//
		// nf.setParentNotifyId(1L);
		nf.setSendTime(now);
		nf.setContent(comment);
		nf.setStatus(Constants.Status.ACTIVE);
		nf.setSendUserId(u.getUserId());
		nf.setSendUserName(u.getUserFullName());
		notifyDAOHE.saveOrUpdate(nf);
		return nf;
	}

	private boolean validateTransfer() {
		binder.saveAll();
		boolean result = true;
		int countMainProcess = 0, countCooProcess = 0, countRefProcess = 0, countCommentProcess = 0;

		final Set<Listitem> listSelectedItem = lstDeptUser.getSelectedItems();
		for (Listitem selectedItem : listSelectedItem) {
			NodeDeptUser item = (NodeDeptUser) selectedItem.getValue();
			if (item.getProcessType().equals(Constants.PROCESS_TYPE.MAIN)) {
				countMainProcess++;
			} else if (item.getProcessType().equals(Constants.PROCESS_TYPE.COOPERATE)) {
				countCooProcess++;
			} else {
				countCommentProcess++;
			}
		}

		// neu chon tat ca la nhan de biet
		if (listSelectedItem.size() == countRefProcess && countRefProcess != 0) {
			return true;
		}
		if (countMainProcess > 1) {
			Messagebox.show("Bạn chỉ chọn 1 người/đơn vị xử lý chính");
			return false;
		}
		if (countCooProcess > 0) {
			if (countMainProcess == 0) {
				Messagebox.show("Bạn chưa chọn người/đơn vị xử lý chính");
				return false;
			}
		}
		return result;
	}
	// </editor-fold>
*/}

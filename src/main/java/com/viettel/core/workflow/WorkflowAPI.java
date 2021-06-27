/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Sessions;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.sys.DAO.HolidaysManagementDAOHE;
import com.viettel.core.user.BO.Department;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.core.user.model.UserToken;
import com.viettel.core.workflow.BO.Flow;
import com.viettel.core.workflow.BO.Node;
import com.viettel.core.workflow.BO.NodeDeptUser;
import com.viettel.core.workflow.BO.NodeToNode;
import com.viettel.core.workflow.BO.Process;
import com.viettel.core.workflow.DAO.CoreProcessDAOHE;
import com.viettel.core.workflow.DAO.FlowDAOHE;
import com.viettel.core.workflow.DAO.NodeDAOHE;
import com.viettel.core.workflow.DAO.NodeToNodeDAOHE;
import com.viettel.module.phamarcy.BO.PhaFile;
import com.viettel.module.phamarcy.DAO.Organization.PhaFileDAO;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Files;
import com.viettel.voffice.BO.Ycnk.YcnkFile;
import com.viettel.voffice.DAO.FilesDAOHE;
import com.viettel.voffice.DAOHE.YcnkFileDAOHE;

/**
 *
 * @author duv
 */
public class WorkflowAPI extends BaseComposer {

	private static final long serialVersionUID = 1L;

	public static final long OPTION_ALWAYS_SEND = 2L;
	public static final long OPTION_ALWAYS_CHOOSE = 1L;
	public static final long OPTION_NONE = 0L;

	public static final String PROCESSING_GENERAL_PAGE = "/Pages/core/workflow/processingPage.zul";
	/**
	 * Fixed string to select all records of file status in CATEGORY table
	 * condition: CATEGORY_TYPE_CODE equals FILE_STATUS
	 */
	public static final String CAT_FILE_STATUS = "FILE_STATUS";
	/**
	 * Fixed string to select all records of form name in CATEGORY table
	 * condition: CATEGORY_TYPE_CODE equals FORM attention: form is an uri *.zul
	 */
	public static final String CAT_ACTION_FORM = "FORM";
	public static final String CAT_PROCEDURE = "OBJECT";
	private static volatile WorkflowAPI instance;
	private static HashMap<Long, Category> listStatus;
	private static volatile boolean IS_UPDATE_STATUS;

	public static WorkflowAPI getInstance() {
		if (instance != null) {
			return instance;
		}
		instance = new WorkflowAPI();
		return instance;
	}

	public WorkflowAPI() {

	}

	private Process saveProcess(Long nodeType, Long processParentId, Node previousNode, Node nextNode, Long docId,
			Long docType, String note, NodeDeptUser configuredUserNDU) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		List<NodeDeptUser> listUserConfiguredInNode = coreDAOHE.getNodeDeptUserOfNode(nextNode.getNodeId());
		UserToken user = (UserToken) Sessions.getCurrent().getAttribute("userToken");
		Long userId = user.getUserId();
		// Long deptId = user.getDeptId();
		Users userLogin = new UserDAOHE().getUserById(userId);
		Long deptId = userLogin.getDeptId();
		String username = userLogin.getUserName();
		// String deptname = user.getDeptName();
		String deptname = "";
		Department dept = new DepartmentDAOHE().findDeptById(deptId);
		if (dept != null) {
			deptname = dept.getDeptName();
		}
		// Long posId = user.getPosId();
		// Trang thai cua ho so: moi tao (NEW)
		// Trang thai cua ho so sau nay se lay theo truong STATUS trong bang
		// NODE
		Long status = -1l;
		// status = Constants.DOCUMENT_STATUS.NEW;
		// if(previousNode!=null)
		// status = previousNode.getStatus();

		Long order = 0l;
		List<Process> listProcessOfFlow = coreDAOHE.getProcessByObjType(docId, docType);
		if (listProcessOfFlow.size() > 0) {
			Process maxProcess = listProcessOfFlow.get(listProcessOfFlow.size() - 1);
			order = maxProcess.getOrderProcess() + 1;
		}

		if (note == null) {
			note = "--NOTE--";
		}

		// Kieu chuyen action: Khoi tao luong
		// Ten kieu chuyen action: Khoi tao luong
		Long actionType = null;
		String actionTypeName = null;

		Long previousId = null;
		Long nextId = null;
		if (previousNode != null) {
			previousId = previousNode.getNodeId();
		}
		if (nextNode != null) {
			nextId = nextNode.getNodeId();
		}

		if (Constants.NODE_ASSOCIATE_TYPE.START == nodeType) {
			actionType = Constants.NODE_ASSOCIATE_TYPE.START;
			actionTypeName = "Khởi tạo luồng";
			status = Constants.PROCESS_STATUS.INITIAL;
			listUserConfiguredInNode.clear();
			NodeDeptUser ndu = new NodeDeptUser();
			ndu.setUserId(getUserId());
			ndu.setUserName(getUserName());
			ndu.setDeptId(getDeptId());
			ndu.setDeptName(getDeptName());
			ndu.setProcessType(Constants.PROCESS_TYPE.MAIN);
			listUserConfiguredInNode.add(ndu);
		} else {
			NodeToNodeDAOHE node2node = new NodeToNodeDAOHE();
			List<NodeToNode> lstActionNodes = node2node.getNodeToNodes(previousId, nextId);
			if (!lstActionNodes.isEmpty()) {
				NodeToNode actionNode = lstActionNodes.get(0);
				actionType = actionNode.getType();
				actionTypeName = actionNode.getAction();
				// viethd 28/01/2015
				// using status of NodeToNode object
				status = actionNode.getStatus();
			}
		}

		Date deadline = null;
		Date finish = null;
		Long receiverId;
		String receiverName = null;
		Long receiverGroupId = null;
		String receiverGroupName = null;
		UserDAOHE userDAOHE = new UserDAOHE();
		Users receiver;
		Long processType;

		if (nextNode != null && Constants.NODE_TYPE.NODE_TYPE_FINISH.equals(nextNode.getType())) {
			finish = new Date();
		}
		receiverId = configuredUserNDU.getUserId();
		if (receiverId != null) {// case specific receiver configured
			receiverName = configuredUserNDU.getUserName();
			receiver = userDAOHE.getUserById(receiverId);
			receiverGroupId = receiver.getDeptId();
			Department depart = new DepartmentDAOHE().findDeptById(receiver.getDeptId());
			if (depart != null) {
				receiverGroupName = depart.getDeptName();
			}
			// receiverGroupName = receiver.getDeptName();
		} else {// case deparment configured

			if (configuredUserNDU.getPosId() != null) {// case user's position
														// configured
				receiverGroupId = configuredUserNDU.getDeptId();
				receiverGroupName = configuredUserNDU.getDeptName();
			} else {// case only department configured
				// insert into Process ?
			}
		}
		processType = configuredUserNDU.getProcessType();

		Process p = coreDAOHE.insertProcess(docId, docType, userId, username, deptId, deptname, receiverId,
				receiverName, receiverGroupId, receiverGroupName, nextId, processParentId, processType, status,
				deadline, finish, actionType, actionTypeName, order, note, previousId);
		return p;
	}

	// save process record
	// and update status of Document
	public void saveProcessData(Long nextNodeId, Long previousNodeId, Long docId, Long docType,
			NodeDeptUser configuredUserNDU, Long processParentId, String note) {
		NodeDAOHE nodeDAOHE = new NodeDAOHE();
		Node nextNode = nodeDAOHE.getNodeById(nextNodeId);
		Node previousNode = nodeDAOHE.getNodeById(previousNodeId);
		Long ACTION_TYPE = Constants.NODE_ASSOCIATE_TYPE.NORMAL;
		if (previousNode == null) {
			ACTION_TYPE = Constants.NODE_ASSOCIATE_TYPE.START;
		}

		// save process information of document
		this.saveProcess(ACTION_TYPE, processParentId, previousNode, nextNode, docId, docType, note, configuredUserNDU);

		// update status of document YCNK_FILE
		Long status;
		if (previousNode == null) // case initiation of flow
		{
			status = Constants.PROCESS_STATUS.INITIAL;
		} else {
			status = previousNode.getStatus();
		}
		YcnkFile currentDoc = (new YcnkFileDAOHE()).findById(docId);
		currentDoc.setStatusCode(status);
	}

	// save process record
	// and update status of Document
	public void sendDocToOneNDU(Long docId, Long docType, NodeToNode action, String note, Long processParentId,
			NodeDeptUser configuredUserNDU) {
		NodeDAOHE nodeDAOHE = new NodeDAOHE();
		Node nextNode = nodeDAOHE.getNodeById(action.getNextId());
		Node previousNode = nodeDAOHE.getNodeById(action.getPreviousId());
		Long ACTION_TYPE = Constants.NODE_ASSOCIATE_TYPE.NORMAL;
		if (previousNode == null) {
			ACTION_TYPE = Constants.NODE_ASSOCIATE_TYPE.START;
		}

		// save process information of document
		this.saveProcess(ACTION_TYPE, processParentId, previousNode, nextNode, docId, docType, note, configuredUserNDU);

	}

	public void sendDocToListNDUs(Long docId, Long docType, NodeToNode action, String note, Long processParentId,
			List<NodeDeptUser> lstChoosenUser) {
		// send to selected users
		// process parent is processCurrent
		for (NodeDeptUser ndu : lstChoosenUser) {
			this.sendDocToOneNDU(docId, docType, action, note, processParentId, ndu);
			this.updateDocStatus(action, docId);
			System.out.println("---Save process: " + action.getAction() + " to user:" + ndu.getUserId());
		}
	}

	public List<NodeDeptUser> getAssignUser(Long fileId, int type) {
		return null;
	}

	public List<NodeDeptUser> getAssignUser(Long fileId, int type, Long deptId) {
		return null;
	}

	public void disableAssign(Long fileId, int type) {
	}

	public void cleanHoSo(Long fileId) {

		// AssignDAOHE mAssign = new AssignDAOHE();
		// mAssign.deactiveRecord("Assign", fileId);
		// mAssign.deactiveRecord("PhaEvaluation", fileId);
		// mAssign.deactiveRecord("PhaDocumentary", fileId);

		// PhaEvaluationDao mEvaluation = new PhaEvaluationDao();
		// mEvaluation.backupEvaluation(fileId);
	}

	public void saveAssign(Long fileId, List<NodeDeptUser> lstChoosenUser, int type) {

		if (type == Constants.AssignType.CHUYEN_GIA) {
			PhaFile phaFile = new PhaFileDAO().findByFileSystemId(fileId);
			if (phaFile != null && !Constants.Status.ACTIVE.equals(phaFile.getIsAddition())) {
				// luu dead line cho chuyen gia
				phaFile.setExpertDeadLine(new HolidaysManagementDAOHE().getDeadline(phaFile.getPaymentDate(), 6L));
				new PhaFileDAO().saveOrUpdate(phaFile);
			}
		}

		if (type == Constants.AssignType.CHUYEN_VIEN) {
			PhaFile phaFile = new PhaFileDAO().findByFileSystemId(fileId);
			if (phaFile != null && !Constants.Status.ACTIVE.equals(phaFile.getIsAddition())) {
				// luu dead line cho chuyen vien
				phaFile.setDeadLine(
						new HolidaysManagementDAOHE().getDeadline(new Date(), Constants.NUMBER_DAY_PROCESS));
			}
			if (phaFile != null) {
				phaFile.setProcessor(lstChoosenUser.get(0).getUserName());
				new PhaFileDAO().saveOrUpdate(phaFile);
			}
		}

		// AssignDAOHE mDAO = new AssignDAOHE();
		// mDAO.disableAssign(fileId, type);
		// mDAO.saveAssign(fileId, lstChoosenUser, type);
	}

	// Set isActive = 0 doi voi nhung Evaluation cu khong duoc phan cong moi
	public void disableOldEvaluation(Long fileId) {

	}

	public void updateDocStatus(NodeToNode action, Long docId) {
		// update status of document (in table FILES)
		Long status;
		if (action.getPreviousId() == null) // case: initiation of flow
		{
			status = Constants.PROCESS_STATUS.INITIAL;
		} else {
			status = action.getStatus();
		}
		Files currentDoc = (new FilesDAOHE()).findById(docId);
		currentDoc.setStatus(status);
		currentDoc.setModifyDate(new Date());
		new FilesDAOHE().saveOrUpdate(currentDoc);

		if (status.equals(Constants.PROCESS_STATUS.PROPOSED)) {
			PhaFile phaFile = new PhaFileDAO().findByFileSystemId(docId);
			if (phaFile != null && (phaFile.getCode() == null)) {
				String prefix = "TT-";
				if (currentDoc.getTypeAdd() != null) {// hoso xac nhan quang cao
					prefix = "QC-";
				}
				phaFile.setCode(StringUtils.getAutoPhaFileCode(prefix));
				new PhaFileDAO().saveOrUpdate(phaFile);
			}
		}
	}

	public static synchronized String getStatusName(Long status) {
		String statusName;
		if (listStatus == null || IS_UPDATE_STATUS == true) {
			listStatus = new HashMap<Long, Category>();
			// viethd:
			// find all status from category tables
			CategoryDAOHE catDAOHE = new CategoryDAOHE();
			List<Category> listCats = catDAOHE.findAllCategory(CAT_FILE_STATUS);
			for (Category cat : listCats) {
				listStatus.put(Long.parseLong(cat.getValue()), cat);
			}
			// update status set = false;
			IS_UPDATE_STATUS = false;
		}
		Category cat = listStatus.get(status);
		if (status == null || cat == null) {
			return "";
		}
		statusName = cat.getName();
		return statusName;
	}

	public static String getDocumentTypeCode(Long documentTypeCode) {
		String documentTypeCodeName = "";
		if (documentTypeCode == -1L) {
			documentTypeCodeName = "Chưa ch�?n loại hồ sơ";
		} else if (documentTypeCode == Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_TAOMOI) {
			documentTypeCodeName = Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_TAOMOI_STR;
		} else if (documentTypeCode == Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_BOSUNG) {
			documentTypeCodeName = Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_BOSUNG_STR;
		} else if (documentTypeCode == Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_GIAHAN) {
			documentTypeCodeName = Constants.RAPID_TEST.DOCUMENT_TYPE_CODE_GIAHAN_STR;
		}
		return documentTypeCodeName;
	}

	public Long getProcedureTypeIdByCode(String code) {
		Long flowId;
		// viethd:
		// find flow id due to FLOW_CODE from category tables
		CategoryDAOHE catDAOHE = new CategoryDAOHE();
		List<Category> listCats = catDAOHE.findCategoryByCodeAndCatType(code, CAT_PROCEDURE);

		if (listCats.isEmpty()) {
			return null;
		} else {
			flowId = listCats.get(0).getCategoryId();
		}
		return flowId;
	}

	public Long getCatIdByCode(String code) {
		Long id = -1l;
		CategoryDAOHE catDAOHE = new CategoryDAOHE();
		List<Category> listCats = catDAOHE.findCategoryByCode(code);
		if (!listCats.isEmpty()) {
			id = listCats.get(0).getCategoryId();
		}
		return id;
	}

	public Flow getFlowByFileId(Long fileId) {
		FilesDAOHE fileDAOHE = new FilesDAOHE();
		Files f = fileDAOHE.findById(fileId);
		return (new FlowDAOHE()).getFlowById(f.getFlowId());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getFlowByDeptNObject(Long deptId, Long docType) {
		List<Flow> lstFlows = (new FlowDAOHE()).getFlowByDeptNObject(deptId, docType);
		return lstFlows;
	}

	@SuppressWarnings("rawtypes")
	public List getNextActionOfNodeId(Long nodeId) {
		NodeToNodeDAOHE node2NodeDAOHE = new NodeToNodeDAOHE();
		return node2NodeDAOHE.getNextAction(nodeId);
	}

	public Node getNodeById(Long nodeId) {
		NodeDAOHE nodeDAOHE = new NodeDAOHE();
		return nodeDAOHE.getNodeById(nodeId);
	}

	public List<Node> findNodeByFlowIdNStatus(Long flowId, Long status) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findNodeByFlowIdNStatus(flowId, status);
	}

	public List<Node> findNodeByFlowIdNActionStatus(Long flowId, Long status) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findNodeByFlowIdNActionStatus(flowId, status);
	}

	public List<NodeToNode> findActionsByNodeId(Long nodeId) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findActionsByNodeId(nodeId);
	}

	/**
	 *
	 * @param flow
	 * @return
	 */
	public Node getFirstNodeOfFlow(Flow flow) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.getFirstNodeOfFlow(flow);
	}

	/**
	 *
	 * @param docId
	 *            ID of document
	 * @param docType
	 *            Type of document
	 * @param status
	 *            Status of document
	 * @param receiverId
	 *            ID of user receiving this document
	 * @param receiverGroupId
	 *            ID of department receiving this document
	 * @return list of Process objects which are parents of current process
	 */
	public List<Process> findPreviousProcess(Long docId, Long docType, Long status, Long receiverId,
			Long receiverGroupId) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findPreviousProcess(docId, docType, status, receiverId, receiverGroupId);
	}

	public List<Process> findAllCurrentProcess(Long docId, Long docType, Long status) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findALlCurrentProcess(docId, docType, status);
	}

	/**
	 * Find all records NodeDeptUser of a node in flow
	 *
	 * @param nodeId
	 *            having ID=nodeId
	 * @return
	 */
	public List<NodeDeptUser> findNDUsByNodeId(Long nodeId) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findNDUsByNodeId(nodeId);
	}

	public List<NodeDeptUser> findNDUsByAction(NodeToNode action) {
		Long nextId = action.getNextId();
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findNDUsByNodeId(nextId);
	}

	public List<NodeDeptUser> findNDUsByActionId(Long actionId) {
		NodeToNodeDAOHE actionDAOHE = new NodeToNodeDAOHE();
		NodeToNode action = actionDAOHE.findById(actionId);
		if (action == null) {
			return null;
		}
		Long nextId = action.getNextId();
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findNDUsByNodeId(nextId);
	}

	// viethd3 30/01/2015:
	// find all available next-actions of document
	// depending on document status and processing deparment
	// algorithme
	/*
	 * public List<NodeToNode> findAvaiableNextActions(Long docType, Long
	 * docStatus, Long deptId) {
	 * 
	 * List<Flow> lstFlows =
	 * WorkflowAPI.getInstance().getFlowByDeptNObject(deptId, docType); Flow f =
	 * lstFlows.get(0); List<Node> lstNode; List<Node> lstNextNodes = new
	 * ArrayList<>(); Node nextNode; if(docStatus ==
	 * Constants.PROCESS_STATUS.INITIAL) nextNode = WorkflowAPI.getInstance()
	 * .getFirstNodeOfFlow(f); else{ lstNode= WorkflowAPI.getInstance()
	 * .findNodeByFlowIdNStatus(f.getFlowId(), docStatus); for(Node node:
	 * lstNode){ List<NodeToNode> actions = WorkflowAPI.getInstance()
	 * .findActionsByNodeId(node.getNodeId()); for(NodeToNode action : actions){
	 * Node temp = WorkflowAPI.getInstance() .getNodeById(action.getNextId());
	 * lstNextNodes.add(temp); } } nextNode = null; } List<NodeToNode> actions =
	 * new ArrayList<>(); if(nextNode != null) actions =
	 * WorkflowAPI.getInstance().getNextActionOfNodeId(nextNode.getNodeId());
	 * else{ for(Node node:lstNextNodes){
	 * actions.addAll(WorkflowAPI.getInstance().getNextActionOfNodeId(node.
	 * getNodeId())); } } return actions; }
	 */
	@SuppressWarnings({ "unchecked" })
	public List<NodeToNode> findAvaiableNextActions(Long docType, Long docStatus, Long deptId) {
		WorkflowAPI workflowInstance = WorkflowAPI.getInstance();
		List<Flow> lstFlows = workflowInstance.getFlowByDeptNObject(deptId, docType);
		Flow f = lstFlows.get(0);
		List<Node> lstNode;
		Node nextNode;
		List<NodeToNode> actions = new ArrayList<>();
		if (docStatus == Constants.PROCESS_STATUS.INITIAL) {
			nextNode = WorkflowAPI.getInstance().getFirstNodeOfFlow(f);
		} else {
			// lstNode= workflowInstance.findNodeByFlowIdNStatus(f.getFlowId(),
			// docStatus);
			lstNode = workflowInstance.findNodeByFlowIdNActionStatus(f.getFlowId(), docStatus);
			for (Node node : lstNode) {
				actions.addAll(workflowInstance.findActionsByNodeId(node.getNodeId()));
				/*
				 * actions =
				 * workflowInstance.findActionsByNodeId(node.getNodeId());
				 * for(NodeToNode action : actions){ Node temp =
				 * workflowInstance.getNodeById(action.getNextId());
				 * lstNextNodes.add(temp); }
				 */
			}
			nextNode = null;
		}

		if (nextNode != null) {
			actions = workflowInstance.getNextActionOfNodeId(nextNode.getNodeId());
		} else {
			/*
			 * for(Node node:lstNextNodes){
			 * actions.addAll(workflowInstance.getNextActionOfNodeId(node.
			 * getNodeId())); }
			 */
		}
		return actions;
	}

	public Process getCurrentProcess(Long docId, Long docType, Long status, Long receiverId) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		Process p = coreDAOHE.findProcessByStatusAndReceiverId(docId, docType, status, receiverId);
		if (p != null) {
			return p;
		}
		return null;
	}

	public List<Process> getAllProcess(Long docId, Long docType) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		List<Process> p = coreDAOHE.findAllProcess(docId, docType);
		if (p != null) {
			return p;
		}
		return null;
	}

	public static String getCtypeFile(String fileType) {
		String sTemp = "";
		if (("bmp").equals(fileType)) {
			sTemp = "image/bmp";
		} else if (("css").equals(fileType)) {
			sTemp = "text/css";
		} else if (("dtd").equals(fileType)) {
			sTemp = "application/xml-dtd";
		} else if (("doc").equals(fileType)) {
			sTemp = "application/msword";
		} else if (("docx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		} else if (("dotx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
		} else if (("es").equals(fileType)) {
			sTemp = "application/ecmascript";
		} else if (("exe").equals(fileType)) {
			sTemp = "application/octet-stream";
		} else if (("gif").equals(fileType)) {
			sTemp = "image/gif";
		} else if (("gz").equals(fileType)) {
			sTemp = "application/x-gzip";
		} else if (("hqx").equals(fileType)) {
			sTemp = "application/mac-binhex40";
		} else if (("html").equals(fileType)) {
			sTemp = "text/html";
		} else if (("jpg").equals(fileType)) {
			sTemp = "image/jpeg";
		} else if (("js").equals(fileType)) {
			sTemp = "application/x-javascript";
		} else if (("mpeg").equals(fileType)) {
			sTemp = "video/mpeg";
		} else if (("ogg").equals(fileType)) {
			sTemp = "audio/vorbis, application/ogg";
		} else if (("pdf").equals(fileType)) {
			sTemp = "application/pdf";
		} else if (("png").equals(fileType)) {
			sTemp = "text/css";
		} else if (("potx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.presentationml.template";
		} else if (("ppsx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
		} else if (("ppt").equals(fileType)) {
			sTemp = "application/vnd.ms-powerpointtd";
		} else if (("pptx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		} else if (("svg").equals(fileType)) {
			sTemp = "image/svg+xml";
		} else if (("swf").equals(fileType)) {
			sTemp = "application/x-shockwave-flash";
		} else if (("tiff").equals(fileType)) {
			sTemp = "image/tiff";
		} else if (("txt").equals(fileType)) {
			sTemp = "text/plain";
		} else if (("xls").equals(fileType)) {
			sTemp = "application/vnd.ms-excel";
		} else if (("xlsb").equals(fileType)) {
			sTemp = "application/vnd.ms-excel.sheet.binary.macroEnabled.12";
		} else if (("xlsx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if (("xltx").equals(fileType)) {
			sTemp = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
		} else if (("xml").equals(fileType)) {
			sTemp = "application/xml";
		}
		return sTemp;
	}

	public Process getCurrentProcessDesc(Long docId, Long docType, Long status, Long receiverId) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		Process p = coreDAOHE.findProcessByStatusAndReceiverIdDesc(docId, docType, status, receiverId);
		if (p != null) {
			return p;
		}
		return null;
	}

	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<NodeDeptUser> findNDUsByNodeIdAndSendUserId(Long nodeId, Long sendUserId) {
		CoreProcessDAOHE coreDAOHE = new CoreProcessDAOHE();
		return coreDAOHE.findNDUsByNodeIdAndSendUserId(nodeId, sendUserId);
	}

	public static boolean isIS_UPDATE_STATUS() {
		return IS_UPDATE_STATUS;
	}

	public static void setIS_UPDATE_STATUS(boolean iS_UPDATE_STATUS) {
		IS_UPDATE_STATUS = iS_UPDATE_STATUS;
	}

}

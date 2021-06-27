package com.viettel.voffice.BEAN;

public class RetrieveBean {
	private Long userId;
	private String userName;
	private Long deptId;
	private String deptName;
	private Long posId;
	private String posName;
	private Long processId;
	private Long processType;

	public RetrieveBean(Long userId, String userName, Long deptId,
			String deptName, Long posId, String posName, Long processId,
			Long processType) {
		this.userId = userId;
		this.userName = userName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.posId = posId;
		this.posName = posName;
		this.processId = processId;
		this.processType = processType;
	}

	public RetrieveBean(Long deptId, String deptName, Long processId,
			Long processType) {
		this.deptId = deptId;
		this.deptName = deptName;
		this.processId = processId;
		this.processType = processType;
	}

	public RetrieveBean() {

	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public Long getProcessType() {
		return processType;
	}

	public void setProcessType(Long processType) {
		this.processType = processType;
	}
}

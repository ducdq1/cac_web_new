/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow.BO;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "NODE_DEPT_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NodeDeptUser.findAll", query = "SELECT n FROM NodeDeptUser n"),
    @NamedQuery(name = "NodeDeptUser.findByNodeDeptUserId", query = "SELECT n FROM NodeDeptUser n WHERE n.nodeDeptUserId = :nodeDeptUserId"),
    @NamedQuery(name = "NodeDeptUser.findByNodeName", query = "SELECT n FROM NodeDeptUser n WHERE n.nodeName = :nodeName"),
    @NamedQuery(name = "NodeDeptUser.findByDeptId", query = "SELECT n FROM NodeDeptUser n WHERE n.deptId = :deptId"),
    @NamedQuery(name = "NodeDeptUser.findByDeptName", query = "SELECT n FROM NodeDeptUser n WHERE n.deptName = :deptName"),
    @NamedQuery(name = "NodeDeptUser.findByUserName", query = "SELECT n FROM NodeDeptUser n WHERE n.userName = :userName"),
    @NamedQuery(name = "NodeDeptUser.findByProcessType", query = "SELECT n FROM NodeDeptUser n WHERE n.processType = :processType")})
public class NodeDeptUser implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "NODE_DEPT_USER_SEQ", sequenceName = "NODE_DEPT_USER_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NODE_DEPT_USER_SEQ")
    @Column(name = "NODE_DEPT_USER_ID")
    private Long nodeDeptUserId;
    @Column(name = "NODE_ID")
    private Long nodeId;
    @Size(min = 1, max = 200)
    @Column(name = "NODE_NAME")
    private String nodeName;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Size(min = 1, max = 200)
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "USER_ID")
    private Long userId;
    @Size(min = 1, max = 200)
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "POS_ID")
    private Long posId;
    @Size(min = 1, max = 100)
    @Column(name = "POS_NAME")
    private String posName;
    @Column(name = "PROCESS_TYPE")
    private Long processType;
    @Column(name = "DEPT_LEVEL")
    private Long deptLevel;
    @Column(name = "ALLIAS")
    private String allias;
    @Column(name = "USE_ALLIAS")
    private Long useAllias;
    @Column(name = "OPTION_SELECTED")
    private Long optionSelected;
    @Transient
    private String fullName;

    public NodeDeptUser() {
    }

    public NodeDeptUser(Long nodeDeptUserId) {
        this.nodeDeptUserId = nodeDeptUserId;
    }

    public NodeDeptUser(Long nodeDeptUserId, String nodeName, Long deptId,
            String deptName, String userName, Long processType) {
        this.nodeDeptUserId = nodeDeptUserId;
        this.nodeName = nodeName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.userName = userName;
        this.processType = processType;
    }

    public Long getNodeDeptUserId() {
        return nodeDeptUserId;
    }

    public void setNodeDeptUserId(Long nodeDeptUserId) {
        this.nodeDeptUserId = nodeDeptUserId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Long getProcessType() {
        return processType;
    }

    public void setProcessType(Long processType) {
        this.processType = processType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(Long deptLevel) {
        this.deptLevel = deptLevel;
    }

    @Override
    public NodeDeptUser clone() throws CloneNotSupportedException {
        NodeDeptUser ndu = (NodeDeptUser) super.clone();
        ndu.setNodeDeptUserId(null);
        return ndu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nodeDeptUserId != null ? nodeDeptUserId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NodeDeptUser)) {
            return false;
        }
        NodeDeptUser other = (NodeDeptUser) object;
        if ((this.nodeDeptUserId == null && other.nodeDeptUserId != null)
                || (this.nodeDeptUserId != null && !this.nodeDeptUserId
                .equals(other.nodeDeptUserId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Flow.NodeDeptUser[ nodeDeptUserId="
                + nodeDeptUserId + " ]";
    }

    /**
     * @return the allias
     */
    public String getAllias() {
        return allias;
    }

    /**
     * @param allias the allias to set
     */
    public void setAllias(String allias) {
        this.allias = allias;
    }

    /**
     * @return the useAllias
     */
    public Long getUseAllias() {
        return useAllias;
    }

    /**
     * @param useAllias the useAllias to set
     */
    public void setUseAllias(Long useAllias) {
        this.useAllias = useAllias;
    }

    /**
     * @return the optionSelected
     */
    public Long getOptionSelected() {
        return optionSelected;
    }

    /**
     * @param optionSelected the optionSelected to set
     */
    public void setOptionSelected(Long optionSelected) {
        this.optionSelected = optionSelected;
    }
}

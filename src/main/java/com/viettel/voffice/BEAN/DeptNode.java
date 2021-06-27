package com.viettel.voffice.BEAN;

import com.viettel.utils.StringUtils;
import com.viettel.core.user.BO.Department;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @see http://zkfiddle.org/sample/3dm77ch/1-MVVM-tree#source-4
 */
public class DeptNode {

    private String address;
    private String deptCode;
    private Long deptId;
    private String deptName;
    private String description;
    private String email;
    private String fax;
    private String status;
    private String telephone;
    private Long deptTypeId;
    private String userId;
    private DeptNode _parent;
    private List<DeptNode> _children;
    private int _index;
    private String _label = "";

    public DeptNode(String deptName, String deptCode) {
        this.deptCode = deptCode;
        this.deptName = deptName;
        _label = deptName;
    }

    public DeptNode(DeptNode parent, int index, String label) {
        _parent = parent;
        _index = index;
        _label = label;
    }

    public DeptNode(DeptNode parent, Department entity) {
        _parent = parent;
        _label = entity.getDeptName();
        this.deptId = entity.getDeptId();
        this.deptName = entity.getDeptName();
        deptName = StringUtils.escapeHtml(deptName);
        if (deptName.length() > 300) {
            deptName = deptName.substring(0, 299);
        }
        this.status = entity.getStatus().toString();
        this.address = entity.getAddress();
        this.deptCode = entity.getDeptCode();
    }

    public void setParent(DeptNode parent) {
        _parent = parent;
    }

    public DeptNode getParent() {
        return _parent;
    }

    public void appendChild(DeptNode child) {
        if (_children == null) {
            _children = new ArrayList<DeptNode>();
        }
        _children.add(child);
    }

    public List<DeptNode> getChildren() {
        if (_children == null) {
            return Collections.emptyList();
        }
        return _children;
    }

    public void setIndex(int index) {
        _index = index;
    }

    public int getIndex() {
        return _index;
    }

    public String getLabel() {
        return _label;
    }

    public String toString() {
        return getLabel();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getDeptTypeId() {
        return deptTypeId;
    }

    public void setDeptTypeId(Long deptTypeId) {
        this.deptTypeId = deptTypeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
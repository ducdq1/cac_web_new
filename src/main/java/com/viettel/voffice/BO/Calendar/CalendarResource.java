/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Calendar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "CALENDAR_RESOURCE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarResource.findAll", query = "SELECT c FROM CalendarResource c"),
    @NamedQuery(name = "CalendarResource.findByCalendarResourceId", query = "SELECT c FROM CalendarResource c WHERE c.calendarResourceId = :calendarResourceId"),
    @NamedQuery(name = "CalendarResource.findByResourceName", query = "SELECT c FROM CalendarResource c WHERE c.resourceName = :resourceName"),
    @NamedQuery(name = "CalendarResource.findByResourceType", query = "SELECT c FROM CalendarResource c WHERE c.resourceType = :resourceType"),
    @NamedQuery(name = "CalendarResource.findByAcceptStatus", query = "SELECT c FROM CalendarResource c WHERE c.acceptStatus = :acceptStatus"),
    @NamedQuery(name = "CalendarResource.findByAcceptDate", query = "SELECT c FROM CalendarResource c WHERE c.acceptDate = :acceptDate"),
    @NamedQuery(name = "CalendarResource.findByAcceptUserName", query = "SELECT c FROM CalendarResource c WHERE c.acceptUserName = :acceptUserName"),
    @NamedQuery(name = "CalendarResource.findByAcceptNote", query = "SELECT c FROM CalendarResource c WHERE c.acceptNote = :acceptNote")})
public class CalendarResource implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "CALENDAR_RESOURCE_SEQ", sequenceName = "CALENDAR_RESOURCE_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CALENDAR_RESOURCE_SEQ")
    @Column(name = "CALENDAR_RESOURCE_ID")
    private Long calendarResourceId;
    @Size(max = 500)
    @Column(name = "RESOURCE_NAME")
    private String resourceName;
    @Column(name = "RESOURCE_TYPE")
    private Long resourceType;
    @Column(name = "ACCEPT_STATUS")
    private Long acceptStatus;
    @Column(name = "ACCEPT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptDate;
    @Column(name = "ACCEPT_USER")
    private Long acceptUser;
    @Size(max = 500)
    @Column(name = "ACCEPT_USER_NAME")
    private String acceptUserName;
    @Size(max = 500)
    @Column(name = "ACCEPT_NOTE")
    private String acceptNote;
    @Column(name = "RESOURCE_ID")
    private Long resourceId;
    @Column(name = "CALENDAR_ID")
    private Long calendarId;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Transient
    private Long isDuplicated;

    public CalendarResource() {
    }

    public CalendarResource(Long calendarResourceId) {
        this.calendarResourceId = calendarResourceId;
    }

    public Long getCalendarResourceId() {
        return calendarResourceId;
    }

    public void setCalendarResourceId(Long calendarResourceId) {
        this.calendarResourceId = calendarResourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getResourceType() {
        return resourceType;
    }

    public void setResourceType(Long resourceType) {
        this.resourceType = resourceType;
    }

    public Long getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(Long acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Long getAcceptUser() {
        return acceptUser;
    }

    public void setAcceptUser(Long acceptUser) {
        this.acceptUser = acceptUser;
    }

    public String getAcceptUserName() {
        return acceptUserName;
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public String getAcceptNote() {
        return acceptNote;
    }

    public void setAcceptNote(String acceptNote) {
        this.acceptNote = acceptNote;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
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

    public Long getIsDuplicated() {
        return isDuplicated;
    }

    public void setIsDuplicated(Long isDuplicated) {
        this.isDuplicated = isDuplicated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarResourceId != null ? calendarResourceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CalendarResource)) {
            return false;
        }
        CalendarResource other = (CalendarResource) object;
        if ((this.calendarResourceId == null && other.calendarResourceId != null) || (this.calendarResourceId != null && !this.calendarResourceId.equals(other.calendarResourceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Calendar.CalendarResource[ calendarResourceId=" + calendarResourceId + " ]";
    }
    
}

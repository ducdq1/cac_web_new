/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Calendar;

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
@Table(name = "CALENDAR_PARTICIPANTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarParticipants.findAll", query = "SELECT c FROM CalendarParticipants c"),
    @NamedQuery(name = "CalendarParticipants.findByParticipantId", query = "SELECT c FROM CalendarParticipants c WHERE c.participantId = :participantId"),
    @NamedQuery(name = "CalendarParticipants.findByDeptName", query = "SELECT c FROM CalendarParticipants c WHERE c.deptName = :deptName"),
    @NamedQuery(name = "CalendarParticipants.findByUserName", query = "SELECT c FROM CalendarParticipants c WHERE c.userName = :userName"),
    @NamedQuery(name = "CalendarParticipants.findByParticipantRole", query = "SELECT c FROM CalendarParticipants c WHERE c.participantRole = :participantRole"),
    @NamedQuery(name = "CalendarParticipants.findByAcceptStatus", query = "SELECT c FROM CalendarParticipants c WHERE c.acceptStatus = :acceptStatus"),
    @NamedQuery(name = "CalendarParticipants.findByAcceptNote", query = "SELECT c FROM CalendarParticipants c WHERE c.acceptNote = :acceptNote")})
public class CalendarParticipants implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "PARTICIPANT_SEQ", sequenceName = "PARTICIPANT_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTICIPANT_SEQ")
    @Column(name = "PARTICIPANT_ID")
    private Long participantId;
    @Size(max = 500)
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Size(max = 500)
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "PARTICIPANT_ROLE")
    private Long participantRole;
    @Column(name = "ACCEPT_STATUS")
    private Long acceptStatus;
    @Size(max = 500)
    @Column(name = "ACCEPT_NOTE")
    private String acceptNote;
    @Column(name = "CALENDAR_ID")
    private Long calendarId;
    @Transient
    private Long isDuplicated;

    public CalendarParticipants() {
    }

    public CalendarParticipants(Long participantId) {
        this.participantId = participantId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getParticipantRole() {
        return participantRole;
    }

    public void setParticipantRole(Long participantRole) {
        this.participantRole = participantRole;
    }

    public Long getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(Long acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public String getAcceptNote() {
        return acceptNote;
    }

    public void setAcceptNote(String acceptNote) {
        this.acceptNote = acceptNote;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
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
        hash += (participantId != null ? participantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CalendarParticipants)) {
            return false;
        }
        CalendarParticipants other = (CalendarParticipants) object;
        if ((this.participantId == null && other.participantId != null) || (this.participantId != null && !this.participantId.equals(other.participantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Calendar.CalendarParticipants[ participantId=" + participantId + " ]";
    }
    
}

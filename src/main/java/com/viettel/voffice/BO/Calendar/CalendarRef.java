/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Calendar;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "CALENDAR_REF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarRef.findAll", query = "SELECT c FROM CalendarRef c"),
    @NamedQuery(name = "CalendarRef.findByCalendarRefId", query = "SELECT c FROM CalendarRef c WHERE c.calendarRefId = :calendarRefId"),
    @NamedQuery(name = "CalendarRef.findByRefTitle", query = "SELECT c FROM CalendarRef c WHERE c.refTitle = :refTitle"),
    @NamedQuery(name = "CalendarRef.findByRefNote", query = "SELECT c FROM CalendarRef c WHERE c.refNote = :refNote"),
    @NamedQuery(name = "CalendarRef.findByRefLink", query = "SELECT c FROM CalendarRef c WHERE c.refLink = :refLink")})
public class CalendarRef implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "CALENDAR_REF_SEQ", sequenceName = "CALENDAR_REF_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CALENDAR_REF_SEQ")
    @Column(name = "CALENDAR_REF_ID")
    private Long calendarRefId;
    @Size(max = 500)
    @Column(name = "REF_TITLE")
    private String refTitle;
    @Size(max = 1000)
    @Column(name = "REF_NOTE")
    private String refNote;
    @Size(max = 1000)
    @Column(name = "REF_LINK")
    private String refLink;
    @Column(name = "CALENDAR_ID")
    private Long calendarId;
    @Column(name = "ATTACH_ID")
    private Long attachId;

    public CalendarRef() {
    }

    public CalendarRef(Long calendarRefId) {
        this.calendarRefId = calendarRefId;
    }

    public Long getCalendarRefId() {
        return calendarRefId;
    }

    public void setCalendarRefId(Long calendarRefId) {
        this.calendarRefId = calendarRefId;
    }

    public String getRefTitle() {
        return refTitle;
    }

    public void setRefTitle(String refTitle) {
        this.refTitle = refTitle;
    }

    public String getRefNote() {
        return refNote;
    }

    public void setRefNote(String refNote) {
        this.refNote = refNote;
    }

    public String getRefLink() {
        return refLink;
    }

    public void setRefLink(String refLink) {
        this.refLink = refLink;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarRefId != null ? calendarRefId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CalendarRef)) {
            return false;
        }
        CalendarRef other = (CalendarRef) object;
        if ((this.calendarRefId == null && other.calendarRefId != null) || (this.calendarRefId != null && !this.calendarRefId.equals(other.calendarRefId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Calendar.CalendarRef[ calendarRefId=" + calendarRefId + " ]";
    }
    
}

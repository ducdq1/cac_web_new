/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO.Calendar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HaVM2
 */
@Entity
@Table(name = "CALENDAR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c"),
    @NamedQuery(name = "Calendar.findByCalendarId", query = "SELECT c FROM Calendar c WHERE c.calendarId = :calendarId"),
    @NamedQuery(name = "Calendar.findByTitle", query = "SELECT c FROM Calendar c WHERE c.title = :title"),
    @NamedQuery(name = "Calendar.findByDescription", query = "SELECT c FROM Calendar c WHERE c.description = :description"),
    @NamedQuery(name = "Calendar.findByLocationId", query = "SELECT c FROM Calendar c WHERE c.locationId = :locationId"),
    @NamedQuery(name = "Calendar.findByLocationName", query = "SELECT c FROM Calendar c WHERE c.locationName = :locationName"),
    @NamedQuery(name = "Calendar.findByTimeStart", query = "SELECT c FROM Calendar c WHERE c.timeStart = :timeStart"),
    @NamedQuery(name = "Calendar.findByTimeEnd", query = "SELECT c FROM Calendar c WHERE c.timeEnd = :timeEnd"),
    @NamedQuery(name = "Calendar.findByRecurrenceType", query = "SELECT c FROM Calendar c WHERE c.recurrenceType = :recurrenceType"),
    @NamedQuery(name = "Calendar.findByRecurrenceStartDate", query = "SELECT c FROM Calendar c WHERE c.recurrenceStartDate = :recurrenceStartDate"),
    @NamedQuery(name = "Calendar.findByRecurrenceEndDate", query = "SELECT c FROM Calendar c WHERE c.recurrenceEndDate = :recurrenceEndDate"),
    @NamedQuery(name = "Calendar.findByRecurrenceDay", query = "SELECT c FROM Calendar c WHERE c.recurrenceDay = :recurrenceDay"),
    @NamedQuery(name = "Calendar.findByStatus", query = "SELECT c FROM Calendar c WHERE c.status = :status")})
public class Calendar implements Serializable {
    private static final long serialVersionUID = 1L;
    @SequenceGenerator(name = "CALENDAR_SEQ", sequenceName = "CALENDAR_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CALENDAR_SEQ")
    @Column(name = "CALENDAR_ID")
    private Long calendarId;
    @Size(max = 250)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "LOCATION_ID")
    private Long locationId;
    @Size(max = 250)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Column(name = "TIME_START")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStart;
    @Column(name = "TIME_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnd;
    @Column(name = "RECURRENCE_TYPE")
    private Long recurrenceType;
    @Column(name = "RECURRENCE_START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recurrenceStartDate;
    @Column(name = "RECURRENCE_END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recurrenceEndDate;
    @Column(name = "RECURRENCE_DAY")
    private Long recurrenceDay;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_USER_ID")
    private Long createUserId;
    @Column(name = "MAN_CHIEF")
    private String manChief;
    @Column(name = "MAN_PARTICIPANT")
    private String manParticipant;
    @Column(name = "MAN_PREPARE")
    private String manPrepare;
    @Transient
    private String searchText;

    public Calendar() {
    }

    public Calendar(Long calendarId) {
        this.calendarId = calendarId;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Long getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(Long recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public Date getRecurrenceStartDate() {
        return recurrenceStartDate;
    }

    public void setRecurrenceStartDate(Date recurrenceStartDate) {
        this.recurrenceStartDate = recurrenceStartDate;
    }

    public Date getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(Date recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public Long getRecurrenceDay() {
        return recurrenceDay;
    }

    public void setRecurrenceDay(Long recurrenceDay) {
        this.recurrenceDay = recurrenceDay;
    }

    public Long getStatus() {
        return status;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getManChief() {
        return manChief;
    }

    public void setManChief(String manChief) {
        this.manChief = manChief;
    }

    public String getManParticipant() {
        return manParticipant;
    }

    public void setManParticipant(String manParticipant) {
        this.manParticipant = manParticipant;
    }

    public String getManPrepare() {
        return manPrepare;
    }

    public void setManPrepare(String manPrepare) {
        this.manPrepare = manPrepare;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarId != null ? calendarId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar other = (Calendar) object;
        if ((this.calendarId == null && other.calendarId != null) || (this.calendarId != null && !this.calendarId.equals(other.calendarId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.Calendar.Calendar[ calendarId=" + calendarId + " ]";
    }
    
}

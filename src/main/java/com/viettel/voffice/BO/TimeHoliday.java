/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.BO;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Linhdx
 */
@Entity
@Table(name = "TIME_HOLIDAY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TimeHoliday.findAll", query = "SELECT t FROM TimeHoliday t"),
    @NamedQuery(name = "TimeHoliday.findByTimeHolidayId", query = "SELECT t FROM TimeHoliday t WHERE t.timeHolidayId = :timeHolidayId"),
    @NamedQuery(name = "TimeHoliday.findByName", query = "SELECT t FROM TimeHoliday t WHERE t.name = :name"),
    @NamedQuery(name = "TimeHoliday.findByTimeDate", query = "SELECT t FROM TimeHoliday t WHERE t.timeDate = :timeDate"),
    @NamedQuery(name = "TimeHoliday.findByIsActive", query = "SELECT t FROM TimeHoliday t WHERE t.isActive = :isActive"),
    @NamedQuery(name = "TimeHoliday.findByDescription", query = "SELECT t FROM TimeHoliday t WHERE t.description = :description"),
    @NamedQuery(name = "TimeHoliday.findByDateType", query = "SELECT t FROM TimeHoliday t WHERE t.dateType = :dateType")})
public class TimeHoliday implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @SequenceGenerator(name = "TIME_HOLIDAY_SEQ", sequenceName = "TIME_HOLIDAY_SEQ")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIME_HOLIDAY_SEQ")
    @Column(name = "TIME_HOLIDAY_ID")
    private Long timeHolidayId;
    @Size(max = 500)
    @Column(name = "NAME")
    private String name;
    @Column(name = "TIME_DATE")
    @Temporal(TemporalType.DATE)
    private Date timeDate;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "DATE_TYPE")
    private Long dateType;
    private transient Long arrange;

    public TimeHoliday() {
    }

    public TimeHoliday(Long timeHolidayId) {
        this.timeHolidayId = timeHolidayId;
    }

    public Long getTimeHolidayId() {
        return timeHolidayId;
    }

    public void setTimeHolidayId(Long timeHolidayId) {
        this.timeHolidayId = timeHolidayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDateType() {
        return dateType;
    }

    public void setDateType(Long dateType) {
        this.dateType = dateType;
    }

    public Long getArrange() {
        return arrange;
    }

    public void setArrange(Long arrange) {
        this.arrange = arrange;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeHolidayId != null ? timeHolidayId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeHoliday)) {
            return false;
        }
        TimeHoliday other = (TimeHoliday) object;
        if ((this.timeHolidayId == null && other.timeHolidayId != null) || (this.timeHolidayId != null && !this.timeHolidayId.equals(other.timeHolidayId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.viettel.voffice.BO.TimeHoliday[ timeHolidayId=" + timeHolidayId + " ]";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model.Calendar;

import com.viettel.voffice.BO.Calendar.Calendar;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HaVM2
 */
public class DateCalendar implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1312829207388699160L;
	private Date date;
    private String text;
    private List<Calendar> lstCalendar;

    public DateCalendar() {
    }
    
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Calendar> getLstCalendar() {
        return lstCalendar;
    }

    public void setLstCalendar(List<Calendar> lstCalendar) {
        this.lstCalendar = lstCalendar;
    }
}

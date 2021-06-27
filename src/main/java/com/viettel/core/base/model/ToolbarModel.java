/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.core.base.model;

import java.util.Date;

/**
 *
 * @author HaVM2
 */
public class ToolbarModel {
    String searchText;
    Long fromDate;
    Long toDate;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Date getFromDate() {
        return new Date(fromDate);
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return new Date(toDate);
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }
}

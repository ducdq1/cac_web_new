/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.model;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author HaVM2
 */
public class PagingListModel {
    int count;
    List lstReturn;

	private BigDecimal tongTien;
	private BigDecimal soDuDauKy;
	
	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}
	
    public PagingListModel(List lstItems, Long count){
        this.count = count.intValue();
        lstReturn = lstItems;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List getLstReturn() {
        return lstReturn;
    }

    public void setLstReturn(List lstReturn) {
        this.lstReturn = lstReturn;
    }

	public BigDecimal getSoDuDauKy() {
		return soDuDauKy;
	}

	public void setSoDuDauKy(BigDecimal soDuDauKy) {
		this.soDuDauKy = soDuDauKy;
	}
    
    
}

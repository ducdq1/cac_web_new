/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import com.viettel.voffice.BO.Ycnk.YcnkProduct;

/**
 *
 * @author Linhdx
 */
public class YcnkProductModel {

    private Long key;
    private YcnkProduct ycnkProduct;

    public YcnkProductModel() {

    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public YcnkProduct getYcnkProduct() {
        return ycnkProduct;
    }

    public void setYcnkProduct(YcnkProduct ycnkProduct) {
        this.ycnkProduct = ycnkProduct;
    }

}

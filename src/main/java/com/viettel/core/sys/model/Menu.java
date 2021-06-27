/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.model;

import com.viettel.core.sys.BO.Objects;
import java.util.List;

/**
 *
 * @author HaVM2
 */
public class Menu {

    private Long menuId;
    private String menuName;
    private String menuUrl;
    private Long parentId;
    private String img;
    private List lstMenu;

    public Menu() {
    }

    public Menu(Objects o) {
        menuId = o.getObjectId();
        menuName = o.getObjectName();
        menuUrl = o.getObjectUrl();
        parentId = o.getParentId();
        //img = o.get;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List getLstMenu() {
        return lstMenu;
    }

    public void setLstMenu(List lstMenu) {
        this.lstMenu = lstMenu;
    }
}

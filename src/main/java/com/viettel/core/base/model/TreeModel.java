/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.model;

import org.zkoss.zul.AbstractTreeModel;

/**
 *
 * @author ChucHV Xay dung tree gom cac phong ban va user cua cac phong ban do
 */
public class TreeModel extends AbstractTreeModel<Object> {

    private static final long serialVersionUID = 7822729366554623684L;

    //Root can be null || Department
    public TreeModel(TreeNode root) {
        super(root);
    }

    @Override
    public boolean isLeaf(Object e) {
        TreeNode node = (TreeNode) e;
        return node.isLeaf();
    }

    //Get child at i index
    @Override
    public TreeNode getChild(Object e, int i) {
        TreeNode node = (TreeNode) e;
        return node.getChild(i);
    }

    //Get number of children (all dept childrent and user)
    @Override
    public int getChildCount(Object e) {
        TreeNode node = (TreeNode) e;
        return node.getChildCount();
    }

}

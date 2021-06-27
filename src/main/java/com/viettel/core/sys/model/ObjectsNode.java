/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.model;

import com.viettel.core.base.model.TreeNode;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.DAO.ObjectsDAOHE;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ChucHV
 */
public class ObjectsNode extends TreeNode {

    public ObjectsNode(Long id, String name) {
        super(id, name);
    }

    @Override
    public boolean isLeaf() {
        if (listChild != null) {
            return listChild.isEmpty();
        } else {
            listChild = new ArrayList<>();
            ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
            List<Objects> result = objectsDAOHE.searchChildren(getId());
            if (result == null) {
                return true;
            } else {
                for (Objects object : result) {
                    listChild.add(new ObjectsNode(object.getObjectId(), object.getObjectName()));
                }
                return result.isEmpty();
            }
        }
    }

    @Override
    public TreeNode getChild(int i) {
        return listChild.get(i);
    }

    @Override
    public int getChildCount() {
        if (listChild != null) {
            return listChild.size();
        }

        listChild = new ArrayList<>();
        ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
        List<Objects> result = objectsDAOHE.searchChildren(getId());
        if (result != null) {
            for (Objects object : result) {
                listChild.add(new ObjectsNode(object.getObjectId(), object.getObjectName()));
            }

        }
        return listChild.size();
    }
}

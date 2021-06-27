/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.model;

import com.viettel.core.sys.BO.Applications;
import com.viettel.core.sys.BO.Objects;
import com.viettel.core.sys.DAO.ApplicationDAOHE;
import com.viettel.core.sys.DAO.ObjectsDAOHE;
import com.viettel.core.base.model.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ChucHV
 */
public class AppNode extends TreeNode {

    public AppNode(Long id, String name) {
        super(id, name);
    }

    @Override
    public boolean isLeaf() {
        if (listChild != null) {
            return listChild.isEmpty();
        } else {
            listChild = new ArrayList<>();
            if (getId() > 0) {
                ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
                List<Objects> result = objectsDAOHE.getObjectsByAppId(getId());
                if (result == null) {
                    return true;
                } else {
                    for (Objects objects : result) {
                        listChild.add(new ObjectsNode(objects.getObjectId(), objects.getObjectName()));
                    }
                    return result.isEmpty();
                }
            } else {
                ApplicationDAOHE appDAOHE = new ApplicationDAOHE();
                List<Applications> result = appDAOHE.getAll();
                if (result == null) {
                    return true;
                } else {
                    for (Applications app : result) {
                        listChild.add(new AppNode(app.getAppId(), app.getAppName()));
                    }
                    return result.isEmpty();
                }
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
        if (getId() > 0) {
            ObjectsDAOHE objectsDAOHE = new ObjectsDAOHE();
            List<Objects> result = objectsDAOHE.getObjectsByAppId(getId());
            if (result != null) {
                for (Objects objects : result) {
                    listChild.add(new ObjectsNode(objects.getObjectId(), objects.getObjectName()));
                }
            }
        } else {
            ApplicationDAOHE appDAOHE = new ApplicationDAOHE();
            List<Applications> result = appDAOHE.getAll();
            if (result != null) {
                for (Applications app : result) {
                    listChild.add(new AppNode(app.getAppId(), app.getAppName()));
                }
            }
        }
        return listChild.size();
    }

}

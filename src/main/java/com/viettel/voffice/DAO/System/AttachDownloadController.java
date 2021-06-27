/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System;

import com.viettel.utils.FileUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.core.user.BO.Users;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

/**
 *
 * @author giangpn
 */
public class AttachDownloadController extends BaseComposer {

    @Wire
    Listbox lbAttachs;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp); //To change body of generated methods, choose Tools | Templates.
        Long objectId = (Long) Executions.getCurrent().getArg().get("objectId");
        Long objectType = (Long) Executions.getCurrent().getArg().get("objectType");
        String loadAll = (String)Executions.getCurrent().getArg().get("loadAll"); 
        AttachDAOHE adhe = new AttachDAOHE();
        List<Attachs> lstAttachs = loadAll ==null ? adhe.getByObjectIdAndType(objectId, objectType) : adhe.getAllByObjectIdAndType(objectId, objectType);
        
        if(lstAttachs.size() > 0)
        {
            UserDAOHE uDaoHe = new UserDAOHE();
            List<Long> userIds = new ArrayList();
            for(Attachs at:lstAttachs)
            {
                if(at.getCreatorId() !=null)
                {
                    userIds.add(at.getCreatorId());
                    //tim user o day, hoac theo cach duoi
                }
                if(at.getModifierId() !=null)
                {
                    userIds.add(at.getModifierId());
                }
            }
            
            List<Users> lstUser = uDaoHe.getListByKeys(userIds);
            for(Attachs at:lstAttachs)
            {
                if(at.getCreatorId() !=null)
                {
                    for(Users u:lstUser)
                    {
                        if(at.getCreatorId().equals(u.getUserId()))
                        {
                            at.setCreatorName(u.getFullName());
                        }
                        if(at.getModifierId().equals(u.getUserId()))
                        {
                            at.setModifierName(u.getFullName());
                        }
                    }
                }
            }
        }
        
        ListModelArray lstModel = new ListModelArray(lstAttachs);
        lbAttachs.setModel(lstModel);
    }
   

    @Listen("onSelect=#lbAttachs")
    public void onLoadPage(Event event) throws FileNotFoundException {
        Attachs att = lbAttachs.getSelectedItem().getValue();
        String path = att.getAttachPath() + File.separator + att.getAttachId();
        File f = new File(path);
        if (f.exists()) {
            File tempFile = FileUtil.createTempFile(f, att.getAttachName());
            Filedownload.save(tempFile, null);
        } else {
            showNotification("File không còn tồn tại trên hệ thống");
        }
    }
}

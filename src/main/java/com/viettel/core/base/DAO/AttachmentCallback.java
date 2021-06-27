/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.core.base.DAO;

import com.viettel.voffice.BO.Document.Attachs;

/**
 *
 * @author giangnh20
 */
public interface AttachmentCallback {
    
    public void onUploadFinished(Attachs attachObj, String uploadPath);
    
    public void onUploadFailed();
    
}

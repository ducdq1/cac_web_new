/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.model;

import com.viettel.voffice.BO.Document.*;
import java.io.Serializable;
import com.viettel.core.workflow.BO.Process;
/**
 *
 * @author giangpn
 */

public class DocumentPublishProcess implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4353865309561376853L;

	private Process process;

    private DocumentPublish documentPublish;

    
    public DocumentPublishProcess() {
    }

    public DocumentPublishProcess(DocumentPublish documentPublish,Process process) {
        this.process = process;
        this.documentPublish = documentPublish;
    }
    
    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public DocumentPublish getDocumentPublish() {
        return documentPublish;
    }

    public void setDocumentPublish(DocumentPublish documentPublish) {
        this.documentPublish = documentPublish;
    }
}

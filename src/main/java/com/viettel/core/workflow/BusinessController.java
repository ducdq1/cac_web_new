/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.workflow;

import java.util.HashMap;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import com.viettel.core.base.DAO.BaseComposer;

/**
 *
 * @author duv
 */
public class BusinessController extends BaseComposer{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3214054883383090580L;
	@Wire
    private Button btnSubmit;
    private Window parent;
    
    @SuppressWarnings("unchecked")
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        HashMap<String, Object> arguments = 
                (HashMap<String, Object>) Executions.getCurrent().getArg();
        parent = (Window) arguments.get("parentProcessingWindow");
    }
    
    @Listen("onClick=#btnSubmit")
    public void onSubmit(){
        System.out.println("Submit business page ok!!!");
        callBackAfterSubmit();
    }

    /**
     * @return the parent
     */
    public Window getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Window parent) {
        this.parent = parent;
    }
    
    protected void callBackAfterSubmit(){
        Events.sendEvent("onCallBackAfterSign", this.getParent(), null);
    }
    
}

/*    */ package com.viettel.voffice.framework;

/*    */ import java.io.IOException;

/*    */ import javax.servlet.http.HttpServletResponse;

/*    */ import org.apache.struts2.ServletActionContext;

/*    */
/*    */ import com.opensymphony.xwork2.Action;
/*    */ import com.opensymphony.xwork2.ActionInvocation;
/*    */ import com.opensymphony.xwork2.ActionSupport;
/*    */ import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.viettel.utils.LogUtils;
/*    */ import com.viettel.utils.WFUtil;
/*    */ import com.viettel.voffice.dojotag.ActionMessage;

/*    */
/*    */ class ActionPreResultListener/*    */ implements PreResultListener /*    */ {
	/*    */ public void beforeResult(ActionInvocation ai, String resultCode) /*    */ {
		/* 55 */ ActionSupport as = null;
		/* 56 */ if ((ai.getAction() instanceof Action)) /* 57 */ {
			as = (ActionSupport) ai.getAction();
		}
		/*    */ try /*    */ {
			/* 60 */ HttpServletResponse response = ServletActionContext.getResponse();
			/* 61 */ if (("input").equals(resultCode)) {
				/* 62 */ ActionMessage aMessage = WFUtil.getActionMessage();
				/* 63 */ aMessage.setType("error");
				/* 64 */ aMessage.setContent("Giá trị nhập vào không hợp lệ");
				if (as != null) {
					/* 65 */ aMessage.setFieldErrors(as.getFieldErrors());
				}
				/* 66 */ response.setContentType("application/json; charset=UTF-8");
				/* 67 */ response.getWriter().print(aMessage.toJSONContent());
				/* 68 */ ai.setResultCode("none");
				/* 69 */ return;
				/* 70 */ }
			if (("Message").equalsIgnoreCase(resultCode)) {
				/*    */ } /* 82 */ else if (("notAllowedURL").equalsIgnoreCase(resultCode)) {
				/* 83 */ ActionMessage aMessage = WFUtil.getActionMessage();
				/* 84 */ aMessage.setType("error");
				/* 85 */ aMessage.setContent("Bạn không có quyền thực hiện chức năng này");
				if (as != null) {
					/* 86 */ aMessage.setFieldErrors(as.getFieldErrors());
				}
				/* 87 */ response.setContentType("application/json; charset=UTF-8");
				/* 88 */ response.getWriter().print(aMessage.toJSONContent());
				/* 89 */ ai.setResultCode("none");
				/*    */ }
			/*    */ } catch (IOException ex) {
			/* 92 */ LogUtils.addLog(ex);
			/*    */ }
		/*    */ }
	/*    */ }

/*
 * Location: C:\Work\RDFW 315.jar Qualified Name:
 * com.viettel.framework.interceptor.ActionPreResultListener JD-Core Version:
 * 0.6.2
 */
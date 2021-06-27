package com.viettel.module.phamarcy.viewmodel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Iframe;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.utils.EncryptionUtil;

public class ZKPDFViewerVM extends BaseComposer {
 
	private static final long serialVersionUID = 1L;
	 
	@Wire
	private Iframe reportframe;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Execution execution = Executions.getCurrent();
 
		String 	filePath = execution.getParameter("filePath");
		if (filePath == null)
			filePath = "";
		if (filePath.isEmpty() == false) {
			filePath = EncryptionUtil.decode(filePath);
			File f = new File(filePath);
			byte[] buffer = new byte[(int) f.length()];
			FileInputStream fs = new FileInputStream(f);
			fs.read(buffer);
			fs.close();
			ByteArrayInputStream is = new ByteArrayInputStream(buffer);
			AMedia amedia = new AMedia(filePath, "pdf", "application/pdf", is);
			reportframe.setContent(amedia);
		}
		}
	
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}
}

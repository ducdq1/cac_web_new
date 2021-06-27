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
import org.zkoss.zul.Image;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.module.phamarcy.utils.EncryptionUtil;

public class ZKCollectionViewerVM extends BaseComposer {

	private static final long serialVersionUID = 1L;
	@Wire
	private Image img;
	@Wire
	private Iframe reportframe;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Execution execution = Executions.getCurrent();

		String	filePath = execution.getParameter("filePath");
		String ext = execution.getParameter("extension").toLowerCase();
		String ctype;
		boolean isImage = false;
		switch (ext) {
		// =================== DOCUMENT =======================
		case "pdf":
			ctype = "application/pdf";
			break;
		case "exe":
			ctype = "application/octet-stream";
			break;
		case "zip":
			ctype = "application/zip";
			break;
		case "doc":
			ctype = "application/msword";
			break;
		case "xls":
			ctype = "application/vnd.ms-excel";
			break;
		case "ppt":
			ctype = "application/vnd.ms-powerpoint";
			break;
		// =================== IMAGE =======================
		case "gif":
			ctype = "image/gif";
			isImage = true;
			break;
		case "png":
			ctype = "image/png";
			isImage = true;
			break;
		case "jpeg":
		case "jpg":
			ctype = "image/jpg";
			isImage = true;
			break;
		// =================== VIDEO =======================
		case "3gp":
			ctype = "video/3gpp";
			break;
		case "3g2":
			ctype = "video/3g2";
			break;
		case "avi":
			ctype = "video/avi";
			break;
		case "mp4":
			ctype = "video/mp4";
			break;
		case "ogv":
			ctype = "video/ogg";
			break;
		case "mov":
			ctype = "video/quicktime";
			break;
		// =================== AUDIO =======================
		case "mp3":
			ctype = "audio/mpeg";
			break;
		case "wav":
			ctype = "audio/x-wav";
			break;
		case "Wav":
			ctype = "audio/x-wav";
			break;
		case "WAV":
			ctype = "audio/x-wav";
			break;
		case "gsm":
			ctype = "audio/x-gsm";
			break;

		default:
			ctype = "application/force-download";
		}

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
			AMedia amedia = new AMedia(filePath, ext, ctype, is);
			if (!isImage) {
				reportframe.setContent(amedia);
				img.setVisible(false);
			}else{
				reportframe.setVisible(false);
				img.setVisible(true);
				org.zkoss.image.AImage aa = new org.zkoss.image.AImage(filePath);
				img.setContent(aa);
			}
		}
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		return super.doBeforeCompose(page, parent, compInfo);
	}
}

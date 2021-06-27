package org.zkoss.zk.au.http;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;

public class MyUploader extends AuUploader {
	protected String handleError(Throwable ex) {
		if (ex instanceof SizeLimitExceededException) {
			SizeLimitExceededException e = (SizeLimitExceededException) ex;
			return "Kích thước file (" + e.getActualSize()
					+ ") vượt quá dung lượng cho phép (" + e.getPermittedSize()
					+ ")";
		}
		return super.handleError(ex);
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.base.DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;

import com.viettel.convert.service.PdfDocxFile;
import com.viettel.module.phamarcy.utils.EncryptionUtil;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_Cos;
import com.viettel.utils.FileUtil;
import com.viettel.utils.LogUtils;
import com.viettel.utils.ResourceBundleUtil;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.NotifyDAOHE;

/**
 *
 * @author giangpn
 */
@SuppressWarnings("serial")
public class AttachDAO extends BaseGenericForwardComposer {

	public void saveFileAttach(WordprocessingMLPackage wmp, String fileName, Long objectId, Long objectType,
			Long attachType) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (wmp == null) {
			return;
		}
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		Attachs attach = new Attachs();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		String folderPath = ResourceBundleUtil.getString("dir_upload");
		FileUtil.mkdirs(folderPath);
		String separator = ResourceBundleUtil.getString("separator");
		folderPath += separator + Constants_Cos.OBJECT_TYPE_STR.PERMIT_STR;
		fileName = FileUtil.getSafeFileName(fileName);
		try {
			attach.setAttachPath(folderPath);
			attach.setAttachName(fileName);
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(objectId);
			attach.setCreatorId(getUserId());
			attach.setCreatorName(getFullName());
			attach.setDateCreate(new Date());
			attach.setModifierId(getUserId());
			attach.setDateModify(new Date());
			attach.setAttachCat(objectType);// van ban den
			if (attachType != null) {
				attach.setAttachType(attachType);
			}
			attachDAOHE.saveOrUpdate(attach);
			String path = folderPath + separator + attach.getAttachId();
			attach.setAttachPath(path);
			attachDAOHE.saveOrUpdate(attach);
			File fd = new File(folderPath);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			File f = new File(attach.getFullPathFile());
			if (f.exists()) {
			} else {
				f.createNewFile();
			}

			wmp.save(f);
			// AttachDAO base = new AttachDAO();
			// base.downloadFileAttach(attach);

			// luu file pdf cho nay
			InputStream is = new FileInputStream(f);
			XWPFDocument document = new XWPFDocument(is);

			// 2) Prepare Pdf options
			PdfOptions options = PdfOptions.create();

			// 3) Convert XWPFDocument to Pdf
			OutputStream out = new FileOutputStream(new File(f.getAbsolutePath() + f.getName() + ".pdf"));
			PdfConverter.getInstance().convert(document, out, options);
			// String filePath = f.getAbsolutePath();
		} catch (IOException | Docx4JException ex) {
			LogUtils.addLog(ex);
		}  
	}

	public void saveFileAttach(PdfDocxFile pdfDocxFile, String fileName, Long objectId, Long objectType,
			Long attachType) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (pdfDocxFile == null) {
			return;
		}
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		Attachs attach = new Attachs();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		String folderPath = ResourceBundleUtil.getString("dir_upload");
		FileUtil.mkdirs(folderPath);
		String separator = ResourceBundleUtil.getString("separator");
		folderPath += separator + Constants_Cos.OBJECT_TYPE_STR.PERMIT_STR;
		OutputStream outputStream = null;
		fileName=FileUtil.getSafeFileName(fileName);
		try {
			attach.setAttachPath(folderPath);
			attach.setAttachName(fileName);
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(objectId);
			attach.setCreatorId(getUserId());
			attach.setCreatorName(getFullName());
			attach.setDateCreate(new Date());
			attach.setModifierId(getUserId());
			attach.setDateModify(new Date());
			attach.setAttachCat(objectType);// giay phep
			if (attachType != null) {
				attach.setAttachType(attachType);
			}
			attachDAOHE.saveOrUpdate(attach);
			String path = folderPath + separator + attach.getAttachId();
			attach.setAttachPath(path);
			attachDAOHE.saveOrUpdate(attach);
			File fd = new File(folderPath);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			File f = new File(attach.getFullPathFile());
			if (f.exists()) {
			} else {
				f.createNewFile();
			}

			outputStream = new FileOutputStream(f);
			outputStream.write(pdfDocxFile.getContent());

		} catch (IOException ex) {
			LogUtils.addLog(ex);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			 
		}
	}

	// hieptq update 170315
	public void saveFileAttachPdfSign(String fileName, Long objectId, Long objectType, Long attachType)
			throws IOException {
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		Attachs attach = new Attachs();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		// String folderPath = ResourceBundleUtil.getString("dir_upload");
		// String separator = ResourceBundleUtil.getString("separator");
		// folderPath += separator + Constants_Cos.OBJECT_TYPE_STR.PERMIT_STR;

		ResourceBundle rb = ResourceBundle.getBundle("config");
		String separator = rb.getString("separator");
		//fileName=FileUtil.getSafeFileName(fileName);
		File afile = new File(fileName);
		String fName = afile.getName();
		// hieptq update 306015
		String[] parts = fName.split("_");
		String signType = parts[0];
		Long fileId = Long.parseLong(parts[1]);
		String folderPath = "";
		// hieptq update 150715 sua duong dan file
		if ("DN".equals(signType)) {
			// folderPath = rb.getString("signPdfBusiness");
			folderPath = Constants.FILE_PATH.BUSINESS + separator;
		}
		// ky lanh dao
		if (("LD").equals(signType)) {
			// folderPath = rb.getString("signPdfPermit");
			folderPath = Constants.FILE_PATH.PERMIT + separator;
		}
		// ky van thu
		if (("VT").equals(signType)) {
			// folderPath = rb.getString("signPdfPermit");
			folderPath = Constants.FILE_PATH.PERMIT + separator;
		}
		// ky tu choi LD
		if (("TCLD").equals(signType)) {
			// folderPath = rb.getString("signPdfReject");
			folderPath = Constants.FILE_PATH.REJECT + separator;
		}
		// ky tu choi VT
		if (("TCVT").equals(signType)) {
			// folderPath = rb.getString("signPdfReject");
			folderPath = Constants.FILE_PATH.REJECT + separator;
		}
		// ky bo sung lanh dao
		if (("BSLD").equals(signType)) {
			// folderPath = rb.getString("signPdfAddition");
			folderPath = Constants.FILE_PATH.ADDITION + separator;
		}
		// ky bo sung van thu
		if (("BSVT").equals(signType)) {
			// folderPath = rb.getString("signPdfAddition");
			folderPath = Constants.FILE_PATH.ADDITION + separator;
		}
		// FileUtil.mkdirs(folderPath);

		attach.setAttachPath(folderPath);
		// fName = fName.substring(1, fName.length());
		attach.setAttachName(fName);
		attach.setIsActive(Constants.Status.ACTIVE);
		// hieptq update 010715 chuyen sang luu fileId thay cho permitId
		// attach.setObjectId(objectId);
		attach.setObjectId(fileId);
		attach.setCreatorId(getUserId());
		attach.setCreatorName(getFullName());
		attach.setDateCreate(new Date());
		attach.setModifierId(getUserId());
		attach.setDateModify(new Date());
		attach.setAttachCat(objectType);// giay phep
		if (attachType != null) {
			attach.setAttachType(attachType);
		}
		attachDAOHE.saveOrUpdate(attach);
		// String path = folderPath + attach.getAttachId() + "\\";
		// attach.setAttachPath(path);
		// attachDAOHE.saveOrUpdate(attach);
		// xoa file temp
		// nghiepnc fix, viec xoa file temp se xu ly khi tao file ky
		// String pathSignTemp = rb.getString("signTemp");
		// File file1 = new File(pathSignTemp);
		// for (File file : file1.listFiles()) {
		// file.delete();
		// }

	}

	public void saveFileAttachPDF(WordprocessingMLPackage wmp, String fileName, Long objectId, Long objectType,
			Long attachType) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (wmp == null) {
			return;
		}
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		Attachs attach = new Attachs();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		String folderPath = ResourceBundleUtil.getString("dir_upload");
		FileUtil.mkdirs(folderPath);

		fileName = FileUtil.getSafeFileName(fileName);
		try {
			attach.setAttachPath(folderPath);
			attach.setAttachName(fileName);
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(objectId);
			attach.setCreatorId(getUserId());
			attach.setCreatorName(getFullName());
			attach.setDateCreate(new Date());
			attach.setModifierId(getUserId());
			attach.setDateModify(new Date());
			attach.setAttachCat(objectType);// van ban den
			if (attachType != null) {
				attach.setAttachType(attachType);
			}
			attachDAOHE.saveOrUpdate(attach);

			File fd = new File(folderPath);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			File f = new File(attach.getFullPathFile());
			if (f.exists()) {
			} else {
				f.createNewFile();
			}

			wmp.save(f);
			AttachDAO base = new AttachDAO();
			base.downloadFileAttach(attach);

			// luu file pdf cho nay
			InputStream is = new FileInputStream(f);
			XWPFDocument document = new XWPFDocument(is);

			// 2) Prepare Pdf options
			PdfOptions options = PdfOptions.create();

			// 3) Convert XWPFDocument to Pdf
			OutputStream out = new FileOutputStream(new File(folderPath + f.getName() + ".pdf"));
			PdfConverter.getInstance().convert(document, out, options);

		} catch (IOException | Docx4JException ex) {
			LogUtils.addLog(ex);
		} 
	}

	public void saveFileAttach(Media media, Long objectId, Long objectType, Long attachType) throws IOException {
		saveFileAttach(media, objectId, objectType, attachType, null);
	}

	/**
	 * @modified by giangnh20
	 * @reason: Them call back ho tro lay id attach & duong dan luu file
	 * @modifiedDate: 18/03/2015
	 * @param media
	 * @param objectId
	 * @param objectType
	 * @param attachType
	 * @param callback
	 * @throws IOException
	 */
	public void saveFileAttach(Media media, Long objectId, Long objectType, Long attachType,
			AttachmentCallback callback) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (media == null) {
			if (callback != null) {
				callback.onUploadFailed();
			}
			return;
		}
		AttachDAOHE attachDAOHE = new AttachDAOHE();
		Attachs attach = new Attachs();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)

		String folderPath = "";
		String separator = "/";//= ResourceBundleUtil.getString("separator");
		if (!"/".equals(separator) && !("\\").equals(separator)) {
			separator = "/";
		}

		String fileName = new BaseGenericForwardComposer().removeVietnameseChar(new Date().getTime()+"_"+media.getName());

		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			fileName = FileUtil.getSafeFileName(fileName);
			attach.setAttachName(fileName);
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(objectId);
			attach.setCreatorId(getUserId());
			attach.setCreatorName(getFullName());
			attach.setDateCreate(new Date());
			attach.setModifierId(getUserId());
			attach.setDateModify(new Date());
			attach.setAttachCat(objectType);// van ban den
			if (attachType != null) {
				attach.setAttachType(attachType);
			}
			attachDAOHE.saveOrUpdate(attach);
			// ngay hien tai
			Date date = new Date();
			if (objectType == Constants.OBJECT_TYPE.COSMETIC_PUBLIC_PROFILE) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_PUBLIC_PROFILE_STR;
			} // template
			else if (objectType == Constants.OBJECT_TYPE.TEMPLATE) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.TEMPLATE_STR;
			} // CA
			else if (objectType == Constants.OBJECT_TYPE.COSMETIC_CA_ATTACHMENT) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.COSMETIC_DOCUMENT_TYPE_CA;
			} else if (objectType == Constants.OBJECT_TYPE.CAC_IMAGE
					|| objectType == Constants.OBJECT_TYPE.CAC_IMAGE_TSKT) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.IMAGE_STR;
			}else if (objectType == Constants.OBJECT_TYPE.CAC_AVATAR
					 ) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.AVATAR_STR;
			}  
			String path = folderPath + separator + attach.getAttachId();
			attach.setAttachPath(path);
			attachDAOHE.saveOrUpdate(attach);
			String dir_upload = ResourceBundleUtil.getString("dir_upload") + separator;
			File fd = new File(dir_upload + folderPath);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			File f = new File(dir_upload + attach.getFullPathFile());
			if (!f.exists()) {
				// tao folder
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				f.createNewFile();
			}
			// save to hard disk and database
			inputStream = media.getStreamData();
			outputStream = null;
			outputStream = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
			if (callback != null) {
				callback.onUploadFinished(attach, getRelativePath(attach.getFullPathFile()));
			}
		} catch (IOException ex) {
			LogUtils.addLog(ex);
			if (callback != null) {
				callback.onUploadFailed();
			}
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	// nghiepnc
	public void saveFileAttach(Media media, Long attachId, Attachs att, Long objectId, Long objectType, Long attachType,
			String attachDes) throws IOException {
		AttachDAOHE attachDAOHE = new AttachDAOHE();

		Attachs attach = new Attachs();
		if (media == null) {

			att.setAttachDes(attachDes);
			if (getUserId() != null) {
				att.setModifierId(getUserId());
			} else {
				att.setModifierId(null);
			}
			att.setDateModify(new Date());
			att.setAttachType(attachType);
			attachDAOHE.saveOrUpdate(att);

			return;
		}

		String folderPath = "";
		String separator = ResourceBundleUtil.getString("separator");
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String fileName = FileUtil.getSafeFileName(media.getName());
		try {
			attach.setAttachPath(folderPath);
			attach.setAttachId(attachId);
			attach.setAttachName(fileName);
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(objectId);

			try {
				if (getUserId() != null) {
					attach.setCreatorId(getUserId());
					attach.setModifierId(getUserId());
				} else {
					attach.setCreatorId(null);
					attach.setModifierId(null);
				}

				if (getFullName() != null) {
					attach.setCreatorName(getFullName());
				} else {
					attach.setCreatorName(null);
				}
			} catch (NullPointerException e) {
				LogUtils.addLog(e);
			}

			attach.setDateCreate(new Date());
			attach.setDateModify(new Date());
			attach.setAttachCat(objectType);
			attach.setAttachDes(attachDes);
			if (attachType != null) {
				attach.setAttachType(attachType);
			}
			attachDAOHE.saveOrUpdate(attach);
			if (objectType == Constants.OBJECT_TYPE.COSMETIC_PUBLIC_PROFILE) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_PUBLIC_PROFILE_STR;
			} else if (objectType == Constants.OBJECT_TYPE.TEMPLATE) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.TEMPLATE_STR;
			} else if (objectType == Constants.OBJECT_TYPE.REGISTER) {
				folderPath += separator + Constants.OBJECT_TYPE_STR.COSMETIC_REGISTER_STR;
			} else {
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				folderPath += separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_DOCUMENT_STR + separator
						+ dateFormat.format(date) + separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_FILE_TYPE_STR
						+ separator + objectId;
			}

			String path = folderPath + separator + attach.getAttachId();
			// update lai attach path
			attach.setAttachPath(path);
			attachDAOHE.saveOrUpdate(attach);
			// tao folder
			String dir_upload = ResourceBundleUtil.getString("dir_upload") + separator;

			File fd = new File(dir_upload + folderPath);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			// tao file
			File f = new File(dir_upload + attach.getFullPathFile());
			if (f.exists()) {
			} else {
				f.createNewFile();
			}
			// save to hard disk and database
			inputStream = media.getStreamData();
			outputStream = null;
			outputStream = new FileOutputStream(f);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
			// String filePath = f.getAbsolutePath();
		} catch (IOException ex) {
			LogUtils.addLog(ex);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	// nghiepnc
		public void saveFileAttachRegister(Media media,  Long objectId, Long objectType, Long attachType,
				String attachDes) throws IOException {
			AttachDAOHE attachDAOHE = new AttachDAOHE();

			Attachs attach = new Attachs();
			 

			String folderPath = "";
			String separator = ResourceBundleUtil.getString("separator");
			InputStream inputStream = null;
			OutputStream outputStream = null;
			String fileName = new BaseGenericForwardComposer().removeVietnameseChar(media.getName());
			 fileName = FileUtil.getSafeFileName(media.getName());
			try {
				attach.setAttachPath(folderPath);
				attach.setAttachName(fileName);
				attach.setIsActive(Constants.Status.ACTIVE);
				attach.setObjectId(objectId);

				try {
					if (getUserId() != null) {
						attach.setCreatorId(getUserId());
						attach.setModifierId(getUserId());
					} else {
						attach.setCreatorId(null);
						attach.setModifierId(null);
					}

					if (getFullName() != null) {
						attach.setCreatorName(getFullName());
					} else {
						attach.setCreatorName(null);
					}
				} catch (NullPointerException e) {
					LogUtils.addLog(e);
				}

				attach.setDateCreate(new Date());
				attach.setDateModify(new Date());
				attach.setAttachCat(objectType);
				attach.setAttachDes(attachDes);
				if (attachType != null) {
					attach.setAttachType(attachType);
				}
				attachDAOHE.saveOrUpdate(attach);
				if (objectType == Constants.OBJECT_TYPE.COSMETIC_PUBLIC_PROFILE) {
					folderPath += separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_PUBLIC_PROFILE_STR;
				} else if (objectType == Constants.OBJECT_TYPE.TEMPLATE) {
					folderPath += separator + Constants.OBJECT_TYPE_STR.TEMPLATE_STR;
				} else if (objectType == Constants.OBJECT_TYPE.REGISTER) {
					folderPath += separator + Constants.OBJECT_TYPE_STR.COSMETIC_REGISTER_STR;
				} else {
					Date date = new Date();
					DateFormat dateFormat = new SimpleDateFormat("yyyy");
					folderPath += separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_DOCUMENT_STR + separator
							+ dateFormat.format(date) + separator + Constants.OBJECT_TYPE_STR.RAPID_TEST_FILE_TYPE_STR
							+ separator + objectId;
				}

				String path = folderPath + separator + attach.getAttachId();
				// update lai attach path
				attach.setAttachPath(path);
				attachDAOHE.saveOrUpdate(attach);
				// tao folder
				String dir_upload = ResourceBundleUtil.getString("dir_upload") + separator;

				File fd = new File(dir_upload + folderPath);
				if (!fd.exists()) {
					fd.mkdirs();
				}
				// tao file
				File f = new File(dir_upload + attach.getFullPathFile());
				if (f.exists()) {
				} else {
					f.createNewFile();
				}
				// save to hard disk and database
				inputStream = media.getStreamData();
				outputStream = null;
				outputStream = new FileOutputStream(f);

				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, len);
				}
				// String filePath = f.getAbsolutePath();
			} catch (IOException ex) {
				LogUtils.addLog(ex);
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		
	/**
	 * Tra ve duong dan tuong doi, bo di thu muc upload cau hinh trong file
	 * config
	 *
	 * @param absolutePath
	 * @return
	 */
	public static String getRelativePath(String absolutePath) {
		String folderPath = ResourceBundleUtil.getString("dir_upload");
		String separator = ResourceBundleUtil.getString("separator");
		if (!("/").equals(separator) && !("\\").equals(separator)) {
			separator = "/";
		}
		if (!folderPath.endsWith(separator)) {
			folderPath += separator;
		}
		if (absolutePath.startsWith(folderPath)) {
			return absolutePath.replace(folderPath, "").trim();
		}
		return absolutePath;
	}

	/**
	 * Xoa file
	 *
	 * @author giangnh20
	 * @param path
	 * @param absolute
	 * @return
	 */
	public static boolean removeFile(String path, boolean absolute) {
		String folderPath = ResourceBundleUtil.getString("dir_upload");
		String separator = ResourceBundleUtil.getString("separator");
		if (!("/").equals(separator)&& !("\\").equals(separator)) {
			separator = "/";
		}
		if (!absolute) {
			path = folderPath + (path.startsWith("/") ? path : (separator + path));
		}
		File f = new File(path);
		if (f.exists()) {
			f.delete();
		}
		return true;
	}

	/**
	 * Xoa file voi duong dan tuong doi
	 *
	 * @author giangnh20
	 * @param path
	 * @return
	 */
	public static boolean removeFile(String path) {
		return removeFile(path, false);
	}

	public Attachs saveFileAttachNotify(Media media, Notify notify) throws IOException {
		// Neu ung dung chua co avatar thi return
		if (media == null) {
			return null;
		}

		AttachDAOHE attachDAOHE = new AttachDAOHE();
		// Lay duong dan tuyet doi cua thu muc /Share/img (nam trong folder
		// target)
		String folderPath = ResourceBundleUtil.getString("dir_upload");
		FileUtil.mkdirs(folderPath);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		Attachs attach = null;
		boolean saveSuccess = true;
		try {

			attach = new Attachs();
			attach.setAttachPath(folderPath);
			String fileName = media.getName();
			fileName = FileUtil.getSafeFileName(fileName);
			attach.setAttachName(fileName);
			attach.setIsActive(Constants.Status.ACTIVE);
			attach.setObjectId(notify.getNotifyId());
			attach.setCreatorId(getUserId());
			attach.setCreatorName(getFullName());
			attach.setAttachCat(Constants.OBJECT_TYPE.NOTIFY);
			attachDAOHE.saveOrUpdate(attach);

			File fd = new File(folderPath);
			if (!fd.exists()) {
				fd.mkdirs();
			}
			//
			File f = new File(attach.getFullPathFile());

			if (f.exists()) {
			} else {
				f.createNewFile();
			}
			// save to hard disk and database
			inputStream = media.getStreamData();
			outputStream = null;
			outputStream = new FileOutputStream(f);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}
			if (attach.getAttachId() != null) {
				NotifyDAOHE notifyDAOHE = new NotifyDAOHE();
				notify.setAttachId(attach.getAttachId());
				notifyDAOHE.saveOrUpdate(notify);
			}

		} catch (IOException ex) {
			saveSuccess = false;
			LogUtils.addLog(ex);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		if (saveSuccess) {
			return attach;
		} else {
			return null;
		}
	}

	public void downloadFileAttach(Attachs curAtt) throws FileNotFoundException {
		if (curAtt != null) {
			String path = curAtt.getFullPathFile();
			path = ResourceBundleUtil.getString("dir_upload") + path;
			File f = new File(path);
			if (f.exists()) {
				File tempFile = FileUtil.createTempFile(f, curAtt.getAttachName());
				Filedownload.save(tempFile, path);
			} else {
				showNotify("File không còn tồn tại trên hệ thống!");
			}
		} else {
			showNotify("File không còn tồn tại trên hệ thống!");
		}
	}

	// hieptq update 300615
	public void downloadFileAttachOriginal(Attachs curAtt) throws FileNotFoundException {
		if (curAtt != null) {
			String path = curAtt.getFullPathFileOriginal();
			ResourceBundle rb = ResourceBundle.getBundle("config");
			String filePath = rb.getString("signPdf");
			path = filePath + path;
			File f = new File(path);
			if (f.exists()) {
				File tempFile = FileUtil.createTempFile(f, curAtt.getAttachName());
				Filedownload.save(tempFile, path);
			} else {
				showNotify("File không còn tồn tại trên hệ thống!");
			}
		} else {
			showNotify("File không còn tồn tại trên hệ thống!");
		}
	}

	public void downloadFileAttachById(Long attId) throws FileNotFoundException {
		if (attId != null) {
			AttachDAOHE attdaohe = new AttachDAOHE();
			Attachs curAtt = attdaohe.findById(attId);
			String path = curAtt.getFullPathFile();
			path = ResourceBundleUtil.getString("dir_upload") + path;
			File f = new File(path);
			if (f.exists()) {
				File tempFile = FileUtil.createTempFile(f, curAtt.getAttachName());
				Filedownload.save(tempFile, path);
			} else {
				showNotify("File không còn tồn tại trên hệ thống!");
			}
		} else {
			showNotify("File không còn tồn tại trên hệ thống!");
		}
	}

	public int checkAttachFiles(Long fileId, Long attType) {
		AttachDAOHE attdaohe = new AttachDAOHE();
		Attachs obj = attdaohe.getLastAttach(fileId, attType);
		if (obj == null) {
			return Constants.CHECK_VIEW.NOT_VIEW;
		} else {
			return Constants.CHECK_VIEW.VIEW;
		}
	}

	public List<Attachs> getAllAttachByObjType(Long objectId, List<Long> attType) {
		AttachDAOHE daohe = new AttachDAOHE();
		return daohe.findAllAttachByIdType(objectId, attType);
	}

	public void viewPdfOnBrowser(String path) {
		File f = new File(path);
		if (f.exists()) {
			String URL;
			URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
			URL = URL + EncryptionUtil.encode(path);
			String link = String.format(
					"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')", URL);
			Clients.evalJavaScript(link);
		} else {
			showWarningNotify(getLabel("file_not_exist"));
		}
	}

	public void viewPdfOnBrowser(Attachs att) {
		if(att==null){
			showWarningNotify(getLabel("file_not_exist"));
		}
		
		String path = att.getFullPathFile();
		path = ResourceBundleUtil.getString("dir_upload") + path;
		File f = new File(path);
		if (f.exists()) {
			String URL;
			URL = "Pages/module/phamarcy/generalview/viewPDF.zul?filePath=";
			URL = URL + EncryptionUtil.encode(path);
			String link = String.format(
					"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')", URL);
			Clients.evalJavaScript(link);

		} else {
			showWarningNotify(getLabel("file_not_exist"));
		}
	}

	public void viewCollectionOnBrowser(Attachs att) {

		String ext = FilenameUtils.getExtension(att.getFullPathFile());

		String path = att.getFullPathFile();
		path = ResourceBundleUtil.getString("dir_upload") + path;
		File f = new File(path);
		if (f.exists()) {
			String URL;
			URL = "Pages/module/phamarcy/generalview/viewCollection.zul?filePath=";
			URL = URL + EncryptionUtil.encode(path);
			URL = URL + "&extension=" + ext;
			String link = String.format(
					"window.open('%s','','top=100,left=200,height=600,width=800,scrollbars=1,resizable=1')", URL);
			Clients.evalJavaScript(link);

		} else {
			showWarningNotify(getLabel("file_not_exist"));
		}
	}

	/**
	 * lay value tu file language_vi.properties
	 * 
	 * @param key
	 * @return
	 */
	public String getLabel(String key) {
		try {
			return ResourceBundleUtil.getString(key, "language_vi");
		} catch (UnsupportedEncodingException ex) {
			LogUtils.addLog(ex);
		}
		return "";
	}
}

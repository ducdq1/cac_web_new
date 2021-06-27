/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.utils;

import java.util.Arrays;

/**
 *
 * @author ngoctm3@viettel.com.vn
 * @since since_text
 * @version 1.0
 */
public final class Constants {
	 
	public static final Long BAO_GIA_STATUS_TAO_MOI = 0L;
	public static final Long BAO_GIA_STATUS_DA_XUAT_BAO_GIA = 1L;
	public static final Long DONG_Y_CAP_GIAY_TN = 0L;
	public static final Long DONG_Y_SDBS = 1L;
	public static final Long DONG_Y_TU_CHOI = 2L;
	public static final Long YEU_CAU_XEM_XET_LAI = 3L;
	public static final Long NUMBER_DAY_PROCESS = 10L;// 10 ngay xu ly

	public static final Long DA_SOAN_CV_SDBS = 4L;

	public static final String OBJECT_CONSTANT = "OBJECT";
	public static final Long OBJECT_DOC_OUT = 301L;
	public static final long COMBOBOX_HEADER_VALUE = -1l;
	public static final String COMBOBOX_HEADER_VALUE_STRING = "-1";
	public static final String COMBOBOX_HEADER_TEXT = "--Tất cả--";
	public static final String COMBOBOX_HEADER_TEXT_SELECT = "--Chọn--";
	public static final String COMBOBOX_INSPECT_AUDIT_NOT_CHECK = "Chưa kiểm tra";
	public static final String COMBOBOX_INSPECT_AUDIT_CHECKED = "Đã kiểm tra";
	public static final String COMBOBOX_INSPECT_VIOLATION_NOT_PAY = "Chưa nộp tiền";
	public static final String COMBOBOX_INSPECT_VIOLATION_PAYED = "Đã nộp tiền";
	public static final String COMBOBOX_INSPECT_VIOLATION_NOT_EXECUTED = "Chưa thực hiện";
	public static final String COMBOBOX_INSPECT_VIOLATION_EXECUTED = "Đã thực hiện";
	public static final long COMBOBOX_INSPECT_VIOLATION_NOT_EXECUTED_VALUE = 0L;
	public static final long COMBOBOX_INSPECT_VIOLATION_EXECUTED_VALUE = 1L;
	public static final String COMBOBOX_INSPECT_VIOLATION_NOT_DO = "Chưa làm việc";
	public static final String COMBOBOX_INSPECT_VIOLATION_DONE = "Đã làm việc";
	public static final String COMBOBOX_INSPECT_AUDIT_FORM1 = "Thanh tra đột xuất";
	public static final String COMBOBOX_INSPECT_AUDIT_FORM2 = "Thanh tra theo kế hoạch";
	public static final String COMBOBOX_INSPECT_AUDIT_FORM3 = "Kiểm tra đột xuất";
	public static final String COMBOBOX_INSPECT_AUDIT_FORM4 = "Kiểm tra theo kế hoạch";
	public static final String STATUS_DA_GUI_TRUONG_TIEU_BAN = "Đã gửi trưởng tiểu ban xem xét lại";
	public static final String STATUS_CHUYEN_LAI_TRUONG_TIEU_BAN = "Chuyển lại cho trưởng tiểu ban";
	public static final String STATUS_DUNG_TIEN_DO = "Đúng tiến độ";
	public static final String STATUS_KHONG_DUNG_TIEN_DO = "Không đúng tiến độ";
	public static final String PHAMARCY_FILE = "Hồ sơ đăng ký thông tin thuốc";
	public static final String CONFIRM_PHAMARCY_FILE = "Hồ sơ xác nhận quảng cáo thuốc";
	public static final long COMBOBOX_INSPECT_VIOLATION_NOT_DO_VALUE = 0L;
	public static final long COMBOBOX_INSPECT_VIOLATION_DONE_VALUE = 1L;
	public static final String LM_NAME_FIELD = "name";
	public static final long default_profile_id = 22L;
	public static final int tree_wrap_text_length = 20;
	public static final long max_semesester_number = 15L;
	public static final String VSA_USER_TOKEN = "vsaUserToken";
	public static final String prefix_outsite_office = "9999";
	public static final int PAGE_ZISE = 10;
	public static final Long CUC_ATTP_ID = 3006L;
	public static final Long CUC_QLD_ID = 3300L;
	public static final Long IS_VALIDATE_OK = 1L;
	public static final int MAX_ATTACH_FILE_SIZE = 5242880;
	public static final String IS_VALIDATE_NOT_OK_SR = "Bạn vui lòng nhập đủ thông tin";
	public static final String PHA_REGISTER_FORM = "Tài liệu thông tin cho cán bộ y tế";// hinh
																						// thuc
																						// dang
																						// ky
	/*
	 * public static final char[] captchars = { 'A', 'B', 'C', 'D', 'E', 'F',
	 * 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U',
	 * 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
	 * 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
	 * 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	 */

	public interface CALENDAR_STATUS {

		public static final Long NEW_CREATE = 0L; // Mới tao, chua nop
		public static final Long APPROVE_WAIT = 1L; // gui cho phe duyet
		public static final Long APPROVE_ACCEPT = 2L; // Phe duyet
		public static final Long APPROVE_REJECT = 3L; // Tu choi
		public static final Long APPROVE_CANCEL = 4L; // Da phe duyet, huy
		public static final Long USER_CANCEL = 5L; // Nguoi dung huy lich
	}

	public interface MONEY_TYPE {
		public static final String VND = "VNĐ";
		public static final String USD = "USD";
	}

	public interface CALENDAR_VIEWTYPE {

		public static final Long VIEW_ONLY = 0L;
		public static final Long DEPT_APPROVE = 1L;
		public static final Long USER_APPROVE = 2L;
	}

	public interface CALENDAR_PARTICIPANT_TYPE {

		public static final Long PARTICIPANT = 0l;
		public static final Long PREPARE = 1l;
		public static final Long CHIEF = 2l;
		public static final Long APPROVE = 3l;
	}

	public interface ACTION {

		public interface TYPE {

			public static final Long INSERT = 0l;
			public static final Long UPDATE = 1l;
			public static final Long DELETE = 2l;
			public static final Long APPROVE = 3l;
			public static final Long REJECT = 4l;
			public static final Long VIEW = 5l;
			public static final Long TRANSFER = 6l;
			public static final Long LOCK = 7l;
			public static final Long UNLOCK = 8l;
			public static final Long LOGIN = 9l;
			public static final Long LOGOUT = 10l;
			public static final Long REGISTER = 11l;
			public static final Long LOOKUP = 12l;
		}

		public interface NAME {
			public static final String INSERT = "Thêm mới";
			public static final String UPDATE = "Cập nhật";
			public static final String DELETE = "Xóa";
			public static final String APPROVE = "Phê duyệt";
			public static final String REJECT = "Từ chối";
			public static final String VIEW = "Xem";
			public static final String TRANSFER = "Chuyển";
			public static final String LOCK = "Khóa";
			// hoangnv28
			public static final String DEFAULT = "Gửi xử lý";
			public static final String RETRIEVE = "Thu hồi";
			public static final String SUMPLEMENT = "Gửi bổ sung";
			public static final String PUT_IN_BOOK = "Vào sổ văn bản";
			public static final String GET_OPINION = "Xin ý kiến";
			public static final String GIVE_OPINION = "Cho ý kiến";
			public static final String CREATE_FILE = "Tạo hồ sơ";
			public static final String CREATE_DRAFT = "Tạo dự thảo";
			public static final String RETURN = "Trả lại";
			public static final String TRANSFORM_TO_PROCESS = "Chuyển văn xử lý";
			public static final String TRANSFORM_TO_RECEIVE_TO_KNOW = "Nhận để biết";
			public static final String UNLOCK = "Mở khóa";
			public static final String LOGIN = "Đăng nhập";
			public static final String LOGOUT = "Đăng xuất";
			public static final String CAPTCHA_LOG = "Log Captcha";
			public static final String PHA_THAMDINH = "Thẩm định";
			public static final String PHA_THAMDINH_LAI = "Thẩm định lại";
			public static final String PHA_TONGHOP_THAMDINH = "Tổng hợp kết luận thẩm định";
			public static final String PHA_TONGHOP_LAI_THAMDINH = "Tổng hợp lại kết luận thẩm định";
			public static final String PHA_CHUYEN_LAI_CHUYEN_GIA = "Chuyển hồ sơ lại cho chuyên gia";
			public static final String TONG_HOP_Y_KIEN_CHUYEN_GIA = "Tổng hợp lại ý kiến chuyên gia";
		}
	}

	public interface NODE_TYPE {

		public static final Long NODE_TYPE_START = 2l;
		public static final Long NODE_TYPE_FINISH = 3l;
		public static final Long NODE_TYPE_WORK = 1l;
	}

	public interface NODE_ASSOCIATE_TYPE {

		public static final Long ALL = 0L;
		public static final Long NORMAL = 1l;

		// Auto-send to next user
		public static final Long TRANSPARENT = 2l;
		// Action to start procedure
		public static final Long START = -1l;
		// Return to previous user
		public static final Long RETURN_PREVIOUS = 3l;
		// Return to author of document (creator of document)
		public static final Long RETURN_AUTHOR = 4l;
		// Alway send to configured users
		public static final Long ALWAYS_SEND = 5l;
		// Business send to processing deparment
		public static final Long SUBMIT = 6l;
	}

	public interface NODE_ACTOR_RELATIVE {

		public static final Long ALL = -1l;
		public static final Long PARENT = -2l;
		public static final Long CURRENT = -3l;
		public static final Long CHILD = -4l;
		public static final Long SAME_PARENT = -6l;
		public static final Long SAME_PARENT_LEVEL = -7l;
	}

	// hoangnv28
	public interface Notification {

		public static final String EXIST_WARNING = "%s đã tồn tại";
		public static final String NOT_EXIST_WARNING = "%s không tồn tại";
		public static final String INT_WARNING = "Bạn phải nhập %s lớn hơn 0";
		public static final String INPUT_WARNING = "Bạn chưa nhập %s";
		public static final String SELECT_WARNING = "Bạn chưa chọn %s";
		public static final String SAVE_SUCCESS = "Lưu %s thành công";
		public static final String SAVE_ERROR = "Lưu %s thất bại";
		public static final String UPDATE_SUCCESS = "Cập nhật %s thành công";
		public static final String UPDATE_ERROR = "Cập nhật $s thất bại";
		public static final String LOCK_CONFIRM = "Bạn có muốn khóa %s";
		public static final String LOCK_SUCCESS = "Khóa %s thành công";
		public static final String LOCK_ERROR = "Khóa %s thất bại";
		public static final String UNLOCK_COFIRM = "Bạn có muốn mở khóa %s";
		public static final String UNLOCK_SUCCESS = "Mở khóa %s thành công";
		public static final String UNLOCK_ERROR = "Mở khóa %s thất bại";
		public static final String DELETE_CONFIRM = "Bạn có muốn xóa %s";
		public static final String DELETE_SUCCESS = "Xóa %s thành công";
		public static final String DELETE_ERROR = "Xóa %s thất bại";
		public static final String ERROR = "error";
		public static final String INFO = "info";
		public static final String WARNING = "warning";
		public static final String SAVE_CONFIRM = "Bạn có chắc muốn lưu %s lại không?";
		public static final String APROVE_CONFIRM = "Bạn có chắc muốn phê duyệt %s không?";
		public static final String APROVE_SUCCESS = "Phê duyệt %s thành công";
		public static final String APROVE_FAILED = "Từ chối phê duyệt %s thành công";
		public static final String APROVE_SUCCESS1 = "Tiếp nhận %s thành công";
		public static final String APROVE_ERROR = "Phê duyệt %s thất bại";
		public static final String REJECT_SUCCESS = "Từ chối %s thành công";
		public static final String REJECT_ERROR = "Từ chối %s thất bại";
		public static final String BILL_CONFIRM = "Hóa đơn đã xác nhận";
		public static final String IMP_CONFIRM = "Bạn có chắc muốn nhập liệu %s không?";
		public static final String IMP_SUCCESS = "Nhập liệu %s thành công";
		public static final String IMP_ERROR = "Nhập liệu %s không thành công";
		public static final String SENDED_SUCCESS = "Đã gửi %s thành công";
		public static final String COLLECT_IDEA_SUCCESS = "Tổng hợp %s thành công";
		public static final String SEND_REQUEST_RECONSIDERATION_SUCCESS = "Gửi %s thành công";
		public static final String SAVE_DATA_SUCCESS = "Lưu %s thành công";
		public static final String COMMON_ERROR = "Có lỗi dữ liệu, vui lòng thử lại";
		public static final String COMMON_ERROR_NOT_PREV = "Bạn không có quyền xử lý hồ sơ!";
		public static final String COMMON_EXECUTE_ERROR = "Có lỗi trong quá trình xử lý!";
		public static final String SYSTEM_CONFIRM = "Bạn có chắc chắn muốn thực hiện xử lý '%s' không?";
		public static final String TH_YKTD_TC = "Gửi tổng hợp %s thành công";
		public static final String TH_YKTD_TC_TEMP = "Tổng hợp %s thành công";//
		public static final String THAMDINH_SUCCESS = "Gửi thẩm định %s thành công";
	}

	/**
	 * Status 0: delete;1: active
	 */
	public interface Status {

		public static final Long ACTIVE = 1l;
		public static final Long INACTIVE = 0l;
		public static final Long DELETE = -1L;
		public static final Long CLONE = -2L;

		public static final Long UPDATEPENDING = 2L;
		public static final Long UPDATEACCEPT = 3L;
		public static final Long UPDATEREJECT = -2L;
		// hoangnv28
		public static final String ACTIVE_MSG = "Hoạt động";
		public static final String INACTIVE_MSG = "Bị khóa";
		public static final String DELETE_MSG = "Bị xóa";
		// nghiepnc
		public static final String PHEDUYET = "Đã phê duyệt";
		public static final String CHUAPHEDUYET = "Chưa phê duyệt";
		public static final String TUCHOI = "Đã từ chối";
	}

	/**
	 * type of subject.major choosed in popup
	 */
	public interface ChooserType {

		public static final String SUBJECT = "0";
		public static final String MAJOR = "1";
	}

	public interface Report {

		public static final String TOPIC_TEMPLATE_PATH = "/WEB-INF/template/report/knowledge/DSDeTai.xls";
		public static final String TOPIC_EXPORT_PATH = "/share/templateReport/knowledge/DSDeTai";
		public static final String BOOK_TEMPLATE_PATH = "/WEB-INF/template/report/knowledge/DSSach.xls";
		public static final String BOOK_EXPORT_PATH = "/share/templateReport/knowledge/DSSach";
		public static final String WORKSHOP_TEMPLATE_PATH = "/WEB-INF/template/report/knowledge/DSHoiThao.xls";
		public static final String WORKSHOP_EXPORT_PATH = "/share/templateReport/knowledge/DSHoiThao";

		public static final String TEMPLATE_CV_SDBS = "/WEB-INF/template/confirm/CV_SDBS.docx";

		public static final String TEMPLATE_CV_TUCHOI = "/WEB-INF/template/confirm/CV_TUCHOI.docx";
	}

	public interface Type {

		public static final long TOPIC = 0l;
		public static final long BOOK = 1l;
		public static final long WORKSHOP = 2l;
		public static final String LEAD = "Lãnh đạo";
		public static final String CLERICAL = "Văn thư";
		public static final String EXPERT = "Chuyên viên";
		public static final String OTHER = "Khác";
	}

	public interface Task {

		public static final long NEED_PROCESS = 0L;
		public static final long PROCESSING = 1L;
		public static final String PROCESSING_STR = "Đang thực hiện";
		public static final long FINISH = 2L;
		public static final String FINISH_STR = "Hoàn thành";
		public static final long MAX_PERCENT = 100L;
	}

	public interface CATEGORY_TYPE {

		public static final String DEPARTMENT_TYPE = "DEPARTMENT_TYPE";
		public static final String RESOURCE_TYPE = "RESOURCE_TYPE"; // Do mat
		// cua van
		// ban
		public static final String VOFFICE_CAT_SECRET = "VOFFICE_CAT_SECRET"; // Do
		// mat
		// cua
		// van
		// ban
		public static final String DOCUMENT_TYPE = "VOFFICE_CAT_DOCTYPE"; // Loai
		// van
		// ban
		public static final String DOCUMENT_FIELD = "VOFFICE_CAT_DOCFIELD"; // Linh
		// vuc
		public static final String VOFFICE_CAT_URGENCY = "VOFFICE_CAT_URGENCY"; // Do
		// khan
		public static final String ACCESS_TYPE = "TC"; // Muc do truy cap
		public static final String IMPORTANT_TYPE = "IPT"; // Muc do quan trong
		public static final String SEND_TYPE = "VOPTN"; // Phuong thuc gui
		public static final String RECEIVE_TYPE = "VOFFICE_RECEIVE_TYPE";// Phuong
		// thuc
		// nhan
		public static final String FILE = "TTHC"; // Loai ho so
		/*
		 * public static final String[] toListCatType = { "VOFFICE_CAT_DOCTYPE",
		 * "VOFFICE_CAT_SECRET", "VOFFICE_CAT_URGENCY", "VOFFICE_CAT_DOCFIELD",
		 * "DEPARTMENT_TYPE", "RESOURCE_TYPE" };
		 */
		public static final String VOFFICE_CAT_BOOK_IN = "VOFFICE_CAT_BOOK_IN";
		public static final String VOFFICE_CAT_PRIORITY = "VOFFICE_CAT_PRIORITY";
		public static final String VOFFICE_CAT_NEXTSTAGE = "VOFFICE_CAT_NEXTSTAGE";
		public static final String VOFFICE_CAT_TYPE = "VOFFICE_CAT_TYPE";
		public static final Long PRIORITY_NORMAL = 791L;
		public static final Long DOCUMENT_RECEIVE = 600L;
		public static final String RAPID_TEST_FILE_TYPE = "RAPID_TEST_FILE_TYPE";
		public static final String COSMETIC_FILE_TYPE = "COSMETIC_FILE_TYPE";
		public static final String PHAMARCY_FILE_TYPE = "PHAMARCY_FILE_TYPE";
		public static final String INSPECT_BEHAVIOUS_MEDICINE_FILE_TYPE = "THANHTRA_DM_HANHVIVIPHAM_MEDICINE";
		public static final String INSPECT_BEHAVIOUS_COSMETIC_FILE_TYPE = "THANHTRA_DM_HANHVIVIPHAM_COSMETIC";
		public static final String INSPECT_COSMETIC_TYPE_OF_BUSINESS = "THANHTRA_DM_LOAIHINH_DOANHNGHIEP_VIPHAM_MYPHAM";
		public static final String INSPECT_VIPHAMTHUOC_ADD_PENATILIZE = "THANHTRA_VIPHAMTHUOC_XUPHATBOSUNG";
		public static final String INSPECT_VIPHAMMYPHAM_ADD_PENATILIZE = "THANHTRA_VIPHAMMYPHAM_XUPHATBOSUNG";
		public static final String INSPECT_KIEMTRATHUOC_LOAIHINH_DN = "THANHTRA_KIEMTRATHUOC_LOAIHINH_DN";
		public static final String INSPECT_KIEMTRAMYPHAM_LOAIHINH_DN = "THANHTRA_KIEMTRAMYPHAM_LOAIHINH_DN";

		public static final String XN_QCT_OTHER_FILE_TYPE = "XN_QCT_OTHER_ATTACH";
		public static final String XN_QCT_ADS_FILE_TYPE_1 = "XN_QCT_PHUONG_TIEN_QC_BAO";
		public static final String XN_QCT_ADS_FILE_TYPE_2 = "XN_QCT_PHUONG_TIEN_HOI_NGHI_SU_KIEN";
		public static final String XN_QCT_ADS_FILE_TYPE_3 = "XN_QCT_PHUONG_TIEN_KHAC";

		// linhdx thanh toan
		public static final String PROCEDURE = "OBJECT"; // Danh muc thu tuc xet
															// nghiem nhanh
		public static final String SUB_PROCEDURE = "SUB_PROCEDURE";// Thuc tuc
																	// con
		public static final String FEE = "FEE"; // Danh muc fee
		public static final String FEE_PAYMENT_TYPE = "FEE_PAYMENT_TYPE";// Hinh
																			// thuc
																			// thanh
																			// toan
		public static final String RAPID_TEST_OBJECT = "HS_RAPIDTEST_KIT";
		public static final String COSMETIC_OBJECT = "HS_COSMETIC_KIT";
		public static final String PHAMARCY_OBJECT = "HS_PHAMARCY_KIT";// tao
																		// object
																		// cua
																		// phamarcy
																		// luu
																		// trong
																		// bang
																		// category
		public static final String PROCEDURE_TEMPLATE_TYPE = "PROCEDURE_TEMPLATE_TYPE";// Bieu
																						// mau
																						// trong
																						// thu
																						// tuc
		public static final String FILE_STATUS = "FILE_STATUS"; // Trang thai ho
																// so
		public static final String COS_INGREDIENT = "COS_INGREDIENT"; // Thanh
																		// phan
																		// trong
																		// khai
																		// baos
		public static final String PAYMENT_STATUS = "PAYMENT_STATUS"; // Trang
																		// thai
																		// ho so

		public static final String ADS_MEDIA = "XNQC_DM";
		public static final String XNQC_DM3 = "XNQC_DM3";
		public static final String XNQC_DM2 = "XNQC_DM2";
		public static final String XNQC_DM1 = "XNQC_DM1";

		public static final String XN_QCT_OTHER_ATTACH1 = "XN_QCT_OTHER_ATTACH1";
		public static final String XN_QCT_OTHER_ATTACH2 = "XN_QCT_OTHER_ATTACH2";
		public static final String XN_QCT_OTHER_ATTACH6 = "XN_QCT_OTHER_ATTACH6";
		public static final String XN_QCT_OTHER_ATTACH7 = "XN_QCT_OTHER_ATTACH7";

		public static final String CONFIRM_PHAMARCY_OBJECT = "HS_CONFIRM_PHARMACY_KIT";

		public static final String XN_QC_NOI_DUNG_THAM_DINH = "XN_QC_NOI_DUNG_THAM_DINH";// noi
																							// dung
																							// tham
																							// dinh
	}

	public interface DOCUMENT_STATUS {

		public static final Long DRAFT = 0L; // du thao
		public static final Long PUBLISH = 1L; // da ban hanh
		public static final Long INSTRUCTION = 2L; // da xin y kien lanh dao
		public static final Long RETURN = 3L; // Lãnh đạo trả lại
		public static final Long APPROVAL = 4L; // "�?ã phê duyệt va gui xu ly
												// tiep";
		public static final Long SEND_COORDINATE = 5L; // "�?ã gửi phối hợp";
		public static final Long RECEIVE_COORDINATE = 6L; // "�?ã nhận ý kiến
															// phối hợp";
		public static final Long ASSIGN_NEXT = 7L; // Da chuyen tiep
		public static final Long APPROVAL_FORM = 8L; // Da phe duyet ve the thuc
		public static final Long JUST_REFERENCE = 11L; // Cong bo van ban
		public static final Long ASSIGN_NUMBER = 12L;
		// van ban den
		public static final Long NEW = 0L;// moi nhan
		public static final Long PROCESSING = 1L; // dang xu li
		public static final Long PROCESSED = 2L;// da xu li
		public static final Long RETRIEVE_ALL = 3L;// thu hoi tat ca
		public static final Long RETURN_ALL = 4L;// tra lai tat ca
		// Trang thai dang String
		public static final String DRAFT_STR = "Dự thảo";
		public static final String PUBLISH_STR = "Đã ban hành";
		public static final String INSTRUCTION_STR = "Đã gửi lãnh đạo";
		public static final String RETURN_STR = "Lãnh đạo trả lại";
		public static final String APPROVAL_STR = "Đã phê duyệt";
		public static final String SEND_COORDINATE_STR = "Đã trình xử lý";
		public static final String RECEIVE_COORDINATE_STR = "Đã nhận ý kiến phối hợp";
	}

	// hoangnv28
	public interface DOCUMENT_MENU {

		public static final int ALL = 0;// menu tiep nhan van ban,du thao
		public static final int WAITING_PROCESS = 1;// menu cho xu li
		public static final int PROCESSED = 3; // menu da xu li
		public static final int PROCESSING = 2;// menu dang xu li
		public static final int RECEIVE_TO_KNOW = 4;// menu nhan de biet
		public static final int RETRIEVED = 5;// menu da thu hoi
		public static final int MENU_PUBLISHED = 6;// menu da ban hanh(doi voi
		// vb di)
		public static final int WAITING_GIVE_OPINION = 7;// menu cho y kien
		public static final int GAVE_OPINION = 8;// menu da cho y kien
		public static final int REPORT = 9;// Báo cáo in sổ
		public static final int VIEW = 10;// Báo cáo in sổ
	}

	public interface DOCUMENT_STORE {

		public static final Long STORE_NEXT = 1L; // Da chuyen luu tru
		public static final Long STORE_RECORD = 2L; // Da luu tru ho so
		public static final Long STORAGE = 3L; // Da luu tru
	}

	public interface ROLE {

		public static final Long EXPERT = 0L;
		public static final Long CLERICAL = 1L;
		public static final Long LEAD = 2L;
		public static final Long ALL = 3L;
		public static final String LEAD_POSITION = "LD";
		public static final Long VAN_THU = 502L;
		public static final Long VAN_THU_BO = 359L;
		public static final Long VAN_THU_CUC = 800L;
		public static final Long VAN_THU_DON_VI = 1600L;
		public static final Long VAN_THU_PHONG = 1650L;
	}

	public interface MONITOR {

		public static final String VBDi_TK = "VBDi_TK"; // MH TK vbdi
		public static final String VBDi_VS = "VBDi_VS"; // MH vao so VBDi
		public static final String VBDi_CBH = "VBDi_CBH"; // MH VB cho ban hanh
		public static final String VBDi_DVXL = "VBDi_DVXL"; // MH VB don vi cho
		// xy ly
		public static final String VBDi_VBNB = "VBDi_VBNB"; // MH VB den noi bo
		public static final String VBDi_DT = "VBDi_DT"; // MH DS VB du thao
		public static final String VBDi_DTA = "VBDi_DTA"; // MH DS VB du thao da
		// duoc Approve
		public static final String VBDi_TDHB = "VBDi_TDHB"; // MH DS VB theo doi
		// hoi bao
		public static final String VBDi_CXL = "VBDi_CXL"; // MH VB cho LD xu ly
		public static final String VBDi_DXL = "VBDi_DXL"; // MH VB LD da xu ly
		public static final String VBDi_DT_HSCV = "VBDi_DT_HSCV"; // MH DS VB du
		// thao cua
		// HSCV
		public static final String VBDi_BH_HSCV = "VBDi_BH_HSCV"; // MH DS VB da
		// ban hanh
		// cua HSCV
		public static final String VBDi_PD_BH_HSCV = "VBDi_PD_BH_HSCV"; // DS VB
		// chua
		// phe
		// duyet
		// hoac
		// chua
		// ban
		// hanh
	}

	public interface PROCESS_STATUS {

		public static Long INITIAL = 0L; // moi khoi tao
		public static Long SENT_DISPATCH = 11L; // �?ã trả yêu cầu kiểm tra lại
		public static Long SENT_RETURN = 15L; // �?ã gửi CV SDBS
		public static Long VT_RETURN = 23L;//
		public static Long NEW = 0L; // moi den
		public static Long BOOKED = 1L; // da luu so don vi
		public static Long INSTRUCTION = 2L; // da chuyen xin y kien chi dao
		public static Long ASSIGNED = 3L; // da phan cong cho chuyen vien
		public static Long FINISH_1 = 4L; // hoan thanh (dong nghia voi ket thuc
		// VB)
		public static Long FINISH_2 = 14L; // hoan thanh (khi chuyen VB cho don
		// vi)
		public static Long RETURN = 5L; // tra lai
		public static Long APPROVED = 6L; // da phe duyet
		public static Long ASSIGN_NEXT = 7L; // da chuyen tiep
		public static Long DOING = 8L; // dang xu ly
		public static Long PHA_DOING = -2L; // dang xu ly
		public static Long DID = 9L; // da xu ly
		public static Long PUBLISHED = 10L; // da ban hanh
		public static Long PROPOSED = 3L;// �?�? xuất
		public static Long READ = 12L;// �?ã đ�?c
		public static Long RETRIEVE = 13L;// Thu hoi
		// Array luu trang thai cua process tuong ung voi cac menu: "Cho xu li",
		// "Dang xu li", "Da xu li".

		// Phamarcy update status

	};

	public static boolean isWaitingProcess(Long processStatus) {
		return Arrays.asList(new Constants().waitingProcessStatus()).contains(processStatus);
	}

	public static boolean isProcessing(Long processStatus) {
		return Arrays.asList(new Constants().processingStatus()).contains(processStatus);
	}

	public interface PROCESS_TYPE {

		public static Long COOPERATE = 0L; // phoi hop
		public static Long MAIN = 1L; // Xu ly chinh
		public static Long RECEIVE_TO_KNOW = 2L;// Nhan de biet
		public static Long COMMENT = 3L;// Cho y kien
		public static Long APPROVE = 4L;
		public static Long REFERENCE = 5L; // tham khao
		public static Long CHANGED_STATUS = 6L;// Thay doi trang thai
	}

	public interface RECEIVE_USER_TYPE {

		public static Long OFFICE_LEADER = 1l; // lanh dao van phong
		public static Long LEADER = 2l; // lanh dao don vi
		public static Long OFFICE_PROCESS = 3l; // don vi xu ly
		public static Long MONITER = 4l; // Phong xu ly(Phong giam sat xu ly)
	}

	public interface OBJECT_TYPE {
		
		public static Long REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN = 11L;
		public static Long REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES = 12L;
		public static Long REGISTER_ATTACHMENT_TYPE_CERTIFICATE = 10L;
		
		
		public static Long QC_THUOC = 5200L;
		
		public static Long DOCUMENT_RECEIVE = 1L; // Van ban den
		public static Long DOCUMENT_PUBLISH = 2L; // Van ban di
		public static Long DOCUMENT_REF = 3L; // Van ban tham khao
		public static Long PROFILE_WORK = 4L; // ho so cong viec
		public static Long PROFILE_STORE = 5L; // Ho so luu tru
		public static Long FORM = 6L; // Phieu yeu cau
		public static Long FORM_ATTACH_FILE = 7L;// file bieu mau
		public static Long NOTIFY = 8L;
		public static Long CALENDAR = 9L; // Ho so
		//
		// phuc vu cho xem log
		//
		public static Long LOG_IN = 10L;
		public static Long LOG_OUT = 11L;
		public static Long USER = 12L;
		public static Long DEPT = 13L;
		public static Long CATEGORY = 14L;
		public static Long ROLE = 15L;
		public static Long FLOW = 16L;
		public static Long TASK = 17L;
		public static Long NOTIFY_OBJECT_TYPE = 18L;
		public static Long TECHNICAL_STANDARD_ATTACH = 19L;
		// Xet nghiem nhanh
		public static Long FILES_RAPIDTEST = 20L; // Ho so
		public static Long RAPID_TEST_FILE_TYPE = 21L;
		public static Long RAPID_TEST_HO_SO_GOC = 22L;
		public static Long RAPID_TEST_SDBS_DISPATCH = 23L;
		public static Long RAPID_TEST_PERMIT = 24L;// Giay phep
		public static Long YCNK_FILE = 25L;
		public static Long FILES = 26L; // Ho so
		public static Long TEMPLATE = 27L; // Bieu mau
		public static Long RAPID_TEST_PUBLIC_PROFILE = 35L;
		public static Long RAPID_TEST_PAYMENT_ALREADY = 36L;
		public static Long COSMETIC_FILE_TYPE = 37L;
		public static Long PAYMENT_EXPORT_PDF = 38L;

		public static Long COSMETIC_PERMIT = 39L;// Giay phep
		public static Long COSMETIC_REJECT_DISPATH = 40L;// Cong van tu choi
		public static Long COSMETIC_PUBLIC_PROFILE = 41L;
		public static Long COSMETIC_HO_SO_GOC = 42L;
		public static Long COSMETIC_CA_ATTACHMENT = 43L;
		public static Long CAPTCHA_LOG = 44L;
		public static Long REGISTER = 45L;
		public static Long LOOKUP = 46L;
		public static Long COSMETIC_SDBS_DISPATH = 44L;// Cong van sua doi bo
														// sung
		public static Long COSMETIC_PERMIT_VT = 47L;// file ky van thu dong dau
													// so
		public static Long COSMETIC_SDBS_DISPATH_VT = 48L;// file ky van thu
															// dong dau so BS
		public static Long COSMETIC_REJECT_DISPATH_VT = 49L;// file ky van thu
															// dong dau so TC
		// public static Long COSMETIC_REJECT = 26L;//file ky van thu dong dau
		// so TC
		public static Long CAC_IMAGE = 50L;
		public static Long CAC_IMAGE_TSKT = 51L;
		public static Long CAC_AVATAR = 52L;
		// --------- DANH MUC PHAMARCY -----------------//
		public static Long PHAMARCY_FILE_TYPE = 50L; // tep dinh kem
		public static Long PHAMARCY_HO_SO_GOC = 51L;
		public static Long PHAMARCY_REJECT_DISPATH = 52L;// Cong van tu choi
		public static Long PHAMARCY_PERMIT = 53L;// Giay phep
		public static Long PHAMARCY_SDBS_DISPATH = 54L;// Cong van sua doi bo
														// sung
		public static Long PHAMARCY_PERMIT_VT = 55L;// file ky van thu dong dau
													// so
		public static Long PHAMARCY_REJECT_DISPATH_VT = 56L;// file ky van thu
															// dong dau so TC
		public static Long PHAMARCY_SDBS_DISPATH_VT = 57L;// file ky van thu
															// dong dau so BS
		public static Long PHAMARCY_PAYMENT_ALREADY = 58L;// phieu uy nhiem chi

		public static Long XN_QCT_OTHER_FILE_TYPE = 65L;// tai lieu dinh kem
														// khac
		public static Long XN_QCT_FILE_TYPE_1 = 66L;// phuong tien quang cao
													// tren bao chi
		public static Long XN_QCT_FILE_TYPE_2 = 67L;// phuong tien quang cao qua
													// hoi thao, hoi nghi...
		public static Long XN_QCT_FILE_TYPE_3 = 68L;// phuong tien quang cao
													// khac
	}

	public interface OBJECT_TYPE_STR {

		public static String DOCUMENT_RECEIVE_STR = "Văn bản đến";
		public static String DOCUMENT_PUBLISH_STR = "Văn bản đi";
		public static String FILES_RAPIDTEST_STR = "Hồ sơ xét nghiệm nhanh"; // Ho
																				// so
		public static String RAPID_TEST_FILE_TYPE_STR = "rapidtest";
		public static String RAPID_TEST_PUBLIC_PROFILE_STR = "Profile";
		public static String RAPID_TEST_DOCUMENT_STR = "Document";
		public static String COSMETIC_DOCUMENT_STR = "Document";
		public static String COSMETIC_DOCUMENT_TYPE_CA = "CA";
		public static String COSMETIC_REGISTER_STR = "Register";
		public static String TEMPLATE_STR = "Template";
		public static String IMAGE_STR = "images";
		public static String AVATAR_STR = "avatar";
	}

	// ID của chức năng
	public interface OBJECT_ID {

		public static Long TIEP_NHAN_VAN_BAN = 11L;
	}

	public interface RECORDS_TYPE {

		public static Long REGISTER_RECORDS = 4l; // ho so dang ky
		public static Long RECORDS_ADVERTISE = 1l; // So hs qcao
		public static Long RECORDS_PUBLIC = 2l; // So hs cong bo
		public static Long RECORDS_DDK = 3l; // So hs DDK
	}

	public interface POSITION {
		// BTP

		public static String LEAD_CODE = "BTP_LD_P"; // Lanh dao
		public static String LEAD_OFFICE_CODE = "BTP_LD_VP"; // Lanh dao van
		// phong
		public static String LEADER_CODE = "BTP_LD";
		public static String STAFF_CODE = "POS8"; // Nhan vien
		// CUC ATTP
		// public static String LEAD_CODE = "VOFFICE_LD"; // Lanh dao
		// public static String LEAD_OFFICE_CODE = "VOFFICE_LD_VP"; // Lanh dao
		// van phong
		// public static String LEADER_CODE = "VOFFICE_LD";
		public static String LEAD_SMS_CODE = "^BTP_LD(.*)SMS$";
		public static String SMS_CODE = "_SMS";

		public static Long CHUYENVIEN = 23L;
		public static Long CHUYENGIA = 300L;
		public static Long TRUONG_TIEU_BAN = 350L;
		public static Long VANTHU = 15L;
		public static Long TRUONG_PHONG = 4L;
		public static Long PHO_PHONG = 3L;
	}

	public interface ROLES {

		public static String LEAD_ROLE = "voffice_cvp"; // Chanh van phong
		public static String LEAD_OFFICE_ROLE = "voffice_ld"; // Lanh dao
		public static String STAFF_ROLE = "voffice_cv"; // Chuyen vien
		public static String CLERICAL_ROLE = "voffice_vt"; // Van thu
		public static String LEAD_MONITOR_ROLE = "voffice_ld_pc";
		public static String CBLT_CQ = "voffice_cblt_cq"; // Can bo luu tru co
		// quan
		// ATTP HQMC
		public static String LEAD_UNIT = "voffice_lddv"; // lanh dao don vi
	}

	public interface DEPARTMENT {

		public static Long VIETTEL_ID = 801l;// id cua tap doan VIETTEL
		public static Long BTP_ID = 52l; // id cua bo tu phap
		public static Long DEPT_TYPE_PHONG = 8l; // Phong cua don vi
		public static String DEPT_TYPE_CODE_PHONG = "Văn phòng"; // ma cua phong
		public static Long DEPT_KE_TOAN = 3304L; // Phong cua don vi

		public static Long TIEU_BAN_PHAP_CHE = 3452L;
		public static Long TIEU_BAN_THONG_TIN = 3453L;
		public static Long TIEU_BAN_SP = 3453L;
	}

	public interface CommentType {

		public static Long APPROVED = 1l; // Phe duyet
		public static Long RETURN = 2l; // tra lai
		public static Long COORDINATE = 3l; // phoi hop
		public static Long INSTRUCTION = 4l; // gui xin y kien lanh dao
	}

	public interface NOTIFY_TYPE {

		public static Long BY_RECEIVE_DOC = 1l; // Hồi báo bằng VB đến
		public static Long OTHER = 0l; // Hồi báo bằng dạng khác
		public static Long LEADER_OPINION = 2l;// �? kiến lãnh đạo
		public static Long RETURN = 3l;// Trả lại
	}

	public interface NOTIFY_STATUS {

		public static Long ON_TIME = 1l; // �?ã hồi báo (đúng hạn)
		public static Long OUT_TIME = 0l; // �?ã hồi báo (quá th�?i hạn)
		public static String ON_TIME_STR = "Đã hồi báo (đúng hạn)";
		public static String OUT_TIME_STR = "Đã hồi báo (quá thời hạn)";
		public static Long ACTIVE = 1L; // Dang theo doi hoi bao
		public static Long INACTIVE = 2L; // Ket thuc theo doi hoi bao
	}

	public interface ROLE_STAFF {

		public static final String ROLE_PUBLISH = "ROLE_STAFF_PUBLISH";
		public static final String ROLE_ASSIGN = "ROLE_STAFF_ASSIGN";
	}

	public interface FILE_STATUS {
		public static final Long NEW = 0L;
		public static final Long VT_REJECT = 23L;
		public static final Long PROCESSING = 1L;// Mới tao, chua nop
		public static final Long SENDING = 2l; // da hoan thanh
		public static final Long FINISHED = 3l; // da hoan thanh
		public static final Long REJECT = 4l; // da tra lai
		public static final Long APPROVE = 5l; // da tiep nhan
		public static final Long DA_PHAN_CONG_CV = 6L;
		// Da chuyen ho so toi chuyen gia
		public static final Long DA_CHUYEN_HS_TOI_CHUYENGIA = 30L;
		public static final Long PHA_DA_THAMDINH = 1L;
		public static final Long DA_TONG_HOP_Y_KIEN_THAM_DINH = 31L;
		public static final Long DA_TONG_HOP_LAI_Y_KIEN_THAM_DINH = 45L;
		public static final Long LDP_DONG_Y_CAP_GTN = 33L;
		public static final Long LDP_DONG_Y_SDBS = 34L;
		public static final Long LDP_DONG_Y_TUCHOI = 35L;
		public static final Long LDP_YEU_CAU_XEM_XET_LAI = 36L;
		public static final Long YEU_CAU_XEM_XET_LAI_CV_TU_CHOI = 25L;
		public static final Long YEU_CAU_XEM_XET_LAI_CV_SDBS = 27L;
		public static final Long LDC_TU_CHOI_PHE_DUYET = 20L;
		public static final Long LDP_YEU_CAU_THAM_DINH_LAI = 11L;
		public static final Long DN_GUI_THAM_DINH_LAI = 99L;
		public static final Long DA_THONG_BAO_SDBS = 15L;
		public static final Long DA_PHAN_CONG = 6L;
		public static final Long DA_NOP_PHI = 7L;
		public static final Long DA_KY_CV_SDBS = 8L;
		public static final Long KT_TU_CHOI_XAC_NHAN_PHI = 21L;
		public static final Long KT_TU_CHOI_XAC_NHAN_PHI_BO_SUNG = 68L;
		public static final Long CV_YEU_CAU_BO_SUNG_PHI = 64L;
		public static final Long DA_XAC_NHAN_PHI_BO_SUNG = 67L;
		public static final Long VT_YEU_CAU_BO_SUNG_PHI = 65L;

		public static final Long DA_TAO_GIAY_TIEP_NHAN = 37L;
		public static final Long DA_TAO_CV_TU_CHOI = 39L;
		public static final Long DA_TRINH_GIAY_TIEP_NHAN = 44L;
		public static final Long DA_TAO_CV_SUA_DOI = 38L;

		public static final Long DA_TAO_LAI_GIAY_TIEP_NHAN = 46L;
		public static final Long DA_TAO_LAI_CV_TU_CHOI = 47L;
		public static final Long DA_TAO_LAI_CV_SUA_DOI = 48L;
		public static final Long DA_GUI_CHUYENGIA_THAM_DINH_LAI = 50L;

		public static final Long CV_SOAN_CV_TUCHOI = 35L;

		public static final Long YEU_CAU_DU_THAO_GIAY_XAC_NHAN = 51L;
		public static final Long DA_TRINH_DU_THAO_GIAY_XAC_NHAN = 52L;
		public static final Long DA_TRINH_GIAY_XAC_NHAN = 53L;
		public static final Long DA_KY_GIAY_XAC_NHAN = 54L;
		public static final Long DA_DONG_DAU_GIAY_XAC_NHAN = 55L;
		public static final Long DA_DONG_DAU_CV_SDBS = 15L;
		public static final Long DA_DONG_DAU_CV_TU_CHOI = 26L;
		public static final Long DA_TRINH_CV_TU_CHOI = 70L;
		public static final Long DA_KY_CV_TU_CHOI = 41L;
		public static final Long TRA_XEM_LAI_GIAY_XN = 56L;
		public static final Long DA_TRINH_LAI_DU_THAO_GIAY_XAC_NHAN = 57L;
		public static final Long DA_TRINH_CV_SDBS = 28L;
		public static final Long LDC_YEU_CAU_XEM_LAI_CV_SDBS = 58L;
		public static final Long LDC_YEU_CAU_XEM_LAI_CV_TU_CHOI = 59L;

	}

	public interface FILE_DESCRIPTION {

		public static final String ANNOUNCEMENT_FILE01 = "announcementFile01";//
		public static final String RE_ANNOUNCEMENT = "reAnnouncement";//
		public static final String RE_CONFIRM_FUNC_IMP = "reConfirmFuncImport";
		public static final String RE_CONFIRM_NORMAL_VN = "reConfirmNormalVN";
		public static final String RE_CONFIRM_FUNC_VN = "reConfirmFuncVN";
		public static final String REC_CONFIRM_NORMAL_IMP = "reConfirmNormalImp";
		public static final String CONFIRM_FUNC_IMP = "confirmFuncImport";
		public static final String CONFIRM_FUNC_VN = "confirmFuncVN";
		public static final String CONFIRM_NORMAL_IMP = "confirmNormalImport";
		public static final String ANNOUNCEMENT_FILE03 = "announcementFile03";
		public static final String ANNOUNCEMENT_4STAR = "announcement4star";
		public static final String CONFIRM_NORMAL_VN = "confirmNormalVN";
		public static final String CONFIRM_SATISFACTORY = "confirmSatisfactory";
	}

	public interface DIALOG_CONTROL {

		public static final String DEPARTMENT = "DEPARTMENT";
		public static final String USERS = "USERS";
		public static final String CATEGORY = "CATEGORY";
	}

	public interface UPLOAD {

		public static final String ATTACH_PATH = "/Share/upload/";
		public static final String AVATAR_PATH = "/Share/avatar/";
	}

	public interface RECORD_MODE {

		public static final String DELETE = "Delete";
		public static final String EDIT = "Edit";
		public static final String VIEW = "View";
		public static final String CREATE = "Create";
		public static final String PUBLISH = "Publish";
		public static final String ASSIGN_NUMBER = "AssignNumber";
		public static final String ASSIGN_AND_PUBLISH = "AssignAndPublish";
		public static final String CREATE_DOCOUT = "CreateDocOut";
		public static final String EDIT_DOCOUT = "EditDocOut";
	}

	public interface BUTTON_TOOLBAR_TYPE {

		public static final Long TRANSFER = 1L; // nut chuyen xu ly thong thuong
		public static final Long RETURN = 2L; // tra lai van ban
		public static final Long FINISH = 3L; // ket thuc van ban
		public static final Long RETRIEVE = 4L; // thu hoi van ban
	}

	public interface TREE_TYPE {

		public static int FULL = 1;
		public static int DEPT_ONLY = 2;
		public static int SPECIAL_DEPT = 3;
	}

	public interface TREE_MODE {

		public static Long SINGLE = 1L;
		public static Long MULTIPLE = 2L;
	}

	//
	// dung object_type trong bang attachs de phan biet: dinh kem cho vb den/vb
	// di/y kien
	// dung attach_type de phan biet loai file dinh kem cho vb di: phieu
	// trinh,du thao,lien quan
	public interface ATTACH_TYPE {

		public static Long ATT_REPORT = 1L; // file dinh kem la phieu trinh
		public static Long ATT_DRAFT = 2L; // file dinh kem la du thao
		public static Long ATT_DOC_RELATION = 3L; // file dinh kem la van ban
													// lien quan
	}

	public interface DOCUMENT_TYPE_NAME {

		public static final String DOCUMENT = "Văn bản";
		public static final String FILE = "Sản phẩm";
		public static final String ATTACH = "File đính kèm";
		public static final String PRODUCT = "Sản phẩm";
		public static final String ACCOUNT = "Tài khoản";
		public static final String BILL = "Biên lai";
		public static final String TMP_BILL = "Biên lai";
		public static final String TMP_IDEA = "ý kiến thẩm định";
		public static final String REQUEST_RECONSIDERATION = "yêu cầu xem xét lại";
		public static final String DATA = "dữ liệu";
		public static final String SAVE = "lưu";
		public static final String IDEA = "ý kiến";
		public static final String FILE_INFO = "Thông tin hồ sơ";
	}

	public interface TYPE_FILE {

		public static final String DELETE = "1";
		public static final String NOTDELETE = "0";
	}

	public interface USER_TYPE {

		public static final Long ADMIN = 1L;
		public static final Long NOT_ADMIN = 0L;
		public static final Long ENTERPRISE_USER = 2L;
	}

	public interface RAPID_TEST_STATUS {

		public static Long NEW = 0L; // moi den
		public static Long BOOKED = 1L; // da luu so don vi
		public static Long INSTRUCTION = 2L; // da chuyen xin y kien chi dao
		public static Long ASSIGNED = 3L; // da phan cong cho chuyen vien
		public static Long FINISH_1 = 4L; // hoan thanh (dong nghia voi ket thuc
	}

	public interface PROCEDURE_TEMPLATE_TYPE {

		public static Long ADDITIONAL_REQUEST = 0L;
		public static final String ADDITIONAL_REQUEST_STR = "Công văn Sửa đổi bổ sung";
		public static Long PERMIT = 1L;
		public static final String PERMIT_STR = "Giấy phép";
		public static Long PHILEPHI = 2L;
		public static final String PHILEPHI_STR = "Biên lai thu ti�?n phí, lệ phí";
	}

	public interface PAYMENT {

		public int FEE_PAYMENT_TYPE_NHIEU_HO_SO = 2;
		public int FEE_PAYMENT_TYPE_MOT_HO_SO = 0;

		public String PAYMENT_DNNOPTIEN = "0";
		public String PAYMENT_KTXACNHAN = "1";
		public String PAYMENT_KTTUCHOI = "2";

		public interface PHASE {

			public Long EVALUATION = 0L; // Giai doan tham dinh
			public Long PERMIT = 1L; // Giai doan cap so
		}
	}

	public interface RAPID_TEST {

		public String TYPE_CONFIG = "RAPID_TEST_CONFIG";
		public String CODE_CONFIG = "RAPID_TEST_CONFIG1";
		public Long DOCUMENT_TYPE_CODE_TAOMOI = 16L;
		public Long DOCUMENT_TYPE_CODE_BOSUNG = 17L;
		public Long DOCUMENT_TYPE_CODE_GIAHAN = 18L;
		public String DOCUMENT_TYPE_CODE_TAOMOI_STR = "Hồ sơ đề nghị đăng ký lưu hành bộ xét nghiệm nhanh thực phẩm";
		public String DOCUMENT_TYPE_CODE_BOSUNG_STR = "Hồ sơ đề nghị gia hạn đăng ký lưu hành bộ xét nghiệm nhanh thực phẩm";
		public String DOCUMENT_TYPE_CODE_GIAHAN_STR = "Hồ sơ đề nghị gia hạn đăng ký lưu hành bộ xét nghiệm nhanh thực phẩm";
		public Long DINH_TINH = 1L;
		public Long BAN_DINH_TINH = 2L;
		public Long DINH_LUONG = 3L;
		public String DINH_TINH_STR = "Định tính";
		public String BAN_DINH_TINH_STR = "Bán định lượng";
		public String DINH_LUONG_STR = "Định lượng";

		public interface PAYMENT {

			public Long PAY_NEW = 1L; // Moi tao
			public Long PAY_ALREADY = 2L;// Da thanh toan
			public Long PAY_CONFIRMED = 3L;// Da xac nhan
			public Long PAY_REJECTED = 4L;// Da xac nhan tu choi
			public String PAY_NEW_STR = "Chưa thanh toán"; // Moi tao
			public String PAY_ALREADY_STR = "Đã thanh toán";// Da thanh toan
			public String PAY_CONFIRMED_STR = "Đã xác nhận";// Da xac nhan
			public String PAY_REJECTED_STR = "Đã từ chối";// Da xac nhan
			public Long BILL_NEW = 1L; // Moi tao
			public Long BILL_CONFIRMED = 2L;// Da xac nhan
			public Long BILL_REJECTED = 3L;// Da xac nhan tu choi
			public String BILL_NEW_STR = "Mới tạo"; // Moi tao
			public String BILL_CONFIRMED_STR = "Đã xác nhận";// Da xac nhan
			public String BILL_REJECTED_STR = "Từ chối";// Da xac nhan
			public Long FEE_PAYMENT_TYPE_CODE_KEYPAY = 1L;
			public Long FEE_PAYMENT_TYPE_CODE_CHUYENKHOAN = 2L;
			public Long FEE_PAYMENT_TYPE_CODE_TIENMAT = 3L;
			public String FEE_PAYMENT_TYPE_CODE_KEYPAY_STR = "Keypay";
			public String FEE_PAYMENT_TYPE_CODE_CHUYENKHOAN_STR = "Chuyển khoản";
			public String FEE_PAYMENT_TYPE_CODE_TIENMAT_STR = "Tiền mặt";
			public int FEE_PAYMENT_TYPE_MOT_HO_SO = 1;
			public int FEE_PAYMENT_TYPE_NHIEU_HO_SO = 2;

			public interface PHASE {

				public Long EVALUATION = 0L; // Giai doan tham dinh
				public Long PERMIT = 1L; // Giai doan cap so
			}
		}

		public interface ADDITIONAL_REQUEST {

			public interface TYPE_EXPORT {

				public int EX_TEMP = 1; // Tao file chua ky
				public int EX_SIGN = 2;// Tao file da ky
			}
		}

		public static final Long CATEGORY_SYSTEM = 1l;
		public static final Long CATEGORY_NORMAL = 0l;
	}

	public interface PLACE {

		public static String NATION = "nation";
		public static String PROVINCE = "province";
		public static String DISTRICT = "district";
		public static String VILLAGE = "village";
		public static String VNCODE = "VN";
	}

	public interface REPORT_TYPE {

		public static String REPORT_ALL = "reportAll";
	}

	public interface MENU_TYPE {

		public static String CREATE_STR = "create";
		public static String WAITPROCESS_STR = "waitprocess";
		public static String PROCESSING_STR = "processing";
		public static String PROCESSED_STR = "processed";
		public static String DEPT_PROCESS_STR = "deptprocess";
	}

	public interface COSMETIC {

		public Long DOCUMENT_TYPE_CODE_TAOMOI = 16L;
		public Long DOCUMENT_TYPE_CODE_BOSUNG = 17L;
		public Long DOCUMENT_TYPE_CODE_GIAHAN = 18L;
		public String DOCUMENT_TYPE_CODE_TAOMOI_STR = "Hồ sơ đề nghị đăng ký lưu hành mỹ phẩm";
		public String DOCUMENT_TYPE_CODE_BOSUNG_STR = "Hồ sơ đề nghị gia hạn đăng ký lưu hành mỹ phẩm";
		public String DOCUMENT_TYPE_CODE_GIAHAN_STR = "Hồ sơ đề nghị gia hạn đăng ký lưu hành mỹ phẩm";
	}

	public interface PHAMARCY {
		public Long SDBS = 0L;
		public Long TU_CHOI = 1L;
		/// -- CHUC DANH THAM DINH --//
		public Long TYPE_LANH_DAO_PHONG = 0L;
		public Long TYPE_CHUYEN_VIEN = 1L;
		public Long TYPE_CHUYEN_GIA = 2L;
		public Long TYPE_TRUONG_TIEU_BAN = 3L;

		// -----------------------------------//
		public Long DOCUMENT_TYPE_CODE_TAOMOI = 16L;
		public Long DOCUMENT_TYPE_CODE_BOSUNG = 17L;
		public Long DOCUMENT_TYPE_CODE_GIAHAN = 18L;

	}

	public interface PERMIT_STATUS {

		public Long SIGNED = 1L;// Da ky
		public Long PROVIDED_NUMBER = 2L;
	}

	public interface EVALUTION {

		public Long FILE_OK = 1L; // Dong y
		public Long FILE_NOK = 0L;
		public Long FILE_NEED_ADD = 2L;// yeu cau bo sung

		public interface LEGAL {

			public Long OK = 1L; // Dong y
			public Long NOK = 0L;
			public Long NEED_ADD = 2L;// yeu cau bo sung
		}

		public interface EFFECTIVE {

			public Long NO_LIMIT = 1L; // Khong gioo han
			public Long YEAR_3 = 2L;
			public Long YEAR_5 = 3L;// yeu cau bo sung
		}

		public interface USER_EVALUATION_TYPE {

			public Long STAFF = 1L; // Nhan vien
			public Long PP = 2L;// Phó phòng
			public Long TP = 3L;// Trưởng phòng
			public Long CP = 4L;
			public Long CT = 5L;// Cục trưởng
			public Long VT = 6L;// Van thu
		}

		public interface EVALUATION_LEADER_APPROVE {

			public Long APPROVE_FILE = 1L;
			public Long APPROVE_DISPATCH = 2L;
		}

		public interface DISPATCH_STATUS {

			public Long SIGNED = 1L;// Da ky
			public Long PROVIDED_NUMBER = 2L;
		}

		public interface BOOK_TYPE {

			public String BOOK_IN = "1";// Da ky
			public String BOOK_REJECT = "2";// Cong van tu choi
			public String BOOK_PERMIT = "3";// Dong y cap giay phep
			public String BOOK_ADD_REQUEST = "4";// Yeu cau bo sung
		}

		public interface BOOK_ID {

			public Long BOOK_ID_SOTIEPNHAN = 2450L;// Da ky
		}
	}

	public interface CHECK_VIEW {

		public int VIEW = 1;
		public int NOT_VIEW = 0;
	}

	public interface HOLIDAY {

		public Long ACTIVE = 1l;
		public Long INACTIVE = 0l;
		public String NOTI_NOT_DEL = "Ngày chọn xóa phải lớn hơn ngày hiện tại";
		public String NOTI_NOT_EDIT = "Ngày chọn sửa phải lớn hơn ngày hiện tại";
		public String COMBOBOX_HEADER_SELECT = "---Chọn---";
		public String NOTI_EMPTY_HOLIDAY_NAME = "Tên sự kiện không thể để trống";
		public String NOTI_EMPTY_HOLIDAY_DATE = "Ngày sự kiện không thể để trống";
		public String NOTI_HOLIDAY = "Ngày nghỉ không thể là Thứ bảy hoặc Chủ nhật";
		public String NOTI_WORK_HOLIDAY = "Ngày đi làm bù phải là Thứ bảy hoặc chủ nhật";
		public String NOTI_DATA_EXISTS = "Trùng dữ liệu đã tạo trên hệ thống";
		public String NOTI_SAVE_FINISH = "Lưu thành công";
	}

	public interface WARNING {

		public Long DEADLINE_ON = 1L;// Dung han
		public Long DEADLINE_MISS = 2L;// Qua han
		public Long DEADLINE_RECENT = 3L;// Gan den han

		public Long NUM_DAY_PROCESS = 3L;// 3 ngay xu ly

	}

	public interface IMPORT_EXCEL {

		public int PRODUCT_TYPE_ROW_INDEX = 22;
		public int PRODUCT_PRESENTATION_ROW_INDEX = 7;
		public int PRODUCT_TYPE_COL_INDEX = 14;
		public int PRODUCT_PRESENTATION_INDEX = 15;
	}

	public interface COUNT_HOME_TYPE {
		
		public static String CHO_TIEP_NHAN = "CHO_TIEP_NHAN";
		public static String TOTAL = "TOTAL";
		public static String WAIT_PROCESS = "WAIT_PROCESS";
		public static String PROCESS = "PROCESS";
		public static String FINISH = "FINISH";
		public static String FINISH_REJECT = "FINISH_REJECT";
		public static String PERMIT = "PERMIT";
		public static String WAIT_PAYMENT = "WAIT_PAYMENT";
		// STATUS_DATHONGBAOSDBS
		public static String STATUS_DATHONGBAOSDBS = "STATUS_DATHONGBAOSDBS";
		
	}

	public interface FILE_STATUS_CODE {

		// �?ã nộp
		public static long STATUS_DN_DANOP = 3L;
		// �?ã trả kết quả
		public static long FINISH = 4L;

		// �?ã trả kết quả ho so tu choi
		public static long FINISH_REJECT = 26L;
		// �?ã thẩm định
		public static long STATUS_CV_DATHAMDINH = 1L;
		// �?ã xem xét
		public static long STATUS_TP_DAXEMXET = 2L;
		// �?ã tiếp nhận
		public static long STATUS_VT_DATIEPNHAN = 5L;
		// Mới tạo
		public static long STATUS_MOITAO = 0L;
		// �?ã thanh toán
		public static long STATUS_DN_DATHANHTOAN = 7L;
		// �?ã phân công
		public static long STATUS_TP_DAPHANCONG = 6L;
		// �?ã thẩm xét
		public static long STATUS_TP_DATHAMXET = 9L;
		// �?ã ký công văn S�?BS
		public static long STATUS_LD_DAKYCONGVANSDBS = 8L;
		// �?ã phê duyệt hồ sơ, yêu cầu nộp lệ phí
		public static long STATUS_LD_DAPHEDUYET = 10L;
		// �?ã thông báo yêu cầu S�?BS
		public static long STATUS_DATHONGBAOSDBS = 11L;
		// �?ã xác nhận thanh toán
		public static long STATUS_KT_DAXACNHANTHANHTOAN = 12L;
		// �?ã sửa đổi bổ sung
		public static long STATUS_DN_DASDBS = 14L;
		// Trả yêu cầu kiểm tra lại
		public static long STATUS_TRAYEUCAUKIEMTRALAI = 15L;
		// �?ã gửi phiếu báo thu
		public static long STATUS_VT_DAGUIPHIEUBAOTHU = 16L;
		// Ch�? phân công
		public static long STATUS_CHOPHANCONG = 17L;
		// Từ chối phê duyệt
		public static long STATUS_LD_TUCHOIDUYET = 20L;
		// Chuyên viên thẩm định đat
		public static long STATUS_CV_THAMDINHDAT = 22L;
		// Chuyên viên thẩm định không đạt
		public static long STATUS_CV_THAMDINHKHONGDAT = 18L;
		// Chuyên viên thẩm định yêu cầu bổ sung
		public static long STATUS_CV_THAMDINHYCSDBS = 19L;
		// �?ã từ chối xác nhận phí
		public static long STATUS_KT_TUCHOIXACNHANPHI = 21L;
	}

	// linhdx
	// tim vi tri de chen anh chu ky
	public interface REPLATE_CHARACTER_WHEN_FIND_LOCATION_TO_SIGN {

		// Doanh nghiep
		public static String LOCATION_BUSINESS = "<DN>";// Vi tri ky cua DN
		public static String XN_LOCATION_BUSINESS = "<XNDN>";
		public static String LOCATION_LEADER = "<SI>";// Vi tri ky cua lanh dao
		public static String LOCATION_VANTHU = "<VT>";// Vi tri ky cua lanh dao
		public static String LOCATION_REJECT = "<TC>";// Vi tri ky cua lanh dao
														// tu choi
		public static String LOCATION_ADD = "<BS>";// Vi tri ky cua lanh dao bo
													// sung
		public static String LOCATION_VANTHU_ADD = "<BSVT>";
		public static String LOCATION_VANTHU_REJECT = "<TCVT>";

		public static String CONFIRM_ADDITIONAL_LOCATION_ADD = "<XNBS>";
		public static String CONFIRM_ADDITIONAL_LOCATION_ACCEPT = "<XNSI>";
		public static String CONFIRM_ADDITIONAL_LOCATION_REJECT = "<XNTC>";
		public static String CONFIRM_VT_LOCATION_ACCEPT = "<XNVT>";
	}

	public interface FILE_PATH {

		// Doanh nghiep
		public static String BUSINESS = "Business";
		public static String PERMIT = "Permit";
		public static String REJECT = "Reject";
		public static String ADDITION = "Addition";
	}

	public interface PREVIEW_TYPE {

		public static final Long ADDITION = 0L;
		public static final Long REJECT = 1L;
	}

	// Phamarcy date format
	public static final String DATE_VN_FORMAT = "dd/MM/yyyy";
	public static final Long MIN_FEE = 1800000L;

	public interface AssignType {
		public static final int CHUYEN_VIEN = 0;
		public static final int CHUYEN_GIA = 1;
	}

	/* THANH TRA THUOC */
	public interface INSPECT {
		public static final Long AUDIT_VIOLATION_TYPE_MEDICINE = 0L;
		public static final Long AUDIT_VIOLATION_TYPE_COSMETIC = 1L;
	}

	/* XAC NHAN QUANG CAO */
	public interface CONFIRM_TYPE {
		public static final int BAO_HINH_BAO_NOI = 0;
		public static final int HOI_NGHI_SU_KIEN = 1;
		public static final int LOAI_KHAC = 2;
	}

	public interface SYSTEM_TYPE {
		public static final long XAC_NHAN_QC = 8400L;
	}

	public interface SIGN_TYPE {
		public static final int DONG_Y = 1;
		public static final int VAN_THU_DONG_DAU_DONG_Y = 4;
		public static final int TU_CHOI = 2;
		public static final int SDBS = 3;
	}

	public interface MODE {
		public static final String LDP_SDBS = "LDPtraxemxetlaiCVSDBS";
		public static final String LDP_TUCHOI = "LDPtraxemxetlaiCVTC";
		public static final String LDC_SDBS = "LDCtraxemxetlaiCVSDBS";
		public static final String LDC_TUCHOI = "LDCtraxemxetlaiCVTC";

		public static final String DN_NOP_MOI_HS = "sendfile";
	}

	public char[] getCaptchars() {
		return new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9' };
	}

	public String[] toListCatType() {
		return new String[] { "VOFFICE_CAT_DOCTYPE", "VOFFICE_CAT_SECRET", "VOFFICE_CAT_URGENCY",
				"VOFFICE_CAT_DOCFIELD", "DEPARTMENT_TYPE", "RESOURCE_TYPE" };
	}

	public Long[] waitingProcessStatus() {
		return new Long[] { Constants.PROCESS_STATUS.NEW, Constants.PROCESS_STATUS.BOOKED,
				Constants.PROCESS_STATUS.READ };
	}

	public Long[] processingStatus() {
		return new Long[] { Constants.PROCESS_STATUS.INSTRUCTION,
				Constants.PROCESS_STATUS.ASSIGN_NEXT, Constants.PROCESS_STATUS.DID };
	}
	
	
	public Long[] processedStatus() {
		return new Long[] { Constants.PROCESS_STATUS.FINISH_2, Constants.PROCESS_STATUS.RETURN,
				Constants.PROCESS_STATUS.FINISH_1 };
	}

	public interface BUSINESS_TYPE {
		public static final String FOREIGN_BUSINESSES = "BUSINESS_TYPE9"; // Doanh nghiệp nước ngoài
		public static final String LOCAL_BUSINESSES = "BUSINESS_TYPE8"; // DN/Cơ sở đăng ký trong nước
	}
	/**
	 * 
	 * @author nhatnt4
	 *
	 */
	public interface TypeRegister{
		public static final int TYPE_TAXCODE = 1;
		public static final int TYPE_LICENSES_OFFICES = 2;
		public static final int TYPE_LICENSES_MEDICINES = 3;		
	}
}

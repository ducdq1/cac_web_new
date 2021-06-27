/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.utils;

/**
 *
 * @author ngoctm3@viettel.com.vn
 * @since since_text
 * @version 1.0
 */
public final class Constants_XNN {

	public interface PLACE {
		public static String NATION = "nation";
		public static String PROVINCE = "province";
		public static String DISTRICT = "district";
		public static String VILLAGE = "village";
		public static String VNCODE = "VN";
	}

	public interface Status {
		public static final Long ACTIVE = 1l;
		public static final Long INACTIVE = 0l;
		public static final Long DELETE = -1L;
		// nghiepnc
		public static final String PHEDUYET = "Đã phê duyệt";
		public static final String CHUAPHEDUYET = "Chưa phê duyệt";
		public static final String TUCHOI = "Đã từ chối";
	}

	public interface Notification {

		public static final String REGISTER_SUCCESS = "Đăng ký thành công";
	}

	public interface CATEGORY_TYPE {
		public static final String BUSINESS_TYPE = "BUSINESS_TYPE";
		public static final String USER_TYPE = "USER_TYPE";
	}

	public interface USER_TYPE {
		public static final Long ADMIN = 1L;
		public static final Long NOT_ADMIN = 0L;
		public static final Long ENTERPRISE_USER = 2L;
	}

	// Phamarcy
	public interface STATUS_FILE_TYPE {
		public static final Long ALL = 0L; // Tat ca
		public static final Long CREATED = 1L; // Moi tao
		public static final Long WAITING_ACCEPT = 2L; // Cho tiep nhan
		public static final Long NOTI_FEE = 3L; // Thong bao nop phi
		public static final Long VT_REQUEST_ADDITIONAL = 4L; // Van thu yeu cau bo sung
		public static final Long DN_FEED = 5L; // Doanh nghiep nop phi
		public static final Long KT_PROCESSING = 6L; // Dang xu ly
		public static final Long REQUEST_ADDITIONAL = 17L; // Van thu dong dau da bi yeu cau bo sung
		public static final Long REQUEST_REJECT = 18L; // Van thu dong dau da bi tu choi
		public static final Long LDC_SIGNED = 21L; // Da cap giay tiep nhan
		public static final Long DN_SUBMITTED_ADDITIONAL = 23L; // Da sua doi bo sung ho so
	}
}

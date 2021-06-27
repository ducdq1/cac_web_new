/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.utils;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ngoctm3@viettel.com.vn
 * @since since_text
 * @version 1.0
 */
public final class Constants_Cos {

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
        //nghiepnc
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

    public interface EVALUTION {

        public Long FILE_OK = 1L; //Dong y
        public Long FILE_NOK = 0L;
        public Long FILE_NEED_ADD = 2L;//yeu cau bo sung

        public interface LEGAL {

            public Long OK = 1L; //Dong y
            public Long NOK = 0L;
            public Long NEED_ADD = 2L;//yeu cau bo sung
        }

        public interface EFFECTIVE {

            public Long NO_LIMIT = 1L; //Khong gioo han
            public Long YEAR_3 = 2L;
            public Long YEAR_5 = 3L;//yeu cau bo sung
        }

        public interface USER_EVALUATION_TYPE {

            public Long STAFF = 1L; //Nhan vien
            public Long PP = 2L;//Phó phòng
            public Long TP = 3L;//Trưởng phòng
            public Long CP = 4L;
            public Long CT = 5L;//Cục  trưởng
            public Long VT = 6L;//Van thu
        }

        public interface EVALUATION_LEADER_APPROVE {

            public Long APPROVE_FILE = 1L;
            public Long APPROVE_DISPATCH = 2L;
        }

        public interface DISPATCH_STATUS {

            public Long SIGNED = 1L;// Da ky
            public Long PROVIDED_NUMBER = 2L;
        }

        public interface PERMIT_STATUS {

            public Long SIGNED = 1L;// Da ky
            public Long PROVIDED_NUMBER = 2L;
        }

        public interface BOOK_TYPE {

            public String BOOK_IN = "1";// Da ky
            public String BOOK_DISPATCH = "2";
            public String BOOK_OUT = "3";
        }
    }

    public interface OBJECT_TYPE_STR {

        public static String RAPID_TEST_FILE_TYPE_STR = "rapidtest";
        public static String RAPID_TEST_PUBLIC_PROFILE_STR = "Profile";
        public static String RAPID_TEST_DOCUMENT_STR = "Document";
        public static String TEMPLATE_STR = "Template";
        public static String PERMIT_STR = "Permit";
        public static String REJECT_STR = "Reject";
        public static String SDBS_STR = "SDBS";
        public static String ADD_STR = "Add";
        public static String CA_STR = "CA";
    }

    public interface OBJECT_TYPE {

        //Xet nghiem nhanh
        public static Long FILES_RAPIDTEST = 20L; // Ho so
        public static Long RAPID_TEST_FILE_TYPE = 21L;
        public static Long RAPID_TEST_HO_SO_GOC = 22L;
        public static Long RAPID_TEST_SDBS_DISPATCH = 23L;
        public static Long RAPID_TEST_PERMIT = 24L;//Giay phep
        public static Long YCNK_FILE = 25L;
        public static Long FILES = 26L; // Ho so

        public static Long TEMPLATE = 27L; // Bieu mau
        public static Long RAPID_TEST_PUBLIC_PROFILE = 35L;

        public static Long RAPID_TEST_PAYMENT_ALREADY = 36L;

        public static Long COSMETIC_FILE_TYPE = 37L;

        public static Long PAYMENT_EXPORT_PDF = 38L;
        public static Long CA_IMAGE_ATTACH = 39L;

    }

    public interface ROLE_ID {

        //Xet nghiem nhanh
        public static Long QLD_LDC = 2004L;
        public static Long QLD_LDP = 2003L;
        public static Long QLD_CB = 2002L;
        public static Long QLD_KT = 2001L;
        public static Long QLD_VT = 2000L;
        public static Long QLD_DN = 2300L;

    }

    public interface ATTACH_TYPE {

        //Xet nghiem nhanh
        public static Long COSMETIC_PERMIT = 39L;//Giay phep
        public static Long COSMETIC_REJECT_DISPATH = 40L;//Cong van tu choi
        public static Long COSMETIC_PUBLIC_PROFILE = 41L;
        public static Long COSMETIC_HO_SO_GOC = 42L;
        public static Long COSMETIC_CA_ATTACHMENT = 43L;
        public static Long REGISTER = 45L;
        public static Long COSMETIC_SDBS_DISPATH = 44L;//Cong van sua doi bo sung
        public static Long COSMETIC_PERMIT_VT = 47L;// file ky van thu dong dau so
    }

}

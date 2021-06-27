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
public final class Constants_CKS {

    public interface TYPE_USER {

        //LÃNH ĐẠO
        public static String LD = "LD";
        //VĂN THƯ
        public static String VT = "VT";
    }

    public interface FILE_STATUS_CODE {

        // Đã nộp
        public static long STATUS_DN_DANOP = 3L;
        // Đã trả kết quả
        public static long FINISH = 4L;
        // Đã thẩm định
        public static long STATUS_CV_DATHAMDINH = 1L;
        // Đã xem xét
        public static long STATUS_TP_DAXEMXET = 2L;
        // Đã tiếp nhận
        public static long STATUS_VT_DATIEPNHAN = 5L;
        // Mới tạo
        public static long STATUS_MOITAO = 0L;
        // Đã thanh toán
        public static long STATUS_DN_DATHANHTOAN = 7L;
        // Đã phân công
        public static long STATUS_TP_DAPHANCONG = 6L;
        // Đã thẩm xét
        public static long STATUS_TP_DATHAMXET = 9L;
        // Đã ký công văn SĐBS
        public static long STATUS_LD_DAKYCONGVANSDBS = 8L;
        // Đã phê duyệt hồ sơ, yêu cầu nộp lệ phí
        public static long STATUS_LD_DAPHEDUYET = 10L;
        // Đã thông báo yêu cầu SĐBS
        public static long STATUS_DATHONGBAOSDBS = 15L;
        // Đã xác nhận thanh toán
        public static long STATUS_KT_DAXACNHANTHANHTOAN = 12L;
        // Đã sửa đổi bổ sung
        public static long STATUS_DN_DASDBS = 14L;
        // Trả yêu cầu kiểm tra lại
        public static long STATUS_TRAYEUCAUKIEMTRALAI = 11L;
        // Đã gửi phiếu báo thu
        public static long STATUS_VT_DAGUIPHIEUBAOTHU = 16L;
        // Chờ phân công
        public static long STATUS_CHOPHANCONG = 17L;
        // Từ chối phê duyệt
        public static long STATUS_LD_TUCHOIDUYET = 20L;
        // Chuyên viên thẩm định đat
        public static long STATUS_CV_THAMDINHDAT = 22L;
        // Chuyên viên thẩm định không đạt
        public static long STATUS_CV_THAMDINHKHONGDAT = 18L;
        // Chuyên viên thẩm định yêu cầu bổ sung
        public static long STATUS_CV_THAMDINHYCSDBS = 19L;
        // Đã từ chối xác nhận phí
        public static long STATUS_KT_TUCHOIXACNHANPHI = 21L;
    }
}

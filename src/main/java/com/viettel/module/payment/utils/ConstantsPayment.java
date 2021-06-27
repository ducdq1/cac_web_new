/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.module.payment.utils;

/**
 *
 * @author hoang_000
 */
public final class ConstantsPayment {
     public interface PAYMENT {
        public static Long PAY_NEW = 1L; //Moi tao
        public static Long PAY_ALREADY = 2L;//Da thanh toan
        public static Long PAY_CONFIRMED = 3L;//Da xac nhan
        public static Long PAY_REJECTED = 4L;//Da xac nhan tu choi
        public static Long FEE_PAYMENT_TYPE_CODE_KEYPAY = 1L;
        public static Long FEE_PAYMENT_TYPE_CODE_CHUYENKHOAN = 2L;
        public static Long FEE_PAYMENT_TYPE_CODE_TIENMAT = 3L;
        public static int FEE_PAYMENT_TYPE_MOT_HO_SO = 1;
        public static int FEE_PAYMENT_TYPE_NHIEU_HO_SO = 2;
        public static int PAYMENT_TAO_MOI = 1;
        public static int PAYMENT_CHINH_SUA = 2;
       public static Long CATEGORY_TYPE_PAYMENT=99L;
     }
}

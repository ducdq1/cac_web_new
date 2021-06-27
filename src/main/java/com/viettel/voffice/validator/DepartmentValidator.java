package com.viettel.voffice.validator;



import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class DepartmentValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {
        //all the bean properties
        Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

        //first let's check the passwords match
        //validatePasswords(ctx, (String)beanProps.get("password").getValue(), (String)ctx.getValidatorArg("retypedPassword"));
        //validateAge(ctx, (Integer)beanProps.get("age").getValue());
        //validateWeight(ctx, (Double)beanProps.get("weight").getValue());
        validateEmail(ctx, (String) beanProps.get("email").getValue());
        //validateCaptcha(ctx, (String)ctx.getValidatorArg("captcha"), (String)ctx.getValidatorArg("captchaInput"));
        validateDeptCode(ctx, (String) beanProps.get("deptCode").getValue());
        validateDeptName(ctx, (String) beanProps.get("deptName").getValue());
    }

    private void validatePasswords(ValidationContext ctx, String password, String retype) {
        if (password == null || retype == null || (!password.equals(retype))) {
            this.addInvalidMessage(ctx, "password", "Your passwords do not match!");
        }
    }

    private void validateAge(ValidationContext ctx, int age) {
        if (age <= 0) {
            this.addInvalidMessage(ctx, "age", "Your age should be > 0!");
        }
    }

    private void validateWeight(ValidationContext ctx, double weight) {
        if (weight <= 0) {
            this.addInvalidMessage(ctx, "weight", "Your weight should be > 0!");
        }
    }

    private void validateEmail(ValidationContext ctx, String email) {
        if (email != null) {
            if (!email.matches(".+@.+\\.[a-z]+")) {
                this.addInvalidMessage(ctx, "email", "Email sai định dạng!");
            }
        }
    }

    private void validateCaptcha(ValidationContext ctx, String captcha, String captchaInput) {
        if (captchaInput == null || !captcha.equals(captchaInput)) {
            this.addInvalidMessage(ctx, "captcha", "The captcha doesn't match!");
        }
    }

    private void validateDeptCode(ValidationContext ctx, String deptCodeInput) {
        if (deptCodeInput == null || deptCodeInput == "") {
            this.addInvalidMessage(ctx, "deptCode", "Bạn phải nhập mã phòng ban!");
        }
    }

    private void validateDeptName(ValidationContext ctx, String deptNameInput) {
        if (deptNameInput == null || deptNameInput == "") {
            this.addInvalidMessage(ctx, "deptName", "Bạn phải nhập tên phòng ban!");
        }
    }
}

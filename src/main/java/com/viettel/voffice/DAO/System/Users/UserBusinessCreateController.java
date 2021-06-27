/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAO.System.Users;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.google.common.base.Objects;
import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.core.sys.BO.Category;
import com.viettel.core.sys.DAO.CategoryDAOHE;
import com.viettel.core.sys.DAO.RegisterDAOHE;
import com.viettel.core.user.BO.RoleUserDept;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.RoleUserDeptDAOHE;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.module.phamarcy.utils.EncryptionUtil;
import com.viettel.utils.Constants;
import com.viettel.utils.Constants_Cos;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Business;
import com.viettel.voffice.BO.Register;
import com.viettel.voffice.BO.Document.Attachs;
import com.viettel.voffice.DAOHE.AttachDAOHE;
import com.viettel.voffice.DAOHE.BusinessDAOHE;
import com.viettel.ws.SendEmailSms;

/**
 *
 * @author HaVM2
 */
public class UserBusinessCreateController extends BaseComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8347010487262064139L;
	@Wire
	Window createUserBusiness;
	Window parentWindow;
	@Wire
	private Image avatar;
	private Long id;
	private Register register;
	private Business business;
	// TrungMHV add: 03/02/2016
	private Attachs attachNoCert;
	private Attachs attachNoCertVNOffice;
	private Attachs attachNoCertInMedicines;

	private String sTinh, sHuyen, sXa, sLoaiHinh, sChucVu;

	private String password;
	private Business bus;
	private RoleUserDept roleUserDept;
	private Users user;
	private boolean denyApproval;
	@Wire
	private Label lbTopWarning;
	// nhatnt4 add 13/04/2016
	@Wire
	private Label lbAccountLogin,lbPassWord;

	@SuppressWarnings("unchecked")
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent, ComponentInfo compInfo) {
		Map<String, Object> arguments = (Map<String, Object>) Executions.getCurrent().getArg();
		id = (Long) arguments.get("id");
		RegisterDAOHE obj = new RegisterDAOHE();
		register = obj.findViewByFileId(id);

		parentWindow = (Window) arguments.get("parentWindow");
		CategoryDAOHE cdhe = new CategoryDAOHE();
		if (register.getBusinessProvince() != null)
			sTinh = cdhe.LayTruongPlace_Name(Long.parseLong(register.getBusinessProvince()));
		if (register.getBusinessDistrict() != null)
			sHuyen = cdhe.LayTruongPlace_Name(Long.parseLong(register.getBusinessDistrict()));
		if (register.getBusinessTown() != null)
			sXa = cdhe.LayTruongPlace_Name(Long.parseLong(register.getBusinessTown()));
		sLoaiHinh = register.getBusinessTypeName();
		sChucVu = register.getPosName();

		// Lay cac tap tin dinh kem gan vao attachNoCert, attachNoCertVNOffice,
		// attachNoCertInMedicines
		getAttachs(register);

		denyApproval = checkDuplicateByEmailManage(register);

		business = getBusinessFromRegister(id);

		return super.doBeforeCompose(page, parent, compInfo);
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String warning = denyApproval ? getLabelRt("register_warning_approval") : "";
		if (denyApproval) {
			lbTopWarning.setVisible(true);
			lbTopWarning.setValue(warning);
		} else {
			lbTopWarning.setVisible(false);
		}
		setLabelAccountLogin(register);
		lbPassWord.setValue("******");
	}

	/**
	 * ham set value cho label tai khoan dang nhap
	 * 
	 * @author nhatnt4
	 * @param register
	 *            13.4.2016
	 */
	private void setLabelAccountLogin(Register register) {
		if (register != null) {
			if (register.getStatus() == Constants.Status.UPDATEACCEPT
					|| register.getStatus() == Constants.Status.UPDATEREJECT
					|| register.getStatus() == Constants.Status.UPDATEPENDING) {
				RegisterDAOHE obj = new RegisterDAOHE();
				String code = null;
				int type = 0;
				if (register.getBusinessTaxCode() != null) {
					code = register.getBusinessTaxCode();
					type = Constants.TypeRegister.TYPE_TAXCODE;
				} else if (register.getNumberLicensesOfficesVn() != null) {
					code = register.getNumberLicensesOfficesVn();
					type = Constants.TypeRegister.TYPE_LICENSES_OFFICES;
				} else if (register.getNumberLicensesInMedicines() != null) {
					code = register.getNumberLicensesInMedicines();
					type = Constants.TypeRegister.TYPE_LICENSES_MEDICINES;
				}
				Register registerActive = obj.findViewByFileIdStatusActive(code, type);
				if (registerActive != null) {
					lbAccountLogin
							.setValue(registerActive.getManageEmail() != null ? registerActive.getManageEmail() : "");
				}
			} else {
				lbAccountLogin.setValue(register.getManageEmail() != null ? register.getManageEmail() : "");
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #btnPheDuyet")
	public void onClickPheDuyet() {
		String message = String.format(Constants.Notification.APROVE_CONFIRM, Constants.DOCUMENT_TYPE_NAME.ACCOUNT);

		Messagebox.show(message, "Xác nhận", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event event) {
						if (null != event.getName()) {
							switch (event.getName()) {
							case Messagebox.ON_OK:
								createOrUpdateAccount();
								break;
							case Messagebox.ON_CANCEL:
								break;
							}
						}
					}
				});

	}

	/**
	 * Kiem tra su ton tai cua account co user name trung voi emailManage can
	 * phe duỵet
	 * 
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 24, 2016
	 * @updatedDate: Feb 24, 2016
	 * @return:boolean
	 */
	private boolean checkDuplicateByEmailManage(Register register) {
		boolean result = false;
		UserDAOHE userDAO = new UserDAOHE();
		Users user = null;
		// Kiem tra su ton tai account trong User Table
		if (Constants.Status.INACTIVE == register.getStatus()) {
			user = userDAO.getUserByName(register.getManageEmail());
		}
		if (user != null) {
			result = true;
		}

		return result;
	}

	/**
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 24, 2016
	 * @updatedDate: Feb 24, 2016
	 * @return:void
	 */

	private void createOrUpdateAccount() {
		RegisterDAOHE registerDAOHE = new RegisterDAOHE();
		Register reg = registerDAOHE.findViewByFileId(id);
		Long status = reg.getStatus();

		if (Constants.Status.INACTIVE == status) {
			// Tao account moi
			createAcount();
		} else if (Constants.Status.UPDATEPENDING == status) {
			// Cap nhat account
			updateAccount();
		}
	}

	/**
	 * Cap nhat lai thong tin doanh nghiep
	 * 
	 * @param:
	 * @createdDate: Feb 15, 2016
	 * @updatedDate: Feb 15, 2016
	 * @return:void
	 */
	private void updateAccount() {
		try {
			Business business = null;
			Users user;
			RegisterDAOHE regisDAO = new RegisterDAOHE();
			BusinessDAOHE busDAO = new BusinessDAOHE();
			UserDAOHE userDAO = new UserDAOHE();
			Register regis = regisDAO.findViewByFileId(id);
			Category cat = new CategoryDAOHE().findById(regis.getBusinessTypeId());
			if (cat != null && cat.getCode() != null
					&& cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES)) {
				// Get MST DN
				String businessTaxCode = regis.getBusinessTaxCode();
				business = busDAO.findByTaxCode(businessTaxCode);

			} else if (cat != null && cat.getCode() != null
					&& cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES)) {
				if (StringUtils.isNotNullNotEmptyNotWhiteSpace(regis.getNumberLicensesOfficesVn())) {
					// Co Giay phep thanh lap van phong dai dien tai Viet Nam
					String numberLicenseOfficeVn = regis.getNumberLicensesOfficesVn();
					String businessTaxCode = regis.getBusinessTaxCode();
					business = busDAO.findByTaxCodeAndLicenseOffice(businessTaxCode, numberLicenseOfficeVn);

				} else {
					String numberLicenseInMedicine = regis.getNumberLicensesInMedicines();
					business = busDAO.findByLicenseInMedicine(numberLicenseInMedicine);
				}
			}

			if (business != null) {
				business.setBusinessTypeId(regis.getBusinessTypeId());
				business.setBusinessTypeName(regis.getBusinessTypeName());
				business.setNumberCertificate(regis.getNumberCertificate());
				business.setBusinessProvince(regis.getBusinessProvince());
				business.setBusinessDistrict(regis.getBusinessDistrict());
				business.setBusinessTown(regis.getBusinessTown());
				business.setNumberLicensesInMedicines(regis.getNumberLicensesInMedicines());
				business.setNumberLicensesOfficesVn(regis.getNumberLicensesOfficesVn());
				business.setBusinessName(regis.getBusinessNameVi());
				business.setBusinessNameEng(regis.getBusinessNameEng());
				business.setBusinessNameAlias(regis.getBusinessNameAlias());
				business.setBusinessTaxCode(regis.getBusinessTaxCode());
				business.setBusinessLicense(regis.getBusinessLicense());
				business.setGoverningBody(regis.getGoverningBody());
				business.setBusinessAddress(regis.getBusinessAdd());
				business.setBusinessTelephone(regis.getBusinessTelephone());
				business.setManageEmail(regis.getManageEmail());
				business.setBusinessFax(regis.getBusinessFax());
				business.setBusinessWebsite(regis.getBusinessWebsite());
				business.setBusinessEstablishYear(regis.getBusinessEstablishYear());
				business.setOfficeVNName(regis.getOfficeVNName());
				business.setOfficeVNAddress(regis.getOfficeVNAddress());
				business.setOfficeVNPhoneNumber(regis.getOfficeVNPhoneNumber());

				// Cap nhat vao bang business
				busDAO.saveOrUpdate(business);

				// Cap nhat trang thai trong bang register
				regis.setStatus(Constants.Status.UPDATEACCEPT);
				regisDAO.saveOrUpdate(regis);

				// Lay user quan ly doanh nghiep nay
				user = userDAO.getUserInBusiness(business.getBusinessId());
				// Xu ly khi save thanh cong
				createUserBusiness.onClose();
				Events.sendEvent("onVisible", parentWindow, null);
				// Gui email thong bao
				if (user != null && user.getEmail() != null) {
					SendEmailSms ses = new SendEmailSms();
					String businessName = StringUtils.getValue(business.getBusinessName());
					String acceptContentEmail = getLabelRt("update_email_accept");

					acceptContentEmail = acceptContentEmail.replace("reg_Bussiness_Name", businessName);
					ses.sendEmailManual("Thong bao trang thai phe duyet tai khoan", user.getEmail(),
							acceptContentEmail);
				}
				showNotification("Phê duyệt thành công", Constants.Notification.INFO);

			}

		} catch (Exception ex) {
			LogUtils.addLog(ex);
			showNotification("Phê duyệt thất bại", Constants.Notification.ERROR);
		}

	}

	private void createAcount() {
		try {
			// Lưu tai khoan vao bang user
			// update trang thai bang Register

			RegisterDAOHE registerDAOHE = new RegisterDAOHE();
			Register reg = registerDAOHE.findViewByFileId(id);
			reg.setStatus(Constants.Status.ACTIVE);
			registerDAOHE.saveOrUpdate(reg);

			// tao bang bussiness
			BusinessDAOHE busDAOHE = new BusinessDAOHE();
			bus = new Business();
			bus.setBusinessName(reg.getBusinessNameVi());
			bus.setBusinessNameEng(reg.getBusinessNameEng());
			bus.setBusinessNameAlias(reg.getBusinessNameAlias());
			bus.setBusinessTaxCode(reg.getBusinessTaxCode());
			bus.setBusinessTypeName(reg.getBusinessTypeName());
			bus.setBusinessTypeId(reg.getBusinessTypeId());
			bus.setBusinessLicense(reg.getBusinessLicense());
			bus.setBusinessProvince(reg.getBusinessProvince());
			bus.setBusinessAddress(reg.getBusinessAdd());
			bus.setBusinessTelephone(reg.getBusinessTelephone());
			bus.setBusinessFax(reg.getBusinessFax());
			bus.setBusinessWebsite(reg.getBusinessWebsite());
			bus.setGoverningBody(reg.getGoverningBody());
			bus.setManageEmail(reg.getManageEmail());
			bus.setBusinessTown(reg.getBusinessTown());
			bus.setBusinessDistrict(reg.getBusinessDistrict());
			bus.setBusinessEstablishYear(reg.getBusinessEstablishYear());
			bus.setIsActive(Constants.Status.ACTIVE);
			bus.setOfficeVNName(reg.getOfficeVNName());
			bus.setOfficeVNAddress(reg.getOfficeVNAddress());
			bus.setOfficeVNPhoneNumber(reg.getOfficeVNPhoneNumber());
			bus.setNumberCertificate(reg.getNumberCertificate());
			bus.setNumberLicensesInMedicines(reg.getNumberLicensesInMedicines());
			bus.setNumberLicensesOfficesVn(reg.getNumberLicensesOfficesVn());
			busDAOHE.saveOrUpdate(bus);
			// Tao bang user
			UserDAOHE userDAOHE = new UserDAOHE();
			user = new Users();
			user.setUserType(Constants.USER_TYPE.ENTERPRISE_USER);
			user.setUserName(reg.getBusinessTaxCode());
			// chucvu
			user.setPosId(reg.getPosId());
			user.setPosName(reg.getPosName());
			// //set password mac dinh
			// Feb 23, 2016 - TrungMHV - Username tu email doanh nghiep - KH yeu
			// cau sua
			String userName = reg.getManageEmail();

			user.setUserName(userName);
			//byte[] salt = new byte[20];
			//new SecureRandom().nextBytes(salt);
			//user.setSalt(salt);
			user.setPassword(password.toString());

			user.setStatus(Constants.Status.ACTIVE);
			user.setFullName(reg.getUserFullName());
			user.setEmail(reg.getUserEmail());
			user.setTelephone(reg.getUserMobile());
			user.setBusinessId(bus.getBusinessId());
			user.setBusinessName(bus.getBusinessName());
			userDAOHE.saveOrUpdate(user);
			// //Tao bang role_user_dept
			RoleUserDeptDAOHE roleUserDeptDAOHE = new RoleUserDeptDAOHE();
			roleUserDept = new RoleUserDept();
			roleUserDept.setIsActive(1L);
			roleUserDept.setUserId(user.getUserId());
			roleUserDept.setRoleId(Constants_Cos.ROLE_ID.QLD_DN);
			roleUserDeptDAOHE.saveOrUpdate(roleUserDept);
			createUserBusiness.onClose();
			Events.sendEvent("onVisible", parentWindow, null);
			showNotification("Phê duyệt thành công", Constants.Notification.INFO);

			SendEmailSms ses = new SendEmailSms();
			StringBuffer msge = new StringBuffer("Kính gửi: Doanh nghiệp <b>" + reg.getBusinessNameVi() + "</b>");
			if (reg.getBusinessTaxCode() != null)
				msge.append(" - Có mã số thuế: <b>" + reg.getBusinessTaxCode() + "</b><br>");
			else
				msge.append("<br>");
			msge.append(" Tài khoản của quý khách đã được kích hoạt.<br>");
			msge.append(" Tài khoản đăng nhập: <b>" + user.getUserName() + "</b> - Mật khẩu mặc định: <b>"
					+ password.toString() + "</b><br>");
			msge.append("<br>");
			msge.append(" Trân trọng,<br>");
			msge.append(" Cục QLD");
			ses.sendEmailManual("Thong bao trang thai phe duyet tai khoan", user.getEmail(), msge.toString());
		} catch (Exception e) {
			LogUtils.addLog(e);
			showNotification("Phê duyệt không thành công", Constants.Notification.ERROR);
			RegisterDAOHE registerDAOHE = new RegisterDAOHE();
			Register reg = new RegisterDAOHE().findViewByFileId(id);
			reg.setStatus(Constants.Status.INACTIVE);
			registerDAOHE.saveOrUpdate(reg);
			if (bus != null && bus.getBusinessId() != null)
				new BusinessDAOHE().delete(bus);
			if (user != null && user.getUserId() != null)
				new UserDAOHE().delete(user);
			if (roleUserDept != null && roleUserDept.getRoleUserDeptId() != null)
				new RoleUserDeptDAOHE().delete(roleUserDept);
		} finally {
		}
	}

	@Listen("onClick = #btnTuChoi")
	public void onClickTuChoi() {
		HashMap<String, Object> arguments = new HashMap<>();
		arguments.put("parentWindow", createUserBusiness);
		arguments.put("crudMode", "CREATE");
		arguments.put("id", id);
		createWindow("windowcreateUserBusiness", "/Pages/admin/user/userdialogTuChoi.zul", arguments, Window.MODAL);
	}

	@Listen("onClose = #createUserBusiness")
	public void onClose() {
		createUserBusiness.onClose();
		Events.sendEvent("onVisible", parentWindow, null);
	}

	@Listen("onVisible = #createUserBusiness")
	public void onVisible() {
		Events.sendEvent("onVisible", parentWindow, null);
	}

	/**
	 * Check hien thi button phe duyet
	 * 
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 24, 2016
	 * @updatedDate: Feb 24, 2016
	 * @return:boolean
	 */
	public boolean checkVisibleApproveButton() {
		return (CheckVisibleButton() && !denyApproval);
	}

	public boolean CheckVisibleButton() {
		if (register.getStatus() == Constants.Status.ACTIVE || register.getStatus() == Constants.Status.DELETE
				|| register.getStatus() == Constants.Status.UPDATEACCEPT
				|| register.getStatus() == Constants.Status.UPDATEREJECT) {
			return false;
		}
		return true;
	}

	public Register getRegister() {
		return register;
	}

	public String getsTinh() {
		return sTinh;
	}

	public String getsHuyen() {
		return sHuyen;
	}

	public String getsXa() {
		return sXa;
	}

	public String getsLoaiHinh() {
		return sLoaiHinh;
	}

	public String getsChucVu() {
		return sChucVu;
	}

	public String getPassword() throws UnsupportedEncodingException {
		// password = PublicFunctionModel.RandomNumber();
		if (!register.getStatus().equals(Constants.Status.ACTIVE)) {
			// password = ResourceBundleUtil.getString("passworDefault",
			// "config");
			password = StringUtils.randomstring();
		}
		
		return password;
	}

	private boolean isRandomPassword(Register register) {
		boolean result = true;
		if (register.getStatus().equals(Constants.Status.UPDATEACCEPT)
				|| register.getStatus().equals(Constants.Status.UPDATEPENDING)
				|| register.getStatus().equals(Constants.Status.UPDATEREJECT)) {
			result = false;
		}

		return result;
	}

	/**
	 * Thuc hien download
	 * 
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 3, 2016
	 * @updatedDate: Feb 3, 2016
	 * @return:void
	 */

	@Listen("onClick=#onDownloadFileNoCertificate")
	public void onDownloadFileNoCertificate() throws FileNotFoundException {
		if (attachNoCert != null) {
			downloadAttach(attachNoCert.getAttachId());
		}
	}

	@Listen("onClick=#onDownloadFileLicensesEstablishedOffices")
	public void onDownloadFileLicensesEstablishedOffices() throws FileNotFoundException {
		if (attachNoCertVNOffice != null) {
			downloadAttach(attachNoCertVNOffice.getAttachId());
		}
	}

	@Listen("onClick=#onDownloadFileNoLicensesInMedicines")
	public void onDownloadFileNoLicensesInMedicines() throws FileNotFoundException {
		if (attachNoCertInMedicines != null) {
			downloadAttach(attachNoCertInMedicines.getAttachId());
		}
	}

	/**
	 * get attachment of account
	 * 
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 3, 2016
	 * @updatedDate: Feb 3, 2016
	 * @return:void
	 */
	private void getAttachs(Register register) {

		AttachDAOHE attachDAOHE = new AttachDAOHE();

		if (Constants.BUSINESS_TYPE.LOCAL_BUSINESSES.equals(getBussinessCode(register.getBusinessTypeId()))) {
			attachNoCert = attachDAOHE.getLastAttach(id, Constants.OBJECT_TYPE.REGISTER,
					Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE);
		} else if (Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES.equals(getBussinessCode(register.getBusinessTypeId()))) {
			attachNoCertVNOffice = attachDAOHE.getLastAttach(id, Constants.OBJECT_TYPE.REGISTER,
					Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN);

			attachNoCertInMedicines = attachDAOHE.getLastAttach(id, Constants.OBJECT_TYPE.REGISTER,
					Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES);
		}
	}

	/**
	 * Lay ten file dinh kem
	 * 
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 3, 2016
	 * @updatedDate: Feb 3, 2016
	 * @return:String
	 */
	public String getAttachNameNumberCertificate() {
		return getAttachName(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE);
	}

	public String getAttachLicenseOfficeVN() {
		return getAttachName(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN);
	}

	public String getAttachNameLicenseInMedicines() {
		return getAttachName(Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES);
	}

	private String getAttachName(Long bussinessTypeId) {
		String result = "";

		if (Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_CERTIFICATE == bussinessTypeId) {
			result = (attachNoCert != null ? attachNoCert.getAttachName() : "");
		} else if (Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_OFFICES_VN == bussinessTypeId) {
			result = (attachNoCertVNOffice != null ? attachNoCertVNOffice.getAttachName() : "");
		} else if (Constants.OBJECT_TYPE.REGISTER_ATTACHMENT_TYPE_LICENSES_IN_MEDICINES == bussinessTypeId) {
			result = (attachNoCertInMedicines != null ? attachNoCertInMedicines.getAttachName() : "");
		}
		return result;
	}

	private Business getBusinessFromRegister(Long idParam) {
		Business businessTemp = null;
		RegisterDAOHE regisDAO = new RegisterDAOHE();
		BusinessDAOHE busDAO = new BusinessDAOHE();
		Register regis = regisDAO.findViewByFileId(idParam);
		Category cat = new CategoryDAOHE().findById(regis.getBusinessTypeId());
		if (cat != null && cat.getCode() != null && cat.getCode().equals(Constants.BUSINESS_TYPE.LOCAL_BUSINESSES)) {
			// Get MST DN
			String businessTaxCode = regis.getBusinessTaxCode();
			businessTemp = busDAO.findByTaxCode(businessTaxCode);

		} else if (cat != null && cat.getCode() != null
				&& cat.getCode().equals(Constants.BUSINESS_TYPE.FOREIGN_BUSINESSES)) {
			if (StringUtils.isNotNullNotEmptyNotWhiteSpace(regis.getNumberLicensesOfficesVn())) {
				// Co Giay phep thanh lap van phong dai dien tai Viet Nam
				String numberLicenseOfficeVn = regis.getNumberLicensesOfficesVn();
				String businessTaxCode = regis.getBusinessTaxCode();
				businessTemp = busDAO.findByTaxCodeAndLicenseOffice(businessTaxCode, numberLicenseOfficeVn);

			} else {
				String numberLicenseInMedicine = regis.getNumberLicensesInMedicines();
				businessTemp = busDAO.findByLicenseInMedicine(numberLicenseInMedicine);
			}
		}
		return businessTemp;
	}

	public boolean compareObject(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (obj1 != null && obj2 != null) {
			if (Objects.equal(obj1, obj2)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public Business getBusiness() {
		return business;
	}

}

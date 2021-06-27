/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.utils.Constants;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Register;

/**
 *
 * @author Administrator
 */
public class RegisterDAOHE extends GenericDAOHibernate<Register, Long> {

	public RegisterDAOHE() {
		super(Register.class);
	}

	@Override
	public void saveOrUpdate(Register register) {
		if (register != null) {
			super.saveOrUpdate(register);
		}
		getSession().flush();
	}

	public Register findViewByFileId(Long registerId) {
		Query query = getSession().createQuery("select a from Register a where a.registerId = :registerId");
		query.setParameter("registerId", registerId);
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Register) result.get(0);
		}
	}

	public PagingListModel search(Register searchForm, int start, int take) {
		List listParam = new ArrayList();
		StringBuilder strBuf = new StringBuilder("select r from Register r where 1 = 1  ");
		StringBuilder strCountBuf = new StringBuilder("select count(r) from Register r where 1 = 1 ");
		StringBuilder hql = new StringBuilder();
		if (searchForm != null) {
			if (searchForm.getBusinessNameVi() != null && !searchForm.getBusinessNameVi().isEmpty()) {
				hql.append(" and lower(r.businessNameVi) like ? ");
				listParam.add(StringUtils.toLikeString(searchForm.getBusinessNameVi()));
			}
			if (searchForm.getBusinessTaxCode() != null && !searchForm.getBusinessTaxCode().isEmpty()) {
				hql.append(" and lower(r.businessTaxCode) like ? ");
				listParam.add(StringUtils.toLikeString(searchForm.getBusinessTaxCode()));
			}
			if (searchForm.getUserFullName() != null && !searchForm.getUserFullName().isEmpty()) {
				hql.append(" and lower(r.userFullName) like ? ");
				listParam.add(StringUtils.toLikeString(searchForm.getUserFullName()));
			}
			if (searchForm.getManageEmail() != null && !searchForm.getManageEmail().isEmpty()) {
				hql.append(" and lower(r.manageEmail) like ? ");
				listParam.add(StringUtils.toLikeString(searchForm.getManageEmail()));
			}
			if (searchForm.getStatus() != null && searchForm.getStatus() != -2L) {
				hql.append(" and lower(r.status) like ? ");
				listParam.add(searchForm.getStatus());
			}
		}
		hql.append(" order by r.dateCreate DESC, r.businessTaxCode ");
		strBuf.append(hql);
		strCountBuf.append(hql);

		Query query = session.createQuery(strBuf.toString());
		Query countQuery = session.createQuery(strCountBuf.toString());

		for (int i = 0; i < listParam.size(); i++) {
			query.setParameter(i, listParam.get(i));
			countQuery.setParameter(i, listParam.get(i));
		}

		query.setFirstResult(start);
		if (take < Integer.MAX_VALUE) {
			query.setMaxResults(take);
		}

		List lst = query.list();
		Long count = (Long) countQuery.uniqueResult();
		PagingListModel model = new PagingListModel(lst, count);
		return model;

	}

	public boolean checkBusinessTaxCode(String businessTaxCode) {
		String HQL = "SELECT COUNT(d) FROM Register d WHERE Status IN(0,1) AND businessTaxCode =:businessTaxCode";
		Query query = session.createQuery(HQL.toString());
		query.setParameter("businessTaxCode", businessTaxCode);
		Long count = (Long) query.list().get(0);
		if (count > 0)
			return true;
		return false;
	}

	public boolean checkDuplicateUser(String userName) throws NullPointerException {
		String hql = "select count(u) from Register u where lower(u.userName)=? and status != -1";
		Query query = session.createQuery(hql);
		query.setParameter(0, userName.toLowerCase());

		int count = ((Long) query.uniqueResult()).intValue();
		if (count > 0) {
			return false;
		}
		return true;
	}

	/**
	 * Thuc hien update register
	 * 
	 * @author: trungmhv
	 * @modifier trungmhv
	 * @param:
	 * @createdDate: Feb 18, 2016
	 * @updatedDate: Feb 18, 2016
	 * @return:int
	 */
	public int updateRegister(Register register) {
		StringBuffer strQuery = new StringBuffer(500);

		strQuery.append("Update Register ");
		strQuery.append("set	manageEmail = :manageEmail ");
		strQuery.append(", userFullName = :userFullName ");
		strQuery.append(", userTelephone = :userTelephone ");
		strQuery.append(", userMobile = :userMobile ");
		strQuery.append(", businessTypeId = :businessTypeId ");
		strQuery.append(", businessTypeName = :businessTypeName ");
		strQuery.append(", businessNameVi = :businessNameVi ");
		strQuery.append(", businessNameEng = :businessNameEng ");
		strQuery.append(", governingBody = :governingBody ");
		strQuery.append(", userType = :userType ");
		strQuery.append(", businessNameAlias = :businessNameAlias ");
		strQuery.append(", businessTaxCode = :businessTaxCode ");
		strQuery.append(", businessLicense = :businessLicense ");
		strQuery.append(", businessAdd = :businessAdd ");
		strQuery.append(", businessProvince = :businessProvince ");
		strQuery.append(", businessDistrict = :businessDistrict ");
		strQuery.append(", businessTown = :businessTown ");
		strQuery.append(", businessTelephone = :businessTelephone ");
		strQuery.append(", businessFax = :businessFax ");
		strQuery.append(", businessWebsite = :businessWebsite ");
		strQuery.append(", businessLawRep = :businessLawRep ");
		strQuery.append(", description = :description ");
		strQuery.append(", businessEstablishYear = :businessEstablishYear ");
		strQuery.append(", officeVNName = :officeVNName ");
		strQuery.append(", officeVNAddress = :officeVNAddress ");
		strQuery.append(", officeVNMobileNumber = :officeVNMobileNumber ");
		strQuery.append(", officeVNPhoneNumber = :officeVNPhoneNumber ");
		strQuery.append(", userEmail =:userEmail ");
		strQuery.append(", status = :status ");
		strQuery.append(", reason = :reason ");
		strQuery.append(", businessProvinceId = :businessProvinceId ");
		strQuery.append(", posId = :posId ");
		strQuery.append(", posName = :posName ");
		strQuery.append(", numberCertificate = :numberCertificate ");
		strQuery.append(", numberLicensesOfficesVn = :numberLicensesOfficesVn ");
		strQuery.append(", numberLicensesInMedicines = :numberLicensesInMedicines ");
		strQuery.append(", dateCreate = :dateCreate ");
		strQuery.append(" WHERE ");
		strQuery.append("	registerId = :registerId ");

		// Create query update
		Query query = session.createQuery(strQuery.toString());
		query.setParameter("manageEmail", register.getManageEmail());
		query.setParameter("userFullName", register.getUserFullName());
		query.setParameter("userTelephone", register.getUserTelephone());
		query.setParameter("userMobile", register.getUserMobile());
		query.setParameter("businessTypeId", register.getBusinessTypeId());
		query.setParameter("businessTypeName", register.getBusinessTypeName());
		query.setParameter("businessNameVi", register.getBusinessNameVi());
		query.setParameter("businessNameEng", register.getBusinessNameEng());
		query.setParameter("governingBody", register.getGoverningBody());
		query.setParameter("userType", register.getUserType());
		query.setParameter("businessNameAlias", register.getBusinessNameAlias());
		query.setParameter("businessTaxCode", register.getBusinessTaxCode());
		query.setParameter("businessLicense", register.getBusinessLicense());
		query.setParameter("businessAdd", register.getBusinessAdd());
		query.setParameter("businessProvince", register.getBusinessProvince());
		query.setParameter("businessDistrict", register.getBusinessDistrict());
		query.setParameter("businessTown", register.getBusinessTown());
		query.setParameter("businessTelephone", register.getBusinessTelephone());
		query.setParameter("businessFax", register.getBusinessFax());
		query.setParameter("businessWebsite", register.getBusinessWebsite());
		query.setParameter("businessLawRep", register.getBusinessLawRep());
		query.setParameter("description", register.getDescription());
		query.setParameter("businessEstablishYear", register.getBusinessEstablishYear());
		query.setParameter("officeVNName", register.getOfficeVNName());
		query.setParameter("officeVNAddress", register.getOfficeVNAddress());
		query.setParameter("officeVNMobileNumber", register.getOfficeVNMobileNumber());
		query.setParameter("officeVNPhoneNumber", register.getOfficeVNPhoneNumber());
		query.setParameter("userEmail", register.getUserEmail());
		query.setParameter("status", register.getStatus());
		query.setParameter("reason", register.getReason());
		query.setParameter("businessProvinceId", register.getBusinessProvinceId());
		query.setParameter("posId", register.getPosId());
		query.setParameter("posName", register.getPosName());
		query.setParameter("numberCertificate", register.getNumberCertificate());
		query.setParameter("numberLicensesOfficesVn", register.getNumberLicensesOfficesVn());
		query.setParameter("numberLicensesInMedicines", register.getNumberLicensesInMedicines());
		query.setParameter("dateCreate", register.getDateCreate());
		query.setParameter("registerId", register.getRegisterId());

		int result = query.executeUpdate();
		return result;
	}

	public long countUserByBusinessTaxCode(String businessTaxCode) {
		String HQL = "SELECT count(*) FROM Register r WHERE r.businessTaxCode = :businessTaxCode";
		Query query = session.createQuery(HQL.toString());
		query.setParameter("businessTaxCode", businessTaxCode);

		long countUser = (long) query.list().get(0);
		return countUser;
	}

	public long getStatusByBusinessTaxCode(String businessTaxCode) {
		String HQL = "SELECT r.status FROM Register r WHERE r.businessTaxCode = :businessTaxCode";
		Query query = session.createQuery(HQL.toString());
		query.setParameter("businessTaxCode", businessTaxCode);

		long status = (long) query.list().get(0);
		return status;
	}

	/**
	 * Update Register by Taxi code
	 * 
	 * @param register
	 * @return
	 */
	public int updateByTaxCode(Register register) {
		StringBuffer strQuery = new StringBuffer(500);

		strQuery.append("Update Register ");
		strQuery.append("set	manageEmail = :manageEmail ");
		strQuery.append(", userFullName = :userFullName ");
		strQuery.append(", userTelephone = :userTelephone ");
		strQuery.append(", userMobile = :userMobile ");
		strQuery.append(", businessTypeId = :businessTypeId ");
		strQuery.append(", businessTypeName = :businessTypeName ");
		strQuery.append(", businessNameVi = :businessNameVi ");
		strQuery.append(", businessNameEng = :businessNameEng ");
		strQuery.append(", governingBody = :governingBody ");
		strQuery.append(", userType = :userType ");
		strQuery.append(", businessNameAlias = :businessNameAlias ");
		// strQuery.append(", businessTaxCode = :businessTaxCode ");
		strQuery.append(", businessLicense = :businessLicense ");
		strQuery.append(", businessAdd = :businessAdd ");
		strQuery.append(", businessProvince = :businessProvince ");
		strQuery.append(", businessDistrict = :businessDistrict ");
		strQuery.append(", businessTown = :businessTown ");
		strQuery.append(", businessTelephone = :businessTelephone ");
		strQuery.append(", businessFax = :businessFax ");
		strQuery.append(", businessWebsite = :businessWebsite ");
		strQuery.append(", businessLawRep = :businessLawRep ");
		strQuery.append(", description = :description ");
		strQuery.append(", businessEstablishYear = :businessEstablishYear ");
		strQuery.append(", officeVNName = :officeVNName ");
		strQuery.append(", officeVNAddress = :officeVNAddress ");
		strQuery.append(", officeVNMobileNumber = :officeVNMobileNumber ");
		strQuery.append(", officeVNPhoneNumber = :officeVNPhoneNumber ");
		strQuery.append(", userEmail =:userEmail ");
		strQuery.append(", status = :status ");
		strQuery.append(", reason = :reason ");
		strQuery.append(", businessProvinceId = :businessProvinceId ");
		strQuery.append(", posId = :posId ");
		strQuery.append(", posName = :posName ");
		strQuery.append(", numberCertificate = :numberCertificate ");
		strQuery.append(", numberLicensesOfficesVn = :numberLicensesOfficesVn ");
		strQuery.append(", numberLicensesInMedicines = :numberLicensesInMedicines ");
		strQuery.append(", dateCreate = :dateCreate ");
		strQuery.append(" WHERE ");
		strQuery.append("	businessTaxCode = :businessTaxCode ");

		// Create query update
		Query query = session.createQuery(strQuery.toString());
		query.setParameter("manageEmail", register.getManageEmail());
		query.setParameter("userFullName", register.getUserFullName());
		query.setParameter("userTelephone", register.getUserTelephone());
		query.setParameter("userMobile", register.getUserMobile());
		query.setParameter("businessTypeId", register.getBusinessTypeId());
		query.setParameter("businessTypeName", register.getBusinessTypeName());
		query.setParameter("businessNameVi", register.getBusinessNameVi());
		query.setParameter("businessNameEng", register.getBusinessNameEng());
		query.setParameter("governingBody", register.getGoverningBody());
		query.setParameter("userType", register.getUserType());
		query.setParameter("businessNameAlias", register.getBusinessNameAlias());
		query.setParameter("businessTaxCode", register.getBusinessTaxCode());
		query.setParameter("businessLicense", register.getBusinessLicense());
		query.setParameter("businessAdd", register.getBusinessAdd());
		query.setParameter("businessProvince", register.getBusinessProvince());
		query.setParameter("businessDistrict", register.getBusinessDistrict());
		query.setParameter("businessTown", register.getBusinessTown());
		query.setParameter("businessTelephone", register.getBusinessTelephone());
		query.setParameter("businessFax", register.getBusinessFax());
		query.setParameter("businessWebsite", register.getBusinessWebsite());
		query.setParameter("businessLawRep", register.getBusinessLawRep());
		query.setParameter("description", register.getDescription());
		query.setParameter("businessEstablishYear", register.getBusinessEstablishYear());
		query.setParameter("officeVNName", register.getOfficeVNName());
		query.setParameter("officeVNAddress", register.getOfficeVNAddress());
		query.setParameter("officeVNMobileNumber", register.getOfficeVNMobileNumber());
		query.setParameter("officeVNPhoneNumber", register.getOfficeVNPhoneNumber());
		query.setParameter("userEmail", register.getUserEmail());
		query.setParameter("status", register.getStatus());
		query.setParameter("reason", register.getReason());
		query.setParameter("businessProvinceId", register.getBusinessProvinceId());
		query.setParameter("posId", register.getPosId());
		query.setParameter("posName", register.getPosName());
		query.setParameter("numberCertificate", register.getNumberCertificate());
		query.setParameter("numberLicensesOfficesVn", register.getNumberLicensesOfficesVn());
		query.setParameter("numberLicensesInMedicines", register.getNumberLicensesInMedicines());
		query.setParameter("dateCreate", register.getDateCreate());

		int result = query.executeUpdate();
		return result;
	}

	public Register findViewByUserEmail(String userEmail) {
		Query query = getSession().createQuery("select a from Register a where a.userEmail = :userEmail");
		query.setParameter("userEmail", userEmail);
		@SuppressWarnings("rawtypes")
		List result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return (Register) result.get(0);
		}
	}

	public long countUserByNumberLicensesInMedicines(String numberLicensesInMedicines) {
		String HQL = "SELECT count(*) FROM Register r WHERE r.numberLicensesInMedicines = :numberLicensesInMedicines";
		Query query = session.createQuery(HQL.toString());
		query.setParameter("numberLicensesInMedicines", numberLicensesInMedicines);

		long countUser = (long) query.list().get(0);
		return countUser;
	}

	public long getStatusByNumberLicensesInMedicines(String numberLicensesInMedicines) {
		String HQL = "SELECT r.status FROM Register r WHERE r.numberLicensesInMedicines = :numberLicensesInMedicines";
		Query query = session.createQuery(HQL.toString());
		query.setParameter("numberLicensesInMedicines", numberLicensesInMedicines);

		long status = (long) query.list().get(0);
		return status;
	}

	/**
	 * Update Register by Taxi code
	 * 
	 * @param register
	 * @return
	 */
	public int updateByNumberLicensesInMedicines(Register register) {
		StringBuffer strQuery = new StringBuffer(500);

		strQuery.append("Update Register ");
		strQuery.append("set	manageEmail = :manageEmail ");
		strQuery.append(", userFullName = :userFullName ");
		strQuery.append(", userTelephone = :userTelephone ");
		strQuery.append(", userMobile = :userMobile ");
		strQuery.append(", businessTypeId = :businessTypeId ");
		strQuery.append(", businessTypeName = :businessTypeName ");
		strQuery.append(", businessNameVi = :businessNameVi ");
		strQuery.append(", businessNameEng = :businessNameEng ");
		strQuery.append(", governingBody = :governingBody ");
		strQuery.append(", userType = :userType ");
		strQuery.append(", businessNameAlias = :businessNameAlias ");
		// strQuery.append(", businessTaxCode = :businessTaxCode ");
		strQuery.append(", businessLicense = :businessLicense ");
		strQuery.append(", businessAdd = :businessAdd ");
		strQuery.append(", businessProvince = :businessProvince ");
		strQuery.append(", businessDistrict = :businessDistrict ");
		strQuery.append(", businessTown = :businessTown ");
		strQuery.append(", businessTelephone = :businessTelephone ");
		strQuery.append(", businessFax = :businessFax ");
		strQuery.append(", businessWebsite = :businessWebsite ");
		strQuery.append(", businessLawRep = :businessLawRep ");
		strQuery.append(", description = :description ");
		strQuery.append(", businessEstablishYear = :businessEstablishYear ");
		strQuery.append(", officeVNName = :officeVNName ");
		strQuery.append(", officeVNAddress = :officeVNAddress ");
		strQuery.append(", officeVNMobileNumber = :officeVNMobileNumber ");
		strQuery.append(", officeVNPhoneNumber = :officeVNPhoneNumber ");
		strQuery.append(", userEmail =:userEmail ");
		strQuery.append(", status = :status ");
		strQuery.append(", reason = :reason ");
		strQuery.append(", businessProvinceId = :businessProvinceId ");
		strQuery.append(", posId = :posId ");
		strQuery.append(", posName = :posName ");
		strQuery.append(", numberCertificate = :numberCertificate ");
		strQuery.append(", numberLicensesOfficesVn = :numberLicensesOfficesVn ");
		// strQuery.append(", numberLicensesInMedicines =
		// :numberLicensesInMedicines ");
		strQuery.append(", dateCreate = :dateCreate ");
		strQuery.append(" WHERE ");
		strQuery.append("	numberLicensesInMedicines = :numberLicensesInMedicines ");

		// Create query update
		Query query = session.createQuery(strQuery.toString());
		query.setParameter("manageEmail", register.getManageEmail());
		query.setParameter("userFullName", register.getUserFullName());
		query.setParameter("userTelephone", register.getUserTelephone());
		query.setParameter("userMobile", register.getUserMobile());
		query.setParameter("businessTypeId", register.getBusinessTypeId());
		query.setParameter("businessTypeName", register.getBusinessTypeName());
		query.setParameter("businessNameVi", register.getBusinessNameVi());
		query.setParameter("businessNameEng", register.getBusinessNameEng());
		query.setParameter("governingBody", register.getGoverningBody());
		query.setParameter("userType", register.getUserType());
		query.setParameter("businessNameAlias", register.getBusinessNameAlias());
		// query.setParameter("businessTaxCode", register.getBusinessTaxCode());
		query.setParameter("businessLicense", register.getBusinessLicense());
		query.setParameter("businessAdd", register.getBusinessAdd());
		query.setParameter("businessProvince", register.getBusinessProvince());
		query.setParameter("businessDistrict", register.getBusinessDistrict());
		query.setParameter("businessTown", register.getBusinessTown());
		query.setParameter("businessTelephone", register.getBusinessTelephone());
		query.setParameter("businessFax", register.getBusinessFax());
		query.setParameter("businessWebsite", register.getBusinessWebsite());
		query.setParameter("businessLawRep", register.getBusinessLawRep());
		query.setParameter("description", register.getDescription());
		query.setParameter("businessEstablishYear", register.getBusinessEstablishYear());
		query.setParameter("officeVNName", register.getOfficeVNName());
		query.setParameter("officeVNAddress", register.getOfficeVNAddress());
		query.setParameter("officeVNMobileNumber", register.getOfficeVNMobileNumber());
		query.setParameter("officeVNPhoneNumber", register.getOfficeVNPhoneNumber());
		query.setParameter("userEmail", register.getUserEmail());
		query.setParameter("status", register.getStatus());
		query.setParameter("reason", register.getReason());
		query.setParameter("businessProvinceId", register.getBusinessProvinceId());
		query.setParameter("posId", register.getPosId());
		query.setParameter("posName", register.getPosName());
		query.setParameter("numberCertificate", register.getNumberCertificate());
		query.setParameter("numberLicensesOfficesVn", register.getNumberLicensesOfficesVn());
		query.setParameter("numberLicensesInMedicines", register.getNumberLicensesInMedicines());
		query.setParameter("dateCreate", register.getDateCreate());

		int result = query.executeUpdate();
		return result;
	}

	public List<Register> findAll(String businessTaxCode, String numberLicensesInMedicines, String manageEmail,
			Long status) {

		String strQuery = "FROM Register R WHERE (R.businessTaxCode = :businessTaxCode OR R.numberLicensesInMedicines = :numberLicensesInMedicines OR R.manageEmail = :manageEmail ) AND R.status = :status";
		Query query = getSession().createQuery(strQuery);
		query.setParameter("businessTaxCode", businessTaxCode);
		query.setParameter("numberLicensesInMedicines", numberLicensesInMedicines);
		query.setParameter("manageEmail", manageEmail);
		query.setParameter("status", status);
		@SuppressWarnings("unchecked")
		List<Register> result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return result;
		}
	}

	/**
	 * @author nhatnt4 ham get register da phe duyet
	 * @param registerId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Register findViewByFileIdStatusActive(String code, int type) {
		List result = null;
		switch (type) {
		case Constants.TypeRegister.TYPE_TAXCODE:
			Query query = getSession()
					.createQuery("select a from Register a where a.businessTaxCode = :taxCode and a.status = 1");
			query.setParameter("taxCode", code);
			result = query.list();
			break;
		case Constants.TypeRegister.TYPE_LICENSES_OFFICES:
			Query queryOffices = getSession().createQuery(
					"select a from Register a where a.numberLicensesOfficesVn = :taxCode and a.status = 1");
			queryOffices.setParameter("taxCode", code);
			result = queryOffices.list();
			break;
		case Constants.TypeRegister.TYPE_LICENSES_MEDICINES:
			Query queryMedicine = getSession().createQuery(
					"select a from Register a where a.numberLicensesInMedicines = :taxCode and a.status = 1");
			queryMedicine.setParameter("taxCode", code);
			result = queryMedicine.list();
			break;
		default:
			break;
		}

		if (result == null || result.isEmpty()) {
			return null;
		} else {
			return (Register) result.get(0);
		}

	}

	public Register findByTaxCodeWithActiveAndUpdateAccept(String taxCode) {
		Query query = getSession().createQuery(
				"SELECT a FROM Register a WHERE a.businessTaxCode = :taxCode and a.status =3 Order By registerId DESC");
		query.setParameter("taxCode", taxCode);
		List result = query.list();
		if (result != null && result.size() != 0) {
			return (Register) result.get(0);
		} else {
			Query query1 = getSession().createQuery(
					"SELECT a FROM Register a WHERE a.businessTaxCode = :taxCode and a.status =1 Order By registerId DESC");
			query1.setParameter("taxCode", taxCode);
			List result1 = query1.list();
			if (result1 != null && result1.size() != 0) {
				return (Register) result1.get(0);
			} else {
				return new Register();
			}
		}
	}

	public Register findByTaxCodeAndOfficeWithActiveAndUpdateAccept(String taxCode, String officeCertificate) {
		Query query = getSession().createQuery(
				"SELECT a FROM Register a WHERE a.businessTaxCode = :taxCode and a.numberLicensesOfficesVn =:numberLicensesOfficesVn and a.status = 3 Order By registerId DESC");
		query.setParameter("taxCode", taxCode);
		query.setParameter("numberLicensesOfficesVn", officeCertificate);
		List result = query.list();
		if (result != null && result.size() != 0) {
			return (Register) result.get(0);
		} else {
			Query query1 = getSession().createQuery(
					"SELECT b FROM Register b WHERE b.businessTaxCode = :taxCode and b.numberLicensesOfficesVn =:numberLicensesOfficesVn and b.status = 1 Order By registerId DESC");
			query1.setParameter("taxCode", taxCode);
			query1.setParameter("numberLicensesOfficesVn", officeCertificate);
			List result1 = query1.list();
			if (result1 != null && result1.size() != 0) {
				return (Register) result1.get(0);
			} else {
				return new Register();
			}
		}
	}

	public Register findByMedicineCerWithActiveAndUpdateAccept(String medicineCertificate) {
		Query query = getSession().createQuery(
				"SELECT a FROM Register a WHERE a.numberLicensesInMedicines = :medicineCertificate and a.status = 3 Order By registerId DESC");
		query.setParameter("medicineCertificate", medicineCertificate);
		List result = query.list();
		if (result != null && result.size() != 0) {
			return (Register) result.get(0);
		} else {

			Query query1 = getSession().createQuery(
					"SELECT b FROM Register b WHERE b.numberLicensesInMedicines = :medicineCertificate and b.status = 1 Order By registerId DESC");
			query1.setParameter("medicineCertificate", medicineCertificate);
			List result1 = query1.list();
			if (result1 != null && result1.size() != 0) {
				return (Register) result1.get(0);
			} else {
				return new Register();
			}
		}
	}

	public Register findByTaxCode(String taxCode) {
		Query query = getSession().createQuery(
				"SELECT a FROM Register a WHERE a.businessTaxCode = :taxCode and (a.status = 2 or a.status=-2) ");
		query.setParameter("taxCode", taxCode);
		List result = query.list();
		if (result != null && result.size() != 0) {
			return (Register) result.get(0);
		} else {
			return new Register();
		}
	}

	public Register findByTaxCodeAndOfficeCertificate(String taxCode, String officeCertificate) {
		Query query = getSession().createQuery(
				"SELECT a FROM Register a WHERE a.businessTaxCode = :taxCode and a.numberLicensesOfficesVn =:numberLicensesOfficesVn and (a.status = 2 or a.status=-2)");
		query.setParameter("taxCode", taxCode);
		query.setParameter("numberLicensesOfficesVn", officeCertificate);
		List result = query.list();
		if (result != null && result.size() != 0) {
			return (Register) result.get(0);
		} else {
			return new Register();
		}
	}

	public Register findByMedicineCertificate(String medicineCertificate) {
		Query query = getSession().createQuery(
				"SELECT a FROM Register a WHERE a.numberLicensesInMedicines = :medicineCertificate and (a.status = 2 or a.status=-2)");
		query.setParameter("medicineCertificate", medicineCertificate);
		List result = query.list();
		if (result != null && result.size() != 0) {
			return (Register) result.get(0);
		} else {
			return new Register();
		}
	}
}

package com.viettel.module.phamarcy.DAO.PhaMedicine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.module.phamarcy.BO.PhaMedicine;

public class PhaMedicineDAO extends GenericDAOHibernate<PhaMedicine, Long> {

	public PhaMedicineDAO() {
		super(PhaMedicine.class);
	}

	@Override
	public Long create(PhaMedicine o) {
		Long id = super.create(o);
		getSession().flush();
		return id;
	}

	@Override
	public void saveOrUpdate(PhaMedicine phaMedicine) {
		if (phaMedicine != null) {
			super.saveOrUpdate(phaMedicine);
		}

		getSession().flush();

	}

	/**
	 * lay danh sach ten thuoc cua ho so
	 * 
	 * @author ducdq1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getListPhamarcyName() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT DISTINCT name FROM PhaMedicine");
		Query query = session.createQuery(sqlBuilder.toString());
		List<String> lstPhamarcyName = query.list();
		if (lstPhamarcyName != null) {
			return lstPhamarcyName;
		}
		return new ArrayList<String>();

	}

	/**
	 * lay danh sach ten thuoc cua ho so
	 * 
	 * @author ducdq1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getListPhamarcyNameByFileId(Long fileId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT name FROM PhaMedicine Where fileId= ?");
		Query query = session.createQuery(sqlBuilder.toString()).setParameter(0, fileId);
		List<String> lstPhamarcyName = query.list();
		if (lstPhamarcyName != null) {
			StringBuilder str = new StringBuilder("   ");
			for (String string : lstPhamarcyName) {
				str.append(string + "; ");
			}
			return str.substring(0, str.toString().length() - 2).trim();
		}
		return "";

	}

	/**
	 * lay danh sach ten thuoc cua ho so
	 * 
	 * @author ducdq1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getListPhamarcyNameByPhaFileId(Long phaFileId) {
		StringBuilder lstStrPhamarcyName = new StringBuilder("");
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT name FROM PhaMedicine where fileId=?");
		Query query = session.createQuery(sqlBuilder.toString());
		query.setParameter(0, phaFileId);

		List<String> lstPhamarcyName = query.list();
		if (lstPhamarcyName != null) {
			int count = 0;
			int size = lstPhamarcyName.size();
			for (String string : lstPhamarcyName) {
				lstStrPhamarcyName.append(string);
				count++;
				if (count < size) {
					lstStrPhamarcyName.append(", ");
				}
			}
		}
		return lstStrPhamarcyName.toString();

	}

	@SuppressWarnings("unchecked")
	public List<String> getListPhamarcyManufactory() {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT DISTINCT manufacturer FROM PhaMedicine");
		Query query = session.createQuery(sqlBuilder.toString());
		List<String> lstPhamarcyManu = query.list();
		if (lstPhamarcyManu != null) {
			return lstPhamarcyManu;
		}
		return new ArrayList<String>();

	}

	@SuppressWarnings("unchecked")
	public List<PhaMedicine> findMedicinesByFileId(Long id) {
		Query query = getSession().getNamedQuery("PhaMedicine.findMedicinesByFileId");
		query.setParameter("fileId", id);
		List<PhaMedicine> result = query.list();
		if (result.isEmpty()) {
			return null;
		} else {
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PhaMedicine> copyMedicines(Long id) {
		List<PhaMedicine> listPhaMedicine = new ArrayList<PhaMedicine>();
		Query query = getSession().getNamedQuery("PhaMedicine.findMedicinesByFileId");
		query.setParameter("fileId", id);
		List<PhaMedicine> result = query.list();
		for (PhaMedicine phaMedicine : result) {
			PhaMedicine item = new PhaMedicine();
			item.setCreatedWhen(phaMedicine.getCreatedWhen());
			item.setCreatedWho(phaMedicine.getCreatedWho());
			item.setDangBaoChe(phaMedicine.getDangBaoChe());
			item.setFormalAndObject(phaMedicine.getFormalAndObject());
			item.setHamLuong(phaMedicine.getHamLuong());
			item.setHoatchat(phaMedicine.getHoatchat());
			item.setIsActive(phaMedicine.getIsActive());
			item.setManufacturer(phaMedicine.getManufacturer());
			item.setMedicineType(phaMedicine.getMedicineType());
			item.setModifiedWhen(phaMedicine.getModifiedWhen());
			item.setModifiedWho(phaMedicine.getModifiedWho());
			item.setName(phaMedicine.getName());
			item.setRegistrationNo(phaMedicine.getRegistrationNo());
			item.setTimes(phaMedicine.getTimes());
			item.setXuatXu(phaMedicine.getXuatXu());
			listPhaMedicine.add(item);
		}
		return listPhaMedicine;
	}
}

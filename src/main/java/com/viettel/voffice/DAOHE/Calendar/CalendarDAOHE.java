/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.voffice.DAOHE.Calendar;

import com.viettel.utils.Constants;
import com.viettel.utils.DateTimeUtils;
import com.viettel.utils.LogUtils;
import com.viettel.utils.StringUtils;
import com.viettel.voffice.BO.Calendar.Calendar;
import com.viettel.voffice.BO.Calendar.CalendarParticipants;
import com.viettel.voffice.BO.Calendar.CalendarResource;
import com.viettel.core.user.BO.Department;
import com.viettel.voffice.BO.Home.Notify;
import com.viettel.core.user.BO.Users;
import com.viettel.core.user.DAO.DepartmentDAOHE;
import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.user.DAO.UserDAOHE;
import com.viettel.voffice.model.Calendar.DateCalendar;
import java.util.List;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class CalendarDAOHE extends GenericDAOHibernate<Calendar, Long> {

	public CalendarDAOHE() {
		super(Calendar.class);
	}

	public void saveCalendar(Calendar calendar, List<CalendarParticipants> lstParticipants,
			List<CalendarResource> lstResources) {
		if (lstParticipants != null && lstParticipants.size() > 0) {
			StringBuilder manChief = new StringBuilder();
			StringBuilder manParticipants = new StringBuilder();
			StringBuilder manPrepare = new StringBuilder();
			for (CalendarParticipants par : lstParticipants) {
				if (par.getParticipantRole().equals(1l)) {
					if (par.getUserName() != null && par.getUserName().length() > 0) {
						manPrepare.append(par.getUserName()).append("(").append(par.getDeptName()).append(");");
					} else {
						manPrepare.append(par.getDeptName()).append(";");
					}
				} else if (par.getParticipantRole().equals(2l)) {
					if (par.getUserName() != null && par.getUserName().length() > 0) {
						manChief.append(par.getUserName()).append("(").append(par.getDeptName()).append(");");
					} else {
						manChief.append(par.getDeptName()).append(";");
					}

				} else {
					if (par.getUserName() != null && par.getUserName().length() > 0) {
						manParticipants.append(par.getUserName()).append("(").append(par.getDeptName()).append(");");
					} else {
						manParticipants.append(par.getDeptName()).append(";");
					}
				}
			}
			calendar.setManChief(manChief.toString());
			calendar.setManParticipant(manParticipants.toString());
			calendar.setManPrepare(manPrepare.toString());
		}
		boolean isCreate = true;
		if (calendar.getCalendarId() == null) {
			session.save(calendar);
			if (lstParticipants != null && lstParticipants.size() > 0) {
				for (CalendarParticipants par : lstParticipants) {
					par.setCalendarId(calendar.getCalendarId());
					session.save(par);
				}
			}
			if (lstResources != null && lstResources.size() > 0) {
				for (CalendarResource par : lstResources) {
					par.setCalendarId(calendar.getCalendarId());
					session.save(par);
				}
			}

		} else {
			isCreate = false;
			session.update(calendar);
			List<Long> lstNotDel = new ArrayList();
			if (lstParticipants != null && lstParticipants.size() > 0) {
				for (CalendarParticipants par : lstParticipants) {
					par.setCalendarId(calendar.getCalendarId());
					session.saveOrUpdate(par);
					lstNotDel.add(par.getParticipantId());
				}
			}
			String hql = "delete from CalendarParticipants cp where cp.calendarId = :calendarId and cp.participantId not in (:lstNotDel)";
			if (lstNotDel.isEmpty()) {
				hql = "delete from CalendarParticipants cp where cp.calendarId = :calendarId";
			}
			Query query = session.createQuery(hql);
			query.setParameter("calendarId", calendar.getCalendarId());
			if (!lstNotDel.isEmpty()) {
				query.setParameterList("lstNotDel", lstNotDel);
			}
			query.executeUpdate();

			lstNotDel.clear();
			if (lstResources != null && lstResources.size() > 0) {
				for (CalendarResource par : lstResources) {
					par.setCalendarId(calendar.getCalendarId());
					session.saveOrUpdate(par);
					lstNotDel.add(par.getResourceId());
				}
			}
			hql = "delete from CalendarResource cp where cp.calendarId = :calendarId and cp.resourceId not in (:lstNotDel)";
			if (lstNotDel.isEmpty()) {
				hql = "delete from CalendarResource cp where cp.calendarId = :calendarId";
			}
			query = session.createQuery(hql);
			query.setParameter("calendarId", calendar.getCalendarId());
			if (!lstNotDel.isEmpty()) {
				query.setParameterList("lstNotDel", lstNotDel);
			}
			query.executeUpdate();

		}

		//
		// Send notify
		//
		UserDAOHE udhe = new UserDAOHE();
		Users u = udhe.findById(calendar.getCreateUserId());
		StringBuilder notifyContent = new StringBuilder();
		if (isCreate) {
			notifyContent.append("Mời tham gia lịch :").append(calendar.getTitle()).append("\r\n");
		} else {
			notifyContent.append("Chỉnh sửa thông tin lịch :").append(calendar.getTitle()).append("\r\n");
		}
		if (calendar.getLocationName() != null && calendar.getLocationName().trim().length() > 0) {
			notifyContent.append("Tại :" + calendar.getLocationName()).append("\r\n");
		}

		notifyContent.append("Thời gian từ :").append(DateTimeUtils.convertDateTimeToString(calendar.getTimeStart()))
				.append("\r\n");
		notifyContent.append("Đến :").append(DateTimeUtils.convertDateTimeToString(calendar.getTimeEnd()))
				.append("\r\n");
		Notify notify = new Notify();
		notify.setContent(notifyContent.toString());
		notify.setSendUserId(calendar.getCreateUserId());
		notify.setSendUserName(u.getFullName());
		notify.setObjectId(calendar.getCalendarId());
		notify.setObjectType(Constants.OBJECT_TYPE.CALENDAR);
		notify.setSendTime(new Date());
		StringBuilder multiUser = new StringBuilder(";");
		if (lstParticipants != null && lstParticipants.size() > 0) {
			for (CalendarParticipants par : lstParticipants) {
				if (par.getUserId() != null) {
					multiUser.append(par.getUserId()).append(";");
				}
			}
		}
		notify.setMultiUser(multiUser.toString());
		session.save(notify);

	}

	public void deptResponseCalendar(Long deptId, Long calendarId, Long status) {
		//
		// Cap nhat trang thai cua cac thanh vien tham gia
		//
		String hql = "update CalendarParticipants cp set cp.acceptStatus = ? where cp.calendarId = ? and cp.deptId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, status);
		query.setParameter(1, calendarId);
		query.setParameter(2, deptId);
		query.executeUpdate();
		//
		// cap nhat trang thai cua cac tai nguyen cua don vi
		//
		hql = "update CalendarResource cp set cp.acceptStatus = ? where cp.calendarId = ? and cp.deptId = ? ";
		query = session.createQuery(hql);
		query.setParameter(0, status);
		query.setParameter(1, calendarId);
		query.setParameter(2, deptId);
		query.executeUpdate();

		Calendar calendar = findById(calendarId);
		CalendarParticipants par = getParticipantOfDept(calendarId, deptId);
		if (par != null && par.getParticipantRole().equals(Constants.CALENDAR_PARTICIPANT_TYPE.APPROVE)) {
			calendar.setStatus(status);
			update(calendar);
		} else {
			hql = "select count(c) from CalendarParticipants c where c.calendarId = ? and c.acceptStatus <> ?";
			query = session.createQuery(hql);
			query.setParameter(0, calendarId);
			query.setParameter(1, status);
			Long countPar = (Long) query.uniqueResult();

			hql = "select count(c) from CalendarResource c where c.calendarId = ? and c.acceptStatus <> ?";
			query = session.createQuery(hql);
			query.setParameter(0, calendarId);
			query.setParameter(1, status);
			Long countRes = (Long) query.uniqueResult();

			if (countPar + countRes == 0l) {
				calendar.setStatus(status);
			} else {
				calendar.setStatus(Constants.CALENDAR_STATUS.NEW_CREATE);
			}
			update(calendar);
		}

		DepartmentDAOHE ddhe = new DepartmentDAOHE();
		Notify notify = new Notify();
		notify.setContent("Trả lời lịch");
		notify.setUserId(deptId);
		Department d = ddhe.findById(deptId);
		notify.setSendUserName(d.getDeptName());
		notify.setUserId(calendar.getCreateUserId());
		StringBuilder content = new StringBuilder();
		if (Objects.equals(status, Constants.CALENDAR_STATUS.APPROVE_ACCEPT)) {
			content.append("Đồng ý tham gia lịch :");
		} else {
			content.append("Từ chối tham gia lịch :");
		}
		content.append(calendar.getTitle());
		notify.setContent(content.toString());
		notify.setObjectId(calendarId);
		notify.setObjectType(Constants.OBJECT_TYPE.CALENDAR);
		notify.setSendTime(new Date());
		session.save(notify);

	}

	public void userResponseCalendar(Long userId, Long calendarId, Long status) {
		String hql = "update CalendarParticipants cp set cp.acceptStatus = ? where cp.calendarId = ? and cp.userId = ? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, status);
		query.setParameter(1, calendarId);
		query.setParameter(2, userId);
		query.executeUpdate();

		Calendar calendar = findById(calendarId);
		CalendarParticipants par = getParticipantOfUser(calendarId, userId);
		if (par != null && par.getParticipantRole().equals(Constants.CALENDAR_PARTICIPANT_TYPE.APPROVE)) {
			calendar.setStatus(status);
			update(calendar);
		} else {
			hql = "select count(c) from CalendarParticipants c where c.calendarId = ? and c.acceptStatus <> ?";
			query = session.createQuery(hql);
			query.setParameter(0, calendarId);
			query.setParameter(1, status);
			Long countPar = (Long) query.uniqueResult();

			hql = "select count(c) from CalendarResource c where c.calendarId = ? and c.acceptStatus <> ?";
			query = session.createQuery(hql);
			query.setParameter(0, calendarId);
			query.setParameter(1, status);
			Long countRes = (Long) query.uniqueResult();

			if (countPar + countRes == 0l) {
				calendar.setStatus(status);
			} else {
				calendar.setStatus(Constants.CALENDAR_STATUS.NEW_CREATE);
			}
			update(calendar);

		}

		UserDAOHE udhe = new UserDAOHE();
		Notify notify = new Notify();
		notify.setContent("Trả lời lịch");
		notify.setSendUserId(userId);
		Users u = udhe.findById(userId);
		notify.setSendUserName(u.getFullName());
		notify.setUserId(calendar.getCreateUserId());
		StringBuilder content = new StringBuilder();
		if (Objects.equals(status, Constants.CALENDAR_STATUS.APPROVE_ACCEPT)) {
			content.append("Đồng ý tham gia lịch :");
		} else {
			content.append("Từ chối tham gia lịch :");
		}
		content.append(calendar.getTitle());
		notify.setContent(content.toString());
		notify.setObjectId(calendarId);
		notify.setObjectType(Constants.OBJECT_TYPE.CALENDAR);
		notify.setSendTime(new Date());
		session.save(notify);
	}

	public List searchCalendar(Calendar searchForm, Long deptId, List lstUsers, List lstResources, List lstDept) {
		HashMap<String, Object> lstParam = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("select c from Calendar c where 1 = 1 ");
		if (searchForm != null) {
			if (searchForm.getCreateUserId() != null) {
				hql.append(
						" and (c.createUserId = :userId or c.calendarId in (select cp.calendarId from CalendarParticipants cp where cp.userId = :userId))");
				lstParam.put("userId", searchForm.getCreateUserId());
			}
			if (searchForm.getTimeStart() != null) {
				searchForm.setTimeStart(DateTimeUtils.setStartTimeOfDate(searchForm.getTimeStart()));
				hql.append(" and c.timeEnd >= :timeStart");
				lstParam.put("timeStart", searchForm.getTimeStart());
			}

			Date endTime = DateTimeUtils.addOneDay(searchForm.getTimeEnd());
			if (endTime != null) {
				hql.append(" and c.timeStart <= :timeEnd");
				// hql += " and c.startTime <= ?";
				lstParam.put("timeEnd", endTime);
			}

			if (searchForm.getStatus() != null && searchForm.getStatus() > -1l) {
				if (searchForm.getStatus().equals(Constants.CALENDAR_STATUS.NEW_CREATE)) {
					hql.append(" and (c.status is null or c.status = :status) ");
					lstParam.put("status", searchForm.getStatus());

				} else {
					hql.append(" and c.status = :status");
					lstParam.put("status", searchForm.getStatus());
				}
			} else {
				hql.append(" and c.status > -1");
			}

			if (searchForm.getTitle() != null && searchForm.getTitle().trim().length() > 0) {
				hql.append(" and lower(c.title) like :title ESCAPE '/' ");
				lstParam.put("title", StringUtils.toLikeString(searchForm.getTitle().trim()));
			}
			if (searchForm.getLocationName() != null && searchForm.getLocationName().trim().length() > 0) {
				hql.append(" and lower(c.locationName) like :locationName ESCAPE '/' ");
				lstParam.put("locationName", StringUtils.toLikeString(searchForm.getLocationName().trim()));
			}

			if (searchForm.getSearchText() != null && searchForm.getSearchText().trim().length() > 0) {
				hql.append(" and (lower(c.title) like  :searchText ESCAPE '/' "
						+ "or lower(c.locationName) like :searchText ESCAPE '/' "
						+ "or lower(c.manChief) like :searchText ESCAPE '/' "
						+ "or lower(c.manParticipant) like :searchText ESCAPE '/' "
						+ "or lower(c.manPrepare) like :searchText ESCAPE '/')  ");
				lstParam.put("searchText", StringUtils.toLikeString(searchForm.getSearchText().trim()));
				// lstParam.add(StringUtils.toLikeString(searchForm.getSearchText().trim()));
				// lstParam.add(StringUtils.toLikeString(searchForm.getSearchText().trim()));
				// lstParam.add(StringUtils.toLikeString(searchForm.getSearchText().trim()));
				// lstParam.add(StringUtils.toLikeString(searchForm.getSearchText().trim()));
			}
		}

		if (deptId != null) {
			hql.append(
					" and ( c.calendarId in (select cp.calendarId from CalendarParticipants cp where cp.deptId = :deptId)");
			hql.append(
					" or c.calendarId in (select cr.calendarId from CalendarResource cr where cr.deptId = :deptId) ) ");
			lstParam.put("deptId", deptId);
			// lstParam.add(deptId);
		}

		StringBuilder hqlList = new StringBuilder("");

		if (lstUsers != null && lstUsers.size() > 0) {
			hqlList.append(
					"c.calendarId in (select cp.calendarId from CalendarParticipants cp where cp.userId in (:lstUsers))");
			lstParam.put("lstUsers", lstUsers);
		}

		if (lstResources != null && lstResources.size() > 0) {
			if (hqlList.toString().length() > 0) {
				hqlList.append(" or ");
			}
			hqlList.append(
					" c.calendarId in (select cr.calendarId from CalendarResource cr where cr.resourceId in (:lstResources) )");
			lstParam.put("lstResources", lstResources);

		}

		if (lstDept != null && lstDept.size() > 0) {
			if (hqlList.toString().length() > 0) {
				hqlList.append(" or ");
			}
			hqlList.append(
					" c.calendarId in (select cpd.calendarId from CalendarParticipants cpd where cpd.deptId in (:lstDepts) )  ");
			lstParam.put("lstDepts", lstDept);
		}

		if (hqlList.toString().length() > 0) {
			hql.append(" and (").append(hqlList).append(")");
		}

		hql.append(" order by c.timeStart, c.timeEnd");

		Query query = getSession().createQuery(hql.toString());

		if (lstParam.size() > 0) {
			for (Object keyObj : lstParam.keySet()) {
				String key = (String) keyObj;
				Object param = lstParam.get(key);
				if (param.getClass() != ArrayList.class) {
					query.setParameter(key, lstParam.get(key));
				} else {
					query.setParameterList(key, (List) param);
				}
			}
		}

		List lstCalendar = query.list();
		List lstDateCalendar = toDateCalendarList(searchForm.getTimeStart(), searchForm.getTimeEnd(), lstCalendar);
		return lstDateCalendar;
	}

	public List checkCalendarUser(Long calendarId, List lstUsers, Date timeStart, Date timeEnd) {
		HashMap<String, Object> lstParam = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder(
				"select distinct(cp.userId) from CalendarParticipants cp,Calendar c where cp.calendarId = c.calendarId and cp.calendarId <> :calendarId and cp.userId is not null ");
		lstParam.put("calendarId", calendarId);

		hql.append(" and c.timeEnd >= :timeStart");
		lstParam.put("timeStart", timeStart);

		hql.append(" and c.timeStart <= :timeEnd");
		lstParam.put("timeEnd", timeEnd);

		hql.append(" and c.status = :status");
		lstParam.put("status", Constants.CALENDAR_STATUS.APPROVE_ACCEPT);

		if (lstUsers != null && lstUsers.size() > 0) {
			hql.append(" and cp.userId in (:lstUsers))");
			lstParam.put("lstUsers", lstUsers);
		}

		Query query = getSession().createQuery(hql.toString());

		if (lstParam.size() > 0) {
			for (Object keyObj : lstParam.keySet()) {
				String key = (String) keyObj;
				Object param = lstParam.get(key);
				if (param.getClass() != ArrayList.class) {
					query.setParameter(key, lstParam.get(key));
				} else {
					query.setParameterList(key, (List) param);
				}
			}
		}

		List lstPar = query.list();
		return lstPar;
	}

	public List checkCalendarResource(Long calendarId, List lstResources, Date timeStart, Date timeEnd) {
		HashMap<String, Object> lstParam = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder(
				"select distinct(cr.resourceId) from CalendarResource cr,Calendar c where cr.calendarId = c.calendarId and c.calendarId <> :calendarId ");
		lstParam.put("calendarId", calendarId);

		hql.append(" and c.timeEnd >= :timeStart");
		lstParam.put("timeStart", timeStart);

		hql.append(" and c.timeStart <= :timeEnd");
		lstParam.put("timeEnd", timeEnd);

		hql.append(" and c.status = :status");
		lstParam.put("status", Constants.CALENDAR_STATUS.APPROVE_ACCEPT);

		if (lstResources != null && lstResources.size() > 0) {
			hql.append(" and cr.resourceId in (:lstResources))");
			lstParam.put("lstResources", lstResources);
		}

		Query query = getSession().createQuery(hql.toString());

		if (lstParam.size() > 0) {
			for (Object keyObj : lstParam.keySet()) {
				String key = (String) keyObj;
				Object param = lstParam.get(key);
				if (param.getClass() != ArrayList.class) {
					query.setParameter(key, lstParam.get(key));
				} else {
					query.setParameterList(key, (List) param);
				}
			}
		}

		List lstPar = query.list();
		return lstPar;
	}

	@SuppressWarnings("empty-statement")
	public List<DateCalendar> toDateCalendarList(Date startDate, Date endDate, List<Calendar> lstCalendar) {
		List lstDate = new ArrayList();
		int i;
		do {
			DateCalendar date = new DateCalendar();
			date.setDate(startDate);
			date.setLstCalendar(new ArrayList());
			date.setText(getDateDayName(startDate.getDay()) + ", " + DateTimeUtils.convertDateToString(startDate));
			i = 0;
			while (i < lstCalendar.size()) {
				Date startTime = lstCalendar.get(i).getTimeStart();
				int iCompare = DateTimeUtils.compare2Date(startDate, startTime);
				if (iCompare == 0) {
					date.getLstCalendar().add(lstCalendar.get(i));
				} else if (iCompare > 0) {
					iCompare = DateTimeUtils.compare2Date(startDate, lstCalendar.get(i).getTimeEnd());
					if (iCompare <= 0) {
						date.getLstCalendar().add(lstCalendar.get(i));
					}
				}
				i++;
			}
			;
			lstDate.add(date);
			startDate = DateTimeUtils.addOneDay(startDate);
		} while (!startDate.after(endDate));
		return lstDate;
	}

	public String getDateDayName(int day) {
		String dayName;
		switch (day) {
		case 0:
			dayName = "Chủ nhật";
			break;
		case 1:
			dayName = "Thứ hai";
			break;
		case 2:
			dayName = "Thứ ba";
			break;
		case 3:
			dayName = "Thứ tư";
			break;
		case 4:
			dayName = "Thứ năm";
			break;
		case 5:
			dayName = "Thứ sáu";
			break;
		case 6:
			dayName = "Thứ bảy";
			break;
		default:
			dayName = "";
			break;
		}
		return dayName;
	}

	public List getCalendarParticipant(Long calendarId) {
		try {

			String strBuf = " from CalendarParticipants a where a.calendarId = ? ";

			Query query = session.createQuery(strBuf);
			query.setParameter(0, calendarId);
			List lstParticipants = query.list();
			return lstParticipants;
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}
	}

	public CalendarResource getResourceOfDept(Long calendarId, Long deptId) {
		String hql = " from CalendarResource a where a.calendarId = ? and a.deptId=? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, calendarId);
		query.setParameter(1, deptId);
		List lst = query.list();
		if (lst == null || lst.isEmpty()) {
			return null;
		} else {
			return (CalendarResource) lst.get(0);
		}
	}

	public CalendarParticipants getParticipantOfUser(Long calendarId, Long userId) {
		String hql = " from CalendarParticipants a where a.calendarId = ? and a.userId=? ";
		Query query = session.createQuery(hql);
		query.setParameter(0, calendarId);
		query.setParameter(1, userId);
		List lst = query.list();
		if (lst == null || lst.isEmpty()) {
			return null;
		} else {
			return (CalendarParticipants) lst.get(0);
		}
	}

	public CalendarParticipants getParticipantOfDept(Long calendarId, Long deptId) {
		String hql = " from CalendarParticipants a where a.calendarId = ? and a.deptId=? and a.userId is null ";
		Query query = session.createQuery(hql);
		query.setParameter(0, calendarId);
		query.setParameter(1, deptId);
		List lst = query.list();
		if (lst == null || lst.isEmpty()) {
			return null;
		} else {
			return (CalendarParticipants) lst.get(0);
		}
	}

	public List getCalendarResource(Long calendarId) {
		try {
			String strBuf = " from CalendarResource a where a.calendarId = ? ";

			Query query = session.createQuery(strBuf);
			query.setParameter(0, calendarId);
			List lstResource = query.list();
			return lstResource;
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}
	}

	public List getCalendarParticipantAll(Long calendarId, int start, int count) {
		try {

			String strBuf = " from CalendarParticipant a where a.calendarId = ? ";

			Query query = getSession().createQuery("SELECT count(*) " + strBuf);
			query.setParameter(0, calendarId);
			query = getSession().createQuery(strBuf);
			query.setParameter(0, calendarId);

			// query.setFirstResult(start);
			// query.setMaxResults(count);

			List lstCategory = query.list();
			return lstCategory;
		} catch (NullPointerException ex) {
			LogUtils.addLog(ex);
			return null;
		}
	}

	//
	// check duplicate
	// tra ve true = co trung
	// tra ve false khong trng
	//
	public boolean checkDuplicate(Calendar form) {
		boolean bReturn = false;
		List lstParam = new ArrayList();
		if (form.getLocationId() != null) {
			String hql = "select count(c) from Calendar c where c.locationId = ? ";
			lstParam.add(form.getLocationId());
			if (form.getCalendarId() != null) {
				hql += " and c.calendarId != ?";
				lstParam.add(form.getCalendarId());
			}
			if (form.getTimeStart() != null) {
				hql += " and ((c.startTime <= ? and c.endTime is not null and c.endTime >= ?)";
				lstParam.add(form.getTimeStart());
				lstParam.add(form.getTimeStart());
			}

			if (form.getTimeStart() != null) {
				hql += " or (c.startTime <= ? and c.endTime is not null and c.endTime >= ?) or (c.startTime >= ? and c.startTime <=?) or (c.endTime is not null and c.endTime >=? and c.endTime<=?))";
				lstParam.add(form.getTimeEnd());
				lstParam.add(form.getTimeEnd());
				lstParam.add(form.getTimeStart());
				lstParam.add(form.getTimeEnd());
				lstParam.add(form.getTimeStart());
				lstParam.add(form.getTimeEnd());

			} else {
				hql += ")";
			}

			Query query = getSession().createQuery(hql);
			for (int i = 0; i < lstParam.size(); i++) {
				query.setParameter(i, lstParam.get(i));
			}

			int count = ((Long) query.uniqueResult()).intValue();
			if (count > 0) {
				bReturn = true;
			}
		}
		return bReturn;
	}

	public int countCalendar(Long userId, Date startDate, Date endDate) {
		StringBuilder hql = new StringBuilder(
				"select count(c) from Calendar c where c.status>-1 and c.timeStart <= ? and c.timeEnd >= ?");
		hql.append(
				" and ( c.createUserId = ? or c.calendarId in (select cp.calendarId from CalendarParticipants cp where cp.userId = ?)) ");
		Query query = session.createQuery(hql.toString());
		query.setParameter(0, endDate);
		query.setParameter(1, startDate);
		query.setParameter(2, userId);
		query.setParameter(3, userId);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	public int countCalendarInWeek(Long userId) {
		Date startDate = DateTimeUtils.setStartTimeOfDate(new Date());
		Date endDate = new Date();
		int days = 8 - startDate.getDay();
		endDate = DateTimeUtils.addDay(endDate, days);
		endDate = DateTimeUtils.setStartTimeOfDate(endDate);

		return countCalendar(userId, startDate, endDate);

	}

	public int countCalendarInDay(Long userId) {
		Date startDate = DateTimeUtils.setStartTimeOfDate(new Date());
		Date endDate = new Date();
		endDate = DateTimeUtils.addDay(endDate, 1);
		endDate = DateTimeUtils.setStartTimeOfDate(endDate);

		return countCalendar(userId, startDate, endDate);
	}

	public int countCalendarInTomorow(Long userId) {
		Date startDate = DateTimeUtils.addDay(new Date(), 1);
		startDate = DateTimeUtils.setStartTimeOfDate(startDate);
		Date endDate = DateTimeUtils.addDay(startDate, 1);
		return countCalendar(userId, startDate, endDate);
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.core.sys.DAO;

import com.viettel.core.base.DAO.GenericDAOHibernate;
import com.viettel.core.base.model.PagingListModel;
import com.viettel.utils.Constants;
import com.viettel.utils.LogUtils;
import com.viettel.voffice.BO.TimeHoliday;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author hungpv32
 */
public class HolidaysManagementDAOHE extends GenericDAOHibernate<TimeHoliday, Long> {

    public HolidaysManagementDAOHE() {
        super(TimeHoliday.class);
    }

    public PagingListModel search(TimeHoliday searchForm, int start, int take) {
        List listParam = new ArrayList();
            StringBuilder strBuf = new StringBuilder("select r from TimeHoliday r where r.isActive = 1 ");
            StringBuilder strCountBuf = new StringBuilder("select count(r) from TimeHoliday r where r.isActive = 1 ");

            StringBuilder hql = new StringBuilder();
            if (searchForm != null) {
                if (searchForm.getTimeDate() != null) {
                    hql.append(" and r.timeDate = ? ");
                    listParam.add(searchForm.getTimeDate());
                }

                if (searchForm.getDateType() != null) {
                    hql.append(" and r.dateType = ? ");
                    listParam.add(searchForm.getDateType());
                }
                if (searchForm.getArrange() != null) {
                    if (searchForm.getArrange() == 1) {
                        hql.append(" order by nlssort(lower(trim(r.name)),'nls_sort = Vietnamese') asc ");
                    } else if (searchForm.getArrange() == 2) {
                        hql.append(" order by nlssort(lower(trim(r.name)),'nls_sort = Vietnamese') desc ");
                    } else if (searchForm.getArrange() == 3) {
                        hql.append(" ORDER BY r.timeDate asc ");
                    } else if (searchForm.getArrange() == 4) {
                        hql.append(" ORDER BY r.timeDate desc ");
                    }
                }
            }

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

    public boolean hasDuplicate(TimeHoliday timeHoliday) {
        StringBuilder hql = new StringBuilder("select count(r) from TimeHoliday r where r.isActive = 1 ");
        List lstParams = new ArrayList();
        if (timeHoliday.getTimeHolidayId() != null && timeHoliday.getTimeHolidayId() > 0L) {
            hql.append(" and r.timeHolidayId <> ?");
            lstParams.add(timeHoliday.getTimeHolidayId());
        }

        if (timeHoliday.getTimeDate() != null) {
            hql.append(" and r.timeDate = ? ");
            lstParams.add(timeHoliday.getTimeDate());
        }

        Query query = session.createQuery(hql.toString());
        for (int i = 0; i < lstParams.size(); i++) {
            query.setParameter(i, lstParams.get(i));
        }
        Long count = (Long) query.uniqueResult();
        return count > 0L;
    }

    public void delete(Long id) {
        TimeHoliday obj = findById(id);
        obj.setIsActive(Constants.HOLIDAY.INACTIVE);
        update(obj);
    }

    // hàm trả về ngày xử lý xong
    public List getAllHolidays() {
        String stringQuery = "select * from Time_Holiday r where r.is_Active = 1 and r.DATE_TYPE = 1 order by r.time_Date asc";
        Query query = session.createSQLQuery(stringQuery).addEntity(TimeHoliday.class);
        List<TimeHoliday> Holidays = query.list();
        return Holidays;
    }

    public List getAllWorkSatSun() {
        String stringQuery = "select * from Time_Holiday r where r.is_Active = 1 and r.DATE_TYPE = 0 order by r.time_Date asc";
        Query query = session.createSQLQuery(stringQuery).addEntity(TimeHoliday.class);
        List<TimeHoliday> WorkSatSun = query.list();
        return WorkSatSun;
    }

    public boolean checkIsWeekend(Calendar input) {
        boolean ck = false;
        if (input.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || input.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            ck = true;
        }
        return ck;
    }

    public boolean checkIsNgayLamBu(Calendar input) {
        boolean ck = false;
        List<TimeHoliday> WorkSatSun = getAllWorkSatSun();
        for (int j = 0; j < WorkSatSun.size(); j++) {
            if (WorkSatSun.get(j).getTimeDate().getDate() == input.getTime().getDate()
                    && WorkSatSun.get(j).getTimeDate().getMonth() == input.getTime().getMonth()
                    && WorkSatSun.get(j).getTimeDate().getYear() == input.getTime().getYear()) {
                ck = true;
                break;
            }
        }
        return ck;
    }

    public boolean checkIsNgayNghi(Calendar input) {
        boolean ck = false;
        List<TimeHoliday> Holidays = getAllHolidays();
        for (int k = 0; k < Holidays.size(); k++) {
            if (Holidays.get(k).getTimeDate().getDate() == input.getTime().getDate()
                    && Holidays.get(k).getTimeDate().getMonth() == input.getTime().getMonth()
                    && Holidays.get(k).getTimeDate().getYear() == input.getTime().getYear()) {
                ck = true;
                break;

            }
        }
        return ck;
    }

    public Date getDeadline(Date startDate, Long processTime) {
        Calendar calStart = new GregorianCalendar();
        calStart.setTime(startDate);
        Calendar calEnd = new GregorianCalendar();
        calEnd.setTime(startDate);
        calEnd.add(Calendar.DATE, Integer.valueOf(processTime.toString()));
        int countDayAdd = 0;
        int dayCheck = Integer.valueOf(processTime.toString());
        List<TimeHoliday> WorkSatSun = getAllWorkSatSun();
        List<TimeHoliday> Holidays = getAllHolidays();
        //kiem tra ngay bat dau neu la ngay nghi hay cuoi tuan thi ngay bat dau va ket thuc se +1
        int checkstar = 0;
        do {
            if (checkIsNgayNghi(calStart)) {
                calEnd.add(Calendar.DATE, 1);
                calStart.add(Calendar.DATE, 1);
            } else if (checkIsWeekend(calStart)) {
                calEnd.add(Calendar.DATE, 1);
                calStart.add(Calendar.DATE, 1);
            } else {
                checkstar = 1;
            }
        } while (checkstar == 0);

        //kiem tra neu ngay nghi, cuoi tuan thi +1, làm bù thì -1
        for (int i = 0; i < dayCheck; i++) {
            for (int k = 0; k < Holidays.size(); k++) {
                if (Holidays.get(k).getTimeDate().getDate() == calStart.getTime().getDate()
                        && Holidays.get(k).getTimeDate().getMonth() == calStart.getTime().getMonth()
                        && Holidays.get(k).getTimeDate().getYear() == calStart.getTime().getYear()) {
                    countDayAdd++;
                    dayCheck++;
                    System.out.println("nghỉ lễ: " + Holidays.get(k).getTimeDate());
                }
            }
            if (checkIsWeekend(calStart)) {
                countDayAdd++;
                dayCheck++;
            }
            for (int j = 0; j < WorkSatSun.size(); j++) {
                // ngày cuối tuần là ngày làm bù thì trừ 1
                if (WorkSatSun.get(j).getTimeDate().getDate() == calStart.getTime().getDate()
                        && WorkSatSun.get(j).getTimeDate().getMonth() == calStart.getTime().getMonth()
                        && WorkSatSun.get(j).getTimeDate().getYear() == calStart.getTime().getYear()) {
                    countDayAdd--;

                }
            }
            calStart.add(Calendar.DATE, 1);
        }
        calEnd.add(Calendar.DATE, countDayAdd);
        int checkNgayThuong = 0;

        //neu lam bu thi return, cuoi tuan thi +1, ngay thong= ngay lam bu
        do {
            //Kiem tra là ngay lam bu
            if (checkIsNgayLamBu(calEnd)) {
                break;
            } else if (checkIsWeekend(calEnd)) {
                calEnd.add(Calendar.DATE, 1);
            } else if (checkIsNgayNghi(calEnd)) {
                calEnd.add(Calendar.DATE, 1);
            } else {
                checkNgayThuong = 1;
            }
        } while (checkNgayThuong == 0);
        return calEnd.getTime();
    }
}

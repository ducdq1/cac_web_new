package com.viettel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

	@SuppressWarnings("static-access")
	public static Date addOneDay(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
			c.add(c.DAY_OF_MONTH, 1);
		}
		return c.getTime();
	}

	@SuppressWarnings("static-access")
	public static Date addOneHour(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
			c.add(c.HOUR_OF_DAY, 1);
		}
		return c.getTime();
	}

	@SuppressWarnings("static-access")
	public static Date addDay(Date date, int days) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
			c.add(c.DAY_OF_MONTH, days);
		}
		return c.getTime();
	}

	public static Date getAddDate(Date date, int months, int days) {

		Calendar cl = Calendar.getInstance();
		cl.setTime(date);

		cl.add(Calendar.MONTH, months);
		cl.add(Calendar.DATE, days);

		return cl.getTime();
	}

	@SuppressWarnings("deprecation")
	public static int compare2Date(Date date1, Date date2) {
		int iReturn = 0;
		if (date1.getYear() > date2.getYear()) {
			iReturn = 1;
		} else if (date1.getYear() < date2.getYear()) {
			iReturn = -1;
		} else if (date1.getMonth() > date2.getMonth()) {
			iReturn = 1;
		} else if (date1.getMonth() < date2.getMonth()) {
			iReturn = -1;
		} else if (date1.getDate() > date2.getDate()) {
			iReturn = 1;
		} else if (date1.getDate() < date2.getDate()) {
			iReturn = -1;
		}
		return iReturn;
	}

	public static java.util.Date convertStringToDate(String date, String pattern) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.parse(date);
	}

	public static java.util.Date convertStringToDate(String date) throws ParseException {
		if ((date == null) || (("").equals(date))) {
			return null;
		}
		String pattern = "dd/MM/yyyy";
		return convertStringToDate(date, pattern);
	}

	public static String convertDateToString(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (date == null) {
			throw new IllegalArgumentException("Method argument must be not null");
		}
		return dateFormat.format(date);
	}

	public static String convertDateTimeToString(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		if (date == null) {
			throw new IllegalArgumentException("Method argument must be not null");
		}
		return dateFormat.format(date);
	}

	public static String convertDateTimeToStringOnlyDate(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (date == null) {
			return "";
			//throw new IllegalArgumentException("Method argument must be not null");
		}
		return dateFormat.format(date);
	}

	public static String convertDateTimeToStringWithPatern(java.util.Date date, String patern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(patern);
		if (date == null) {
			return "";
			//throw new IllegalArgumentException("Method argument must be not null");
		}
		return dateFormat.format(date);
	}
	
	public static java.util.Date getDate() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	public static java.sql.Date convertToSqlDate(java.util.Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}

	public static java.util.Date convertToUtilDate(java.sql.Date sqlDate) {
		return new java.util.Date(sqlDate.getTime());
	}

	public static Date getWeekStart(Date curr) {
		if (curr == null) {
			curr = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(curr);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

		Date weekStart = c.getTime();

		return weekStart;
	}

	public static Date getWeekEnd(Date curr) {
		if (curr == null) {
			curr = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(getWeekStart(curr));
		c.add(Calendar.DAY_OF_MONTH, 6);
		Date weekEnd = c.getTime();
		return weekEnd;
	}

	public static int getNumberOfWorkingDay(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		int startDay = startCal.get(Calendar.DAY_OF_WEEK);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.set(Calendar.HOUR_OF_DAY, 23);
		endCal.set(Calendar.MINUTE, 59);
		endCal.set(Calendar.SECOND, 59);
		endCal.set(Calendar.MILLISECOND, 0);

		double daysBetween = Math
				.ceil(((double) (endCal.getTimeInMillis() - startCal.getTimeInMillis())) / (24 * 60 * 60 * 1000));
		int weeks = (int) Math.floor(daysBetween / 7) * 5;
		int remainDays = (int) Math.ceil(daysBetween % 7);
		int numberOfWorkingDays = weeks + remainDays;

		while ((Calendar.SUNDAY == startDay || Calendar.SATURDAY == startDay) && remainDays > 0) {
			if (Calendar.SATURDAY == startDay) {
				startDay = Calendar.SUNDAY;
				remainDays--;
				numberOfWorkingDays--;
				continue;
			}
			if (Calendar.SUNDAY == startDay) {
				startDay = Calendar.MONDAY;
				remainDays--;
				numberOfWorkingDays--;
			}
		}

		// Vì tính cả startDay nên số ngày thêm vào bị trừ đi 1
		// Ví dụ 16, 17 là 2 ngày. nên sẽ là 16 + 1 để ra ngày 17 (endDay) chứ
		// không phải là 16 + 2
		int offset = startDay + remainDays - 1 - Calendar.SATURDAY;
		if (offset == 0) {
			numberOfWorkingDays -= 1;
		} else if (offset >= 1) {
			numberOfWorkingDays -= 2;
		}
		return numberOfWorkingDays;
	}

	public static Date getEndWorkingDate(Date startDate, int numberOfWorkingDays) {
		int numberOfWorkingDayToAdd = numberOfWorkingDays;
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		int startDay = startCal.get(Calendar.DAY_OF_WEEK);

		while (Calendar.SATURDAY == startDay || Calendar.SUNDAY == startDay) {
			startCal.add(Calendar.DAY_OF_YEAR, 1);
			startDay = startCal.get(Calendar.DAY_OF_WEEK);
		}
		/*
		 * vì startDay khác thứ 7, CN nên số ngày được + sẽ giảm đi 1 do
		 * workingDay đã tính cả ngày hiện tại.
		 */
		numberOfWorkingDayToAdd--;
		int numberOfWeekToAdd = numberOfWorkingDayToAdd / 5;
		int remainDays = numberOfWorkingDayToAdd % 5;

		while (remainDays > 0) {
			startCal.add(Calendar.DAY_OF_YEAR, 1);
			startDay = startCal.get(Calendar.DAY_OF_WEEK);
			while (Calendar.SATURDAY == startDay || Calendar.SUNDAY == startDay) {
				startCal.add(Calendar.DAY_OF_YEAR, 1);
				startDay = startCal.get(Calendar.DAY_OF_WEEK);
			}
			remainDays--;
		}

		if (numberOfWeekToAdd > 0) {
			startCal.add(Calendar.DAY_OF_YEAR, numberOfWeekToAdd * 7);
		}
		int endDay = startCal.get(Calendar.DAY_OF_WEEK);

		// Nếu endDate trùng với SUNDAY || SATURDAY thì lùi về FRIDDAY
		while (Calendar.SUNDAY == endDay || Calendar.SATURDAY == endDay) {
			startCal.add(Calendar.DAY_OF_YEAR, -1);
			endDay = startCal.get(Calendar.DAY_OF_WEEK);
		}
		return startCal.getTime();
	}

	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date setStartTimeOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date setStartTimeOfDatePlusOneHour(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getTime();
	}

	public static Date setEndTimeOfDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static String convertDateToStringFormat(Date date, String format) {
		String result = null;
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(date);
		}
		return result;
	}

	/**
	 * 
	 * Tra ve ngay thang duoc convert dinh dang theo formatDate
	 * 
	 * @author: Nhantd
	 * @param date
	 * @param formatDate
	 * @return
	 * @return: Date
	 * @throws:
	 */
	public static Date formatDateWithFormat(Date date, String formatDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
		try {
			date = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			LogUtils.addLog(e);
		}
		return date;
	}

	/**
	 * http://stackoverflow.com/questions/20165564/calculating-days-between-two-
	 * dates-with-in-java
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static int daysBetween(Calendar day1, Calendar day2) {
		Calendar dayOne = (Calendar) day1.clone(), dayTwo = (Calendar) day2.clone();

		if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
			return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
		} else {
			if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
				// swap them
				Calendar temp = dayOne;
				dayOne = dayTwo;
				dayTwo = temp;
			}
			int extraDays = 0;

			int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

			while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
				dayOne.add(Calendar.YEAR, -1);
				// getActualMaximum() important for leap years
				extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
			}

			return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
		}
	}

}

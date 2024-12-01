package com.utc.dormitory_managing.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class Utils {

	public static class DateRange {
		private Date startDate;
		private Date endDate;
		private float duration;

		public DateRange(Date startDate, Date endDate, float duration) {
			this.startDate = startDate;
			this.endDate = endDate;
			this.duration = duration;
		}
		public DateRange() {
		}

		public Date getStartDate() {
			return startDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public float getDuration() {
			return duration;
		}

		@Override
		public String toString() {
			return "DateRange{" + "startDate=" + startDate + ", endDate=" + endDate + ", duration=" + duration + " days"
					+ '}';
		}
	}
	
	public static String convertDateToString(Date date) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = formatter.format(now);
		System.err.println(dateString);
		return dateString;
	}

	public static Date setTimeContract() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, 5);
		calendar.set(Calendar.DATE, 25);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		return date;
	}

	public static Date getCurrentWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date sevenDaysBefore = calendar.getTime();

		return sevenDaysBefore;
	}

	public static DateRange getCurrentMonth() {
		DateRange dateRange = new DateRange();
		// Lấy ngày hiện tại
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		// Chuyển sang tháng trước
		calendar.add(Calendar.MONTH, -1);

		// Ngày đầu tiên của tháng trước
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date firstDayOfLastMonth = calendar.getTime();
		
		// Ngày cuối cùng của tháng trước
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date lastDayOfLastMonth = calendar.getTime();
		
		dateRange.startDate= firstDayOfLastMonth;
		dateRange.endDate = lastDayOfLastMonth;
		return dateRange;
	}
	
	public static String getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int monthValue = calendar.get(Calendar.MONTH) + 1; // Thêm 1 để chuyển sang từ 1-12
        return String.valueOf(monthValue);
	}

	public static Date getDateNextContract(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//lay thoi gian la 6 thang 1 lan
		calendar.set(Calendar.MONTH, 6);
		return calendar.getTime();
	}
	
}

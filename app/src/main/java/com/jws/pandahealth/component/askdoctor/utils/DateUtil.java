package com.jws.pandahealth.component.askdoctor.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public static String getTime(long time) {
		try {
			long second = (System.currentTimeMillis() - time * 1000) / 1000;
			if (second < 60) {
				return "just now";
			} else if (second < (60 * 60)) {
				long minute = second / 60;
				return minute + "minutes ago ";
			} else if (second < (24 * 60 * 60)) {
				long hour = second / 3600;
				return hour + "hour ago";
			} else if(isYesterday(time * 1000)){
				String timestr = TimeStamp3Date3byFormart(time + "", "HH:mm");
				return "yesterday " + timestr;
			}else if(isSameYear(time * 1000)){
				return TimeStamp3Date3byFormart(""+time,"MM-dd HH:mm");
			} else {
				return TimeStamp3Date31(""+time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断是不是昨天
	 * @param time
	 * @return
	 */
	public static boolean isYesterday(long time){
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int todayDOW = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTimeInMillis(time);
		int timeDOW = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println(todayDOW - timeDOW );
		if(todayDOW - timeDOW == 1){
			return true;
		}
		return false;
	}

	/**
	 * 判断是不是同一年
	 * @param time
	 * @return
	 */
	public static boolean isSameYear(long time){
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int todayDOW = calendar.get(Calendar.YEAR);
		calendar.setTimeInMillis(time);
		int timeDOW = calendar.get(Calendar.YEAR);
		System.out.println(todayDOW - timeDOW );
		if(todayDOW - timeDOW == 0){
			return true;
		}
		return false;
	}

	public static String TimeStamp3Date3byFormart(String timeString, String formatString) {
		Long timestamp = Long.parseLong(timeString) * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		String date = sdf.format(new Date(timestamp));
		return date;
	}

	public static String TimeStamp3Date31(String timeString) {
		Long timestamp = Long.parseLong(timeString) * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(timestamp));
		return date;
	}



}

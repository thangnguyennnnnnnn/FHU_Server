package fpt.fu.dn.fpthospitalcare.fhc.fhcService;

import java.util.Date;

public class DateTimeService {
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	private int second;
	public void createCurrentDateTime() {
		Date date = new Date();
		day = date.getDate();
		month = date.getMonth() + 1;
		year = date.getYear() + 1900;
		hour = date.getHours();
		minute = date.getMinutes();
		second = date.getSeconds();
		
		
	}
	
	public String getCurrentDateTime() {
		createCurrentDateTime();
		//String dayToString = formatDateTime(day);
		//String monthToString = formatDateTime(month);
		//String yearToString = formatDateTime(year);
		//String hourToString = formatDateTime(hour);
		//String minuteToString = formatDateTime(minute);
		//String secondToString = formatDateTime(second);
		return "";
	}
	
	public String formatDateYYMMDD(String date) {
		if (date.length() == 8) {
			String day = date.substring(0,2);
			String month = date.substring(2,4);
			String year = date.substring(4,8);
			return day + "/" + month + "/" +year;
		}
		return "";
	}
	
	public String formatDateHHMMSS(String time) {
		if (time.length() == 6) {
			String hour = time.substring(0,2);
			String minute = time.substring(2,4);
			String second = time.substring(4,6);
			return hour + ":" + minute + ":" +second;
		}
		return "";
	}
}

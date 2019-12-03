package test.range.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {
	
    public static String formatDate(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    public static String getCurrentMonth(int m, String symbol){
    	Calendar cl = Calendar.getInstance();
		int month = cl.get(Calendar.MONTH);
		cl.set(Calendar.MONTH, month - m);
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy"+symbol+"MM");
		String ctime = formatter.format(cl.getTime());
		return ctime;
    }
    /**
     * 获取当月天数
     */
    public static int getCurrentMonthLastDay(){  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    } 
    
    /**
     * 获取某月天数
     */
	public static int getDaysOfMonth(String date) { 
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(toDate(date));  
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
    } 
    
    public static Date toDate(String date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date parseDate = null;
        try {
            parseDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parseDate;
    }
    public static Date toDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (Exception e) {
        }
        if (date == null) {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                date = dateFormat.parse(str);
            } catch (Exception e) {
            }
        }
        if (date == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = dateFormat.parse(str);
            } catch (Exception e) {
            }
        }
        if (date == null) {
            dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                date = dateFormat.parse(str);
            } catch (Exception e) {
            }
        }

        return date;
    }

    public static String getBeforeDayDate(Date date, int day, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,day);
        return sdf.format(calendar.getTime());
    }
    
    /**
     * 
     * @param date
     * @param day
     *            -1 代表获取前一天， 1代表获取后一天
     * @return
     */
    public static Date getBeforeDayDate(Date date, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,day);
        return calendar.getTime();
    }

    /**
     * 获取两个日期之间的所有日期集合
     */
    public static List<String> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> lDate = new ArrayList<String>();
        lDate.add(sdf.format(beginDate));
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(endDate);
        int end = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(beginDate);
        int begin = cal.get(Calendar.DAY_OF_YEAR);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(sdf.format(cal.getTime()));
            } else {
                break;
            }
        }

        if (begin<end){
            lDate.add(sdf.format(endDate));
        }

        return lDate;
    }

    /**
     * 把时间区间拆分成若干小的时间区间 <br>
     * 例如：getTimesBetween2Date("2019-05-27 00:00:00", "2019-05-28 00:00:00", 30)
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;代表把2019年5月27号这一天拆分成每30分钟一个的若干小区间（48个） 返回：<br>
     * 2019-05-27 00:00:00 至 2019-05-27 00:30:00<br>
     * 2019-05-27 00:30:00 至 2019-05-27 01:00:00<br>
     * 2019-05-27 01:00:00 至 2019-05-27 01:30:00<br>
     * 2019-05-27 01:30:00 至 2019-05-27 02:00:00<br>
     * ... ...
     * 
     * @param beginDate
     *            时间区间之开始时间（包含关系，即 >= 这个时间）
     * @param endDate
     *            时间区间之截止时间（不包含，即 < 这个时间
     * @param minuteInterval
     *            小区间的时间间隔，以分钟为单位
     * @return
     */
    public static List<String[]> getTimesBetween2Date(Date beginDate, Date endDate, int minuteInterval) {

        Calendar bCal = Calendar.getInstance();
        bCal.setTime(beginDate);
        List<String[]> result = new LinkedList<String[]>();
        while (true) {
            Date start = bCal.getTime();
            bCal.add(Calendar.MINUTE, minuteInterval);
            if (endDate.equals(bCal.getTime()) || endDate.after(bCal.getTime())) {
                Date end = bCal.getTime();
                result.add(new String[] { formatDate(start, "yyyy-MM-dd HH:mm:ss"),
                        formatDate(end, "yyyy-MM-dd HH:mm:ss") });
            } else {
                break;
            }
        }
        return result;
    }

    /**
     *日期格式转换
     *@author quzb
     *@date 2018/3/13 17:25
     */
    public static String changePattern(String date,String originalPattern,String finalPattern){
        SimpleDateFormat originalSdf = new SimpleDateFormat(originalPattern);
        SimpleDateFormat finalSdf = new SimpleDateFormat(finalPattern);
        try {
            return  finalSdf.format(originalSdf.parse(date));
        }catch (ParseException e){
            return null;
        }

    }
    /**
	 * 计算2个日期相隔秒
	 * @param smdate 较小的日期
	 * @param bdate 较大的日期
	 * @return
	 */
	public static double compareToSecond(Date smdate, Date bdate) {
        double time1 = smdate.getTime();                   
        double time2 = bdate.getTime();         
        double between_seconds=(time2-time1)/(1000);  
            
       return between_seconds;

	}
    /**
     * 获取当前结算周期的开始时间
     * 
     * @param date 日期
     * @param settleDay 结算日，例如16 代表每月16号到下月15号为一周期
     * @return
     */
    public static String getStatStartDateStr(Date date, int settleDay) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String start = year +"-";
		DecimalFormat df = new DecimalFormat("00");
		if (day < settleDay) {
			if (month <=1) {
				start = (year -1) + "-12" + "-" + df.format(settleDay) + " 00:00:00";
			} else {
			    start = start + df.format(month-1) + "-" + df.format(settleDay) + " 00:00:00";
			}
		} else {
			start = start + df.format(month) +"-" + df.format(settleDay) + " 00:00:00";
		}
		return start;
    }
    /**
     * 获取当前结算周期的截止时间
     * 
     * @param date 日期
     * @param settleDay 结算日，例如16 代表每月16号到下月15号为一周期
     * @return
     */
    public static String getStatEndDateStr(Date date, int settleDay) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String start = year +"-";
		DecimalFormat df = new DecimalFormat("00");
		if (day < settleDay) {
			start = start + df.format(month) + "-" + df.format(settleDay) + " 00:00:00";
		} else {
			if (month >= 12) {
				start = (year + 1) + "-01" + "-" + df.format(settleDay) + " 00:00:00";
			} else {
			    start = start + df.format(month+1) + "-" + df.format(settleDay) + " 00:00:00";
			}
		}
		return start;
    }
    
    /**
     * 根据传入的日期，获取统计月（一般是每月16号开始统计，比如当时是2018年3月16号，那么统计区间是2.16到3.16，统计月份是201803）
     * 
     * @param date
     * @return
     */
    public static int getStatMonth(Date date) {
        String str = getStatEndDateStr(date, 16);
    	str = str.substring(0, str.lastIndexOf('-'));
		String yyyyMM = StringUtils.replace(str, "-", "");
		return Integer.parseInt(yyyyMM);
    }
    
    /*
	 * 按照指定格式获取当前时间
	 */
	public static String getTodayDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(System.currentTimeMillis());
	}
	
	public static String getBeforeDate(int daysAgo,String symbol) {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day - daysAgo);
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy"+symbol+"MM"+symbol+"dd");
		String ctime = formatter.format(cl.getTime());
		return ctime;
	}
	
	public static String getIntervalDate(int day,String symbol) {
		 //获取当前月第一天：
		SimpleDateFormat format = new SimpleDateFormat("yyyy"+symbol+"MM"+symbol+"dd");
		Calendar c = Calendar.getInstance();    
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH,day);//设置为1号,当前日期既为本月第一天 
		return format.format(c.getTime());
	}
	
	/*
	 * 根据输入日期获取月份
	 */
	public static String getMonthByInputDate(String date,String inputPattern,String outputPattern){
		String month = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy"+inputPattern+"MM"+inputPattern+"dd");
			Date cdate = format.parse(date);
			month = formatDate(cdate, outputPattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return month;
	}
	
	public static String getMonthByInputDate(String date,int month,String inputPattern,String outputPattern) {
		Calendar c = Calendar.getInstance();//获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy"+inputPattern+"MM"+inputPattern+"dd");
        Date cdate = null;
        try{
          cdate = sdf.parse(date);//初始日期
        }catch(Exception e){
        }
        c.setTime(cdate);//设置日历时间
        c.add(Calendar.MONTH,month);//在日历的月份上增加6个月
        return formatDate(c.getTime(), outputPattern);//得到6个月后的日期
	}
	
	/**
	 * 获取两个时间的毫秒差
	 * @param fromDate
	 * @param toDate
	 * @param pattern
	 * @return
	 */
	public static long getDateInterval(String fromDate,String toDate,String pattern) {
		long milis = 0l;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			long from = sdf.parse(fromDate).getTime();  
			long to = sdf.parse(toDate).getTime();  
			milis = to - from;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return milis;
	}
	
	/**
	 * 获取起始日期+毫秒之后的日期
	 * @param date
	 * @param milis
	 * @return
	 */
	public static String getDateAddMilis(String date,long milis,String orginPattern,String outputPattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(orginPattern);
			long start = sdf.parse(date).getTime();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(start+milis); 
			Date cdate = c.getTime();
			date = formatDate(cdate,outputPattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(String date1,String date2,String orginPattern){
    	int days = 0;
    	SimpleDateFormat sdf = new SimpleDateFormat(orginPattern);
    	try {
			Date sdate = sdf.parse(date1);
			Date edate = sdf.parse(date2);
			days = (int) ((edate.getTime() - sdate.getTime()) / (1000*3600*24));
		} catch (Exception e) {
			e.printStackTrace();
		}
        return days;
    }

	/**
	 * 得到当前时间前n个时间单位的时间
	 */
	public static String getIntervalTime(Date date, int interval, String timeUnit, String originPattern){
		SimpleDateFormat sdf = new SimpleDateFormat(originPattern);
		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		switch (timeUnit){
			case "hour":cd.add(cd.HOUR, interval);break;
			case "minute":cd.add(cd.MINUTE, interval);break;
			case "second":cd.add(cd.SECOND, interval);break;
			case "year":cd.add(cd.YEAR, interval);break;
			case "month":cd.add(cd.MONTH, interval);break;
			case "day":cd.add(cd.DATE, interval);break;
		}
		String newDate = "";
		try {
			newDate = sdf.format(cd.getTime());
		}catch (Exception e){
			e.printStackTrace();
		}
		return newDate;
	}
	
	

}

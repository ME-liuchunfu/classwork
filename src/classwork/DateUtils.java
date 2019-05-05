package classwork;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 时间工具类
 * @author hp
 *
 */
public class DateUtils {
	 
	  /**
	   * 时间格式
	   */
	  public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  
	  /**
	   * 时间格式
	   */
	  public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS1 =new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	  
	  /**
	   * 获取当前时间
	   * @return
	   */
	  public static String getyyyy_MM_dd_HH_mm_ss(){
		  Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
		  c.setTime(new java.util.Date());
		  return YYYY_MM_DD_HH_MM_SS.format(c.getTime());
	  }
	  
	  /**
	   * 获取当前时间
	   * @return
	   */
	  public static String getyyyy_MM_dd_HH_mm_ss1(){
		  Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));    //获取东八区时间
		  c.setTime(new java.util.Date());
		  return YYYY_MM_DD_HH_MM_SS1.format(c.getTime());
	  }
	
}

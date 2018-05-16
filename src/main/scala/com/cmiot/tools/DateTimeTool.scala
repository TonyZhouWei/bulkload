package com.cmiot.tools

import java.sql.{Date, Timestamp}
import java.text.SimpleDateFormat

import com.github.nscala_time.time.{StaticDateTime, StaticLocalDate, StaticLocalDateTime}
import org.joda.time.format.{DateTimeFormat, ISODateTimeFormat}
import org.joda.time.{DateTime, LocalDate}

class DateTimeTool private() {

  def getDateTime(s: String) = ISODateTimeFormat.dateTime.parseDateTime(s)

  def getDateTime(s: String, format: String) = DateTimeFormat.forPattern(format).parseDateTime(s)

  def getDate(s: String) = new LocalDate(getDateTime(s))

  def getDate(s: String, format: String) = new LocalDate(getDateTime(s, format))

  def toDate(s: String, format: String = "yyyyMMdd") = if (invalidDateString(s)) null else Date.valueOf(getDate(s, format).toString("yyyy-MM-dd"))

  private def invalidDateString(s: String): Boolean = s == null || s.length < 6

  //今天
  def today = Date.valueOf(StaticLocalDate.today.toString("yyyy-MM-dd"))

  // add by tongs 20170919 start
  // 明天
  def tomorrow = Date.valueOf(StaticLocalDate.tomorrow.toString("yyyy-MM-dd"))
  // add by tongs 20170919 end

  //昨天
  def yesterday = Date.valueOf(StaticLocalDate.yesterday.toString("yyyy-MM-dd"))

  //前天
  def beforeYesterday = Date.valueOf(StaticLocalDate.today.minusDays(2).toString("yyyy-MM-dd"))

  //当前时间的TimeStamp
  def now = new Timestamp(StaticDateTime.now.getMillis)

  //获得指定日期上月的开始日和结束日
  def getBeginEndDayOfMonth(s: String, format: String = "yyyyMMdd") = firstDayOfLastMonth(getDate(s, format)).toString("yyyyMMdd") + "~" + lastDayOfLastMonth(getDate(s, format)).toString("yyyyMMdd")

  //指定日期的前一个月最后一天
  def lastDayOfLastMonth(date: LocalDate) = firstDayOfMonth(date).minusDays(1)

  //指定日期的前一个月第一天
  def firstDayOfLastMonth(date: LocalDate) = firstDayOfMonth(date).minusMonths(1)

  //指定日期的前两个月第一天
  def firstDayOfLastTwoMonth(date: LocalDate) = firstDayOfMonth(date).minusMonths(2)

  //指定日期的前三个月第一天
  def firstDayOfLastThreeMonth(date: LocalDate) =  firstDayOfMonth(date).minusMonths(3)

  //指定日期的前四个月第一天
  def firstDayOfLastFourMonth(date: LocalDate) =  firstDayOfMonth(date).minusMonths(4)

  //指定日期的前五个月第一天
  def firstDayOfLastFiveMonth(date: LocalDate) =  firstDayOfMonth(date).minusMonths(5)

  //指定日期本月第一天
  def firstDayOfMonth(date: LocalDate) = date.withDayOfMonth(1)

  //将指定时间字符串转换为指定格式的TimeStamp
  def toTimestamp(s: String, format: String = "yyyyMMddHHmmss") = if (invalidDateString(s)) null else new Timestamp(getDateTime(s, format).getMillis)

  //获取现在时间的"yyyy-MM-dd HH:mm:ss"型字符串
  def sysDateTime = StaticLocalDateTime.now.toString("yyyy-MM-dd HH:mm:ss")

  //获取指定日期上周的开始日和结束日
  def getBeginEndDayOfWeek(s: String, format: String = "yyyyMMdd") = firstDayOfLastWeek(getDate(s,
    format)).toString("yyyyMMdd") + "~" + lastDayOfLastWeek(getDate(s, format)).toString("yyyyMMdd")

  private def firstDayofWeek(date: LocalDate) = date.withDayOfWeek(1)

  //指定日期的上周最后一天
  def lastDayOfLastWeek(date: LocalDate) = firstDayOfWeek(date).minusDays(1)

  //指定日期的上周第一天
  def firstDayOfLastWeek(date: LocalDate) = firstDayOfWeek(date).minusWeeks(1)

  //指定时间所在周的第一天
  def firstDayOfWeek(date: LocalDate) = firstDayofWeek(date)

  //指定时间所在周的最后一天
  def lastDayOfWeek(date: LocalDate) = firstDayofWeek(date).plusDays(6)

  //指定时间的昨天
  def statsYesterday(s: Date, format: String = "yyyy-MM-dd") = DateTime.parse(s.toString).minusDays(1).toString("yyyy-MM-dd")

  // add by tongs 20170915 start
  //指定时间的明天
  def statsTomorrow(s: Date, format: String = "yyyy-MM-dd") = DateTime.parse(s.toString).plusDays(1).toString("yyyy-MM-dd")
   // add by tongs 20170915 end

  //指定时间的前天
  def statsBeforeYesterday(s: Date, format: String = "yyyy-MM-dd") = DateTime.parse(s.toString).minusDays(2).toString("yyyy-MM-dd")

  //指定时间的前几天
  def aFewDaysAgo(s: Date, format: String = "yyyy-MM-dd",days : Int) = DateTime.parse(s.toString).minusDays(days).toString("yyyy-MM-dd")

  def isMonthlyStats(statDate: Date): Boolean = 1 == new LocalDate(statDate.getTime).getDayOfMonth

  def isWeeklyStats(statDate: Date): Boolean = 1 == new LocalDate(statDate.getTime).getDayOfWeek

  //简单判断是否日期类型 标准：连续的8位纯数字
  def isDate(s: String): Boolean = {
    println(s)
    s.matches("\\d{8}")
  }

  /**
    * 将时间的字符串类型，根据格式，转为java.sql.date型
    *
    * @param dateStr
    * @param format
    * @return java.sql.date
    */
  def toDateHbase(dateStr: String, format: String = "yyyy-MM-dd HH:mm:ss") = {
    if (invalidDateString(dateStr)||dateStr.length<10) null else{new Timestamp(new SimpleDateFormat(format).parse(dateStr).getTime())}
  }
}

case object DateTimeTool extends DateTimeTool

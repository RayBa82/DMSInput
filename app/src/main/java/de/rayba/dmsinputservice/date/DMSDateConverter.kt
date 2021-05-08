package de.rayba.dmsinputservice.date

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


/**
 * Gets the float date.
 *
 * @param date
 * the date
 * @return the float date
 * @author RayBa
 * @date 08.04.2012
 */
fun getFloatDate(date: Date?): String {
    val result = StringBuffer()
    val cal = GregorianCalendar.getInstance()
    cal.time = date
    val days: Long = getDaysSinceDelphiNull(date)
    val hours = cal[Calendar.HOUR_OF_DAY].toLong()
    var minutesOfDay = hours * 60
    val minutes = cal[Calendar.MINUTE].toLong()
    minutesOfDay = minutesOfDay + minutes
    val percentage = minutesOfDay / (24.0 * 60.0)
    val percentageString = StringBuffer(percentage.toString())
    percentageString.replace(0, 2, "")
    result.append(days).append(".").append(percentageString)
    return result.toString()
}

/**
 * Gets the days between two dates.
 *
 * @param a the a
 * @param b the b
 * @return the difference
 * @author RayBa
 * @date 08.04.2012
 */
fun getDifference(a: Date?, b: Date?): Long {
    val startCal = GregorianCalendar.getInstance()
    startCal.timeZone = TimeZone.getDefault()
    startCal.time = a
    startCal[Calendar.HOUR_OF_DAY] = 0
    startCal[Calendar.MINUTE] = 0
    startCal[Calendar.MILLISECOND] = 0
    startCal[Calendar.SECOND] = 0
    val endCal = GregorianCalendar.getInstance()
    endCal.timeZone = TimeZone.getDefault()
    endCal.time = b
    endCal[Calendar.HOUR_OF_DAY] = 0
    endCal[Calendar.MINUTE] = 0
    endCal[Calendar.MILLISECOND] = 0
    endCal[Calendar.SECOND] = 0
    val endL = endCal.timeInMillis + endCal.timeZone.getOffset(endCal.timeInMillis)
    val startL = startCal.timeInMillis + startCal.timeZone.getOffset(startCal.timeInMillis)
    return (startL - endL) / (1000 * 60 * 60 * 24)
}

fun getDaysSinceDelphiNull(date: Date?): Long {
    return getDifference(truncate(date), getBaseDate())
}

fun truncate(date: Date?): Date? {
    var date = date
    val cal = Calendar.getInstance()
    cal.time = date
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MILLISECOND] = 0
    date = cal.time
    return date
}

fun getBaseDate(): Date? {
    val cal = GregorianCalendar.getInstance()
    cal[Calendar.DAY_OF_MONTH] = 30
    cal[Calendar.MONTH] = Calendar.DECEMBER
    cal[Calendar.YEAR] = 1899
    return truncate(cal.time)
}

fun addDay(d: Date?): Date? {
    val cal = GregorianCalendar.getInstance()
    cal.time = d
    cal.add(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun stringToDate(date: String?, format: String?): Date? {
    try {
        return SimpleDateFormat(format).parse(date)
    } catch (e: Exception) {
        Log.e("stringToDate", "ERROR CONVERTING DATE TO STRING", e)
    }
    return null
}
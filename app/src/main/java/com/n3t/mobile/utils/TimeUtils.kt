package com.n3t.mobile.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object TimeUtils {
    fun formatDate(date: Long, format: String): String {
        return DateFormat.format(format, date).toString()
    }

    fun formatDate(date: Date, format: String): String {
        return DateFormat.format(format, date).toString()
    }

    fun formatDateWithLocale(date: Date, format: String, locale: Locale): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(date)
    }

    fun timestampToDayString(timestamp: Long): String {
        return DateFormat.format("dd/MM/yyyy", timestamp * 1000).toString()
    }

    fun timestampToDay(timestamp: Long): String {
        return DateFormat.format("EEEE", timestamp * 1000).toString()
    }

    fun timestampToTimeString(timestamp: Long): String {
        return DateFormat.format("HH:mm", timestamp * 1000).toString()
    }

    fun startOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis / 1000
    }

    fun endOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        return calendar.timeInMillis / 1000
    }

    fun currentDayStartMilliseconds(): Long {
        val c = Calendar.getInstance()
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        return c.timeInMillis / 1000
    }

    fun dateToMilliseconds(year: Int, month: Int, day: Int): Long {
        val c = Calendar.getInstance()
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        c[Calendar.DAY_OF_MONTH] = day
        c[Calendar.MONDAY] = month
        c[Calendar.YEAR] = year
        return c.timeInMillis / 1000
    }

    fun dateToTimeInMillis(date: Date): Long {
        val c = Calendar.getInstance()
        c.time = date
        return c.timeInMillis
    }

    fun currentDayEndMilliseconds(): Long {
        val day = (60 * 60 * 24).toLong()
        return currentDayStartMilliseconds() + day
    }

    fun millisToMinutesString(millis: Long): String {
        val seconds = millis / 1000
        return String.format("%02d:%02d", seconds / 60, seconds % 60)
    }

    fun minutesToHoursString(minutes: Long): String {
        return if (minutes > 60) {
            String.format("%02d gi? %02d phút", minutes / 60, minutes % 60)
        } else {
            String.format("%02d phút", minutes)
        }
    }

    fun minutesToHoursFormat(minutes: Long): String {
        return if (minutes > 60) {
            String.format("%02dh%02d", minutes / 60, minutes % 60)
        } else {
            String.format("%02d phút", minutes)
        }
    }

    fun getWeekDayDateTime(): ArrayList<Date> {
        val weekDayList = arrayListOf<Date>()
        val date: Calendar = Calendar.getInstance()
        date.add(Calendar.DATE, -7)
        for (i in 0 until 7) {
            date.add(Calendar.DATE, 1)
            weekDayList.add(date.time)
        }
        return weekDayList
    }

    fun getWeekDayList(): ArrayList<Int> {
        val weekDayList = arrayListOf<Int>()
        val date: Calendar = Calendar.getInstance()
        date.add(Calendar.DATE, -7)
        for (i in 0 until 7) {
            date.add(Calendar.DATE, 1)
            weekDayList.add(getWeekDay(date.time))
        }
        return weekDayList
    }

    fun getMonthDayList(): ArrayList<Int> {
        val monthDayList = arrayListOf<Int>()
        val date: Calendar = Calendar.getInstance()
        date.add(Calendar.DATE, -7)
        for (i in 0 until 7) {
            date.add(Calendar.DATE, 1)
//       AppLogger("BUG").e(date.time.date.toString())
            monthDayList.add(date.time.date)
        }
        return monthDayList
    }

    private fun getWeekDay(date: Date): Int {
        val c: Calendar = Calendar.getInstance()
        c.time = date
        return c.get(Calendar.DAY_OF_WEEK)
    }

    private fun getDateFromTime(time: Long): Date {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return calendar.time
    }

    fun getDatesOfWeek(time: Long): List<Date> {
        val date = Calendar.getInstance(Locale.US)
        date.timeInMillis = time
        date.add(Calendar.DATE, -7)

        val weekDayList = arrayListOf<Date>()
        for (i in 0 until 7) {
            date.add(Calendar.DATE, 1)
            weekDayList.add(date.time)
        }

        return weekDayList
    }

    private fun getDayOfWeek(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return calendar.get(Calendar.DAY_OF_WEEK)
    }
}


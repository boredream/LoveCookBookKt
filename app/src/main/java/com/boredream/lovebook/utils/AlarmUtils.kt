package com.boredream.lovebook.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import androidx.core.database.getLongOrNull
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import java.util.*


// https://developer.android.com/guide/topics/providers/calendar-provider?hl=zh-cn
// Realme 只有自建的 Event 才能查询到，系统软件新建的查询不到。小米则可以。
object AlarmUtils {

    private const val CALENDAR_EVENT_RRULE = "FREQ=YEARLY;INTERVAL=1"
    private const val CALENDAR_EVENT_DURATION = "P36000S"

    @SuppressLint("Range")
    private fun queryCalendarId(context: Context): Long? {
        val uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                val index = it.getColumnIndex(CalendarContract.Calendars._ID)
                val calendarId = it.getLongOrNull(index)
                if (calendarId != null) {
                    return calendarId
                }
            }
        }
        return null
    }

    private fun getDtStart(theDay: TheDay): Long {
        return Calendar.getInstance().run {
            time = TimeUtils.string2Date(theDay.theDayDate, "yyyy-MM-dd")
            set(Calendar.HOUR_OF_DAY, 10)
            timeInMillis
        }
    }

    private fun getEventDescription(theDay: TheDay): String? {
        if (theDay.id == null) return null
        // TODO: 添加个链接可以跳转回app
        return "纪念日ID[${theDay.id}] 请勿修改，否则App中修改纪念日时会无法定位到本日程"
    }

    // 名字+起始日期+规则相同，视为同一个日历
    private fun queryCalendarEventByTitleAndDate(context: Context, theDay: TheDay): Long? {
        val uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.RRULE,
        )
        val selection = "((${CalendarContract.Events.TITLE} = ?) AND (" +
                "${CalendarContract.Events.DTSTART} = ?) AND (" +
                "${CalendarContract.Events.RRULE} = ?))"
        val selectionArgs = arrayOf(
            theDay.name,
            getDtStart(theDay).toString(),
            CALENDAR_EVENT_RRULE,
        )
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                val index = it.getColumnIndex(CalendarContract.Events._ID)
                val eventId = it.getLongOrNull(index)
                if (eventId != null) {
                    return eventId
                }
            }
        }
        return null
    }

    // 利用纪念日ID查询日程
    private fun queryCalendarEventByTheDayId(context: Context, theDay: TheDay): Long? {
        val uri = CalendarContract.Events.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.DESCRIPTION,
        )
        val selection = "(${CalendarContract.Events.DESCRIPTION} = ?)"
        val selectionArgs = arrayOf(getEventDescription(theDay))
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                val index = it.getColumnIndex(CalendarContract.Events._ID)
                val eventId = it.getLongOrNull(index)
                if (eventId != null) {
                    return eventId
                }
            }
        }
        return null
    }

    fun insertOrUpdateCalendarEvent(
        context: Context,
        theDay: TheDay,
        callback: (error: String?) -> Unit
    ) {
        val calendarId = queryCalendarId(context)
        if (calendarId == null) {
            callback.invoke("获取日历 calendarId 失败")
            return
        }

        // 先名字+起始日期查询是否已经创建过相同的日程了
        var eventId = queryCalendarEventByTitleAndDate(context, theDay)
        if (eventId != null) {
            LogUtils.i("already create calendar event $eventId")
            callback.invoke("在该日期下，已经有过相同名称的日程了")
            return
        }

        // 如果上面名字+起始日期未匹配，则可能是修改的日程，再用ID尝试去查询
        eventId = queryCalendarEventByTheDayId(context, theDay)

        val values = ContentValues()
        values.put(CalendarContract.Events.ORGANIZER, context.getString(R.string.app_name))
        values.put(CalendarContract.Events.TITLE, theDay.name)
        values.put(CalendarContract.Events.DESCRIPTION, getEventDescription(theDay))
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId)
        // 必须有开始
        values.put(CalendarContract.Events.DTSTART, getDtStart(theDay))
        // 非重复事件 必须有结束
        // event.put(CalendarContract.Events.DTEND, startMillis + 60 * 60 * 1000)

        // 纪念日业务中，一定是按年重复的
        // RRULE 重复规则协议 https://www.rfc-editor.org/rfc/rfc5545  https://www.jianshu.com/p/8f8572292c58
        values.put(CalendarContract.Events.DURATION, CALENDAR_EVENT_DURATION) // 10小时
        values.put(CalendarContract.Events.RRULE, CALENDAR_EVENT_RRULE) // 每年重复

        values.put(CalendarContract.Events.HAS_ALARM, 1) //设置有闹钟提醒
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)

        if (eventId != null) {
            // 有id，则更新
            context.contentResolver.update(
                ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId),
                values,
                null,
                null
            )
            LogUtils.i("update calendar event success. eventId=$eventId")
        } else {
            // 没有id就创建
            val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            if (uri == null) {
                callback.invoke("日历事件插入失败")
                return
            }
            eventId = ContentUris.parseId(uri)
            LogUtils.i("insert calendar event success. eventId=$eventId")
        }

        insertCalendarNotify(context, eventId)
        // 不care提醒是否创建成功，只要event创建成功则OK
        callback.invoke(null)
    }

    private fun insertCalendarNotify(context: Context, eventId: Long) {
        // 为事件设定提醒
        val values = ContentValues()
        values.put(CalendarContract.Reminders.EVENT_ID, eventId)
        // 提前多少分钟提醒
        values.put(CalendarContract.Reminders.MINUTES, 0)
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        val uri = context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
        LogUtils.i("result uri = $uri")
    }

    @SuppressLint("Range")
    fun queryCalendarRemind(context: Context) {
        val projection = arrayOf(
            CalendarContract.Reminders.EVENT_ID,
            CalendarContract.Reminders.MINUTES,
            CalendarContract.Reminders.METHOD,
        )
        // TODO: 按时间和组织查询当前应用创建的日历事件
        val cursor = context.contentResolver.query(
            CalendarContract.Reminders.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        if (cursor == null) {
            LogUtils.i("查询失败")
            return
        }
        if (cursor.moveToFirst()) {
            do {
                for (index in 0..2) {
                    println(cursor.getString(index))
                }
            } while (cursor.moveToNext())
        }
    }

    @SuppressLint("Range")
    fun queryCalendarEvent(context: Context) {
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.RRULE,
        )
        // TODO: 按时间和组织查询当前应用创建的日历事件
        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        if (cursor == null) {
            LogUtils.i("查询失败")
            return
        }
        if (cursor.moveToFirst()) {
            do {
                for (index in 0..5) {
                    println(cursor.getString(index))
                }
            } while (cursor.moveToNext())
        }
    }

}
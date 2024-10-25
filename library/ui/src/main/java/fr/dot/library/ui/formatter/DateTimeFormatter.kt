package fr.dot.library.ui.formatter

import android.content.Context
import fr.dot.library.ui.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

class DateTimeFormatter internal constructor(
    private val monthNames: MonthNames,
    private val dayOfWeekNames: DayOfWeekNames
) {

    val date = LocalDate.Format {
        dayOfWeek(dayOfWeekNames)
        char(' ')
        dayOfMonth()
        char(' ')
        monthName(monthNames)
        char(' ')
        year()
    }

    val time = LocalTime.Format {
        hour()
        char(':')
        minute()
    }

    val dateTime = LocalDateTime.Format {
        date(date)
        char(' ')
        time(time)
    }

    companion object {

        fun withContext(context: Context): DateTimeFormatter = DateTimeFormatter(
            monthNames = MonthNames(
                january = context.getString(R.string.month_january),
                february = context.getString(R.string.month_february),
                march = context.getString(R.string.month_march),
                april = context.getString(R.string.month_april),
                may = context.getString(R.string.month_may),
                june = context.getString(R.string.month_june),
                july = context.getString(R.string.month_july),
                august = context.getString(R.string.month_august),
                september = context.getString(R.string.month_september),
                october = context.getString(R.string.month_october),
                november = context.getString(R.string.month_november),
                december = context.getString(R.string.month_december)
            ),
            dayOfWeekNames = DayOfWeekNames(
                monday = context.getString(R.string.day_monday),
                tuesday = context.getString(R.string.day_tuesday),
                wednesday = context.getString(R.string.day_wednesday),
                thursday = context.getString(R.string.day_thursday),
                friday = context.getString(R.string.day_friday),
                saturday = context.getString(R.string.day_saturday),
                sunday = context.getString(R.string.day_sunday)
            )
        )

    }

}
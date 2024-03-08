package com.example.doctoron.Interface
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.doctoron.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class Dialog_sucess
(private val color: Int, private val dates: Collection<CalendarDay>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan(5F, color)) // Thay đổi kích thước và màu sắc của chấm
    }
}
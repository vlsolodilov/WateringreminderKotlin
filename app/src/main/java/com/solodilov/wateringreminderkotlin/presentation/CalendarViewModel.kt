package com.solodilov.wateringreminderkotlin.presentation

import android.util.Log
import androidx.lifecycle.*
import com.solodilov.wateringreminderkotlin.domain.usecase.*
import com.solodilov.wateringreminderkotlin.extension.LiveEvent
import com.solodilov.wateringreminderkotlin.extension.MutableLiveEvent
import com.solodilov.wateringreminderkotlin.extension.addDays
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getReminderListUseCase: GetReminderListUseCase,
    private val getPlantUseCase: GetPlantUseCase,
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _calendarItemList = MutableLiveData<List<CalendarItem>>()
    val calendarItemList: LiveData<List<CalendarItem>> = _calendarItemList

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    private val _calendarErrorEvent = MutableLiveEvent()
    val calendarErrorEvent: LiveEvent = _calendarErrorEvent

    init {
        getCalendarItemList()
    }

    private fun getCalendarItemList() {
        viewModelScope.launch(exceptionHandler) {
            _loading.value = true
            val calendarItems = getCalendarItemsFromReminders()
            val increasedCalendarItems = increaseCalendarItems(calendarItems)
            _calendarItemList.value = groupCalendarItems(increasedCalendarItems)
            _loading.value = false
            Log.d("TAG", "getCalendarItemList: ${_calendarItemList.value}")
        }
    }

    private suspend fun getCalendarItemsFromReminders(): List<CalendarItem> {
        val reminderList = getReminderListUseCase()
        val calendarItems: MutableList<CalendarItem> = mutableListOf()
        reminderList.forEach { reminder ->
            val plant = getPlantUseCase(reminder.plantId)
            val calendarItem = CalendarItem(
                date = reminder.lastSignalDate,
                description = "${plant.name} - ${reminder.name}",
                period = reminder.signalPeriod
            )
            calendarItems.add(calendarItem)
        }
        return calendarItems
    }

    private fun increaseCalendarItems(calendarItems: List<CalendarItem>): List<CalendarItem> {
        val increasedCalendarItems: MutableList<CalendarItem> = mutableListOf()
        calendarItems.forEach { calendarItem ->
            for (i in 1..5) {
                val nextDate = calendarItem.date.addDays(calendarItem.period * i)
                increasedCalendarItems.add(calendarItem.copy(date = nextDate))
            }
        }
        return increasedCalendarItems
    }

    private fun groupCalendarItems(calendarItems: List<CalendarItem>): List<CalendarItem> =
        calendarItems
            .groupBy({ it.date }, { it.description })
            .toList().map { (date, descriptions) ->
                CalendarItem(
                    date = date,
                    description = descriptions.joinToString("\n")
                )
            }.sortedBy { it.date }

    private fun handleError(error: Throwable) {
        Log.d("TAG", "handleError: $error")
        _loading.value = false
        _calendarErrorEvent()
    }
}

data class CalendarItem(
    val date: Date,
    val description: String,
    val period: Int = 0,
)
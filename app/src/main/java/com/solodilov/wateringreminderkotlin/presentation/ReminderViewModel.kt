package com.solodilov.wateringreminderkotlin.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.usecase.*
import com.solodilov.wateringreminderkotlin.extension.LiveEvent
import com.solodilov.wateringreminderkotlin.extension.MutableLiveEvent
import com.solodilov.wateringreminderkotlin.ui.DateTimeConverter
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ReminderViewModel @Inject constructor(
    private val getReminderUseCase: GetReminderUseCase,
    private val saveReminderUseCase: SaveReminderUseCase,
    private val deleteReminderUseCase: DeleteReminderUseCase,
) : ViewModel() {


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _savedReminder = MutableLiveData<Reminder>()
    val savedReminder: LiveData<Reminder> = _savedReminder

    private val _saveReminderSuccessEvent = MutableLiveEvent()
    val saveReminderSuccessEvent: LiveEvent = _saveReminderSuccessEvent

    private val _reminderErrorEvent = MutableLiveEvent()
    val reminderErrorEvent: LiveEvent = _reminderErrorEvent

    private val _textFieldErrorEvent = MutableLiveEvent()
    val textFieldErrorEvent: LiveEvent = _textFieldErrorEvent

    private val _deleteReminderSuccessEvent = MutableLiveEvent()
    val deleteReminderSuccessEvent: LiveEvent = _deleteReminderSuccessEvent

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }


    fun getSavedReminder(reminderId: Long) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            getReminderUseCase(reminderId).let { reminder ->
                _savedReminder.value = reminder
            }
            _loading.value = false
        }
    }

    fun createReminder(
        name: String,
        signalTime: String,
        signalPeriod: String,
        lastSignalDate: String,
        plantId: Long,
    ) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            if (isDataValid(name)) {
                    saveReminderUseCase(Reminder(
                        name = name,
                        signalTime = DateTimeConverter.getTime(signalTime),
                        signalPeriod = signalPeriod.toInt(),
                        lastSignalDate = DateTimeConverter.getDate(lastSignalDate),
                        plantId = plantId
                    ))
                    _saveReminderSuccessEvent()

            } else {
                _textFieldErrorEvent()
            }
            _loading.value = false
        }
    }

    fun updateReminder(
        id: Long,
        name: String,
        signalTime: String,
        signalPeriod: String,
        lastSignalDate: String,
        plantId: Long,
    ) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            if (isDataValid(name)) {
                saveReminderUseCase(Reminder(
                    id = id,
                    name = name,
                    signalTime = DateTimeConverter.getTime(signalTime),
                    signalPeriod = signalPeriod.toInt(),
                    lastSignalDate = DateTimeConverter.getDate(lastSignalDate),
                    plantId = plantId
                ))
                _saveReminderSuccessEvent()
            } else {
                _textFieldErrorEvent()
            }
            _loading.value = false
        }
    }

    fun deleteReminder(id: Long) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            deleteReminderUseCase(id)
            _loading.value = false
            _deleteReminderSuccessEvent()
        }
    }

    private fun isDataValid(name: String): Boolean =
        name.trim().isNotBlank()

    private fun handleError(error: Throwable) {
        Log.d("TAG", "handleError: $error")
        _loading.value = false
        _reminderErrorEvent()
    }
}
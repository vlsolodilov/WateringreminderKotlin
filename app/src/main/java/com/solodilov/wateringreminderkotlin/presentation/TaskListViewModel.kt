package com.solodilov.wateringreminderkotlin.presentation

import android.util.Log
import androidx.lifecycle.*
import com.solodilov.wateringreminderkotlin.domain.usecase.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val getReminderListUseCase: GetReminderListUseCase,
    private val getPlantUseCase: GetPlantUseCase,
    private val getReminderUseCase: GetReminderUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    init {
        getTaskList()
    }

    private fun getTaskList() {
        viewModelScope.launch(exceptionHandler) {
            val reminderList = getReminderListUseCase()
            val currentTime = Date()
            val tasks: MutableList<Task> = mutableListOf()
            reminderList.forEach { reminder ->
                val plant = getPlantUseCase(reminder.plantId)
                val reminderDate = reminder.lastSignalDate.addDays(reminder.signalPeriod)
                if (currentTime.after(reminderDate)) {
                    val task = Task(
                        reminderId = reminder.id,
                        taskName = reminder.name,
                        plantName = plant.name,
                        imagePlant = plant.imageUri,
                    )
                    tasks.add(task)
                }
            }
            _taskList.value = tasks
            Log.d("TAG", "getTaskList: $tasks")
        }
    }

    fun changeSelectionTask(task: Task) {
        _taskList.value = _taskList.value?.map { t ->
            if (t.reminderId == task.reminderId) {
                t.copy(isSelect = !task.isSelect)
            } else {
                t.copy()
            }
        }
        Log.d("TAG", "changeSelectionTask: ${_taskList.value}")
    }

    fun selectAllTasks() {
        _taskList.value = _taskList.value?.map { task ->
            task.copy(isSelect = true)
        }

        Log.d("TAG", "selectAllTasks: ${_taskList.value}")
    }

    fun executeSelectedTask() {
        viewModelScope.launch(exceptionHandler) {
            _taskList.value?.filter { task -> task.isSelect }?.map { task ->
                val reminder = getReminderUseCase(task.reminderId)
                reminder.lastSignalDate = getCurrentDate()
                updateReminderUseCase(reminder)
            }
            getTaskList()
        }

        Log.d("TAG", "executeSelectedTask: ${_taskList.value}")
    }

    private fun getCurrentDate(): Date =
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time


    private fun handleError(error: Throwable) {
        Log.d("TAG", "handleError: $error")
        _loading.value = false
    }
}

private fun Date.addDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, days)

    return calendar.time
}

data class Task(
    val reminderId: Long,
    val taskName: String,
    val plantName: String,
    val imagePlant: String,
    var isSelect: Boolean = false,
)
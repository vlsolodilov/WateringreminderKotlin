package com.solodilov.wateringreminderkotlin.presentation

import android.util.Log
import androidx.lifecycle.*
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.domain.usecase.*
import com.solodilov.wateringreminderkotlin.extension.LiveEvent
import com.solodilov.wateringreminderkotlin.extension.MutableLiveEvent
import com.solodilov.wateringreminderkotlin.ui.DateTimeConverter
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PlantViewModel @Inject constructor(
    private val getPlantUseCase: GetPlantUseCase,
    private val savePlantUseCase: SavePlantUseCase,
    private val deletePlantUseCase: DeletePlantUseCase,
    private val getReminderListWithPlantIdUseCase: GetReminderListWithPlantIdUseCase,
    private val saveAndDeleteTempRemindersUseCase: SaveAndDeleteTempRemindersUseCase,
) : ViewModel() {


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _savedPlant = MutableLiveData<Plant>()
    val savedPlant: LiveData<Plant> = _savedPlant

    private val _imageUri = MutableLiveData<String>("empty")
    val imageUri: LiveData<String> = _imageUri

    private val _savePlantSuccessEvent = MutableLiveEvent()
    val savePlantSuccessEvent: LiveEvent = _savePlantSuccessEvent

    private val _plantErrorEvent = MutableLiveEvent()
    val plantErrorEvent: LiveEvent = _plantErrorEvent

    private val _textFieldErrorEvent = MutableLiveEvent()
    val textFieldErrorEvent: LiveEvent = _textFieldErrorEvent

    private val _deletePlantSuccessEvent = MutableLiveEvent()
    val deletePlantSuccessEvent: LiveEvent = _deletePlantSuccessEvent

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    fun getReminderList(plantId: Long): LiveData<List<Reminder>> =
        getReminderListWithPlantIdUseCase(plantId).asLiveData()

    fun getSavedPlant(plantId: Long) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            getPlantUseCase(plantId).let { plant ->
                _savedPlant.value = plant
                _imageUri.value = plant.imageUri
            }
            _loading.value = false
        }
    }

    fun createPlant(
        name: String,
        description: String,
        plantingDate: String,
    ) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            if (isDataValid(name)) {
                _imageUri.value?.let { uri ->
                    Plant(
                        name = name,
                        description = description,
                        plantingDate = DateTimeConverter.getDate(plantingDate),
                        imageUri = uri,
                    )
                }?.let { plant ->
                    val id = savePlantUseCase(plant)
                    saveAndDeleteTempRemindersUseCase(id)
                    _savePlantSuccessEvent()
                }
            } else {
                _textFieldErrorEvent()
            }
            _loading.value = false
        }
    }

    fun updatePlant(
        id: Long,
        name: String,
        description: String,
        plantingDate: String,
    ) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            if (isDataValid(name)) {
                _imageUri.value?.let { uri ->
                    Plant(
                        id = id,
                        name = name,
                        description = description,
                        plantingDate = DateTimeConverter.getDate(plantingDate),
                        imageUri = uri,
                    )
                }?.let { plant ->
                    savePlantUseCase(plant)
                    _savePlantSuccessEvent()
                }
            } else {
                _textFieldErrorEvent()
            }
            _loading.value = false
        }
    }

    fun setImageUri(imageUri: String) {
        _imageUri.value = imageUri
    }

    fun deletePlant(id: Long) {
        viewModelScope.launch (exceptionHandler) {
            _loading.value = true
            deletePlantUseCase(id)
            _loading.value = false
            _deletePlantSuccessEvent()
        }
    }

    private fun isDataValid(name: String): Boolean =
        name.trim().isNotBlank()

    private fun handleError(error: Throwable) {
        Log.d("TAG", "handleError: $error")
        _loading.value = false
        _plantErrorEvent()
    }
}
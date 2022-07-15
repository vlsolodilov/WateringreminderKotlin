package com.solodilov.wateringreminderkotlin.presentation

import androidx.lifecycle.*
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.usecase.GetPlantListUseCase
import javax.inject.Inject

class PlantListViewModel @Inject constructor(
    private val getPlantListUseCase: GetPlantListUseCase,
) : ViewModel()  {

    val plantList: LiveData<List<Plant>> = getPlantListUseCase().asLiveData()

}
package com.solodilov.wateringreminderkotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solodilov.wateringreminderkotlin.presentation.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PlantListViewModel::class)
    fun bindPlantListViewModel(viewModel: PlantListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlantViewModel::class)
    fun bindPlantViewModel(viewModel: PlantViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReminderViewModel::class)
    fun bindReminderViewModel(viewModel: ReminderViewModel): ViewModel
}

package com.solodilov.wateringreminderkotlin.di

import android.app.Application
import com.solodilov.wateringreminderkotlin.ui.MainActivity
import com.solodilov.wateringreminderkotlin.ui.fragment.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ContainerFragment)
    fun inject(fragment: TaskListFragment)
    fun inject(fragment: CalendarFragment)
    fun inject(fragment: PlantListFragment)
    fun inject(fragment: PlantFragment)
    fun inject(fragment: ReminderFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}

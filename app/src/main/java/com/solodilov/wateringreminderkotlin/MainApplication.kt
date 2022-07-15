package com.solodilov.wateringreminderkotlin

import android.app.Application
import com.solodilov.wateringreminderkotlin.di.DaggerApplicationComponent

class MainApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
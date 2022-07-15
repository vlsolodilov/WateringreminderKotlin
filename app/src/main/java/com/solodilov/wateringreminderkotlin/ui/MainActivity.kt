package com.solodilov.wateringreminderkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.solodilov.wateringreminderkotlin.MainApplication
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.ui.fragment.ContainerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MainApplication).component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("TAG", "onCreate: MainActivity")
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.mainContainer, ContainerFragment.newInstance())
                .commit()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if (fragment is ContainerFragment) {
            finishAffinity()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
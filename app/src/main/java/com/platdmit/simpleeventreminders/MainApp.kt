package com.platdmit.simpleeventreminders

import android.app.Application
import androidx.room.Room
import com.platdmit.simpleeventreminders.data.database.DbManager
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid
@HiltAndroidApp
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //Init time library
        JodaTimeAndroid.init(this)
    }
}
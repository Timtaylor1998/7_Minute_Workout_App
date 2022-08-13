package eu.tutorials.a7minuteworkoutapp

import android.app.Application

class WorkoutHistoryApplication: Application() {
    val db by lazy {
        WorkoutHistoryDataBase.getInstance(this)
    }
}
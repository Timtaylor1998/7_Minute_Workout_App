package eu.tutorials.a7minuteworkoutapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkoutHistoryEntity::class], version = 1)
abstract class WorkoutHistoryDataBase:RoomDatabase() {

    abstract fun workoutHistoryDao():WorkoutHistoryDao

    companion object{

        @Volatile
        private var INSTANCE: WorkoutHistoryDataBase? = null

        fun getInstance(context: Context):WorkoutHistoryDataBase{

            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkoutHistoryDataBase::class.java,
                        "WorkoutHistory_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }

        }

    }

}
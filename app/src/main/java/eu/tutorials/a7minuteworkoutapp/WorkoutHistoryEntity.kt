package eu.tutorials.a7minuteworkoutapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workoutHistory-table")
data class WorkoutHistoryEntity(
    @PrimaryKey
    val date: String = ""
)

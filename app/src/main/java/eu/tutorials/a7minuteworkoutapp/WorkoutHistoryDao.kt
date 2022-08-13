package eu.tutorials.a7minuteworkoutapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {

    @Insert
    suspend fun insert(workoutHistoryEntity: WorkoutHistoryEntity)

    @Query(value = "SELECT * FROM 'workoutHistory-table'")
    fun fetchAllDates():Flow<List<WorkoutHistoryEntity>>
/*
    @Update
    suspend fun update(workoutHistoryEntity: WorkoutHistoryEntity)

    @Delete
    suspend fun delete(workoutHistoryEntity: WorkoutHistoryEntity)

    @Query(value = "SELECT * FROM 'workoutHistory-table'")
    fun fetchAllWorkoutHistories():Flow<List<WorkoutHistoryEntity>>

    @Query(value = "SELECT * FROM 'workoutHistory-table' where id=id")
    fun fetchWorkoutHistoryById(id:Int):Flow<WorkoutHistoryEntity>


 */
}
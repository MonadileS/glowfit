package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.WorkoutLog
import com.example.data.model.WorkoutRoutine
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
  @Query("SELECT * FROM workout_routines ORDER BY isFeatured DESC, title ASC")
  fun getAllRoutines(): Flow<List<WorkoutRoutine>>

  @Query("SELECT * FROM workout_routines WHERE id = :id")
  suspend fun getRoutineById(id: String): WorkoutRoutine?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRoutines(routines: List<WorkoutRoutine>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRoutine(routine: WorkoutRoutine)

  @Query("DELETE FROM workout_routines WHERE id = :id")
  suspend fun deleteRoutine(id: String)

  // Logs
  @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
  fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

  @Query("SELECT * FROM workout_logs WHERE dateString = :dateString")
  fun getWorkoutLogsForDate(dateString: String): Flow<List<WorkoutLog>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWorkoutLog(log: WorkoutLog): Long

  @Query("SELECT COUNT(*) FROM workout_logs")
  suspend fun getWorkoutCount(): Int

  @Query("SELECT SUM(durationMinutes) FROM workout_logs")
  suspend fun getTotalMinutes(): Int?

  @Query("SELECT SUM(caloriesBurned) FROM workout_logs")
  suspend fun getTotalCalories(): Int?
}

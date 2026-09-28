package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_routines")
data class WorkoutRoutine(
  @PrimaryKey val id: String,
  val title: String,
  val subtitle: String,
  val category: ExerciseCategory,
  val difficulty: DifficultyLevel,
  val estimatedMinutes: Int,
  val estimatedCalories: Int,
  val exerciseIds: List<String>,
  val isCustom: Boolean = false,
  val isFeatured: Boolean = false,
  val bannerColorHex: String = "#FF80AB",
  val dayCount: Int = 1, // e.g. 21-day challenge or single session
  val description: String = ""
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateString: String, // "YYYY-MM-DD"
  val timestamp: Long = System.currentTimeMillis(),
  val routineId: String,
  val routineTitle: String,
  val category: String,
  val durationMinutes: Int,
  val caloriesBurned: Int,
  val exercisesCompleted: Int,
  val moodAtCompletion: String? = null
)

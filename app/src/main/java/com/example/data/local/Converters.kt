package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.DifficultyLevel
import com.example.data.model.ExerciseCategory
import com.example.data.model.MoodType

class Converters {
  @TypeConverter
  fun fromStringList(value: List<String>?): String {
    return value?.joinToString("|||") ?: ""
  }

  @TypeConverter
  fun toStringList(value: String?): List<String> {
    if (value.isNullOrBlank()) return emptyList()
    return value.split("|||").filter { it.isNotBlank() }
  }

  @TypeConverter
  fun fromCategory(value: ExerciseCategory?): String {
    return value?.name ?: ExerciseCategory.FULL_BODY.name
  }

  @TypeConverter
  fun toCategory(value: String?): ExerciseCategory {
    return try {
      if (value != null) ExerciseCategory.valueOf(value) else ExerciseCategory.FULL_BODY
    } catch (e: Exception) {
      ExerciseCategory.FULL_BODY
    }
  }

  @TypeConverter
  fun fromDifficulty(value: DifficultyLevel?): String {
    return value?.name ?: DifficultyLevel.BEGINNER.name
  }

  @TypeConverter
  fun toDifficulty(value: String?): DifficultyLevel {
    return try {
      if (value != null) DifficultyLevel.valueOf(value) else DifficultyLevel.BEGINNER
    } catch (e: Exception) {
      DifficultyLevel.BEGINNER
    }
  }

  @TypeConverter
  fun fromMoodType(value: MoodType?): String {
    return value?.name ?: MoodType.HAPPY.name
  }

  @TypeConverter
  fun toMoodType(value: String?): MoodType {
    return try {
      if (value != null) MoodType.valueOf(value) else MoodType.HAPPY
    } catch (e: Exception) {
      MoodType.HAPPY
    }
  }
}

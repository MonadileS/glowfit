package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.Badge
import com.example.data.model.MoodLog
import com.example.data.model.SocialPost
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutLog
import com.example.data.model.WorkoutRoutine

@Database(
  entities = [
    WorkoutRoutine::class,
    WorkoutLog::class,
    MoodLog::class,
    Badge::class,
    SocialPost::class,
    UserProfile::class
  ],
  version = 1,
  exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun workoutDao(): WorkoutDao
  abstract fun moodDao(): MoodDao
  abstract fun gamificationDao(): GamificationDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "glowfit_database.db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}

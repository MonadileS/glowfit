package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.MoodLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
  @Query("SELECT * FROM mood_logs ORDER BY dateString DESC, timestamp DESC")
  fun getAllMoodLogs(): Flow<List<MoodLog>>

  @Query("SELECT * FROM mood_logs WHERE dateString = :dateString ORDER BY timestamp DESC LIMIT 1")
  fun getMoodForDate(dateString: String): Flow<MoodLog?>

  @Query("SELECT * FROM mood_logs WHERE dateString = :dateString ORDER BY timestamp DESC LIMIT 1")
  suspend fun getMoodForDateSync(dateString: String): MoodLog?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMoodLog(moodLog: MoodLog): Long

  @Query("DELETE FROM mood_logs WHERE id = :id")
  suspend fun deleteMoodLog(id: Long)
}

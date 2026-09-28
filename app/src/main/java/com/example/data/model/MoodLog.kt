package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MoodType(val displayName: String, val emoji: String, val colorHex: String) {
  ENERGIZED("Energized", "🌸", "#FF6B8B"),
  HAPPY("Happy & Glowing", "✨", "#FFB300"),
  CALM("Calm & Peaceful", "☁️", "#64B5F6"),
  TIRED("Tired / Low Energy", "🌙", "#9575CD"),
  STRESSED("Stressed / Anxious", "🌧️", "#90A4AE"),
  SORE("Sore & Strong", "🎀", "#EC407A"),
  BLOATED("Bloated / Heavy", "🌷", "#BA68C8")
}

@Entity(tableName = "mood_logs")
data class MoodLog(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val dateString: String, // "YYYY-MM-DD"
  val timestamp: Long = System.currentTimeMillis(),
  val moodType: MoodType,
  val energyLevel: Int = 3, // 1 to 5
  val note: String = "",
  val tags: List<String> = emptyList(), // e.g. "Morning Stretch", "Hydrated", "Good Sleep"
  val hadWorkout: Boolean = false
)

package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class Badge(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val iconEmoji: String,
  val category: String, // "Streak", "Workout", "Mood", "Milestone"
  val isUnlocked: Boolean = false,
  val unlockedAtTimestamp: Long? = null,
  val currentProgress: Int = 0,
  val maxProgress: Int = 1,
  val rewardXp: Int = 100
)

@Entity(tableName = "social_posts")
data class SocialPost(
  @PrimaryKey val id: String,
  val authorName: String,
  val authorAvatarEmoji: String,
  val authorBadge: String = "🌸 Glow Girl",
  val timestamp: Long,
  val content: String,
  val workoutTitle: String? = null,
  val workoutMinutes: Int? = null,
  val streakDays: Int? = null,
  val heartsCount: Int = 0,
  val isLikedByMe: Boolean = false,
  val cheersCount: Int = 0,
  val isCheeredByMe: Boolean = false,
  val commentsJson: List<String> = emptyList(),
  val tag: String = "#DailyGlow"
)

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey val id: Int = 1,
  val name: String = "Glow Girl",
  val avatarEmoji: String = "🌸",
  val bio: String = "Blooming into my strongest, happiest self ✨",
  val fitnessGoal: String = "Tone & Energy",
  val targetDailyMinutes: Int = 20,
  val currentStreak: Int = 3,
  val longestStreak: Int = 7,
  val lastActiveDate: String = "",
  val totalMinutesWorkedOut: Int = 185,
  val totalWorkoutsCompleted: Int = 12,
  val totalCaloriesBurned: Int = 1420,
  val totalXp: Int = 450,
  val level: Int = 3,
  val soundEnabled: Boolean = true,
  val darkModeEnabled: Boolean = false,
  // Body tracking
  val weightKg: Float = 55.0f,
  val waistCm: Float = 68.0f,
  val hipsCm: Float = 94.0f,
  val selectedLanguage: String = "en"
)

package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.DefaultPrograms
import com.example.data.local.ExerciseCatalog
import com.example.data.model.Badge
import com.example.data.model.DifficultyLevel
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import com.example.data.model.MoodLog
import com.example.data.model.MoodType
import com.example.data.model.SocialPost
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutLog
import com.example.data.model.WorkoutRoutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class GlowFitRepository(private val database: AppDatabase) {

  private val workoutDao = database.workoutDao()
  private val moodDao = database.moodDao()
  private val gamificationDao = database.gamificationDao()

  val allRoutines: Flow<List<WorkoutRoutine>> = workoutDao.getAllRoutines()
  val allWorkoutLogs: Flow<List<WorkoutLog>> = workoutDao.getAllWorkoutLogs()
  val allMoodLogs: Flow<List<MoodLog>> = moodDao.getAllMoodLogs()
  val allBadges: Flow<List<Badge>> = gamificationDao.getAllBadges()
  val allSocialPosts: Flow<List<SocialPost>> = gamificationDao.getAllSocialPosts()
  val userProfile: Flow<UserProfile?> = gamificationDao.getUserProfile()

  fun getTodayDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
  }

  suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
    // 1. Check Routines
    val existingRoutines = workoutDao.getAllRoutines().firstOrNull()
    if (existingRoutines.isNullOrEmpty()) {
      workoutDao.insertRoutines(DefaultPrograms.defaultRoutines)
    }

    // 2. Check Badges
    val existingBadges = gamificationDao.getAllBadges().firstOrNull()
    if (existingBadges.isNullOrEmpty()) {
      gamificationDao.insertBadges(DefaultPrograms.initialBadges)
    }

    // 3. Check User Profile
    val existingProfile = gamificationDao.getUserProfileSync()
    if (existingProfile == null) {
      val defaultProfile = UserProfile(
        id = 1,
        name = "Glow Girl",
        avatarEmoji = "🌸",
        bio = "Blooming into my strongest, happiest self ✨",
        fitnessGoal = "Tone & Energy",
        targetDailyMinutes = 15,
        currentStreak = 3,
        longestStreak = 7,
        lastActiveDate = getTodayDateString(),
        totalMinutesWorkedOut = 145,
        totalWorkoutsCompleted = 10,
        totalCaloriesBurned = 1120,
        totalXp = 450,
        level = 3
      )
      gamificationDao.insertOrUpdateProfile(defaultProfile)
    }

    // 4. Check Social Posts
    val existingPosts = gamificationDao.getAllSocialPosts().firstOrNull()
    if (existingPosts.isNullOrEmpty()) {
      gamificationDao.insertSocialPosts(DefaultPrograms.initialSocialPosts)
    }

    // 5. Seed initial Mood & Workout history for realistic initial charts
    val existingLogs = workoutDao.getAllWorkoutLogs().firstOrNull()
    if (existingLogs.isNullOrEmpty()) {
      val calendar = Calendar.getInstance()
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      
      // Yesterday
      calendar.add(Calendar.DAY_OF_YEAR, -1)
      val date1 = sdf.format(calendar.time)
      workoutDao.insertWorkoutLog(
        WorkoutLog(
          dateString = date1,
          timestamp = calendar.timeInMillis,
          routineId = "prog_belly_shape",
          routineTitle = "Belly Shape & Hourglass Core",
          category = ExerciseCategory.BELLY_CORE.name,
          durationMinutes = 10,
          caloriesBurned = 85,
          exercisesCompleted = 6,
          moodAtCompletion = "Energized 🌸"
        )
      )
      moodDao.insertMoodLog(
        MoodLog(
          dateString = date1,
          timestamp = calendar.timeInMillis,
          moodType = MoodType.ENERGIZED,
          energyLevel = 5,
          note = "Felt super strong after doing the belly circuit!",
          tags = listOf("Energized", "Post-Workout Glow"),
          hadWorkout = true
        )
      )

      // 2 days ago
      calendar.add(Calendar.DAY_OF_YEAR, -1)
      val date2 = sdf.format(calendar.time)
      workoutDao.insertWorkoutLog(
        WorkoutLog(
          dateString = date2,
          timestamp = calendar.timeInMillis,
          routineId = "prog_booty_sculpt",
          routineTitle = "Booty Lift & Peach Sculpt",
          category = ExerciseCategory.BOOTY_LEGS.name,
          durationMinutes = 12,
          caloriesBurned = 95,
          exercisesCompleted = 6,
          moodAtCompletion = "Happy & Glowing ✨"
        )
      )
      moodDao.insertMoodLog(
        MoodLog(
          dateString = date2,
          timestamp = calendar.timeInMillis,
          moodType = MoodType.HAPPY,
          energyLevel = 4,
          note = "The glute bridges burn was amazing 🍑",
          tags = listOf("Happy", "Hydrated"),
          hadWorkout = true
        )
      )
    }
  }

  suspend fun logWorkoutCompletion(
    routine: WorkoutRoutine,
    actualDurationMinutes: Int,
    moodNote: String?
  ): WorkoutLog = withContext(Dispatchers.IO) {
    val today = getTodayDateString()
    val log = WorkoutLog(
      dateString = today,
      timestamp = System.currentTimeMillis(),
      routineId = routine.id,
      routineTitle = routine.title,
      category = routine.category.name,
      durationMinutes = actualDurationMinutes,
      caloriesBurned = routine.estimatedCalories,
      exercisesCompleted = routine.exerciseIds.size,
      moodAtCompletion = moodNote
    )
    val insertedId = workoutDao.insertWorkoutLog(log)

    // Update Profile Stats & Streak
    val currentProfile = gamificationDao.getUserProfileSync() ?: UserProfile()
    val lastDate = currentProfile.lastActiveDate
    val newStreak = if (lastDate == today) {
      currentProfile.currentStreak // Already logged today
    } else {
      // Check if yesterday
      val cal = Calendar.getInstance()
      cal.add(Calendar.DAY_OF_YEAR, -1)
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      val yesterday = sdf.format(cal.time)
      if (lastDate == yesterday) {
        currentProfile.currentStreak + 1
      } else if (lastDate.isEmpty()) {
        1
      } else {
        1 // Reset streak
      }
    }
    val longest = maxOf(newStreak, currentProfile.longestStreak)
    val newMinutes = currentProfile.totalMinutesWorkedOut + actualDurationMinutes
    val newWorkouts = currentProfile.totalWorkoutsCompleted + 1
    val newCalories = currentProfile.totalCaloriesBurned + routine.estimatedCalories
    val earnedXp = 50 + (actualDurationMinutes * 5)
    val updatedXp = currentProfile.totalXp + earnedXp
    val newLevel = 1 + (updatedXp / 200)

    val updatedProfile = currentProfile.copy(
      currentStreak = newStreak,
      longestStreak = longest,
      lastActiveDate = today,
      totalMinutesWorkedOut = newMinutes,
      totalWorkoutsCompleted = newWorkouts,
      totalCaloriesBurned = newCalories,
      totalXp = updatedXp,
      level = newLevel
    )
    gamificationDao.insertOrUpdateProfile(updatedProfile)

    // Check & Update Badges
    checkAndUpdateBadges(newStreak, newWorkouts, newCalories, routine.category)

    // If mood specified, log mood as well
    if (!moodNote.isNullOrBlank()) {
      moodDao.insertMoodLog(
        MoodLog(
          dateString = today,
          timestamp = System.currentTimeMillis(),
          moodType = MoodType.ENERGIZED,
          energyLevel = 5,
          note = "Post workout: $moodNote",
          tags = listOf("Post-Workout Glow", routine.title),
          hadWorkout = true
        )
      )
    }

    log.copy(id = insertedId)
  }

  private suspend fun checkAndUpdateBadges(
    streak: Int,
    totalWorkouts: Int,
    calories: Int,
    category: ExerciseCategory
  ) {
    val badges = gamificationDao.getAllBadges().firstOrNull() ?: return
    val updatedBadges = badges.map { badge ->
      var unlocked = badge.isUnlocked
      var progress = badge.currentProgress
      var unlockedTimestamp = badge.unlockedAtTimestamp

      when (badge.id) {
        "badge_first_workout" -> {
          progress = minOf(totalWorkouts, badge.maxProgress)
          if (totalWorkouts >= 1 && !unlocked) {
            unlocked = true
            unlockedTimestamp = System.currentTimeMillis()
          }
        }
        "badge_streak_3" -> {
          progress = minOf(streak, badge.maxProgress)
          if (streak >= 3 && !unlocked) {
            unlocked = true
            unlockedTimestamp = System.currentTimeMillis()
          }
        }
        "badge_streak_7" -> {
          progress = minOf(streak, badge.maxProgress)
          if (streak >= 7 && !unlocked) {
            unlocked = true
            unlockedTimestamp = System.currentTimeMillis()
          }
        }
        "badge_booty_queen" -> {
          if (category == ExerciseCategory.BOOTY_LEGS) {
            progress = minOf(progress + 1, badge.maxProgress)
            if (progress >= badge.maxProgress && !unlocked) {
              unlocked = true
              unlockedTimestamp = System.currentTimeMillis()
            }
          }
        }
        "badge_calorie_1000" -> {
          progress = minOf(calories, badge.maxProgress)
          if (calories >= 1000 && !unlocked) {
            unlocked = true
            unlockedTimestamp = System.currentTimeMillis()
          }
        }
        "badge_night_owl" -> {
          if (category == ExerciseCategory.STRETCH_POSTURE) {
            progress = 1
            if (!unlocked) {
              unlocked = true
              unlockedTimestamp = System.currentTimeMillis()
            }
          }
        }
      }
      badge.copy(
        isUnlocked = unlocked,
        currentProgress = progress,
        unlockedAtTimestamp = unlockedTimestamp
      )
    }
    gamificationDao.insertBadges(updatedBadges)
  }

  suspend fun logMood(
    moodType: MoodType,
    energyLevel: Int,
    note: String,
    tags: List<String>
  ) = withContext(Dispatchers.IO) {
    val today = getTodayDateString()
    val moodLog = MoodLog(
      dateString = today,
      timestamp = System.currentTimeMillis(),
      moodType = moodType,
      energyLevel = energyLevel,
      note = note,
      tags = tags,
      hadWorkout = false
    )
    moodDao.insertMoodLog(moodLog)

    // Check Mood Master Badge
    val allMoods = moodDao.getAllMoodLogs().firstOrNull() ?: emptyList()
    val distinctDates = allMoods.map { it.dateString }.distinct().size
    val badges = gamificationDao.getAllBadges().firstOrNull() ?: emptyList()
    badges.find { it.id == "badge_mood_master" }?.let { badge ->
      val progress = minOf(distinctDates, badge.maxProgress)
      val unlocked = distinctDates >= badge.maxProgress
      gamificationDao.updateBadge(
        badge.copy(
          currentProgress = progress,
          isUnlocked = unlocked || badge.isUnlocked,
          unlockedAtTimestamp = if (unlocked && !badge.isUnlocked) System.currentTimeMillis() else badge.unlockedAtTimestamp
        )
      )
    }
  }

  suspend fun createCustomRoutine(
    title: String,
    subtitle: String,
    category: ExerciseCategory,
    difficulty: DifficultyLevel,
    exerciseIds: List<String>,
    estimatedMinutes: Int,
    bannerColorHex: String = "#FF80AB"
  ): WorkoutRoutine = withContext(Dispatchers.IO) {
    val newRoutine = WorkoutRoutine(
      id = "custom_" + UUID.randomUUID().toString().take(8),
      title = title,
      subtitle = subtitle,
      category = category,
      difficulty = difficulty,
      estimatedMinutes = estimatedMinutes,
      estimatedCalories = estimatedMinutes * 8,
      exerciseIds = exerciseIds,
      isCustom = true,
      bannerColorHex = bannerColorHex,
      description = "Personalized custom routine crafted for you."
    )
    workoutDao.insertRoutine(newRoutine)
    newRoutine
  }

  suspend fun deleteRoutine(id: String) = withContext(Dispatchers.IO) {
    workoutDao.deleteRoutine(id)
  }

  suspend fun likeSocialPost(postId: String) = withContext(Dispatchers.IO) {
    val posts = gamificationDao.getAllSocialPosts().firstOrNull() ?: return@withContext
    val post = posts.find { it.id == postId } ?: return@withContext
    val newLiked = !post.isLikedByMe
    val newCount = if (newLiked) post.heartsCount + 1 else maxOf(0, post.heartsCount - 1)
    gamificationDao.updateSocialPost(
      post.copy(isLikedByMe = newLiked, heartsCount = newCount)
    )
  }

  suspend fun cheerSocialPost(postId: String) = withContext(Dispatchers.IO) {
    val posts = gamificationDao.getAllSocialPosts().firstOrNull() ?: return@withContext
    val post = posts.find { it.id == postId } ?: return@withContext
    val newCheered = !post.isCheeredByMe
    val newCount = if (newCheered) post.cheersCount + 1 else maxOf(0, post.cheersCount - 1)
    gamificationDao.updateSocialPost(
      post.copy(isCheeredByMe = newCheered, cheersCount = newCount)
    )
  }

  suspend fun addCommentToPost(postId: String, commentText: String) = withContext(Dispatchers.IO) {
    val posts = gamificationDao.getAllSocialPosts().firstOrNull() ?: return@withContext
    val post = posts.find { it.id == postId } ?: return@withContext
    val profile = gamificationDao.getUserProfileSync()
    val author = profile?.name ?: "You"
    val newComment = "$author: $commentText"
    val updatedComments = post.commentsJson + newComment
    gamificationDao.updateSocialPost(
      post.copy(commentsJson = updatedComments)
    )
  }

  suspend fun createSocialPost(
    content: String,
    workoutTitle: String?,
    workoutMinutes: Int?,
    streakDays: Int?,
    tag: String
  ) = withContext(Dispatchers.IO) {
    val profile = gamificationDao.getUserProfileSync()
    val newPost = SocialPost(
      id = "post_" + UUID.randomUUID().toString().take(8),
      authorName = profile?.name ?: "Glow Girl",
      authorAvatarEmoji = profile?.avatarEmoji ?: "🌸",
      authorBadge = "✨ Level ${profile?.level ?: 1} Glow",
      timestamp = System.currentTimeMillis(),
      content = content,
      workoutTitle = workoutTitle,
      workoutMinutes = workoutMinutes,
      streakDays = streakDays,
      heartsCount = 1,
      isLikedByMe = true,
      tag = tag
    )
    gamificationDao.insertSocialPost(newPost)
  }

  suspend fun updateProfile(
    name: String,
    bio: String,
    avatarEmoji: String,
    fitnessGoal: String,
    targetDailyMinutes: Int,
    darkModeEnabled: Boolean,
    soundEnabled: Boolean,
    weightKg: Float,
    waistCm: Float,
    hipsCm: Float
  ) = withContext(Dispatchers.IO) {
    val current = gamificationDao.getUserProfileSync() ?: UserProfile()
    val updated = current.copy(
      name = name,
      bio = bio,
      avatarEmoji = avatarEmoji,
      fitnessGoal = fitnessGoal,
      targetDailyMinutes = targetDailyMinutes,
      darkModeEnabled = darkModeEnabled,
      soundEnabled = soundEnabled,
      weightKg = weightKg,
      waistCm = waistCm,
      hipsCm = hipsCm
    )
    gamificationDao.insertOrUpdateProfile(updated)
  }
}

package com.example.data.local

import com.example.data.model.Badge
import com.example.data.model.DifficultyLevel
import com.example.data.model.ExerciseCategory
import com.example.data.model.SocialPost
import com.example.data.model.WorkoutRoutine

object DefaultPrograms {
  val defaultRoutines = listOf(
    WorkoutRoutine(
      id = "prog_21day_glow",
      title = "21-Day Glow Up Challenge",
      subtitle = "Full body toning, hourglass shape & radiant energy",
      category = ExerciseCategory.FULL_BODY,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedMinutes = 15,
      estimatedCalories = 120,
      exerciseIds = listOf(
        "ex_jumping_jacks",
        "ex_sumo_squats",
        "ex_russian_twist",
        "ex_glute_bridges",
        "ex_knee_pushups",
        "ex_arm_circles",
        "ex_cat_cow"
      ),
      isCustom = false,
      isFeatured = true,
      bannerColorHex = "#FF4081",
      dayCount = 21,
      description = "Our flagship transformative routine designed specifically for women. Build confidence, lean curves, and sustainable daily habits."
    ),
    WorkoutRoutine(
      id = "prog_belly_shape",
      title = "Belly Shape & Hourglass Core",
      subtitle = "Target lower belly, cinch waistline & tone obliques",
      category = ExerciseCategory.BELLY_CORE,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedMinutes = 10,
      estimatedCalories = 85,
      exerciseIds = listOf(
        "ex_russian_twist",
        "ex_heel_touches",
        "ex_plank_hip_dips",
        "ex_flutter_kicks",
        "ex_dead_bug",
        "ex_cobra_stretch"
      ),
      isCustom = false,
      isFeatured = true,
      bannerColorHex = "#EC407A",
      dayCount = 7,
      description = "Gentle yet intense waist-slimming circuit with zero bulk. Focuses on deep transverse core activation."
    ),
    WorkoutRoutine(
      id = "prog_booty_sculpt",
      title = "Booty Lift & Peach Sculpt",
      subtitle = "Lift, round, and firm glutes without bulky legs",
      category = ExerciseCategory.BOOTY_LEGS,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedMinutes = 12,
      estimatedCalories = 95,
      exerciseIds = listOf(
        "ex_glute_bridges",
        "ex_donkey_kicks",
        "ex_fire_hydrants",
        "ex_frog_pumps",
        "ex_clamshells",
        "ex_butterfly_stretch"
      ),
      isCustom = false,
      isFeatured = true,
      bannerColorHex = "#AB47BC",
      dayCount = 14,
      description = "Zero-equipment glute burn targeting the upper shelf and side hips for that cute natural peach contour."
    ),
    WorkoutRoutine(
      id = "prog_toned_arms_posture",
      title = "Swan Arms & Posture Tone",
      subtitle = "Sculpt graceful shoulders, back & eliminate bra lines",
      category = ExerciseCategory.TONED_ARMS,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedMinutes = 8,
      estimatedCalories = 65,
      exerciseIds = listOf(
        "ex_arm_circles",
        "ex_tricep_dips",
        "ex_knee_pushups",
        "ex_superman_hold",
        "ex_cat_cow"
      ),
      isCustom = false,
      isFeatured = false,
      bannerColorHex = "#7E57C2",
      dayCount = 7,
      description = "Ballet-inspired sequence that improves posture, opens the collarbones, and tones delicate arms."
    ),
    WorkoutRoutine(
      id = "prog_bedtime_relax",
      title = "Bedtime De-Stress & Stretch",
      subtitle = "Melt anxiety, relax tight hips & sleep soundly",
      category = ExerciseCategory.STRETCH_POSTURE,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedMinutes = 10,
      estimatedCalories = 40,
      exerciseIds = listOf(
        "ex_cat_cow",
        "ex_child_pose",
        "ex_butterfly_stretch",
        "ex_cobra_stretch"
      ),
      isCustom = false,
      isFeatured = false,
      bannerColorHex = "#5C6BC0",
      dayCount = 1,
      description = "Soothing nighttime mobility session to calm the nervous system and release daily emotional tension."
    ),
    WorkoutRoutine(
      id = "prog_hiit_fat_burn",
      title = "Quick 7-Min Glow Sweat",
      subtitle = "High-energy calorie burn & mood elevation",
      category = ExerciseCategory.CARDIO_BURN,
      difficulty = DifficultyLevel.INTERMEDIATE,
      estimatedMinutes = 7,
      estimatedCalories = 80,
      exerciseIds = listOf(
        "ex_jumping_jacks",
        "ex_skater_hops",
        "ex_mountain_climbers",
        "ex_sumo_squats",
        "ex_child_pose"
      ),
      isCustom = false,
      isFeatured = false,
      bannerColorHex = "#FF7043",
      dayCount = 1,
      description = "Short, energizing endorphin booster when you're short on time but need a burst of sparkle!"
    )
  )

  val initialBadges = listOf(
    Badge(
      id = "badge_first_workout",
      title = "First Sweat 🌸",
      description = "Completed your very first workout session!",
      iconEmoji = "🌸",
      category = "Workout",
      isUnlocked = true,
      unlockedAtTimestamp = System.currentTimeMillis() - 86400000L * 3,
      currentProgress = 1,
      maxProgress = 1,
      rewardXp = 100
    ),
    Badge(
      id = "badge_streak_3",
      title = "3-Day Blossom 🌿",
      description = "Maintained a 3-day active workout streak!",
      iconEmoji = "🌿",
      category = "Streak",
      isUnlocked = true,
      unlockedAtTimestamp = System.currentTimeMillis() - 86400000L,
      currentProgress = 3,
      maxProgress = 3,
      rewardXp = 150
    ),
    Badge(
      id = "badge_streak_7",
      title = "7-Day Queen 👑",
      description = "Completed a full week streak of self-love & movement.",
      iconEmoji = "👑",
      category = "Streak",
      isUnlocked = false,
      currentProgress = 3,
      maxProgress = 7,
      rewardXp = 300
    ),
    Badge(
      id = "badge_booty_queen",
      title = "Peach Princess 🍑",
      description = "Finish 5 Booty & Leg sculpting workouts.",
      iconEmoji = "🍑",
      category = "Workout",
      isUnlocked = false,
      currentProgress = 2,
      maxProgress = 5,
      rewardXp = 200
    ),
    Badge(
      id = "badge_mood_master",
      title = "Mindful Glow ✨",
      description = "Log your daily mood & feelings 5 days in a row.",
      iconEmoji = "✨",
      category = "Mood",
      isUnlocked = false,
      currentProgress = 3,
      maxProgress = 5,
      rewardXp = 150
    ),
    Badge(
      id = "badge_calorie_1000",
      title = "1,000 Kcal Club 🔥",
      description = "Burn over 1,000 total active calories in GlowFit.",
      iconEmoji = "🔥",
      category = "Milestone",
      isUnlocked = true,
      unlockedAtTimestamp = System.currentTimeMillis() - 3600000L * 12,
      currentProgress = 1420,
      maxProgress = 1000,
      rewardXp = 250
    ),
    Badge(
      id = "badge_night_owl",
      title = "Moonlight Serenity 🌙",
      description = "Complete a calming Bedtime Stretch session.",
      iconEmoji = "🌙",
      category = "Workout",
      isUnlocked = false,
      currentProgress = 0,
      maxProgress = 1,
      rewardXp = 100
    )
  )

  val initialSocialPosts = listOf(
    SocialPost(
      id = "post_1",
      authorName = "Chloe M.",
      authorAvatarEmoji = "🎀",
      authorBadge = "👑 7-Day Queen",
      timestamp = System.currentTimeMillis() - 1000 * 60 * 25, // 25 mins ago
      content = "Just crushed Day 4 of the 21-Day Glow Up Challenge! Feeling so energized today 💕 You girls got this!",
      workoutTitle = "21-Day Glow Up Challenge",
      workoutMinutes = 15,
      streakDays = 4,
      heartsCount = 28,
      cheersCount = 14,
      commentsJson = listOf(
        "Emma: Keep shining babe! ✨",
        "Sophie: Wow so proud of you! Adding this to my evening routine."
      ),
      tag = "#GlowSquad"
    ),
    SocialPost(
      id = "post_2",
      authorName = "Maya R.",
      authorAvatarEmoji = "🌸",
      authorBadge = "🍑 Peach Princess",
      timestamp = System.currentTimeMillis() - 1000 * 60 * 95, // 1.5 hrs ago
      content = "The Booty Lift routine was NO JOKE today 😭🔥 My glutes are definitely feeling the burn! Don't skip the butterfly stretch afterwards!",
      workoutTitle = "Booty Lift & Peach Sculpt",
      workoutMinutes = 12,
      streakDays = 6,
      heartsCount = 42,
      cheersCount = 19,
      commentsJson = listOf(
        "Zara: The frog pumps are my favorite part!!",
        "Chloe: That burn is where the magic happens 🍑💖"
      ),
      tag = "#BootyGlow"
    ),
    SocialPost(
      id = "post_3",
      authorName = "Elena K.",
      authorAvatarEmoji = "✨",
      authorBadge = "🧘‍♀️ Zen Goddess",
      timestamp = System.currentTimeMillis() - 1000 * 60 * 240, // 4 hrs ago
      content = "Had such a stressful day at work, but the Bedtime De-Stress session completely reset my mood. Remember to be gentle with yourself today ☁️🌸",
      workoutTitle = "Bedtime De-Stress & Stretch",
      workoutMinutes = 10,
      streakDays = 12,
      heartsCount = 65,
      cheersCount = 31,
      commentsJson = listOf(
        "Lily: Needed this reminder so much today. Thank you queen 💖"
      ),
      tag = "#MindfulMovement"
    )
  )
}

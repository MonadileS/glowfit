package com.example.data.model

enum class ExerciseCategory(val displayName: String, val emoji: String) {
  BELLY_CORE("Belly & Core", "🌸"),
  BOOTY_LEGS("Booty & Legs", "🍑"),
  TONED_ARMS("Toned Arms", "💪"),
  HOURGLASS("Hourglass Waist", "⏳"),
  FULL_BODY("Full Body Glow", "✨"),
  STRETCH_POSTURE("Stretch & Posture", "🧘‍♀️"),
  CARDIO_BURN("Cardio & Fat Burn", "🔥")
}

enum class DifficultyLevel(val displayName: String) {
  BEGINNER("Beginner"),
  INTERMEDIATE("Intermediate"),
  ADVANCED("Advanced")
}

enum class ExerciseLanguage(val code: String, val displayName: String, val flag: String) {
  ENGLISH("en", "English", "🇬🇧"),
  SPANISH("es", "Español", "🇪🇸"),
  FRENCH("fr", "Français", "🇫🇷"),
  ITALIAN("it", "Italiano", "🇮🇹"),
  TURKISH("tr", "Türkçe", "🇹🇷"),
  RUSSIAN("ru", "Русский", "🇷🇺"),
  CHINESE("zh", "中文", "🇨🇳"),
  HINDI("hi", "हिन्दी", "🇮🇳"),
  POLISH("pl", "Polski", "🇵🇱"),
  KOREAN("ko", "한국어", "🇰🇷")
}

data class Exercise(
  val id: String,
  val name: String,
  val category: ExerciseCategory,
  val bodyPart: String,
  val targetMuscle: String,
  val secondaryMuscles: List<String> = emptyList(),
  val equipment: String = "Bodyweight",
  val instructions: List<String>,
  val multilingualInstructions: Map<String, List<String>> = emptyMap(),
  val durationSeconds: Int = 30, // Default 30s
  val defaultReps: Int = 12,
  val isTimeBased: Boolean = true,
  val difficulty: DifficultyLevel = DifficultyLevel.BEGINNER,
  val estimatedCalories: Int = 15,
  val tips: String = "Keep your core engaged and breathe smoothly.",
  val iconName: String = "fitness",
  val gifUrl: String? = null,
  val thumbnailUrl: String? = null
) {
  fun getInstructionsForLanguage(langCode: String): List<String> {
    return multilingualInstructions[langCode] ?: instructions
  }
}


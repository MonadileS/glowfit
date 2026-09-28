package com.example.data.local

import android.content.Context
import android.util.Log
import com.example.data.model.DifficultyLevel
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object ExerciseDatasetLoader {
  const val RAW_MEDIA_BASE_URL = "https://raw.githubusercontent.com/hasaneyldrm/exercises-dataset/main/"

  fun loadExercisesFromAssets(context: Context): List<Exercise> {
    val exerciseList = mutableListOf<Exercise>()
    try {
      context.assets.open("exercises.json").use { inputStream ->
        val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))
        val jsonString = reader.readText()
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
          val obj = jsonArray.getJSONObject(i)
          val id = obj.optString("id", "")
          val rawName = obj.optString("name", "")
          val formattedName = rawName.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
          }
          val categoryStr = obj.optString("category", "").lowercase()
          val bodyPart = obj.optString("body_part", categoryStr)
          val equipment = obj.optString("equipment", "body weight")
          val target = obj.optString("target", "")

          val secondaryMuscles = mutableListOf<String>()
          val secMusclesArray = obj.optJSONArray("secondary_muscles")
          if (secMusclesArray != null) {
            for (j in 0 until secMusclesArray.length()) {
              secondaryMuscles.add(secMusclesArray.getString(j))
            }
          }

          // Multilingual instruction steps from dataset (all 10 languages: en, es, fr, it, tr, ru, zh, hi, pl, ko)
          val multilingualInstructions = mutableMapOf<String, List<String>>()
          val stepsObj = obj.optJSONObject("instruction_steps")
          if (stepsObj != null) {
            val keys = stepsObj.keys()
            while (keys.hasNext()) {
              val lang = keys.next()
              val langArray = stepsObj.optJSONArray(lang)
              if (langArray != null) {
                val stepList = mutableListOf<String>()
                for (k in 0 until langArray.length()) {
                  stepList.add(langArray.getString(k))
                }
                multilingualInstructions[lang] = stepList
              }
            }
          }

          val defaultEnglishSteps = multilingualInstructions["en"] ?: run {
            val instructionsObj = obj.optJSONObject("instructions")
            val enText = instructionsObj?.optString("en") ?: ""
            if (enText.isNotBlank()) listOf(enText) else listOf("Perform the movement with smooth control and breathe evenly.")
          }

          val rawGif = obj.optString("gif_url", "")
          val rawImage = obj.optString("image", "")

          val gifUrl = if (rawGif.isNotBlank()) {
            if (rawGif.startsWith("http")) rawGif else RAW_MEDIA_BASE_URL + rawGif
          } else null

          val thumbUrl = if (rawImage.isNotBlank()) {
            if (rawImage.startsWith("http")) rawImage else RAW_MEDIA_BASE_URL + rawImage
          } else null

          val appCategory = mapCategory(categoryStr, bodyPart, target)

          exerciseList.add(
            Exercise(
              id = id,
              name = formattedName,
              category = appCategory,
              bodyPart = bodyPart.replaceFirstChar { it.uppercase() },
              targetMuscle = target.replaceFirstChar { it.uppercase() },
              secondaryMuscles = secondaryMuscles,
              equipment = equipment.replaceFirstChar { it.uppercase() },
              instructions = defaultEnglishSteps,
              multilingualInstructions = multilingualInstructions,
              durationSeconds = 30,
              defaultReps = 12,
              isTimeBased = true,
              difficulty = mapDifficulty(equipment),
              estimatedCalories = 15,
              tips = "Maintain controlled breathing and focus on engaging your $target.",
              iconName = categoryStr,
              gifUrl = gifUrl,
              thumbnailUrl = thumbUrl
            )
          )
        }
      }
      Log.d("ExerciseDatasetLoader", "Successfully loaded ${exerciseList.size} exercises from exercises-dataset")
    } catch (e: Exception) {
      Log.e("ExerciseDatasetLoader", "Error parsing exercises.json", e)
    }
    return exerciseList
  }

  private fun mapCategory(category: String, bodyPart: String, target: String): ExerciseCategory {
    return when {
      category.contains("waist") || bodyPart.contains("waist") || target.contains("abs") -> {
        if (target.contains("oblique")) ExerciseCategory.HOURGLASS else ExerciseCategory.BELLY_CORE
      }
      category.contains("upper legs") || category.contains("lower legs") || target.contains("glute") -> {
        ExerciseCategory.BOOTY_LEGS
      }
      category.contains("upper arms") || category.contains("lower arms") || category.contains("shoulders") -> {
        ExerciseCategory.TONED_ARMS
      }
      category.contains("cardio") -> ExerciseCategory.CARDIO_BURN
      category.contains("neck") || target.contains("stretch") || category.contains("stretch") -> {
        ExerciseCategory.STRETCH_POSTURE
      }
      else -> ExerciseCategory.FULL_BODY
    }
  }

  private fun mapDifficulty(equipment: String): DifficultyLevel {
    return when {
      equipment.contains("barbell") || equipment.contains("olympic") -> DifficultyLevel.ADVANCED
      equipment.contains("dumbbell") || equipment.contains("cable") || equipment.contains("machine") -> DifficultyLevel.INTERMEDIATE
      else -> DifficultyLevel.BEGINNER
    }
  }
}

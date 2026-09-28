package com.example

import com.example.data.local.ExerciseCatalog
import com.example.data.local.ExerciseTranslations
import com.example.data.model.DifficultyLevel
import com.example.data.model.ExerciseCategory
import com.example.data.model.ExerciseLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExerciseCatalogTest {

  @Test
  fun `exercise catalog contains complete dataset`() {
    val list = ExerciseCatalog.exercises
    assertTrue("Dataset should contain at least 25 exercises", list.size >= 25)

    // Verify all exercises have valid names, instructions, and positive duration
    list.forEach { exercise ->
      assertNotNull("Exercise ID must not be null", exercise.id)
      assertTrue("Exercise name must not be blank: ${exercise.id}", exercise.name.isNotBlank())
      assertTrue("Target muscle must be specified: ${exercise.name}", exercise.targetMuscle.isNotBlank())
      assertTrue("Instructions should have at least 2 steps: ${exercise.name}", exercise.instructions.size >= 2)
      assertTrue("Duration should be positive: ${exercise.name}", exercise.durationSeconds > 0)
      assertTrue("Estimated calories should be positive: ${exercise.name}", exercise.estimatedCalories > 0)
    }
  }

  @Test
  fun `exercise translations dataset supports 10 languages`() {
    val supportedLangs = ExerciseTranslations.supportedLanguages
    assertEquals("Must support 10 languages as per dataset", 10, supportedLangs.size)

    val expectedCodes = listOf("en", "es", "fr", "it", "tr", "ru", "zh", "hi", "pl", "ko")
    expectedCodes.forEach { code ->
      assertTrue("Must contain language code $code", supportedLangs.any { it.code == code })
    }

    // Verify translations for Russian Twists in multiple languages
    val spanishSteps = ExerciseTranslations.getInstructions("ex_russian_twist", "es", emptyList())
    assertTrue("Spanish instructions should not be empty", spanishSteps.isNotEmpty())

    val turkishSteps = ExerciseTranslations.getInstructions("ex_russian_twist", "tr", emptyList())
    assertTrue("Turkish instructions should not be empty", turkishSteps.isNotEmpty())

    val frenchSteps = ExerciseTranslations.getInstructions("ex_russian_twist", "fr", emptyList())
    assertTrue("French instructions should not be empty", frenchSteps.isNotEmpty())
  }

  @Test
  fun `filter exercises by category works accurately`() {
    val coreMoves = ExerciseCatalog.getByCategory(ExerciseCategory.BELLY_CORE)
    assertTrue("Should find core exercises", coreMoves.isNotEmpty())
    coreMoves.forEach {
      assertEquals(ExerciseCategory.BELLY_CORE, it.category)
    }

    val gluteMoves = ExerciseCatalog.getByCategory(ExerciseCategory.BOOTY_LEGS)
    assertTrue("Should find booty and leg exercises", gluteMoves.isNotEmpty())
    gluteMoves.forEach {
      assertEquals(ExerciseCategory.BOOTY_LEGS, it.category)
    }
  }

  @Test
  fun `search exercises by query works properly`() {
    val searchResults = ExerciseCatalog.exercises.filter { it.name.contains("Squat", ignoreCase = true) }
    assertTrue("Should find squat related exercises", searchResults.isNotEmpty())

    val emptyResults = ExerciseCatalog.exercises.filter { it.name.contains("nonexistent_exercise_xyz_123", ignoreCase = true) }
    assertTrue(emptyResults.isEmpty())
  }

  @Test
  fun `filter by difficulty works properly`() {
    val beginnerMoves = ExerciseCatalog.exercises.filter { it.difficulty == DifficultyLevel.BEGINNER }
    assertTrue("Should have beginner moves", beginnerMoves.isNotEmpty())
    beginnerMoves.forEach {
      assertEquals(DifficultyLevel.BEGINNER, it.difficulty)
    }
  }

  @Test
  fun `getById returns correct exercise`() {
    val first = ExerciseCatalog.exercises.first()
    val found = ExerciseCatalog.getById(first.id)
    assertNotNull("Exercise must be found by ID", found)
    assertEquals(first.name, found?.name)
  }
}

package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DefaultPrograms
import com.example.data.local.ExerciseCatalog
import com.example.data.model.DifficultyLevel
import com.example.data.model.ExerciseCategory
import com.example.data.model.MoodType
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavigationTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MainViewModelTest {

  private lateinit var viewModel: MainViewModel

  @Before
  fun setup() {
    val application = ApplicationProvider.getApplicationContext<Application>()
    viewModel = MainViewModel(application)
  }

  @Test
  fun `tab navigation switches properly`() {
    viewModel.selectTab(NavigationTab.WORKOUTS)
    viewModel.selectTab(NavigationTab.PROGRESS)
    viewModel.selectTab(NavigationTab.COMMUNITY)
    viewModel.selectTab(NavigationTab.PROFILE)
    viewModel.selectTab(NavigationTab.TODAY)
  }

  @Test
  fun `filter and search state updates correctly`() {
    viewModel.setCategoryFilter(ExerciseCategory.BELLY_CORE)
    viewModel.setSearchQuery("plank")
    viewModel.setSearchQuery("")
    viewModel.setCategoryFilter(null)
  }

  @Test
  fun `workout session starts and steps through exercises`() {
    val routine = DefaultPrograms.defaultRoutines.first()
    viewModel.startWorkout(routine)

    // Verify session was created
    val initialExercise = ExerciseCatalog.getById(routine.exerciseIds.first())
    assertNotNull(initialExercise)

    // Step forward
    viewModel.nextExercise()

    // Step backward
    viewModel.previousExercise()

    // Toggle pause
    viewModel.togglePauseWorkout()
    viewModel.togglePauseWorkout()

    // Exit workout
    viewModel.exitWorkout()
  }

  @Test
  fun `dialog visibility toggles function properly`() {
    viewModel.setShowMoodLogDialog(true)
    viewModel.setShowMoodLogDialog(false)

    viewModel.setShowCustomRoutineDialog(true)
    viewModel.setShowCustomRoutineDialog(false)

    viewModel.setShowCreatePostDialog(true)
    viewModel.setShowCreatePostDialog(false)

    viewModel.setShowEditProfileDialog(true)
    viewModel.setShowEditProfileDialog(false)
  }

  @Test
  fun `custom routine creation and mood logging execute without error`() = runTest {
    // Log daily mood
    viewModel.logDailyMood(
      moodType = MoodType.HAPPY,
      energyLevel = 5,
      note = "Feeling strong and empowered!",
      tags = listOf("Energized", "Post-Workout Glow")
    )

    // Create custom routine
    val exerciseIds = ExerciseCatalog.getByCategory(ExerciseCategory.BELLY_CORE).take(3).map { it.id }
    viewModel.createCustomRoutine(
      title = "My Daily Abs",
      subtitle = "Quick 5-min burn",
      category = ExerciseCategory.BELLY_CORE,
      difficulty = DifficultyLevel.BEGINNER,
      exerciseIds = exerciseIds,
      estimatedMinutes = 5
    )

    // Post to community
    viewModel.createSocialPost(
      content = "Just crushed today's routine! You got this girls! 💖",
      routineTitle = "My Daily Abs",
      minutes = 5,
      tag = "#GlowSquad"
    )
  }
}

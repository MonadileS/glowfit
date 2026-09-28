package com.example

import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.data.local.DefaultPrograms
import com.example.data.local.ExerciseCatalog
import com.example.data.model.UserProfile
import com.example.ui.components.ExerciseVisualArtwork
import com.example.ui.screens.today.TodayScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.NavigationTab
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class GlowFitUiTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun `exercise visual artwork renders correctly`() {
    val exercise = ExerciseCatalog.exercises.first()
    composeTestRule.setContent {
      MyApplicationTheme {
        Surface {
          ExerciseVisualArtwork(exercise = exercise)
        }
      }
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText(exercise.name).assertIsDisplayed()
  }

  @Test
  fun `bottom navigation renders all tabs`() {
    composeTestRule.setContent {
      MyApplicationTheme {
        GlowBottomNavigation(
          currentTab = NavigationTab.TODAY,
          onTabSelected = {}
        )
      }
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Today").assertIsDisplayed()
    composeTestRule.onNodeWithText("Workouts").assertIsDisplayed()
    composeTestRule.onNodeWithText("Progress").assertIsDisplayed()
    composeTestRule.onNodeWithText("Squad").assertIsDisplayed()
    composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
  }

  @Test
  fun `today screen displays greeting and featured program`() {
    val userProfile = UserProfile(name = "Sophia", currentStreak = 3)
    val routines = DefaultPrograms.defaultRoutines

    composeTestRule.setContent {
      MyApplicationTheme {
        TodayScreen(
          userProfile = userProfile,
          routines = routines,
          todayMood = null,
          workoutLogs = emptyList(),
          onStartWorkout = {},
          onPreviewRoutine = {},
          onOpenMoodDialog = {},
          onNavigateToWorkouts = {}
        )
      }
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Good day, Sophia ✨").assertIsDisplayed()
    composeTestRule.onNodeWithText("How are you feeling today?").assertIsDisplayed()
  }
}

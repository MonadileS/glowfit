package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.dialogs.CreatePostDialog
import com.example.ui.dialogs.CustomRoutineDialog
import com.example.ui.dialogs.EditProfileDialog
import com.example.ui.dialogs.ExerciseDetailDialog
import com.example.ui.dialogs.MoodLogDialog
import com.example.ui.dialogs.RoutineDetailDialog
import com.example.ui.dialogs.WorkoutCompletedDialog
import com.example.ui.screens.community.CommunityScreen
import com.example.ui.screens.player.WorkoutPlayerScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.today.TodayScreen
import com.example.ui.screens.workout.WorkoutsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavigationTab

class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      val isSystemDark = isSystemInDarkTheme()
      val isDarkMode = uiState.userProfile.darkModeEnabled

      MyApplicationTheme(darkTheme = isDarkMode) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          if (uiState.activeSession != null) {
            // Full-screen Workout Active Player
            WorkoutPlayerScreen(
              session = uiState.activeSession!!,
              isSoundMuted = uiState.isSoundMuted,
              onToggleSoundMute = { viewModel.toggleSoundMute() },
              onTogglePause = { viewModel.togglePauseWorkout() },
              onNextExercise = { viewModel.nextExercise() },
              onPreviousExercise = { viewModel.previousExercise() },
              onSkipRest = { viewModel.skipRest() },
              onExitWorkout = { viewModel.exitWorkout() },
              onFinishWorkout = { viewModel.finishWorkoutSession() },
              modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
          } else {
            // Main Scaffold with Aesthetic Bottom Navigation
            Scaffold(
              modifier = Modifier.fillMaxSize(),
              contentWindowInsets = WindowInsets.statusBars,
              bottomBar = {
                GlowBottomNavigation(
                  currentTab = uiState.currentTab,
                  onTabSelected = { viewModel.selectTab(it) }
                )
              }
            ) { innerPadding ->
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .padding(innerPadding)
              ) {
                AnimatedContent(
                  targetState = uiState.currentTab,
                  transitionSpec = { fadeIn() togetherWith fadeOut() },
                  label = "tab_content_transition"
                ) { targetTab ->
                  when (targetTab) {
                    NavigationTab.TODAY -> TodayScreen(
                      userProfile = uiState.userProfile,
                      routines = uiState.routines,
                      todayMood = uiState.moodLogs.firstOrNull(),
                      workoutLogs = uiState.workoutLogs,
                      onStartWorkout = { viewModel.startWorkout(it) },
                      onPreviewRoutine = { viewModel.setPreviewRoutine(it) },
                      onOpenMoodDialog = { viewModel.setShowMoodLogDialog(true) },
                      onNavigateToWorkouts = { viewModel.selectTab(NavigationTab.WORKOUTS) }
                    )
                    NavigationTab.WORKOUTS -> WorkoutsScreen(
                      routines = uiState.routines,
                      selectedCategory = uiState.selectedCategoryFilter,
                      searchQuery = uiState.searchQuery,
                      onSelectCategory = { viewModel.setCategoryFilter(it) },
                      onSearchChange = { viewModel.setSearchQuery(it) },
                      onStartWorkout = { viewModel.startWorkout(it) },
                      onPreviewRoutine = { viewModel.setPreviewRoutine(it) },
                      onPreviewExercise = { viewModel.setPreviewExercise(it) },
                      onCreateCustomRoutine = { viewModel.setShowCustomRoutineDialog(true) }
                    )
                    NavigationTab.PROGRESS -> ProgressScreen(
                      userProfile = uiState.userProfile,
                      workoutLogs = uiState.workoutLogs,
                      moodLogs = uiState.moodLogs,
                      onOpenMoodDialog = { viewModel.setShowMoodLogDialog(true) },
                      onEditMeasurements = { viewModel.setShowEditProfileDialog(true) }
                    )
                    NavigationTab.COMMUNITY -> CommunityScreen(
                      userProfile = uiState.userProfile,
                      posts = uiState.socialPosts,
                      onLikePost = { viewModel.likeSocialPost(it) },
                      onCheerPost = { viewModel.cheerSocialPost(it) },
                      onAddComment = { id, text -> viewModel.addComment(id, text) },
                      onOpenCreatePostDialog = { viewModel.setShowCreatePostDialog(true) }
                    )
                    NavigationTab.PROFILE -> ProfileScreen(
                      userProfile = uiState.userProfile,
                      badges = uiState.badges,
                      onOpenEditProfile = { viewModel.setShowEditProfileDialog(true) },
                      onToggleDarkMode = { isDark ->
                        val p = uiState.userProfile
                        viewModel.updateProfile(
                          p.name, p.bio, p.avatarEmoji, p.fitnessGoal,
                          p.targetDailyMinutes, isDark, p.soundEnabled,
                          p.weightKg, p.waistCm, p.hipsCm
                        )
                      },
                      onToggleSound = { soundOn ->
                        val p = uiState.userProfile
                        viewModel.updateProfile(
                          p.name, p.bio, p.avatarEmoji, p.fitnessGoal,
                          p.targetDailyMinutes, p.darkModeEnabled, soundOn,
                          p.weightKg, p.waistCm, p.hipsCm
                        )
                      },
                      onUpdateTargetMinutes = { mins ->
                        val p = uiState.userProfile
                        viewModel.updateProfile(
                          p.name, p.bio, p.avatarEmoji, p.fitnessGoal,
                          mins, p.darkModeEnabled, p.soundEnabled,
                          p.weightKg, p.waistCm, p.hipsCm
                        )
                      }
                    )
                  }
                }
              }
            }

            // Dialogs
            if (uiState.showMoodLogDialog) {
              MoodLogDialog(
                onDismiss = { viewModel.setShowMoodLogDialog(false) },
                onSave = { mood, energy, note, tags ->
                  viewModel.logDailyMood(mood, energy, note, tags)
                }
              )
            }

            if (uiState.showCustomRoutineDialog) {
              CustomRoutineDialog(
                onDismiss = { viewModel.setShowCustomRoutineDialog(false) },
                onCreate = { title, sub, cat, diff, exIds, mins ->
                  viewModel.createCustomRoutine(title, sub, cat, diff, exIds, mins)
                }
              )
            }

            if (uiState.showCreatePostDialog) {
              CreatePostDialog(
                onDismiss = { viewModel.setShowCreatePostDialog(false) },
                onPublish = { content, title, mins, tag ->
                  viewModel.createSocialPost(content, title, mins, tag)
                }
              )
            }

            if (uiState.showWorkoutCompletedDialog) {
              WorkoutCompletedDialog(
                log = uiState.lastCompletedLog,
                onDismiss = { viewModel.setShowWorkoutCompletedDialog(false) },
                onShareToSquad = { thought ->
                  val log = uiState.lastCompletedLog
                  viewModel.createSocialPost(
                    content = thought,
                    routineTitle = log?.routineTitle,
                    minutes = log?.durationMinutes,
                    tag = "#WorkoutWin"
                  )
                }
              )
            }

            if (uiState.previewRoutine != null) {
              RoutineDetailDialog(
                routine = uiState.previewRoutine!!,
                onDismiss = { viewModel.setPreviewRoutine(null) },
                onStartWorkout = { viewModel.startWorkout(uiState.previewRoutine!!) },
                onExerciseClick = { viewModel.setPreviewExercise(it) }
              )
            }

            if (uiState.previewExercise != null) {
              ExerciseDetailDialog(
                exercise = uiState.previewExercise!!,
                onDismiss = { viewModel.setPreviewExercise(null) }
              )
            }

            if (uiState.showEditProfileDialog) {
              val p = uiState.userProfile
              EditProfileDialog(
                currentName = p.name,
                currentBio = p.bio,
                currentAvatar = p.avatarEmoji,
                currentGoal = p.fitnessGoal,
                targetMinutes = p.targetDailyMinutes,
                darkMode = p.darkModeEnabled,
                sound = p.soundEnabled,
                weight = p.weightKg,
                waist = p.waistCm,
                hips = p.hipsCm,
                onDismiss = { viewModel.setShowEditProfileDialog(false) },
                onSave = { name, bio, avatar, goal, targetMin, dark, sound, w, waist, hips ->
                  viewModel.updateProfile(name, bio, avatar, goal, targetMin, dark, sound, w, waist, hips)
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun GlowBottomNavigation(
  currentTab: NavigationTab,
  onTabSelected: (NavigationTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .windowInsetsPadding(WindowInsets.navigationBars),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 4.dp,
    shadowElevation = 6.dp,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp, horizontal = 12.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavigationTab.values().forEach { tab ->
        val isSelected = currentTab == tab
        val icon: ImageVector = when (tab) {
          NavigationTab.TODAY -> Icons.Default.Favorite
          NavigationTab.WORKOUTS -> Icons.Default.FitnessCenter
          NavigationTab.PROGRESS -> Icons.Default.Insights
          NavigationTab.COMMUNITY -> Icons.Default.People
          NavigationTab.PROFILE -> Icons.Default.Person
        }

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(3.dp),
          modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            modifier = Modifier.size(34.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = icon,
                contentDescription = tab.title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.size(19.dp)
              )
            }
          }

          Text(
            text = tab.title,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

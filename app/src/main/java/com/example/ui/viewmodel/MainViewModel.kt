package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
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
import com.example.data.repository.GlowFitRepository
import com.example.util.WorkoutSoundEffectPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NavigationTab(val title: String, val iconName: String) {
  TODAY("Today", "favorite"),
  WORKOUTS("Workouts", "fitness_center"),
  PROGRESS("Progress", "insights"),
  COMMUNITY("Squad", "people"),
  PROFILE("Profile", "person")
}

data class ActiveWorkoutSession(
  val routine: WorkoutRoutine,
  val currentExerciseIndex: Int = 0,
  val timeRemainingSeconds: Int = 30,
  val totalElapsedSeconds: Int = 0,
  val isPaused: Boolean = false,
  val isResting: Boolean = false,
  val isFinished: Boolean = false
) {
  val currentExercise: Exercise?
    get() {
      val exerciseId = routine.exerciseIds.getOrNull(currentExerciseIndex) ?: return null
      return ExerciseCatalog.getById(exerciseId)
    }

  val totalExercises: Int
    get() = routine.exerciseIds.size

  val progressFraction: Float
    get() = if (totalExercises > 0) (currentExerciseIndex.toFloat() / totalExercises.toFloat()) else 0f
}

data class MainUiState(
  val currentTab: NavigationTab = NavigationTab.TODAY,
  val routines: List<WorkoutRoutine> = emptyList(),
  val userProfile: UserProfile = UserProfile(),
  val workoutLogs: List<WorkoutLog> = emptyList(),
  val moodLogs: List<MoodLog> = emptyList(),
  val badges: List<Badge> = emptyList(),
  val socialPosts: List<SocialPost> = emptyList(),
  // Workout Filters
  val selectedCategoryFilter: ExerciseCategory? = null,
  val searchQuery: String = "",
  // Active session
  val activeSession: ActiveWorkoutSession? = null,
  // Selected Routine / Exercise detail dialogs
  val previewRoutine: WorkoutRoutine? = null,
  val previewExercise: Exercise? = null,
  // Dialogs
  val showMoodLogDialog: Boolean = false,
  val showCustomRoutineDialog: Boolean = false,
  val showCreatePostDialog: Boolean = false,
  val showWorkoutCompletedDialog: Boolean = false,
  val lastCompletedLog: WorkoutLog? = null,
  val showEditProfileDialog: Boolean = false,
  val isSoundMuted: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: GlowFitRepository

  private val _currentTab = MutableStateFlow(NavigationTab.TODAY)
  private val _selectedCategoryFilter = MutableStateFlow<ExerciseCategory?>(null)
  private val _searchQuery = MutableStateFlow("")
  private val _activeSession = MutableStateFlow<ActiveWorkoutSession?>(null)
  private val _previewRoutine = MutableStateFlow<WorkoutRoutine?>(null)
  private val _previewExercise = MutableStateFlow<Exercise?>(null)
  private val _showMoodLogDialog = MutableStateFlow(false)
  private val _showCustomRoutineDialog = MutableStateFlow(false)
  private val _showCreatePostDialog = MutableStateFlow(false)
  private val _showWorkoutCompletedDialog = MutableStateFlow(false)
  private val _lastCompletedLog = MutableStateFlow<WorkoutLog?>(null)
  private val _showEditProfileDialog = MutableStateFlow(false)
  private val _isSoundMuted = MutableStateFlow(false)

  private var workoutTimerJob: Job? = null

  init {
    val database = AppDatabase.getDatabase(application)
    repository = GlowFitRepository(database)

    viewModelScope.launch {
      ExerciseCatalog.loadFromAssets(application)
      repository.initializeDatabaseIfEmpty()
    }
  }

  val uiState: StateFlow<MainUiState> = combine(
    repository.allRoutines,
    repository.userProfile,
    repository.allWorkoutLogs,
    repository.allMoodLogs,
    repository.allBadges,
    repository.allSocialPosts,
    _currentTab,
    _selectedCategoryFilter,
    _searchQuery,
    _activeSession,
    _previewRoutine,
    _previewExercise,
    _showMoodLogDialog,
    _showCustomRoutineDialog,
    _showCreatePostDialog,
    _showWorkoutCompletedDialog,
    _lastCompletedLog,
    _showEditProfileDialog,
    _isSoundMuted
  ) { args ->
    val routines = args[0] as List<WorkoutRoutine>
    val userProfile = (args[1] as? UserProfile) ?: UserProfile()
    val workoutLogs = args[2] as List<WorkoutLog>
    val moodLogs = args[3] as List<MoodLog>
    val badges = args[4] as List<Badge>
    val socialPosts = args[5] as List<SocialPost>
    val currentTab = args[6] as NavigationTab
    val categoryFilter = args[7] as? ExerciseCategory
    val searchQuery = args[8] as String
    val activeSession = args[9] as? ActiveWorkoutSession
    val previewRoutine = args[10] as? WorkoutRoutine
    val previewExercise = args[11] as? Exercise
    val showMoodDialog = args[12] as Boolean
    val showCustomRoutine = args[13] as Boolean
    val showCreatePost = args[14] as Boolean
    val showWorkoutCompleted = args[15] as Boolean
    val lastCompletedLog = args[16] as? WorkoutLog
    val showEditProfile = args[17] as Boolean
    val isSoundMuted = args[18] as Boolean

    MainUiState(
      currentTab = currentTab,
      routines = routines,
      userProfile = userProfile,
      workoutLogs = workoutLogs,
      moodLogs = moodLogs,
      badges = badges,
      socialPosts = socialPosts,
      selectedCategoryFilter = categoryFilter,
      searchQuery = searchQuery,
      activeSession = activeSession,
      previewRoutine = previewRoutine,
      previewExercise = previewExercise,
      showMoodLogDialog = showMoodDialog,
      showCustomRoutineDialog = showCustomRoutine,
      showCreatePostDialog = showCreatePost,
      showWorkoutCompletedDialog = showWorkoutCompleted,
      lastCompletedLog = lastCompletedLog,
      showEditProfileDialog = showEditProfile,
      isSoundMuted = isSoundMuted
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = MainUiState()
  )

  fun selectTab(tab: NavigationTab) {
    _currentTab.value = tab
  }

  fun toggleSoundMute() {
    val newMuted = !_isSoundMuted.value
    _isSoundMuted.value = newMuted
    WorkoutSoundEffectPlayer.isSoundEnabled = !newMuted
  }

  fun setCategoryFilter(category: ExerciseCategory?) {
    _selectedCategoryFilter.value = if (_selectedCategoryFilter.value == category) null else category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setPreviewRoutine(routine: WorkoutRoutine?) {
    _previewRoutine.value = routine
  }

  fun setPreviewExercise(exercise: Exercise?) {
    _previewExercise.value = exercise
  }

  fun setShowMoodLogDialog(show: Boolean) {
    _showMoodLogDialog.value = show
  }

  fun setShowCustomRoutineDialog(show: Boolean) {
    _showCustomRoutineDialog.value = show
  }

  fun setShowCreatePostDialog(show: Boolean) {
    _showCreatePostDialog.value = show
  }

  fun setShowWorkoutCompletedDialog(show: Boolean) {
    _showWorkoutCompletedDialog.value = show
  }

  fun setShowEditProfileDialog(show: Boolean) {
    _showEditProfileDialog.value = show
  }

  // ---------------- Workout Session Logic ----------------

  fun startWorkout(routine: WorkoutRoutine) {
    val initialExercise = routine.exerciseIds.firstOrNull()?.let { ExerciseCatalog.getById(it) }
    val initialDuration = initialExercise?.durationSeconds ?: 30

    _previewRoutine.value = null
    _activeSession.value = ActiveWorkoutSession(
      routine = routine,
      currentExerciseIndex = 0,
      timeRemainingSeconds = initialDuration,
      totalElapsedSeconds = 0,
      isPaused = false,
      isResting = false,
      isFinished = false
    )
    WorkoutSoundEffectPlayer.playStartWhistle()
    startTimer()
  }

  private fun startTimer() {
    workoutTimerJob?.cancel()
    workoutTimerJob = viewModelScope.launch {
      while (true) {
        delay(1000)
        val session = _activeSession.value ?: break
        if (session.isPaused || session.isFinished) continue

        val newElapsed = session.totalElapsedSeconds + 1

        if (session.timeRemainingSeconds > 1) {
          val remaining = session.timeRemainingSeconds - 1
          // Sound effect for 3, 2, 1 countdown warning
          if (remaining in 1..3) {
            WorkoutSoundEffectPlayer.playCountdownBeep(remaining)
          }
          _activeSession.value = session.copy(
            timeRemainingSeconds = remaining,
            totalElapsedSeconds = newElapsed
          )
        } else {
          // Timer reached 0
          if (session.isResting) {
            // Finished rest, move to next exercise
            val nextIndex = session.currentExerciseIndex + 1
            if (nextIndex < session.routine.exerciseIds.size) {
              val nextExercise = ExerciseCatalog.getById(session.routine.exerciseIds[nextIndex])
              val duration = nextExercise?.durationSeconds ?: 30
              WorkoutSoundEffectPlayer.playStartWhistle()
              _activeSession.value = session.copy(
                currentExerciseIndex = nextIndex,
                timeRemainingSeconds = duration,
                isResting = false,
                totalElapsedSeconds = newElapsed
              )
            } else {
              finishWorkoutSession()
              break
            }
          } else {
            // Exercise ended: Check if more exercises remain -> go to 15s rest
            val nextIndex = session.currentExerciseIndex + 1
            if (nextIndex < session.routine.exerciseIds.size) {
              WorkoutSoundEffectPlayer.playRestChime()
              _activeSession.value = session.copy(
                timeRemainingSeconds = 15, // 15s rest
                isResting = true,
                totalElapsedSeconds = newElapsed
              )
            } else {
              finishWorkoutSession()
              break
            }
          }
        }
      }
    }
  }

  fun togglePauseWorkout() {
    val current = _activeSession.value ?: return
    val willBePaused = !current.isPaused
    _activeSession.value = current.copy(isPaused = willBePaused)
    if (!willBePaused) {
      WorkoutSoundEffectPlayer.playStartWhistle()
    }
  }

  fun skipRest() {
    val session = _activeSession.value ?: return
    if (!session.isResting) return

    val nextIndex = session.currentExerciseIndex + 1
    if (nextIndex < session.routine.exerciseIds.size) {
      val nextExercise = ExerciseCatalog.getById(session.routine.exerciseIds[nextIndex])
      val duration = nextExercise?.durationSeconds ?: 30
      WorkoutSoundEffectPlayer.playStartWhistle()
      _activeSession.value = session.copy(
        currentExerciseIndex = nextIndex,
        timeRemainingSeconds = duration,
        isResting = false
      )
    } else {
      finishWorkoutSession()
    }
  }

  fun nextExercise() {
    val session = _activeSession.value ?: return
    val nextIndex = session.currentExerciseIndex + 1
    if (nextIndex < session.routine.exerciseIds.size) {
      val nextExercise = ExerciseCatalog.getById(session.routine.exerciseIds[nextIndex])
      val duration = nextExercise?.durationSeconds ?: 30
      WorkoutSoundEffectPlayer.playStartWhistle()
      _activeSession.value = session.copy(
        currentExerciseIndex = nextIndex,
        timeRemainingSeconds = duration,
        isResting = false
      )
    } else {
      finishWorkoutSession()
    }
  }

  fun previousExercise() {
    val session = _activeSession.value ?: return
    val prevIndex = maxOf(0, session.currentExerciseIndex - 1)
    val prevExercise = ExerciseCatalog.getById(session.routine.exerciseIds[prevIndex])
    val duration = prevExercise?.durationSeconds ?: 30
    WorkoutSoundEffectPlayer.playStartWhistle()
    _activeSession.value = session.copy(
      currentExerciseIndex = prevIndex,
      timeRemainingSeconds = duration,
      isResting = false
    )
  }

  fun exitWorkout() {
    workoutTimerJob?.cancel()
    _activeSession.value = null
  }

  fun finishWorkoutSession(moodNote: String? = null) {
    val session = _activeSession.value ?: return
    workoutTimerJob?.cancel()
    WorkoutSoundEffectPlayer.playCompletionFanfare(viewModelScope)

    val durationMins = maxOf(1, session.totalElapsedSeconds / 60)

    viewModelScope.launch {
      val log = repository.logWorkoutCompletion(
        routine = session.routine,
        actualDurationMinutes = durationMins,
        moodNote = moodNote
      )
      _lastCompletedLog.value = log
      _showWorkoutCompletedDialog.value = true
      _activeSession.value = null
    }
  }

  // ---------------- Mood Logging ----------------

  fun logDailyMood(
    moodType: MoodType,
    energyLevel: Int,
    note: String,
    tags: List<String>
  ) {
    viewModelScope.launch {
      repository.logMood(moodType, energyLevel, note, tags)
      _showMoodLogDialog.value = false
    }
  }

  // ---------------- Custom Routine Generator ----------------

  fun createCustomRoutine(
    title: String,
    subtitle: String,
    category: ExerciseCategory,
    difficulty: DifficultyLevel,
    exerciseIds: List<String>,
    estimatedMinutes: Int
  ) {
    viewModelScope.launch {
      val hex = when (category) {
        ExerciseCategory.BELLY_CORE -> "#EC407A"
        ExerciseCategory.BOOTY_LEGS -> "#AB47BC"
        ExerciseCategory.TONED_ARMS -> "#7E57C2"
        ExerciseCategory.HOURGLASS -> "#F06292"
        ExerciseCategory.FULL_BODY -> "#FF4081"
        ExerciseCategory.STRETCH_POSTURE -> "#5C6BC0"
        ExerciseCategory.CARDIO_BURN -> "#FF7043"
      }
      repository.createCustomRoutine(
        title = title,
        subtitle = subtitle,
        category = category,
        difficulty = difficulty,
        exerciseIds = exerciseIds,
        estimatedMinutes = estimatedMinutes,
        bannerColorHex = hex
      )
      _showCustomRoutineDialog.value = false
    }
  }

  fun deleteRoutine(id: String) {
    viewModelScope.launch {
      repository.deleteRoutine(id)
    }
  }

  // ---------------- Social Interactions ----------------

  fun likeSocialPost(postId: String) {
    viewModelScope.launch {
      repository.likeSocialPost(postId)
    }
  }

  fun cheerSocialPost(postId: String) {
    viewModelScope.launch {
      repository.cheerSocialPost(postId)
    }
  }

  fun addComment(postId: String, comment: String) {
    if (comment.isBlank()) return
    viewModelScope.launch {
      repository.addCommentToPost(postId, comment)
    }
  }

  fun createSocialPost(content: String, routineTitle: String?, minutes: Int?, tag: String) {
    if (content.isBlank()) return
    viewModelScope.launch {
      val user = uiState.value.userProfile
      repository.createSocialPost(
        content = content,
        workoutTitle = routineTitle,
        workoutMinutes = minutes,
        streakDays = user.currentStreak,
        tag = tag
      )
      _showCreatePostDialog.value = false
    }
  }

  // ---------------- Profile Updates ----------------

  fun updateProfile(
    name: String,
    bio: String,
    avatarEmoji: String,
    fitnessGoal: String,
    targetMinutes: Int,
    darkMode: Boolean,
    sound: Boolean,
    weightKg: Float,
    waistCm: Float,
    hipsCm: Float
  ) {
    viewModelScope.launch {
      repository.updateProfile(
        name = name,
        bio = bio,
        avatarEmoji = avatarEmoji,
        fitnessGoal = fitnessGoal,
        targetDailyMinutes = targetMinutes,
        darkModeEnabled = darkMode,
        soundEnabled = sound,
        weightKg = weightKg,
        waistCm = waistCm,
        hipsCm = hipsCm
      )
      _showEditProfileDialog.value = false
    }
  }
}

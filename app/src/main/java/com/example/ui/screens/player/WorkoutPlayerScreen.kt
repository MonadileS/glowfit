package com.example.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExerciseCatalog
import com.example.ui.components.ExerciseAnimationVisualizer
import com.example.ui.viewmodel.ActiveWorkoutSession

@Composable
fun WorkoutPlayerScreen(
  session: ActiveWorkoutSession,
  isSoundMuted: Boolean = false,
  onToggleSoundMute: () -> Unit = {},
  onTogglePause: () -> Unit,
  onNextExercise: () -> Unit,
  onPreviousExercise: () -> Unit,
  onSkipRest: () -> Unit,
  onExitWorkout: () -> Unit,
  onFinishWorkout: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showExitDialog by remember { mutableStateOf(false) }
  var showInfoDialog by remember { mutableStateOf(false) }

  val currentExercise = session.currentExercise
  val totalMoves = session.routine.exerciseIds.size
  val currentIndex = session.currentExerciseIndex

  val minutes = session.timeRemainingSeconds / 60
  val seconds = session.timeRemainingSeconds % 60
  val formattedTime = String.format("%02d:%02d", minutes, seconds)

  val nextExercise = if (session.isResting && currentIndex + 1 < totalMoves) {
    ExerciseCatalog.getById(session.routine.exerciseIds[currentIndex + 1])
  } else if (!session.isResting && currentIndex + 1 < totalMoves) {
    ExerciseCatalog.getById(session.routine.exerciseIds[currentIndex + 1])
  } else null

  if (showExitDialog) {
    AlertDialog(
      onDismissRequest = { showExitDialog = false },
      title = { Text("End Workout?", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
      text = { Text("Your completed exercises and burned calories will be recorded.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
      confirmButton = {
        Button(
          onClick = {
            showExitDialog = false
            onExitWorkout()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Save & Exit", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showExitDialog = false }) {
          Text("Resume", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 20.dp, vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // ---------------- TOP BAR: Minimalist Navigation Header ----------------
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { showExitDialog = true }
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              Icons.Default.Close,
              contentDescription = "Exit",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = session.routine.title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Exercise ${currentIndex + 1} of $totalMoves",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onToggleSoundMute)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = if (isSoundMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
              contentDescription = if (isSoundMuted) "Unmute" else "Mute",
              tint = if (isSoundMuted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      // Minimalist Progress Bar
      LinearProgressIndicator(
        progress = { (currentIndex.toFloat() + 1f) / totalMoves.toFloat() },
        modifier = Modifier
          .fillMaxWidth()
          .height(4.dp)
          .clip(CircleShape),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
      )
    }

    // ---------------- CENTER STAGE: Apple Fitness+ / Nike Style Countdown & Visualizer ----------------
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      // Phase Tag (WORK vs REST)
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (session.isResting) Color(0xFFEFF6FF) else MaterialTheme.colorScheme.primaryContainer,
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (session.isResting) Color(0xFFBFDBFE) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
      ) {
        Text(
          text = if (session.isResting) "REST INTERVAL" else "WORK PHASE",
          style = MaterialTheme.typography.labelSmall,
          color = if (session.isResting) Color(0xFF2563EB) else MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
        )
      }

      // Exercise Name
      Text(
        text = if (session.isResting) "Take a Breath" else (currentExercise?.name ?: "Workout Move"),
        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      // Target Muscle Subtitle
      if (!session.isResting && currentExercise != null) {
        Text(
          text = "${currentExercise.bodyPart} • Target: ${currentExercise.targetMuscle}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Countdown Stage Visualizer
      val totalTargetSeconds = if (session.isResting) 15 else (currentExercise?.durationSeconds ?: 30)
      Box(
        modifier = Modifier.padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
      ) {
        ExerciseAnimationVisualizer(
          exercise = currentExercise,
          isResting = session.isResting,
          timeRemainingSeconds = session.timeRemainingSeconds,
          totalSeconds = totalTargetSeconds,
          isPaused = session.isPaused,
          showCountdownOverlay = true,
          modifier = Modifier.size(240.dp)
        )
      }

      // Up Next Preview Strip
      if (nextExercise != null) {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth(0.9f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = "UP NEXT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
              Text(
                text = nextExercise.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
            Text(
              text = "${nextExercise.durationSeconds}s",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    // ---------------- BOTTOM CONTROLS: Modern Studio Controls ----------------
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (session.isResting) {
        Button(
          onClick = onSkipRest,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.FastForward, contentDescription = "Skip Rest", tint = Color.White, modifier = Modifier.size(18.dp))
            Text("Skip Rest & Start", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
          }
        }
      } else {
        Row(
          modifier = Modifier.fillMaxWidth(0.85f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Previous Move
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .clickable(enabled = currentIndex > 0, onClick = onPreviousExercise)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
                tint = if (currentIndex > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
              )
            }
          }

          // Main Play / Pause Button (Large Studio Control)
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp,
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .clickable(onClick = onTogglePause)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = if (session.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = if (session.isPaused) "Play" else "Pause",
                tint = Color.White,
                modifier = Modifier.size(34.dp)
              )
            }
          }

          // Next Move
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
              .size(52.dp)
              .clip(CircleShape)
              .clickable(onClick = onNextExercise)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
    }
  }
}


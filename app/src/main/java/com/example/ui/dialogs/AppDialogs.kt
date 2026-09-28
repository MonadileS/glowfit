package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.data.local.ExerciseTranslations
import com.example.ui.components.ExerciseAnimationVisualizer
import com.example.ui.components.ExerciseVisualArtwork
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.ExerciseCatalog
import com.example.data.model.DifficultyLevel
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import com.example.data.model.MoodType
import com.example.data.model.WorkoutLog
import com.example.data.model.WorkoutRoutine

// ---------------- MOOD LOG DIALOG ----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodLogDialog(
  onDismiss: () -> Unit,
  onSave: (MoodType, Int, String, List<String>) -> Unit
) {
  var selectedMood by remember { mutableStateOf(MoodType.HAPPY) }
  var energyLevel by remember { mutableIntStateOf(4) }
  var noteText by remember { mutableStateOf("") }
  val selectedTags = remember { mutableStateListOf<String>() }

  val availableTags = listOf(
    "Morning Stretch 🧘‍♀️",
    "Post-Workout Glow ✨",
    "Good Sleep 🌙",
    "Hydrated 💧",
    "Sore Muscles 🎀",
    "Period Day 🌸",
    "Sweet Walk 🌿",
    "Healthy Meal 🥗"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(28.dp)),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Daily Feeling Check-In 🌸",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "How are you blooming today, babe?",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        // Mood Selectors Grid
        Text(
          text = "Select your current mood:",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.SemiBold
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MoodType.values().take(4).forEach { mood ->
            val isSelected = selectedMood == mood
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(16.dp)
                )
                .clickable { selectedMood = mood }
                .padding(8.dp)
            ) {
              Text(text = mood.emoji, fontSize = 28.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = mood.displayName.takeWhile { it != '/' && it != '&' }.trim(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
              )
            }
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MoodType.values().drop(4).forEach { mood ->
            val isSelected = selectedMood == mood
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(16.dp)
                )
                .clickable { selectedMood = mood }
                .padding(8.dp)
            ) {
              Text(text = mood.emoji, fontSize = 28.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = mood.displayName.takeWhile { it != '/' && it != '&' }.trim(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
              )
            }
          }
        }

        // Energy Slider
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Energy Level", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Text(
              text = "$energyLevel / 5 ✨",
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
          }
          Slider(
            value = energyLevel.toFloat(),
            onValueChange = { energyLevel = it.toInt() },
            valueRange = 1f..5f,
            steps = 3,
            colors = SliderDefaults.colors(
              thumbColor = MaterialTheme.colorScheme.primary,
              activeTrackColor = MaterialTheme.colorScheme.primary
            )
          )
        }

        // Tags
        Text(text = "Add feeling tags:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          availableTags.forEach { tag ->
            val isSelected = selectedTags.contains(tag)
            Surface(
              shape = CircleShape,
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
              ),
              modifier = Modifier
                .clip(CircleShape)
                .clickable {
                  if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                }
            ) {
              Text(
                text = tag,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }

        // Note
        OutlinedTextField(
          value = noteText,
          onValueChange = { noteText = it },
          label = { Text("Personal gratitude or feeling note") },
          placeholder = { Text("e.g. Grateful for morning yoga and sunny weather...") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          maxLines = 3
        )

        // Save Button
        Button(
          onClick = {
            onSave(selectedMood, energyLevel, noteText, selectedTags.toList())
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Save Feeling 💖", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
      }
    }
  }
}

// ---------------- WORKOUT COMPLETED DIALOG ----------------
@Composable
fun WorkoutCompletedDialog(
  log: WorkoutLog?,
  onDismiss: () -> Unit,
  onShareToSquad: (String) -> Unit
) {
  var moodNote by remember { mutableStateOf("Feeling so energized and accomplished! 💕") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.9f)
        .clip(RoundedCornerShape(32.dp)),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Celebration Sparkle
        Surface(
          shape = CircleShape,
          color = Color(0xFFFFF0F5),
          border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFF8FA3)),
          modifier = Modifier.size(72.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(text = "👑", fontSize = 36.sp)
          }
        }

        Text(
          text = "You Crushed It, Queen! ✨",
          style = MaterialTheme.typography.headlineLarge,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Text(
          text = "Workout session completed successfully. Your body & mind thank you!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        // Stats Box
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${log?.durationMinutes ?: 10}m",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
              Text(text = "Duration", style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${log?.caloriesBurned ?: 85}",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFFF7043),
                fontWeight = FontWeight.Bold
              )
              Text(text = "Kcal Burned", style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "+80 XP",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFFAB47BC),
                fontWeight = FontWeight.Bold
              )
              Text(text = "XP Earned", style = MaterialTheme.typography.labelSmall)
            }
          }
        }

        OutlinedTextField(
          value = moodNote,
          onValueChange = { moodNote = it },
          label = { Text("Share a quick thought or feeling:") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          maxLines = 2
        )

        Button(
          onClick = {
            onShareToSquad(moodNote)
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Share Win to Glow Squad 💕", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }

        TextButton(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Done & Back to Home", style = MaterialTheme.typography.labelMedium)
        }
      }
    }
  }
}

// ---------------- CUSTOM ROUTINE GENERATOR DIALOG ----------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomRoutineDialog(
  onDismiss: () -> Unit,
  onCreate: (String, String, ExerciseCategory, DifficultyLevel, List<String>, Int) -> Unit
) {
  var title by remember { mutableStateOf("My Glow Sculpt") }
  var selectedCategory by remember { mutableStateOf(ExerciseCategory.BELLY_CORE) }
  var selectedDifficulty by remember { mutableStateOf(DifficultyLevel.BEGINNER) }
  val selectedExerciseIds = remember {
    mutableStateListOf(
      "ex_russian_twist",
      "ex_glute_bridges",
      "ex_arm_circles",
      "ex_cat_cow"
    )
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .heightIn(max = 640.dp)
        .clip(RoundedCornerShape(28.dp)),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Personalize Routine 🎀",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Routine Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp)
        )

        // Target Category Picker
        Text(text = "Target Area:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          ExerciseCategory.values().forEach { cat ->
            val isSelected = selectedCategory == cat
            Surface(
              shape = CircleShape,
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(CircleShape)
                .clickable { selectedCategory = cat }
            ) {
              Text(
                text = "${cat.emoji} ${cat.displayName}",
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }

        // Exercise Picker List
        Text(
          text = "Select Exercises (${selectedExerciseIds.size} chosen):",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.SemiBold
        )

        ExerciseCatalog.exercises.forEach { exercise ->
          val isSelected = selectedExerciseIds.contains(exercise.id)
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .clickable {
                if (isSelected) {
                  if (selectedExerciseIds.size > 1) selectedExerciseIds.remove(exercise.id)
                } else {
                  selectedExerciseIds.add(exercise.id)
                }
              }
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Text(text = exercise.category.emoji, fontSize = 20.sp)
                Column {
                  Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "${exercise.bodyPart} • ${exercise.durationSeconds}s",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
              Checkbox(
                checked = isSelected,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
              )
            }
          }
        }

        val totalMinutes = (selectedExerciseIds.size * 45) / 60 + 2

        Button(
          onClick = {
            onCreate(
              title,
              "Personalized routine with ${selectedExerciseIds.size} movements",
              selectedCategory,
              selectedDifficulty,
              selectedExerciseIds.toList(),
              totalMinutes
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Create Routine (~$totalMinutes min) ✨", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
      }
    }
  }
}

// ---------------- EXERCISE DETAIL DIALOG ----------------
@Composable
fun ExerciseDetailDialog(
  exercise: Exercise,
  onDismiss: () -> Unit
) {
  var selectedLanguageCode by remember { mutableStateOf("en") }
  var preferDatasetMedia by remember { mutableStateOf(false) }

  // Live interactive preview countdown timer
  var isPreviewPlaying by remember { mutableStateOf(true) }
  var previewRemainingSeconds by remember { mutableIntStateOf(exercise.durationSeconds.coerceAtLeast(30)) }

  LaunchedEffect(isPreviewPlaying) {
    while (isPreviewPlaying) {
      delay(1000L)
      if (previewRemainingSeconds > 1) {
        previewRemainingSeconds -= 1
      } else {
        previewRemainingSeconds = exercise.durationSeconds.coerceAtLeast(30)
      }
    }
  }

  val activeInstructions = remember(exercise.id, selectedLanguageCode) {
    ExerciseTranslations.getInstructions(exercise.id, selectedLanguageCode, exercise.instructions)
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp)),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(44.dp)) {
              Box(contentAlignment = Alignment.Center) {
                Text(text = exercise.category.emoji, fontSize = 22.sp)
              }
            }
            Column {
              Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${exercise.bodyPart} • ${exercise.equipment}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        // View Mode Selector (Unified Live Animation & Countdown vs Dataset Motion GIF)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = !preferDatasetMedia,
            onClick = { preferDatasetMedia = false },
            label = { Text("⚡ Live Motion & Timer", style = MaterialTheme.typography.labelMedium) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f)
          )
          FilterChip(
            selected = preferDatasetMedia,
            onClick = { preferDatasetMedia = true },
            label = { Text("🎬 Dataset Media", style = MaterialTheme.typography.labelMedium) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f)
          )
        }

        if (!preferDatasetMedia) {
          // Unified Animation & Countdown Hero Stage
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ExerciseAnimationVisualizer(
              exercise = exercise,
              isResting = false,
              timeRemainingSeconds = previewRemainingSeconds,
              totalSeconds = exercise.durationSeconds.coerceAtLeast(30),
              isPaused = !isPreviewPlaying,
              showCountdownOverlay = true,
              modifier = Modifier.size(220.dp)
            )

            // Quick Play / Pause / Reset Bar for Live Preview
            Row(
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                  .clip(CircleShape)
                  .clickable { isPreviewPlaying = !isPreviewPlaying }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = if (isPreviewPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPreviewPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = if (isPreviewPlaying) "Pause Preview" else "Play Live Preview",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }

              IconButton(
                onClick = { previewRemainingSeconds = exercise.durationSeconds.coerceAtLeast(30) },
                modifier = Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
              ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset Timer", modifier = Modifier.size(16.dp))
              }
            }
          }
        } else {
          // Dataset Motion GIF / Artwork
          ExerciseVisualArtwork(
            exercise = exercise,
            height = 175.dp,
            showLabels = true,
            preferDatasetMedia = true
          )
        }

        // Target Muscle Pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(text = "🎯 Target:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(
              text = "${exercise.targetMuscle} (${exercise.difficulty.displayName})",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }

        // 10-Language Selection Row (from exercises-dataset)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Language / Idioma:",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "10 Languages 🌐",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            ExerciseTranslations.supportedLanguages.forEach { lang ->
              val isSelected = selectedLanguageCode == lang.code
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.clickable { selectedLanguageCode = lang.code }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Text(text = lang.flag, fontSize = 14.sp)
                  Text(
                    text = lang.displayName,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }
        }

        // Step by Step Instructions
        Text(
          text = "Step-by-step Instructions:",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold
        )
        activeInstructions.forEachIndexed { index, step ->
          Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
          ) {
            Surface(
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(22.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = "${index + 1}",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color.White,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Text(
              text = step,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(1f)
            )
          }
        }

        // Pro Tip Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFECB3))
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "💡", fontSize = 20.sp)
            Text(
              text = exercise.tips,
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF5D4037)
            )
          }
        }

        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Got It! 🌸", color = Color.White)
        }
      }
    }
  }
}

// ---------------- CREATE POST DIALOG ----------------
@Composable
fun CreatePostDialog(
  onDismiss: () -> Unit,
  onPublish: (String, String?, Int?, String) -> Unit
) {
  var contentText by remember { mutableStateOf("") }
  var selectedTag by remember { mutableStateOf("#DailyGlow") }

  val tags = listOf("#DailyGlow", "#GlowSquad", "#BootyBurn", "#MindfulMovement", "#Transformation")

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(28.dp)),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Share with Squad 💕", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        OutlinedTextField(
          value = contentText,
          onValueChange = { contentText = it },
          placeholder = { Text("Inspire other girls! Share your daily workout, mood, or motivation ✨") },
          modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
          shape = RoundedCornerShape(16.dp)
        )

        Text(text = "Select Tag:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          tags.take(3).forEach { tag ->
            val isSelected = selectedTag == tag
            Surface(
              shape = CircleShape,
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(CircleShape)
                .clickable { selectedTag = tag }
            ) {
              Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Button(
          onClick = {
            if (contentText.isNotBlank()) {
              onPublish(contentText, null, null, selectedTag)
            }
          },
          enabled = contentText.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Post to Glow Squad 🌸", color = Color.White)
        }
      }
    }
  }
}

// ---------------- EDIT PROFILE DIALOG ----------------
@Composable
fun EditProfileDialog(
  currentName: String,
  currentBio: String,
  currentAvatar: String,
  currentGoal: String,
  targetMinutes: Int,
  darkMode: Boolean,
  sound: Boolean,
  weight: Float,
  waist: Float,
  hips: Float,
  onDismiss: () -> Unit,
  onSave: (String, String, String, String, Int, Boolean, Boolean, Float, Float, Float) -> Unit
) {
  var name by remember { mutableStateOf(currentName) }
  var bio by remember { mutableStateOf(currentBio) }
  var avatarEmoji by remember { mutableStateOf(currentAvatar) }
  var goal by remember { mutableStateOf(currentGoal) }
  var minutes by remember { mutableIntStateOf(targetMinutes) }
  var isDark by remember { mutableStateOf(darkMode) }
  var soundOn by remember { mutableStateOf(sound) }
  var weightKg by remember { mutableFloatStateOf(weight) }
  var waistCm by remember { mutableFloatStateOf(waist) }
  var hipsCm by remember { mutableFloatStateOf(hips) }

  val avatars = listOf("🌸", "🎀", "✨", "👑", "🍑", "🧘‍♀️", "🌺", "💖")

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .heightIn(max = 600.dp)
        .clip(RoundedCornerShape(28.dp)),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .padding(24.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "Edit Profile & Goals 🎀", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        // Avatar Picker
        Text(text = "Choose Avatar:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          avatars.forEach { emoji ->
            val isSelected = avatarEmoji == emoji
            Surface(
              shape = CircleShape,
              color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
              border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
              ),
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .clickable { avatarEmoji = emoji }
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 20.sp)
              }
            }
          }
        }

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Display Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
          value = bio,
          onValueChange = { bio = it },
          label = { Text("Status Bio") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
          value = goal,
          onValueChange = { goal = it },
          label = { Text("Fitness Goal") },
          placeholder = { Text("e.g. Hourglass Shape & Daily Energy") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp)
        )

        // Body measurements
        Text(text = "Body Measurements (cm / kg):", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = weightKg.toString(),
            onValueChange = { weightKg = it.toFloatOrNull() ?: weightKg },
            label = { Text("Weight (kg)") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          )
          OutlinedTextField(
            value = waistCm.toString(),
            onValueChange = { waistCm = it.toFloatOrNull() ?: waistCm },
            label = { Text("Waist (cm)") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          )
          OutlinedTextField(
            value = hipsCm.toString(),
            onValueChange = { hipsCm = it.toFloatOrNull() ?: hipsCm },
            label = { Text("Hips (cm)") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          )
        }

        Button(
          onClick = {
            onSave(name, bio, avatarEmoji, goal, minutes, isDark, soundOn, weightKg, waistCm, hipsCm)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = CircleShape,
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Save Profile 🌸", color = Color.White)
        }
      }
    }
  }
}

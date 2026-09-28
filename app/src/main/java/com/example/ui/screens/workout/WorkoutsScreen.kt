package com.example.ui.screens.workout

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExerciseCatalog
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import com.example.data.model.WorkoutRoutine
import com.example.ui.components.CategoryFilterPill
import com.example.ui.components.ExerciseVisualArtwork
import com.example.ui.components.GlowCard
import com.example.ui.components.RoutineCard

@Composable
fun WorkoutsScreen(
  routines: List<WorkoutRoutine>,
  selectedCategory: ExerciseCategory?,
  searchQuery: String,
  onSelectCategory: (ExerciseCategory?) -> Unit,
  onSearchChange: (String) -> Unit,
  onStartWorkout: (WorkoutRoutine) -> Unit,
  onPreviewRoutine: (WorkoutRoutine) -> Unit,
  onPreviewExercise: (Exercise) -> Unit,
  onCreateCustomRoutine: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var selectedBodyPartFilter by remember { mutableStateOf<String?>(null) }
  var selectedEquipmentFilter by remember { mutableStateOf<String?>(null) }

  val tabs = listOf("Workouts & Circuits", "Exercise Library (${ExerciseCatalog.count})")

  // Filter routines
  val filteredRoutines = routines.filter { routine ->
    val matchesCategory = selectedCategory == null || routine.category == selectedCategory
    val matchesQuery = searchQuery.isBlank() ||
      routine.title.contains(searchQuery, ignoreCase = true) ||
      routine.subtitle.contains(searchQuery, ignoreCase = true)
    matchesCategory && matchesQuery
  }

  // Filter exercises from dataset
  val filteredExercises = ExerciseCatalog.exercises.filter { exercise ->
    val matchesCategory = selectedCategory == null || exercise.category == selectedCategory
    val matchesBodyPart = selectedBodyPartFilter == null || exercise.bodyPart.equals(selectedBodyPartFilter, ignoreCase = true)
    val matchesEquipment = selectedEquipmentFilter == null || exercise.equipment.equals(selectedEquipmentFilter, ignoreCase = true)
    val matchesQuery = searchQuery.isBlank() ||
      exercise.name.contains(searchQuery, ignoreCase = true) ||
      exercise.targetMuscle.contains(searchQuery, ignoreCase = true) ||
      exercise.bodyPart.contains(searchQuery, ignoreCase = true) ||
      exercise.equipment.contains(searchQuery, ignoreCase = true)
    matchesCategory && matchesBodyPart && matchesEquipment && matchesQuery
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      // Title
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Workouts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Targeted circuits and movement library",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Custom Routine Builder Button
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onCreateCustomRoutine)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(16.dp))
            Text(
              text = "Custom",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // Search Bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text("Search routines, belly, glutes, stretch...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
          if (searchQuery.isNotBlank()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear")
            }
          }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = MaterialTheme.colorScheme.primary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        singleLine = true
      )
    }

    // Category Filter Chips Carousel
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          val isAllSelected = selectedCategory == null
          Surface(
            shape = CircleShape,
            color = if (isAllSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isAllSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            ),
            modifier = Modifier
              .clip(CircleShape)
              .clickable { onSelectCategory(null) }
          ) {
            Text(
              text = "✨ All Focus",
              style = MaterialTheme.typography.labelMedium,
              color = if (isAllSelected) Color.White else MaterialTheme.colorScheme.onSurface,
              fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
          }
        }

        items(ExerciseCategory.values()) { category ->
          CategoryFilterPill(
            category = category,
            isSelected = selectedCategory == category,
            onClick = { onSelectCategory(category) }
          )
        }
      }
    }

    // Segmented Tabs: Routines vs Exercise Library
    item {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(4.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          tabs.forEachIndexed { index, tabTitle ->
            val isSelected = selectedTabIndex == index
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
              shadowElevation = if (isSelected) 2.dp else 0.dp,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { selectedTabIndex = index }
            ) {
              Text(
                text = tabTitle,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(vertical = 10.dp)
              )
            }
          }
        }
      }
    }

    if (selectedTabIndex == 0) {
      // ---------------- ROUTINES TAB ----------------
      if (filteredRoutines.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(text = "🌸", fontSize = 36.sp)
              Text(
                text = "No routines found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Try searching for a different term or clear filters.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(filteredRoutines) { routine ->
          RoutineCard(
            routine = routine,
            onStartClick = { onStartWorkout(routine) },
            onCardClick = { onPreviewRoutine(routine) }
          )
        }
      }
    } else {
      // ---------------- EXERCISE LIBRARY TAB (from exercises-dataset) ----------------
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "🔥", fontSize = 22.sp)
                Column {
                  Text(
                    text = "Complete Movement Library",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Showing ${filteredExercises.size} of ${ExerciseCatalog.count} exercises with animated GIFs & instructions",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            // Quick Body Part Filter Chips Row
            Text(text = "Target Body Area:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              item {
                val isSelected = selectedBodyPartFilter == null
                Surface(
                  shape = CircleShape,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                  modifier = Modifier
                    .clip(CircleShape)
                    .clickable { selectedBodyPartFilter = null }
                ) {
                  Text(
                    text = "All Body Areas",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
              val commonBodyParts = listOf("Waist", "Upper legs", "Lower legs", "Chest", "Back", "Shoulders", "Upper arms", "Cardio")
              items(commonBodyParts) { part ->
                val isSelected = selectedBodyPartFilter?.equals(part, ignoreCase = true) == true
                Surface(
                  shape = CircleShape,
                  color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                  modifier = Modifier
                    .clip(CircleShape)
                    .clickable { selectedBodyPartFilter = if (isSelected) null else part }
                ) {
                  Text(
                    text = part,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }
      }

      if (filteredExercises.isEmpty()) {
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(32.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(text = "📚", fontSize = 36.sp)
              Text(
                text = "No exercises matched your filters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Try clearing search or changing the body area filter.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(filteredExercises) { exercise ->
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(22.dp))
              .clickable { onPreviewExercise(exercise) }
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              ExerciseVisualArtwork(
                exercise = exercise,
                height = 145.dp,
                showLabels = true,
                preferDatasetMedia = true
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "${exercise.bodyPart} • Target: ${exercise.targetMuscle} • ${exercise.equipment}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Surface(
                  shape = CircleShape,
                  color = MaterialTheme.colorScheme.primaryContainer
                ) {
                  Text(
                    text = "Inspect 🌸",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

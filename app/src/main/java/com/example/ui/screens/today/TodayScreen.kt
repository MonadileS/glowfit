package com.example.ui.screens.today

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseCategory
import com.example.data.model.MoodLog
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutLog
import com.example.data.model.WorkoutRoutine

@Composable
fun TodayScreen(
  userProfile: UserProfile,
  routines: List<WorkoutRoutine>,
  todayMood: MoodLog?,
  workoutLogs: List<WorkoutLog>,
  onStartWorkout: (WorkoutRoutine) -> Unit,
  onPreviewRoutine: (WorkoutRoutine) -> Unit,
  onOpenMoodDialog: () -> Unit,
  onNavigateToWorkouts: () -> Unit,
  modifier: Modifier = Modifier
) {
  var waterGlasses by remember { mutableIntStateOf(5) }
  val featuredRoutine = routines.find { it.id == "prog_21day_glow" } ?: routines.firstOrNull()

  val todayMinutes = workoutLogs.filter { true }.sumOf { it.durationMinutes }
  val targetMinutes = userProfile.targetDailyMinutes.coerceAtLeast(15)
  val todayCalories = workoutLogs.sumOf { it.caloriesBurned }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // ---------------- 1. Minimalist Top Header ----------------
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "TODAY'S TARGET",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "Hi, ${userProfile.name}",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        // Clean Streak Badge
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak",
              tint = Color(0xFFEF4444),
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "${userProfile.currentStreak} Day Streak",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // ---------------- 2. Activity Rings / Daily Stats Summary Card (Nike / Apple style) ----------------
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "Daily Activity Target",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            Text(
              text = "${todayMinutes}m / ${targetMinutes}m",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          // Activity Progress Bar
          LinearProgressIndicator(
            progress = { (todayMinutes.toFloat() / targetMinutes.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
          )

          // 3-Metric Clean Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "TIME",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${todayMinutes} min",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Column {
              Text(
                text = "ACTIVE CALS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "$todayCalories kcal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Column {
              Text(
                text = "SESSIONS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${workoutLogs.size} Completed",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }

    // ---------------- 3. Featured Program Hero Card (Nike Training Club / Peloton style) ----------------
    if (featuredRoutine != null) {
      item {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Featured Program",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Text(
              text = "View All",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.clickable(onClick = onNavigateToWorkouts)
            )
          }

          // High-End Minimalist Studio Hero Card
          Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1E1F24),
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(24.dp))
              .clickable { onPreviewRoutine(featuredRoutine) }
          ) {
            Column(
              modifier = Modifier.padding(20.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = MaterialTheme.colorScheme.primary
                ) {
                  Text(
                    text = "DAY 4 OF 21 CHALLENGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }

                Text(
                  text = "${featuredRoutine.exerciseIds.size} Exercises",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFF9CA3AF),
                  fontWeight = FontWeight.SemiBold
                )
              }

              Text(
                text = featuredRoutine.title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
              )

              Text(
                text = featuredRoutine.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFD1D5DB),
                lineHeight = 18.sp
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${featuredRoutine.estimatedMinutes} min",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = "•",
                    color = Color(0xFF6B7280)
                  )
                  Text(
                    text = "${featuredRoutine.estimatedCalories} kcal",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                  )
                }

                Button(
                  onClick = { onStartWorkout(featuredRoutine) },
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Text("Start", fontWeight = FontWeight.Bold, color = Color.White)
                  }
                }
              }
            }
          }
        }
      }
    }

    // ---------------- 4. Targeted Workout Library Carousel ----------------
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Targeted Routines",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(routines.filter { it.id != featuredRoutine?.id }) { routine ->
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = MaterialTheme.colorScheme.surface,
              border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
              modifier = Modifier
                .width(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { onPreviewRoutine(routine) }
            ) {
              Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                  ) {
                    Text(
                      text = routine.category.displayName.uppercase(),
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.primary,
                      fontWeight = FontWeight.Bold,
                      fontSize = 9.sp,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }

                  Text(
                    text = "${routine.estimatedMinutes}m",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                  )
                }

                Text(
                  text = routine.title,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )

                Text(
                  text = routine.subtitle,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 2,
                  overflow = TextOverflow.Ellipsis,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )

                Button(
                  onClick = { onStartWorkout(routine) },
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "Start Routine",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }

    // ---------------- 5. Daily Hydration Widget (Minimalist Segmented Meter) ----------------
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = "Water",
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Hydration ($waterGlasses of 8 glasses)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Row(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape)
                  .clickable { waterGlasses = maxOf(0, waterGlasses - 1) }
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Remove, contentDescription = "Minus", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(14.dp))
                }
              }

              Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape)
                  .clickable { waterGlasses = minOf(12, waterGlasses + 1) }
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(16.dp))
                }
              }
            }
          }

          // 8-Segment Bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            for (i in 1..8) {
              val isFilled = i <= waterGlasses
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isFilled) Color(0xFF2563EB) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .weight(1f)
                  .height(6.dp)
              ) {}
            }
          }
        }
      }
    }

    // ---------------- 6. Mindful Check-in ----------------
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .clickable(onClick = onOpenMoodDialog)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (todayMood != null) "Logged: ${todayMood.moodType.displayName}" else "Daily Mindset Check-In",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (todayMood != null && todayMood.note.isNotBlank()) todayMood.note else "Track energy, mood & wellness notes",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
          ) {
            Text(
              text = if (todayMood != null) "Update" else "Check In",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}


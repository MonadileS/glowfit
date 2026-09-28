package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DifficultyLevel
import com.example.data.model.ExerciseCategory
import com.example.data.model.WorkoutRoutine

@Composable
fun GlowCard(
  modifier: Modifier = Modifier,
  gradientColors: List<Color> = listOf(
    MaterialTheme.colorScheme.surface,
    MaterialTheme.colorScheme.surface
  ),
  borderColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
  shape: RoundedCornerShape = RoundedCornerShape(24.dp),
  onClick: (() -> Unit)? = null,
  content: @Composable () -> Unit
) {
  val cardModifier = if (onClick != null) {
    modifier
      .clip(shape)
      .border(1.dp, borderColor, shape)
      .background(Brush.verticalGradient(gradientColors))
      .clickable(onClick = onClick)
  } else {
    modifier
      .clip(shape)
      .border(1.dp, borderColor, shape)
      .background(Brush.verticalGradient(gradientColors))
  }

  Box(modifier = cardModifier) {
    content()
  }
}

@Composable
fun StreakPill(
  streakDays: Int,
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null
) {
  val infiniteTransition = rememberInfiniteTransition(label = "flame_pulse")
  val scale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.12f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flame_scale"
  )

  Surface(
    modifier = modifier
      .clip(CircleShape)
      .clickable(enabled = onClick != null) { onClick?.invoke() },
    shape = CircleShape,
    color = Color(0xFFFFF1F3),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCD5DC))
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Box(modifier = Modifier.scale(scale)) {
        Icon(
          imageVector = Icons.Default.LocalFireDepartment,
          contentDescription = "Streak Flame",
          tint = Color(0xFFE0486D),
          modifier = Modifier.size(17.dp)
        )
      }
      Text(
        text = "$streakDays Days",
        style = MaterialTheme.typography.labelMedium,
        color = Color(0xFFB33054),
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun CategoryFilterPill(
  category: ExerciseCategory,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bgColor = if (isSelected) {
    MaterialTheme.colorScheme.primary
  } else {
    MaterialTheme.colorScheme.surface
  }
  val textColor = if (isSelected) {
    Color.White
  } else {
    MaterialTheme.colorScheme.onSurface
  }
  val borderStroke = if (isSelected) {
    androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
  } else {
    androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
  }

  Surface(
    modifier = modifier
      .clip(CircleShape)
      .clickable(onClick = onClick),
    shape = CircleShape,
    color = bgColor,
    border = borderStroke,
    shadowElevation = if (isSelected) 2.dp else 0.dp
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Text(text = category.emoji, fontSize = 14.sp)
      Text(
        text = category.displayName,
        style = MaterialTheme.typography.labelMedium,
        color = textColor,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
      )
    }
  }
}

@Composable
fun RoutineCard(
  routine: WorkoutRoutine,
  onStartClick: () -> Unit,
  onCardClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bannerGradient = when (routine.category) {
    ExerciseCategory.BELLY_CORE -> listOf(Color(0xFFFFF0F3), Color(0xFFFCE0E5))
    ExerciseCategory.BOOTY_LEGS -> listOf(Color(0xFFFFF4F0), Color(0xFFFEE1D5))
    ExerciseCategory.TONED_ARMS -> listOf(Color(0xFFF4F0F9), Color(0xFFE5DCF2))
    ExerciseCategory.HOURGLASS -> listOf(Color(0xFFFFF1F6), Color(0xFFFCD5E7))
    ExerciseCategory.FULL_BODY -> listOf(Color(0xFFFFF7EE), Color(0xFFFCE5CA))
    ExerciseCategory.STRETCH_POSTURE -> listOf(Color(0xFFEFF6F3), Color(0xFFD8EBE1))
    ExerciseCategory.CARDIO_BURN -> listOf(Color(0xFFFFF2EF), Color(0xFFFCD9CF))
  }

  val accentColor = when (routine.category) {
    ExerciseCategory.BELLY_CORE -> Color(0xFFD94870)
    ExerciseCategory.BOOTY_LEGS -> Color(0xFFE76F51)
    ExerciseCategory.TONED_ARMS -> Color(0xFF7E57C2)
    ExerciseCategory.HOURGLASS -> Color(0xFFC2185B)
    ExerciseCategory.FULL_BODY -> Color(0xFFD4A373)
    ExerciseCategory.STRETCH_POSTURE -> Color(0xFF52826F)
    ExerciseCategory.CARDIO_BURN -> Color(0xFFE65100)
  }

  GlowCard(
    modifier = modifier.fillMaxWidth(),
    gradientColors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface),
    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
    shape = RoundedCornerShape(22.dp),
    onClick = onCardClick
  ) {
    Column {
      // Header Banner with pastel gradient
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(96.dp)
          .background(Brush.horizontalGradient(bannerGradient))
          .padding(16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.9f),
              modifier = Modifier.padding(bottom = 6.dp)
            ) {
              Text(
                text = "${routine.category.emoji} ${routine.category.displayName}",
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Text(
              text = routine.title,
              style = MaterialTheme.typography.titleMedium,
              color = Color(0xFF221A1F),
              fontWeight = FontWeight.Bold,
              maxLines = 1
            )
          }

          // Exercise category cute icon
          Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.size(38.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = routine.category.emoji, fontSize = 18.sp)
            }
          }
        }
      }

      // Details Body
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Text(
          text = routine.subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = "${routine.estimatedMinutes} min",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFF05D5E),
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = "${routine.estimatedCalories} kcal",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
              )
            }

            Text(
              text = "• ${routine.exerciseIds.size} moves",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Start Button
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
              .clip(CircleShape)
              .clickable(onClick = onStartClick)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Start",
                tint = Color.White,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = "Start",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}

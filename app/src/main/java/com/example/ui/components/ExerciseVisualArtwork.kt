package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory

/**
 * Procedural aesthetic visual illustration component for exercise dataset moves.
 * Displays stylized silhouette, animated target muscle zone glow, and equipment/level indicators,
 * with seamless fallback/support for exercises-dataset media (GIF & thumbnails).
 */
@Composable
fun ExerciseVisualArtwork(
  exercise: Exercise,
  modifier: Modifier = Modifier,
  height: Dp = 160.dp,
  showLabels: Boolean = true,
  preferDatasetMedia: Boolean = false
) {
  val infiniteTransition = rememberInfiniteTransition(label = "exercise_visual_motion")

  // Muscle highlight glowing pulse
  val musclePulse by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "muscle_pulse"
  )

  // Floating particle / sparkle phase
  val floatPhase by infiniteTransition.animateFloat(
    initialValue = -5f,
    targetValue = 5f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "float_phase"
  )

  val themeColors = when (exercise.category) {
    ExerciseCategory.BELLY_CORE -> Pair(Color(0xFFFF4081), Color(0xFFFFB2C9))
    ExerciseCategory.BOOTY_LEGS -> Pair(Color(0xFFAB47BC), Color(0xFFE1BEE7))
    ExerciseCategory.TONED_ARMS -> Pair(Color(0xFF7E57C2), Color(0xFFD1C4E9))
    ExerciseCategory.HOURGLASS -> Pair(Color(0xFFEC407A), Color(0xFFF8BBD0))
    ExerciseCategory.FULL_BODY -> Pair(Color(0xFFFF7043), Color(0xFFFFCCBC))
    ExerciseCategory.STRETCH_POSTURE -> Pair(Color(0xFF5C6BC0), Color(0xFFC5CAE9))
    ExerciseCategory.CARDIO_BURN -> Pair(Color(0xFFFF5252), Color(0xFFFFCDD2))
  }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .height(height)
      .clip(RoundedCornerShape(24.dp)),
    color = themeColors.second.copy(alpha = 0.25f),
    border = androidx.compose.foundation.BorderStroke(1.dp, themeColors.first.copy(alpha = 0.3f))
  ) {
    val mediaUrl = exercise.gifUrl ?: exercise.thumbnailUrl
    if (preferDatasetMedia && mediaUrl != null) {
      Box(modifier = Modifier.fillMaxSize()) {
        SubcomposeAsyncImage(
          model = mediaUrl,
          contentDescription = exercise.name,
          contentScale = ContentScale.Fit,
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
          loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
              Text("Loading motion GIF... ✨", style = MaterialTheme.typography.labelMedium, color = themeColors.first)
            }
          },
          error = {
            ProceduralSilhouetteCanvas(exercise, themeColors, musclePulse, floatPhase)
          }
        )

        if (showLabels) {
          ArtworkOverlays(exercise, themeColors, musclePulse)
        }
      }
    } else {
      Box(modifier = Modifier.fillMaxSize()) {
        ProceduralSilhouetteCanvas(exercise, themeColors, musclePulse, floatPhase)
        if (showLabels) {
          ArtworkOverlays(exercise, themeColors, musclePulse)
        }
      }
    }
  }
}

@Composable
private fun ArtworkOverlays(
  exercise: Exercise,
  themeColors: Pair<Color, Color>,
  musclePulse: Float
) {
  Box(modifier = Modifier.fillMaxSize()) {
    // Top-Left Floating Badge: Target Muscle Pulse
    Row(
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Surface(
        shape = CircleShape,
        color = themeColors.first.copy(alpha = 0.9f)
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = musclePulse))
          )
          Text(
            text = exercise.targetMuscle,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }

      // Equipment Pill
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
        border = androidx.compose.foundation.BorderStroke(1.dp, themeColors.first.copy(alpha = 0.3f))
      ) {
        Text(
          text = exercise.equipment,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
      }
    }

    // Top-Right Floating Badge: Category & Difficulty
    Row(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(12.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        border = androidx.compose.foundation.BorderStroke(1.dp, themeColors.first.copy(alpha = 0.4f))
      ) {
        Text(
          text = "${exercise.category.emoji} ${exercise.difficulty.displayName}",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
      }
    }

    // Bottom Banner Overlay: Exercise Name & Duration
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter),
      color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = exercise.name,
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "⏱️ ${exercise.durationSeconds}s • ~${exercise.estimatedCalories} kcal",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.SemiBold,
          color = themeColors.first
        )
      }
    }
  }
}

@Composable
private fun ProceduralSilhouetteCanvas(
  exercise: Exercise,
  themeColors: Pair<Color, Color>,
  musclePulse: Float,
  floatPhase: Float
) {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height
    val primaryColor = themeColors.first
    val accentColor = themeColors.second

    // Soft ambient background aura
    drawCircle(
      brush = Brush.radialGradient(
        colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
        center = Offset(w * 0.5f, h * 0.5f),
        radius = w * 0.45f
      ),
      center = Offset(w * 0.5f, h * 0.5f),
      radius = w * 0.45f
    )

    // Draw Yoga Mat on bottom
    val matWidth = w * 0.75f
    val matHeight = 12.dp.toPx()
    val matX = (w - matWidth) / 2
    val matY = h * 0.78f
    drawRoundRect(
      color = primaryColor.copy(alpha = 0.4f),
      topLeft = Offset(matX, matY),
      size = Size(matWidth, matHeight),
      cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
    )

    // Draw Stylized Figure Silhouette based on exercise posture
    val figurePaintColor = Color(0xFF4A4458)

    when (exercise.category) {
      ExerciseCategory.BELLY_CORE -> {
        // Aesthetic Reclined Core Pose (Russian twist / Crunch posture)
        val bodyPath = Path().apply {
          moveTo(w * 0.32f, h * 0.45f + floatPhase)
          addOval(Rect(w * 0.28f, h * 0.38f + floatPhase, w * 0.36f, h * 0.48f + floatPhase))
          moveTo(w * 0.34f, h * 0.48f + floatPhase)
          lineTo(w * 0.48f, h * 0.70f) // Spine
          lineTo(w * 0.62f, h * 0.55f + floatPhase * 0.5f) // Thighs lifted
          lineTo(w * 0.74f, h * 0.68f) // Lower legs
        }
        drawPath(
          path = bodyPath,
          color = figurePaintColor,
          style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
        )

        // Arms reaching across body
        drawLine(
          color = figurePaintColor,
          start = Offset(w * 0.40f, h * 0.56f + floatPhase * 0.7f),
          end = Offset(w * 0.52f, h * 0.58f),
          strokeWidth = 6.dp.toPx(),
          cap = StrokeCap.Round
        )

        // Active Abs / Obliques Glowing Target Zone
        drawCircle(
          color = primaryColor.copy(alpha = 0.85f * musclePulse),
          center = Offset(w * 0.44f, h * 0.62f + floatPhase * 0.4f),
          radius = 12.dp.toPx() * musclePulse
        )
        drawCircle(
          color = Color.White,
          center = Offset(w * 0.44f, h * 0.62f + floatPhase * 0.4f),
          radius = 4.dp.toPx()
        )
      }

      ExerciseCategory.BOOTY_LEGS -> {
        // Aesthetic Glute Bridge / Donkey Kick posture
        val spinePath = Path().apply {
          moveTo(w * 0.28f, h * 0.75f)
          addOval(Rect(w * 0.22f, h * 0.70f, w * 0.28f, h * 0.78f))
          moveTo(w * 0.28f, h * 0.75f)
          quadraticTo(w * 0.42f, h * 0.45f + floatPhase * 0.8f, w * 0.56f, h * 0.48f + floatPhase * 0.8f)
          lineTo(w * 0.68f, h * 0.75f)
        }
        drawPath(
          path = spinePath,
          color = figurePaintColor,
          style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
        )

        // Glute Glowing Target Area
        drawCircle(
          color = primaryColor.copy(alpha = 0.9f * musclePulse),
          center = Offset(w * 0.54f, h * 0.50f + floatPhase * 0.8f),
          radius = 14.dp.toPx() * musclePulse
        )
        drawCircle(
          color = Color.White,
          center = Offset(w * 0.54f, h * 0.50f + floatPhase * 0.8f),
          radius = 4.5.dp.toPx()
        )
      }

      ExerciseCategory.TONED_ARMS -> {
        // Aesthetic Sculpted Arm / Plank posture
        val armPath = Path().apply {
          addOval(Rect(w * 0.32f, h * 0.32f + floatPhase, w * 0.38f, h * 0.42f + floatPhase))
          moveTo(w * 0.35f, h * 0.42f + floatPhase)
          lineTo(w * 0.68f, h * 0.60f)
          lineTo(w * 0.75f, h * 0.75f)
          moveTo(w * 0.38f, h * 0.45f + floatPhase)
          lineTo(w * 0.36f, h * 0.75f)
        }
        drawPath(
          path = armPath,
          color = figurePaintColor,
          style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
        )

        // Arms & Shoulders Target Glow
        drawCircle(
          color = primaryColor.copy(alpha = 0.9f * musclePulse),
          center = Offset(w * 0.37f, h * 0.52f + floatPhase * 0.5f),
          radius = 11.dp.toPx() * musclePulse
        )
        drawCircle(
          color = Color.White,
          center = Offset(w * 0.37f, h * 0.52f + floatPhase * 0.5f),
          radius = 3.5.dp.toPx()
        )
      }

      ExerciseCategory.HOURGLASS -> {
        // Curving Standing / Side Plank Hourglass Silhouette
        val hourglassPath = Path().apply {
          addOval(Rect(w * 0.47f, h * 0.22f + floatPhase, w * 0.53f, h * 0.32f + floatPhase))
          moveTo(w * 0.50f, h * 0.32f + floatPhase)
          lineTo(w * 0.50f, h * 0.48f + floatPhase)
          quadraticTo(w * 0.42f, h * 0.53f, w * 0.50f, h * 0.62f)
          lineTo(w * 0.46f, h * 0.78f)
          moveTo(w * 0.50f, h * 0.62f)
          lineTo(w * 0.54f, h * 0.78f)
        }
        drawPath(
          path = hourglassPath,
          color = figurePaintColor,
          style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
        )

        // Hourglass Waist Cinch Glow Points
        drawCircle(
          color = primaryColor.copy(alpha = 0.9f * musclePulse),
          center = Offset(w * 0.45f, h * 0.53f + floatPhase * 0.5f),
          radius = 9.dp.toPx() * musclePulse
        )
        drawCircle(
          color = primaryColor.copy(alpha = 0.9f * musclePulse),
          center = Offset(w * 0.55f, h * 0.53f + floatPhase * 0.5f),
          radius = 9.dp.toPx() * musclePulse
        )
      }

      ExerciseCategory.FULL_BODY -> {
        // Dynamic Jumping Jack / Burpee Star Posture
        val fullBodyPath = Path().apply {
          addOval(Rect(w * 0.46f, h * 0.24f + floatPhase, w * 0.54f, h * 0.34f + floatPhase))
          moveTo(w * 0.50f, h * 0.34f + floatPhase)
          lineTo(w * 0.50f, h * 0.58f + floatPhase)
          moveTo(w * 0.50f, h * 0.40f + floatPhase)
          lineTo(w * 0.34f, h * 0.28f + floatPhase)
          moveTo(w * 0.50f, h * 0.40f + floatPhase)
          lineTo(w * 0.66f, h * 0.28f + floatPhase)
          moveTo(w * 0.50f, h * 0.58f + floatPhase)
          lineTo(w * 0.38f, h * 0.78f)
          moveTo(w * 0.50f, h * 0.58f + floatPhase)
          lineTo(w * 0.62f, h * 0.78f)
        }
        drawPath(
          path = fullBodyPath,
          color = figurePaintColor,
          style = Stroke(width = 6.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Core Center Radiance Glow
        drawCircle(
          color = primaryColor.copy(alpha = 0.85f * musclePulse),
          center = Offset(w * 0.50f, h * 0.50f + floatPhase),
          radius = 14.dp.toPx() * musclePulse
        )
      }

      ExerciseCategory.STRETCH_POSTURE -> {
        // Graceful Cobra / Swan Posture
        val cobraPath = Path().apply {
          addOval(Rect(w * 0.60f, h * 0.35f + floatPhase, w * 0.67f, h * 0.45f + floatPhase))
          moveTo(w * 0.63f, h * 0.45f + floatPhase)
          quadraticTo(w * 0.52f, h * 0.55f, w * 0.30f, h * 0.75f)
          moveTo(w * 0.58f, h * 0.52f + floatPhase)
          lineTo(w * 0.58f, h * 0.75f)
        }
        drawPath(
          path = cobraPath,
          color = figurePaintColor,
          style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
        )

        // Heart & Spine Flexibility Glow
        drawCircle(
          color = primaryColor.copy(alpha = 0.85f * musclePulse),
          center = Offset(w * 0.54f, h * 0.54f + floatPhase * 0.5f),
          radius = 12.dp.toPx() * musclePulse
        )
      }

      ExerciseCategory.CARDIO_BURN -> {
        // High Knees / Sprint Energy Posture
        val cardioPath = Path().apply {
          addOval(Rect(w * 0.45f, h * 0.25f + floatPhase, w * 0.53f, h * 0.35f + floatPhase))
          moveTo(w * 0.49f, h * 0.35f + floatPhase)
          lineTo(w * 0.49f, h * 0.58f + floatPhase)
          moveTo(w * 0.49f, h * 0.58f + floatPhase)
          lineTo(w * 0.64f, h * 0.52f + floatPhase)
          lineTo(w * 0.64f, h * 0.68f)
          moveTo(w * 0.49f, h * 0.58f + floatPhase)
          lineTo(w * 0.42f, h * 0.78f)
          moveTo(w * 0.49f, h * 0.40f + floatPhase)
          lineTo(w * 0.36f, h * 0.46f)
          moveTo(w * 0.49f, h * 0.40f + floatPhase)
          lineTo(w * 0.62f, h * 0.38f)
        }
        drawPath(
          path = cardioPath,
          color = figurePaintColor,
          style = Stroke(width = 6.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Calorie Burn Flame Pulse
        drawCircle(
          color = primaryColor.copy(alpha = 0.9f * musclePulse),
          center = Offset(w * 0.50f, h * 0.48f + floatPhase),
          radius = 15.dp.toPx() * musclePulse
        )
      }
    }

    // Sparkle particles for aesthetic glow
    drawCircle(
      color = accentColor.copy(alpha = 0.8f * musclePulse),
      center = Offset(w * 0.20f, h * 0.30f - floatPhase),
      radius = 3.dp.toPx()
    )
    drawCircle(
      color = primaryColor.copy(alpha = 0.7f),
      center = Offset(w * 0.80f, h * 0.40f + floatPhase),
      radius = 4.dp.toPx()
    )
  }
}

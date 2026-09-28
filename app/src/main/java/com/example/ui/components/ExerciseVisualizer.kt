package com.example.ui.components

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-impact unified animation & countdown stage.
 * Combines dynamic 60fps kinematic movement of the exercise figure / dataset GIF media with
 * an integrated circular countdown timer and digital clock display in the exact same focal place.
 */
@Composable
fun ExerciseAnimationVisualizer(
  exercise: Exercise?,
  isResting: Boolean,
  timeRemainingSeconds: Int = 30,
  totalSeconds: Int = 30,
  isPaused: Boolean = false,
  showCountdownOverlay: Boolean = true,
  preferDatasetMedia: Boolean = true,
  modifier: Modifier = Modifier.size(240.dp)
) {
  val context = LocalContext.current
  var showDatasetGif by remember { mutableStateOf(preferDatasetMedia) }

  // Custom ImageLoader with GIF decoding factory
  val gifImageLoader = remember(context) {
    ImageLoader.Builder(context)
      .components {
        if (Build.VERSION.SDK_INT >= 28) {
          add(ImageDecoderDecoder.Factory())
        } else {
          add(GifDecoder.Factory())
        }
      }
      .build()
  }

  val infiniteTransition = rememberInfiniteTransition(label = "unified_motion_clock")

  // Repetition cycle phase: 0.0 -> 1.0 -> 0.0 (kinetic motion curve)
  val repPhase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = if (isPaused) 100000 else 1800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "rep_phase"
  )

  // Calming breathing phase for rest mode
  val breathPhase by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "breath_phase"
  )

  // Rotating energy aura
  val rotationDegrees by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 7000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotation"
  )

  // Muscle contraction highlight pulse
  val muscleGlow by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "muscle_glow"
  )

  val categoryColor = when {
    isResting -> Color(0xFF5C6BC0)
    exercise?.category == ExerciseCategory.BELLY_CORE -> Color(0xFFFF4081)
    exercise?.category == ExerciseCategory.BOOTY_LEGS -> Color(0xFFAB47BC)
    exercise?.category == ExerciseCategory.TONED_ARMS -> Color(0xFF7E57C2)
    exercise?.category == ExerciseCategory.HOURGLASS -> Color(0xFFEC407A)
    exercise?.category == ExerciseCategory.FULL_BODY -> Color(0xFFFF7043)
    exercise?.category == ExerciseCategory.STRETCH_POSTURE -> Color(0xFF26A69A)
    exercise?.category == ExerciseCategory.CARDIO_BURN -> Color(0xFFFF5252)
    else -> Color(0xFFFF4081)
  }

  val accentColor = when {
    isResting -> Color(0xFFC5CAE9)
    exercise?.category == ExerciseCategory.BELLY_CORE -> Color(0xFFFFB2C9)
    exercise?.category == ExerciseCategory.BOOTY_LEGS -> Color(0xFFE1BEE7)
    exercise?.category == ExerciseCategory.TONED_ARMS -> Color(0xFFD1C4E9)
    exercise?.category == ExerciseCategory.HOURGLASS -> Color(0xFFF8BBD0)
    exercise?.category == ExerciseCategory.FULL_BODY -> Color(0xFFFFCCBC)
    exercise?.category == ExerciseCategory.STRETCH_POSTURE -> Color(0xFFB2DFDB)
    exercise?.category == ExerciseCategory.CARDIO_BURN -> Color(0xFFFFCDD2)
    else -> Color(0xFFFFB2C9)
  }

  val safeTotal = if (totalSeconds > 0) totalSeconds else 30
  val progressFraction = (timeRemainingSeconds.toFloat() / safeTotal.toFloat()).coerceIn(0f, 1f)
  val sweepProgressAngle = progressFraction * 360f

  val minutes = timeRemainingSeconds / 60
  val seconds = timeRemainingSeconds % 60
  val formattedTimerText = String.format("%02d:%02d", minutes, seconds)

  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    // Canvas: Outer Countdown Ring + Animated Kinematic Body Silhouette
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val centerOffset = Offset(w / 2f, h / 2f)
      val radius = (size.minDimension / 2f) - 12.dp.toPx()

      // 1. Ambient Pulsing Neon Glow
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            categoryColor.copy(alpha = 0.22f),
            categoryColor.copy(alpha = 0.05f),
            Color.Transparent
          ),
          center = centerOffset,
          radius = radius * 1.3f
        ),
        radius = radius * 1.3f,
        center = centerOffset
      )

      // 2. Countdown Background Track Ring
      drawCircle(
        color = categoryColor.copy(alpha = 0.15f),
        radius = radius,
        center = centerOffset,
        style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
      )

      // 3. Active Countdown Sweep Arc
      if (sweepProgressAngle > 0f) {
        drawArc(
          brush = Brush.sweepGradient(
            colors = listOf(
              categoryColor.copy(alpha = 0.6f),
              categoryColor,
              accentColor,
              categoryColor
            ),
            center = centerOffset
          ),
          startAngle = -90f,
          sweepAngle = sweepProgressAngle,
          useCenter = false,
          topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
          size = Size(radius * 2, radius * 2),
          style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
        )

        // Indicator bead on the head of the countdown arc
        val angleRad = Math.toRadians(((-90f + sweepProgressAngle).toDouble()))
        val beadX = centerOffset.x + (radius * cos(angleRad)).toFloat()
        val beadY = centerOffset.y + (radius * sin(angleRad)).toFloat()

        drawCircle(
          color = Color.White,
          center = Offset(beadX, beadY),
          radius = 5.dp.toPx()
        )
        drawCircle(
          color = categoryColor,
          center = Offset(beadX, beadY),
          radius = 3.dp.toPx()
        )
      }

      // 4. Subtle Inner Guide Aura
      drawCircle(
        color = categoryColor.copy(alpha = 0.04f),
        radius = radius * 0.85f,
        center = centerOffset
      )

      // 5. Draw Dynamic Animated Exercise Figures (when not showing dataset GIF or in rest)
      if (isResting) {
        drawRestingKinematics(w, h, breathPhase, categoryColor, accentColor)
      } else if (!showDatasetGif || (exercise?.gifUrl == null && exercise?.thumbnailUrl == null)) {
        drawExerciseKinematics(
          w = w,
          h = h,
          exercise = exercise,
          repPhase = repPhase,
          muscleGlow = muscleGlow,
          primaryColor = categoryColor,
          accentColor = accentColor
        )
      }
    }

    // Dataset GIF / Motion Media inside the Countdown Ring
    if (!isResting && showDatasetGif && (exercise?.gifUrl != null || exercise?.thumbnailUrl != null)) {
      val mediaUrl = exercise.gifUrl ?: exercise.thumbnailUrl
      Box(
        modifier = Modifier
          .size((240 * 0.68f).dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
      ) {
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(mediaUrl)
            .crossfade(true)
            .build(),
          imageLoader = gifImageLoader,
          contentDescription = exercise.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    // 6. Integrated In-Place Countdown Digital Badge
    if (showCountdownOverlay) {
      Surface(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .offset(y = (-10).dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
        border = androidx.compose.foundation.BorderStroke(1.dp, categoryColor.copy(alpha = 0.4f)),
        shadowElevation = 4.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(if (isPaused) Color(0xFFFFB300) else categoryColor)
          )
          Text(
            text = formattedTimerText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          if (!isResting && (exercise?.gifUrl != null || exercise?.thumbnailUrl != null)) {
            Box(
              modifier = Modifier
                .padding(start = 4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { showDatasetGif = !showDatasetGif }
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (showDatasetGif) "GIF" else "ANAT",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// KINEMATIC ANIMATED CHARACTER DRAWING FUNCTIONS
// -------------------------------------------------------------

private fun DrawScope.drawExerciseKinematics(
  w: Float,
  h: Float,
  exercise: Exercise?,
  repPhase: Float,
  muscleGlow: Float,
  primaryColor: Color,
  accentColor: Color
) {
  val figureColor = Color(0xFF374151)
  val category = exercise?.category ?: ExerciseCategory.BELLY_CORE

  // Floor Mat
  val matWidth = w * 0.65f
  val matY = h * 0.72f
  drawRoundRect(
    color = primaryColor.copy(alpha = 0.25f),
    topLeft = Offset((w - matWidth) / 2f, matY),
    size = Size(matWidth, 6.dp.toPx()),
    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
  )

  when (category) {
    ExerciseCategory.BELLY_CORE -> {
      // Dynamic Russian Twist / Crunch / Bicycle kinematic motion
      // Torso twists & reclines between 35 and 55 degrees; knees alternate
      val twistAngle = (repPhase - 0.5f) * 2f // -1.0 to 1.0
      val torsoAngle = 40f + (twistAngle * 12f)
      val hipX = w * 0.46f
      val hipY = h * 0.66f

      // Head and torso position
      val torsoLength = h * 0.22f
      val headX = hipX - (torsoLength * 0.7f) + (twistAngle * 10.dp.toPx())
      val headY = hipY - (torsoLength * 0.7f)

      // Knees up & moving
      val kneeX = hipX + (w * 0.16f) - (twistAngle * 8.dp.toPx())
      val kneeY = hipY - (h * 0.14f) + (twistAngle * 6.dp.toPx())
      val footX = kneeX + (w * 0.12f)
      val footY = hipY - (h * 0.04f)

      // Arms reaching across body
      val handX = hipX + (twistAngle * 24.dp.toPx())
      val handY = hipY - (h * 0.08f)

      // Draw Head
      drawCircle(
        color = figureColor,
        center = Offset(headX, headY),
        radius = 8.dp.toPx()
      )

      // Spine / Torso line
      drawLine(
        color = figureColor,
        start = Offset(headX, headY),
        end = Offset(hipX, hipY),
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Legs
      drawLine(
        color = figureColor,
        start = Offset(hipX, hipY),
        end = Offset(kneeX, kneeY),
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round
      )
      drawLine(
        color = figureColor,
        start = Offset(kneeX, kneeY),
        end = Offset(footX, footY),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Arms rotating
      val shoulderX = (headX * 0.6f + hipX * 0.4f)
      val shoulderY = (headY * 0.6f + hipY * 0.4f)
      drawLine(
        color = figureColor,
        start = Offset(shoulderX, shoulderY),
        end = Offset(handX, handY),
        strokeWidth = 5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Active Abdominal Contraction Glow Pulse
      val absX = (shoulderX + hipX) / 2f
      val absY = (shoulderY + hipY) / 2f
      drawCircle(
        color = primaryColor.copy(alpha = 0.85f * muscleGlow),
        center = Offset(absX, absY),
        radius = 11.dp.toPx() * muscleGlow
      )
      drawCircle(
        color = Color.White,
        center = Offset(absX, absY),
        radius = 3.5.dp.toPx()
      )
    }

    ExerciseCategory.BOOTY_LEGS -> {
      // Dynamic Glute Bridge / Donkey Kick motion
      // Hips thrust upwards in rhythm (repPhase: 0.0 bottom -> 1.0 top)
      val shoulderX = w * 0.32f
      val shoulderY = h * 0.68f

      val hipElevation = repPhase * (h * 0.16f)
      val hipX = w * 0.50f
      val hipY = h * 0.66f - hipElevation

      val kneeX = w * 0.66f
      val kneeY = h * 0.60f - (hipElevation * 0.4f)
      val footX = w * 0.68f
      val footY = h * 0.70f

      // Head resting on floor
      drawCircle(
        color = figureColor,
        center = Offset(shoulderX - 12.dp.toPx(), shoulderY + 2.dp.toPx()),
        radius = 7.5.dp.toPx()
      )

      // Back / Pelvis thrust line
      val bridgePath = Path().apply {
        moveTo(shoulderX, shoulderY)
        quadraticTo(hipX, hipY, kneeX, kneeY)
        lineTo(footX, footY)
      }
      drawPath(
        path = bridgePath,
        color = figureColor,
        style = Stroke(width = 6.5.dp.toPx(), cap = StrokeCap.Round)
      )

      // Glute Contraction Glow at peak elevation
      drawCircle(
        color = primaryColor.copy(alpha = 0.9f * repPhase * muscleGlow),
        center = Offset(hipX, hipY),
        radius = 13.dp.toPx() * (0.6f + (0.4f * repPhase))
      )
      drawCircle(
        color = Color.White,
        center = Offset(hipX, hipY),
        radius = 4.dp.toPx() * repPhase
      )
    }

    ExerciseCategory.TONED_ARMS -> {
      // Dynamic Plank / Push-up & Arm flex kinematic motion
      // Torso lowers and pushes up (repPhase: 0 -> 1)
      val pushOffset = repPhase * (h * 0.08f)
      val handX = w * 0.38f
      val handY = h * 0.70f

      val shoulderX = w * 0.38f
      val shoulderY = h * 0.48f + pushOffset

      val headX = shoulderX - 8.dp.toPx()
      val headY = shoulderY - 10.dp.toPx()

      val footX = w * 0.72f
      val footY = h * 0.70f

      // Head
      drawCircle(
        color = figureColor,
        center = Offset(headX, headY),
        radius = 7.5.dp.toPx()
      )

      // Spine / Body rigid line
      drawLine(
        color = figureColor,
        start = Offset(shoulderX, shoulderY),
        end = Offset(footX, footY),
        strokeWidth = 6.5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Arm pushing up
      drawLine(
        color = figureColor,
        start = Offset(shoulderX, shoulderY),
        end = Offset(handX, handY),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Shoulder and Arm Muscle Highlight
      drawCircle(
        color = primaryColor.copy(alpha = 0.85f * muscleGlow),
        center = Offset(shoulderX, shoulderY + (pushOffset * 0.5f)),
        radius = 10.dp.toPx() * muscleGlow
      )
    }

    ExerciseCategory.HOURGLASS -> {
      // Dynamic lateral swaying & waist cinch curvature
      val curveShift = (repPhase - 0.5f) * 18.dp.toPx()
      val headX = w * 0.50f
      val headY = h * 0.32f

      val chestX = w * 0.50f + (curveShift * 0.5f)
      val chestY = h * 0.44f

      val waistX = w * 0.50f - curveShift
      val waistY = h * 0.54f

      val hipX = w * 0.50f + (curveShift * 0.3f)
      val hipY = h * 0.62f

      // Head
      drawCircle(
        color = figureColor,
        center = Offset(headX, headY),
        radius = 7.5.dp.toPx()
      )

      // Hourglass Spine Curve
      val hourglassPath = Path().apply {
        moveTo(headX, headY)
        lineTo(chestX, chestY)
        quadraticTo(waistX, waistY, hipX, hipY)
        lineTo(w * 0.44f, h * 0.72f)
        moveTo(hipX, hipY)
        lineTo(w * 0.56f, h * 0.72f)
      }
      drawPath(
        path = hourglassPath,
        color = figureColor,
        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
      )

      // Oblique Cinch Glow Points
      drawCircle(
        color = primaryColor.copy(alpha = 0.9f * muscleGlow),
        center = Offset(waistX - 8.dp.toPx(), waistY),
        radius = 8.dp.toPx() * muscleGlow
      )
      drawCircle(
        color = primaryColor.copy(alpha = 0.9f * muscleGlow),
        center = Offset(waistX + 8.dp.toPx(), waistY),
        radius = 8.dp.toPx() * muscleGlow
      )
    }

    ExerciseCategory.FULL_BODY -> {
      // Dynamic Jumping Jack Star Open / Close Animation
      val spread = repPhase * 20.dp.toPx() // limbs expand outwards
      val headX = w * 0.50f
      val headY = h * 0.30f - (repPhase * 6.dp.toPx())

      val hipX = w * 0.50f
      val hipY = h * 0.52f

      // Head
      drawCircle(
        color = figureColor,
        center = Offset(headX, headY),
        radius = 8.dp.toPx()
      )

      // Torso
      drawLine(
        color = figureColor,
        start = Offset(headX, headY),
        end = Offset(hipX, hipY),
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Arms spreading overhead into star
      val armY = headY - (repPhase * 8.dp.toPx())
      drawLine(
        color = figureColor,
        start = Offset(headX, headY + 10.dp.toPx()),
        end = Offset(headX - 12.dp.toPx() - spread, armY),
        strokeWidth = 5.dp.toPx(),
        cap = StrokeCap.Round
      )
      drawLine(
        color = figureColor,
        start = Offset(headX, headY + 10.dp.toPx()),
        end = Offset(headX + 12.dp.toPx() + spread, armY),
        strokeWidth = 5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Legs jumping out
      drawLine(
        color = figureColor,
        start = Offset(hipX, hipY),
        end = Offset(hipX - 8.dp.toPx() - spread, h * 0.72f),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )
      drawLine(
        color = figureColor,
        start = Offset(hipX, hipY),
        end = Offset(hipX + 8.dp.toPx() + spread, h * 0.72f),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Center Star Glow Burst
      drawCircle(
        color = primaryColor.copy(alpha = 0.85f * repPhase * muscleGlow),
        center = Offset(hipX, hipY - 6.dp.toPx()),
        radius = 12.dp.toPx() * (0.5f + (0.5f * repPhase))
      )
    }

    ExerciseCategory.STRETCH_POSTURE -> {
      // Dynamic Cobra / Swan Chest Expansion & Breath
      val stretchLift = repPhase * (h * 0.12f)
      val hipX = w * 0.34f
      val hipY = h * 0.70f

      val chestX = w * 0.60f
      val chestY = h * 0.60f - stretchLift

      val headX = chestX + 6.dp.toPx()
      val headY = chestY - 12.dp.toPx()

      // Head
      drawCircle(
        color = figureColor,
        center = Offset(headX, headY),
        radius = 7.5.dp.toPx()
      )

      // Spine graceful curve
      val spinePath = Path().apply {
        moveTo(hipX, hipY)
        quadraticTo(chestX - (w * 0.12f), h * 0.68f, chestX, chestY)
      }
      drawPath(
        path = spinePath,
        color = figureColor,
        style = Stroke(width = 6.5.dp.toPx(), cap = StrokeCap.Round)
      )

      // Arms supporting chest
      drawLine(
        color = figureColor,
        start = Offset(chestX, chestY),
        end = Offset(chestX, h * 0.70f),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Heart & Spine Glow
      drawCircle(
        color = primaryColor.copy(alpha = 0.8f * muscleGlow),
        center = Offset(chestX - 4.dp.toPx(), chestY),
        radius = 11.dp.toPx() * muscleGlow
      )
    }

    ExerciseCategory.CARDIO_BURN -> {
      // Dynamic High Knees / Sprinting Cadence
      val isLeftHigh = repPhase > 0.5f
      val leftKneeY = if (isLeftHigh) h * 0.44f else h * 0.66f
      val rightKneeY = if (isLeftHigh) h * 0.66f else h * 0.44f

      val headX = w * 0.50f
      val headY = h * 0.32f
      val hipX = w * 0.50f
      val hipY = h * 0.52f

      // Head
      drawCircle(
        color = figureColor,
        center = Offset(headX, headY),
        radius = 7.5.dp.toPx()
      )

      // Torso
      drawLine(
        color = figureColor,
        start = Offset(headX, headY),
        end = Offset(hipX, hipY),
        strokeWidth = 6.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Left Leg
      drawLine(
        color = figureColor,
        start = Offset(hipX, hipY),
        end = Offset(w * 0.42f, leftKneeY),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )
      drawLine(
        color = figureColor,
        start = Offset(w * 0.42f, leftKneeY),
        end = Offset(w * 0.40f, h * 0.72f),
        strokeWidth = 5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Right Leg
      drawLine(
        color = figureColor,
        start = Offset(hipX, hipY),
        end = Offset(w * 0.58f, rightKneeY),
        strokeWidth = 5.5.dp.toPx(),
        cap = StrokeCap.Round
      )
      drawLine(
        color = figureColor,
        start = Offset(w * 0.58f, rightKneeY),
        end = Offset(w * 0.60f, h * 0.72f),
        strokeWidth = 5.dp.toPx(),
        cap = StrokeCap.Round
      )

      // Cardio Flame Energy Aura
      drawCircle(
        color = primaryColor.copy(alpha = 0.9f * muscleGlow),
        center = Offset(hipX, hipY),
        radius = 14.dp.toPx() * muscleGlow
      )
    }
  }
}

private fun DrawScope.drawRestingKinematics(
  w: Float,
  h: Float,
  breathPhase: Float,
  primaryColor: Color,
  accentColor: Color
) {
  val centerOffset = Offset(w / 2f, h / 2f - 4.dp.toPx())

  // Breathing Ripple Circles
  drawCircle(
    brush = Brush.radialGradient(
      colors = listOf(primaryColor.copy(alpha = 0.35f), Color.Transparent),
      center = centerOffset,
      radius = 45.dp.toPx() * breathPhase
    ),
    center = centerOffset,
    radius = 45.dp.toPx() * breathPhase
  )

  // Gentle Meditative Cross-Legged Silhouette
  val figureColor = Color(0xFF4B5563)
  val headY = centerOffset.y - (20.dp.toPx() * breathPhase)

  // Head
  drawCircle(
    color = figureColor,
    center = Offset(centerOffset.x, headY),
    radius = 8.dp.toPx()
  )

  // Torso
  drawLine(
    color = figureColor,
    start = Offset(centerOffset.x, headY),
    end = Offset(centerOffset.x, centerOffset.y + 12.dp.toPx()),
    strokeWidth = 6.dp.toPx(),
    cap = StrokeCap.Round
  )

  // Folded Legs Base
  drawLine(
    color = figureColor,
    start = Offset(centerOffset.x - 22.dp.toPx(), centerOffset.y + 16.dp.toPx()),
    end = Offset(centerOffset.x + 22.dp.toPx(), centerOffset.y + 16.dp.toPx()),
    strokeWidth = 6.dp.toPx(),
    cap = StrokeCap.Round
  )
}

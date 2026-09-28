package com.example.ui.screens.profile

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.Badge
import com.example.data.model.UserProfile
import com.example.ui.components.GlowCard
import com.example.ui.components.StreakPill

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
  userProfile: UserProfile,
  badges: List<Badge>,
  onOpenEditProfile: () -> Unit,
  onToggleDarkMode: (Boolean) -> Unit,
  onToggleSound: (Boolean) -> Unit,
  onUpdateTargetMinutes: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val nextLevelXp = userProfile.level * 200
  val currentLevelBaseXp = (userProfile.level - 1) * 200
  val xpInLevel = (userProfile.totalXp - currentLevelBaseXp).coerceAtLeast(0)
  val xpNeeded = (nextLevelXp - currentLevelBaseXp).coerceAtLeast(1)
  val xpProgress = (xpInLevel.toFloat() / xpNeeded.toFloat()).coerceIn(0f, 1f)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "My Glow Profile 🌸",
          style = MaterialTheme.typography.displayMedium,
          fontWeight = FontWeight.Bold
        )

        IconButton(
          onClick = onOpenEditProfile,
          modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
        ) {
          Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = MaterialTheme.colorScheme.primary)
        }
      }
    }

    // Profile Card
    item {
      GlowCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(Color(0xFFFFF0F5), Color(0xFFFCE4EC)),
        borderColor = Color(0xFFFFB4C8),
        shape = RoundedCornerShape(26.dp)
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White,
              shadowElevation = 3.dp,
              modifier = Modifier.size(68.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(text = userProfile.avatarEmoji, fontSize = 36.sp)
              }
            }

            Column(
              verticalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier.weight(1f)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(
                  text = userProfile.name,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF2C1929)
                )
                Surface(
                  shape = CircleShape,
                  color = MaterialTheme.colorScheme.primary
                ) {
                  Text(
                    text = "Lvl ${userProfile.level}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
              }

              Text(
                text = userProfile.bio,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF5D4037)
              )

              Text(
                text = "🎯 Goal: ${userProfile.fitnessGoal}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Level XP Progress
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "✨ Level ${userProfile.level} Glow Princess",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF880E4F)
              )
              Text(
                text = "${userProfile.totalXp} XP",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF880E4F)
              )
            }
            LinearProgressIndicator(
              progress = { xpProgress },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
              color = MaterialTheme.colorScheme.primary,
              trackColor = Color.White.copy(alpha = 0.6f)
            )
          }
        }
      }
    }

    // Streaks & Highlights Grid
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "🔥 ${userProfile.currentStreak} Days", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "Current Streak", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "🏆 ${userProfile.longestStreak} Days", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "Best Streak", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    // Badges & Achievements Collection
    item {
      Text(
        text = "Badges & Trophies 🏆",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
    }

    item {
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        badges.forEach { badge ->
          val isUnlocked = badge.isUnlocked
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (isUnlocked) Color(0xFFFFF0F5) else MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isUnlocked) Color(0xFFFF8FA3) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            ),
            modifier = Modifier
              .width(160.dp)
              .clip(RoundedCornerShape(18.dp))
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(if (isUnlocked) Color.White else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = badge.iconEmoji,
                  fontSize = 24.sp,
                  color = if (isUnlocked) Color.Unspecified else Color.Gray.copy(alpha = 0.5f)
                )
              }

              Text(
                text = badge.title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )

              Text(
                text = badge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2
              )

              if (isUnlocked) {
                Surface(shape = CircleShape, color = Color(0xFFE91E63)) {
                  Text(
                    text = "Unlocked ✨",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 9.sp
                  )
                }
              } else {
                Text(
                  text = "${badge.currentProgress}/${badge.maxProgress}",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 10.sp
                )
              }
            }
          }
        }
      }
    }

    // App Preferences & Settings
    item {
      Text(
        text = "Preferences & Settings ⚙️",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Dark Mode Toggle
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = if (userProfile.darkModeEnabled) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
              Column {
                Text(text = "Dark Mode (Velvet Noir)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Soft nighttime pastel aesthetic", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Switch(
              checked = userProfile.darkModeEnabled,
              onCheckedChange = onToggleDarkMode,
              colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
              )
            )
          }

          // Sound Effects
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
              Column {
                Text(text = "Timer Audio Cues", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Beeps and guidance chime sounds", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Switch(
              checked = userProfile.soundEnabled,
              onCheckedChange = onToggleSound,
              colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
              )
            )
          }

          // Offline Status Indicator
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Default.OfflinePin,
                contentDescription = null,
                tint = Color(0xFF4CAF50)
              )
              Column {
                Text(text = "Offline Mode Enabled", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "All routines, timer & logs work without internet", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
            Surface(shape = CircleShape, color = Color(0xFFE8F5E9)) {
              Text(
                text = "Active",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
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

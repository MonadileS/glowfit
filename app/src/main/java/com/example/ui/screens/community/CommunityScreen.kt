package com.example.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.data.model.SocialPost
import com.example.data.model.UserProfile
import com.example.ui.components.GlowCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CommunityScreen(
  userProfile: UserProfile,
  posts: List<SocialPost>,
  onLikePost: (String) -> Unit,
  onCheerPost: (String) -> Unit,
  onAddComment: (String, String) -> Unit,
  onOpenCreatePostDialog: () -> Unit,
  modifier: Modifier = Modifier
) {
  val commentInputs = remember { mutableStateMapOf<String, String>() }
  val expandedComments = remember { mutableStateMapOf<String, Boolean>() }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Glow Squad 💕",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Cheer, connect & stay motivated together",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Create Post button
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onOpenCreatePostDialog)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = "Post", tint = Color.White, modifier = Modifier.size(16.dp))
            Text(
              text = "Share",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // Community Challenge Banner
    item {
      GlowCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(Color(0xFFFF8FA3), Color(0xFFFF4081)),
        borderColor = Color(0xFFFFB1C5),
        shape = RoundedCornerShape(24.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.9f)
            ) {
              Text(
                text = "🔥 Community Challenge",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFC2185B),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Text(
              text = "21-Day Summer Glow Up 🌸",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "14,820 queens currently participating! Complete today's session to stay on the leaderboard.",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.95f)
            )
          }

          Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.25f),
            modifier = Modifier.size(48.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "👑", fontSize = 24.sp)
            }
          }
        }
      }
    }

    // Quick Share Thought Box
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .clickable(onClick = onOpenCreatePostDialog)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = userProfile.avatarEmoji, fontSize = 20.sp)
            }
          }
          Text(
            text = "Share today's sweat win or cheer someone on... 🌸",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Feed Posts List
    items(posts) { post ->
      val isCommentsOpen = expandedComments[post.id] == true
      val currentCommentInput = commentInputs[post.id] ?: ""

      Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Author Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(42.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(text = post.authorAvatarEmoji, fontSize = 22.sp)
                }
              }

              Column {
                Text(
                  text = post.authorName,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = post.authorBadge,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
              Text(
                text = post.tag,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          // Workout snippet if attached
          if (post.workoutTitle != null) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
              border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(text = "🔥", fontSize = 18.sp)
                Column {
                  Text(
                    text = post.workoutTitle,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Completed ${post.workoutMinutes ?: 15} mins • ${post.streakDays ?: 1} Day Streak 🌸",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
          }

          // Content
          Text(
            text = post.content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
          )

          // Action Buttons: Like, Cheer, Comments
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              // Like Button
              Surface(
                shape = CircleShape,
                color = if (post.isLikedByMe) Color(0xFFFFECEF) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .clip(CircleShape)
                  .clickable { onLikePost(post.id) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (post.isLikedByMe) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = "${post.heartsCount}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (post.isLikedByMe) Color(0xFFE91E63) else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              // Cheer Button
              Surface(
                shape = CircleShape,
                color = if (post.isCheeredByMe) Color(0xFFFFF8E1) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .clip(CircleShape)
                  .clickable { onCheerPost(post.id) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Text(text = "👑", fontSize = 14.sp)
                  Text(
                    text = "Cheer (${post.cheersCount})",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (post.isCheeredByMe) Color(0xFFF57F17) else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }

            // Comments Toggle
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier
                .clip(CircleShape)
                .clickable { expandedComments[post.id] = !isCommentsOpen }
                .padding(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = "Comments",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "${post.commentsJson.size} comments",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          // Expandable Comments Section
          if (isCommentsOpen) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              post.commentsJson.forEach { comment ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = comment,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(10.dp)
                  )
                }
              }

              // Comment Input
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                OutlinedTextField(
                  value = currentCommentInput,
                  onValueChange = { commentInputs[post.id] = it },
                  placeholder = { Text("Write a cheer comment... ✨", fontSize = 12.sp) },
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(16.dp),
                  singleLine = true
                )

                IconButton(
                  onClick = {
                    if (currentCommentInput.isNotBlank()) {
                      onAddComment(post.id, currentCommentInput)
                      commentInputs[post.id] = ""
                    }
                  }
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.primary
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

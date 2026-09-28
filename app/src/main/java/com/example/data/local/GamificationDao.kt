package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Badge
import com.example.data.model.SocialPost
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
  // Badges
  @Query("SELECT * FROM badges ORDER BY isUnlocked DESC, rewardXp ASC")
  fun getAllBadges(): Flow<List<Badge>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBadges(badges: List<Badge>)

  @Update
  suspend fun updateBadge(badge: Badge)

  // Profile
  @Query("SELECT * FROM user_profile WHERE id = 1")
  fun getUserProfile(): Flow<UserProfile?>

  @Query("SELECT * FROM user_profile WHERE id = 1")
  suspend fun getUserProfileSync(): UserProfile?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateProfile(profile: UserProfile)

  // Social
  @Query("SELECT * FROM social_posts ORDER BY timestamp DESC")
  fun getAllSocialPosts(): Flow<List<SocialPost>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSocialPosts(posts: List<SocialPost>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSocialPost(post: SocialPost)

  @Update
  suspend fun updateSocialPost(post: SocialPost)

  @Query("DELETE FROM social_posts WHERE id = :id")
  suspend fun deleteSocialPost(id: String)
}

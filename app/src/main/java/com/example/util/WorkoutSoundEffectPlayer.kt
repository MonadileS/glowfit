package com.example.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Built-in zero-latency Audio Engine for Workout Timing & Sound Effects.
 * Uses Android's system ToneGenerator with dedicated audio streams so it works
 * 100% offline, immediately, with zero assets downloading or network delays.
 */
object WorkoutSoundEffectPlayer {
  private var toneGenerator: ToneGenerator? = null
  var isSoundEnabled: Boolean = true

  init {
    try {
      toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
    } catch (e: Exception) {
      Log.w("SoundPlayer", "ToneGenerator init exception: ${e.message}")
    }
  }

  private fun ensureToneGenerator(): ToneGenerator? {
    if (toneGenerator == null) {
      try {
        toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
      } catch (e: Exception) {
        Log.w("SoundPlayer", "ToneGenerator re-init exception: ${e.message}")
      }
    }
    return toneGenerator
  }

  /**
   * Countdown warning tick for 3, 2, 1 seconds remaining.
   */
  fun playCountdownBeep(secondsLeft: Int) {
    if (!isSoundEnabled) return
    try {
      val toneGen = ensureToneGenerator() ?: return
      when (secondsLeft) {
        3, 2 -> toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        1 -> toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 220)
      }
    } catch (e: Exception) {
      Log.w("SoundPlayer", "Error playing countdown beep: ${e.message}")
    }
  }

  /**
   * Start or Resume whistle/chime when exercise kicks off.
   */
  fun playStartWhistle() {
    if (!isSoundEnabled) return
    try {
      val toneGen = ensureToneGenerator() ?: return
      toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 280)
    } catch (e: Exception) {
      Log.w("SoundPlayer", "Error playing start chime: ${e.message}")
    }
  }

  /**
   * Rest interval calming bell.
   */
  fun playRestChime() {
    if (!isSoundEnabled) return
    try {
      val toneGen = ensureToneGenerator() ?: return
      toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
    } catch (e: Exception) {
      Log.w("SoundPlayer", "Error playing rest chime: ${e.message}")
    }
  }

  /**
   * Halfway encouragement ping.
   */
  fun playHalfwayPing() {
    if (!isSoundEnabled) return
    try {
      val toneGen = ensureToneGenerator() ?: return
      toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 150)
    } catch (e: Exception) {
      Log.w("SoundPlayer", "Error playing halfway ping: ${e.message}")
    }
  }

  /**
   * Celebratory fanfare when workout completes.
   */
  fun playCompletionFanfare(scope: CoroutineScope) {
    if (!isSoundEnabled) return
    scope.launch(Dispatchers.Default) {
      try {
        val toneGen = ensureToneGenerator() ?: return@launch
        toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
        kotlinx.coroutines.delay(220)
        toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 350)
        kotlinx.coroutines.delay(380)
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 500)
      } catch (e: Exception) {
        Log.w("SoundPlayer", "Error playing complete fanfare: ${e.message}")
      }
    }
  }
}

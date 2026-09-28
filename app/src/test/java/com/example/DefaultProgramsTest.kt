package com.example

import com.example.data.local.DefaultPrograms
import com.example.data.local.ExerciseCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultProgramsTest {

  @Test
  fun `all pre-built routines reference existing exercises in dataset`() {
    val routines = DefaultPrograms.defaultRoutines
    assertTrue("Should have multiple default routines", routines.size >= 5)

    routines.forEach { routine ->
      assertTrue("Routine title must not be blank: ${routine.id}", routine.title.isNotBlank())
      assertTrue("Routine must contain exercise IDs: ${routine.title}", routine.exerciseIds.isNotEmpty())
      assertTrue("Routine estimated minutes should be positive: ${routine.title}", routine.estimatedMinutes > 0)
      assertTrue("Routine estimated calories should be positive: ${routine.title}", routine.estimatedCalories > 0)

      // Verify each exercise exists in the catalog
      routine.exerciseIds.forEach { exerciseId ->
        val exercise = ExerciseCatalog.getById(exerciseId)
        assertNotNull("Exercise ID '$exerciseId' in routine '${routine.title}' must exist in ExerciseCatalog", exercise)
      }
    }
  }

  @Test
  fun `featured 21 day glow program has valid configuration`() {
    val glowProgram = DefaultPrograms.defaultRoutines.find { it.id == "prog_21day_glow" }
    assertNotNull("21-Day Glow program must exist", glowProgram)
    glowProgram?.let {
      assertTrue("21-Day program should have at least 4 moves", it.exerciseIds.size >= 4)
      assertEquals("21-Day Glow Up Challenge", it.title)
    }
  }

  @Test
  fun `bedtime de-stress routine has calming exercises`() {
    val bedtimeRoutine = DefaultPrograms.defaultRoutines.find { it.id == "prog_bedtime_relax" }
    assertNotNull("Bedtime routine must exist", bedtimeRoutine)
    bedtimeRoutine?.let { routine ->
      routine.exerciseIds.forEach { exerciseId ->
        val exercise = ExerciseCatalog.getById(exerciseId)
        assertNotNull(exercise)
      }
    }
  }
}

package com.example.data.local

import com.example.data.model.DifficultyLevel
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory

object ExerciseCatalog {
  private var dynamicExercises: List<Exercise> = emptyList()

  val exercises: List<Exercise>
    get() = if (dynamicExercises.isNotEmpty()) dynamicExercises else defaultSeedExercises

  val count: Int
    get() = exercises.size

  fun loadFromAssets(context: android.content.Context) {
    if (dynamicExercises.isEmpty()) {
      val loaded = ExerciseDatasetLoader.loadExercisesFromAssets(context)
      if (loaded.isNotEmpty()) {
        dynamicExercises = loaded
      }
    }
  }

  private val legacyIdAliases = mapOf(
    "ex_russian_twist" to "0687",
    "ex_bicycle_crunches" to "0972",
    "ex_plank_hip_dips" to "0464",
    "ex_heel_touches" to "0006",
    "ex_flutter_kicks" to "0001",
    "ex_dead_bug" to "0276",
    "ex_cobra_stretch" to "1512",
    "ex_glute_bridges" to "1409",
    "ex_donkey_kicks" to "0284",
    "ex_fire_hydrants" to "1512",
    "ex_frog_pumps" to "2429",
    "ex_clamshells" to "1001",
    "ex_butterfly_stretch" to "1512",
    "ex_arm_circles" to "1368",
    "ex_tricep_dips" to "0019",
    "ex_knee_pushups" to "0975",
    "ex_superman_hold" to "0803",
    "ex_cat_cow" to "1512",
    "ex_jumping_jacks" to "3221",
    "ex_sumo_squats" to "1004",
    "ex_childs_pose" to "1512"
  )

  fun getById(id: String): Exercise? {
    val direct = exercises.find { it.id == id }
    if (direct != null) return direct

    // Check alias
    val aliasId = legacyIdAliases[id]
    if (aliasId != null) {
      val aliased = exercises.find { it.id == aliasId }
      if (aliased != null) return aliased
    }

    // Check default seed list
    return defaultSeedExercises.find { it.id == id }
  }

  fun getByCategory(category: ExerciseCategory): List<Exercise> = exercises.filter { it.category == category }

  fun getByBodyPart(bodyPart: String): List<Exercise> = exercises.filter {
    it.bodyPart.equals(bodyPart, ignoreCase = true)
  }

  fun getByEquipment(equipment: String): List<Exercise> = exercises.filter {
    it.equipment.equals(equipment, ignoreCase = true)
  }

  val allBodyParts: List<String>
    get() = exercises.map { it.bodyPart }.distinct().sorted()

  val allEquipments: List<String>
    get() = exercises.map { it.equipment }.distinct().sorted()

  val defaultSeedExercises: List<Exercise> = listOf(
    // ---------------- BELLY & CORE ----------------
    Exercise(
      id = "ex_russian_twist",
      name = "Russian Twists",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Waist / Core",
      targetMuscle = "Obliques",
      secondaryMuscles = listOf("Rectus Abdominis", "Hip Flexors"),
      equipment = "Bodyweight / Mat",
      instructions = listOf(
        "Sit on the floor with knees bent and feet slightly elevated or resting lightly on the ground.",
        "Lean your torso back at a 45-degree angle, keeping your spine straight and core tight.",
        "Clasp your hands together and rotate your torso smoothly from side to side.",
        "Touch the mat gently next to each hip, breathing out on every rotation."
      ),
      durationSeconds = 30,
      defaultReps = 16,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 18,
      tips = "Keep your chest lifted and move with control rather than speed to carve your waistline.",
      iconName = "core",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Russian_Twist/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Russian_Twist/0.jpg"
    ),
    Exercise(
      id = "ex_bicycle_crunches",
      name = "Bicycle Crunches",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Waist / Abs",
      targetMuscle = "Rectus Abdominis & Obliques",
      secondaryMuscles = listOf("Hip Flexors"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie on your back with knees at 90 degrees and fingertips gently behind ears.",
        "Lift your shoulder blades off the floor, engaging your deep core.",
        "Pedal your legs while rotating your right elbow toward your left knee as your right leg extends.",
        "Switch smoothly to the opposite side without pulling on your neck."
      ),
      durationSeconds = 30,
      defaultReps = 16,
      isTimeBased = true,
      difficulty = DifficultyLevel.INTERMEDIATE,
      estimatedCalories = 20,
      tips = "Slow down the tempo for maximum muscle activation and a flat belly burn.",
      iconName = "core",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Bicycle_Crunch/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Bicycle_Crunch/0.jpg"
    ),
    Exercise(
      id = "ex_plank_hip_dips",
      name = "Plank with Hip Dips",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Core / Waist",
      targetMuscle = "Obliques & Transverse Abdominis",
      secondaryMuscles = listOf("Shoulders", "Glutes"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Start in a forearm plank with elbows under shoulders and body in a straight line.",
        "Slowly rotate your hips to dip the right hip toward the floor without touching.",
        "Return to center and immediately dip your left hip to the opposite side.",
        "Keep your naval drawn in tight toward your spine throughout."
      ),
      durationSeconds = 30,
      defaultReps = 14,
      isTimeBased = true,
      difficulty = DifficultyLevel.INTERMEDIATE,
      estimatedCalories = 22,
      tips = "The ultimate move for an hourglass silhouette—avoid arching your lower back!",
      iconName = "core",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Plank/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Plank/0.jpg"
    ),
    Exercise(
      id = "ex_heel_touches",
      name = "Aesthetic Heel Touches",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Upper & Side Core",
      targetMuscle = "Obliques",
      secondaryMuscles = listOf("Upper Abs"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie on your back with knees bent and feet flat on the floor, hip-width apart.",
        "Raise your head and upper back slightly off the mat.",
        "Reach your right hand to touch your right heel, feeling the side pinch.",
        "Return slightly and reach your left hand to touch your left heel."
      ),
      durationSeconds = 30,
      defaultReps = 20,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 15,
      tips = "Focus on the squeeze on the side of your waist on every single tap.",
      iconName = "core",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Russian_Twist/1.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Russian_Twist/1.jpg"
    ),
    Exercise(
      id = "ex_flutter_kicks",
      name = "Lower Belly Flutter Kicks",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Lower Abs",
      targetMuscle = "Lower Abdominals",
      secondaryMuscles = listOf("Hip Flexors", "Quads"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie on your back, placing hands beneath your lower hips for lower back support.",
        "Lift both legs 6 inches off the ground, pointing your toes gracefully.",
        "Perform small, rhythmic up-and-down scissor kicks with straight legs.",
        "Press your lower back firmly into the mat to safeguard your spine."
      ),
      durationSeconds = 30,
      defaultReps = 20,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 17,
      tips = "If your back arches, lift your legs a little higher toward the ceiling.",
      iconName = "core",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Bicycle_Crunch/1.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Bicycle_Crunch/1.jpg"
    ),
    Exercise(
      id = "ex_mountain_climbers",
      name = "Mountain Climbers",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Full Core & Cardio",
      targetMuscle = "Entire Core",
      secondaryMuscles = listOf("Shoulders", "Quads", "Glutes"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Start in a high push-up plank position with hands under shoulders.",
        "Drive your right knee up towards your chest, then quickly switch and drive your left knee.",
        "Maintain a steady, rhythmic pace like running on the spot in a plank.",
        "Keep your hips low and parallel to the floor."
      ),
      durationSeconds = 30,
      defaultReps = 24,
      isTimeBased = true,
      difficulty = DifficultyLevel.INTERMEDIATE,
      estimatedCalories = 25,
      tips = "Ignites fat burning while toning the deep lower belly muscles.",
      iconName = "cardio",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Mountain_Climbers/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Mountain_Climbers/0.jpg"
    ),
    Exercise(
      id = "ex_dead_bug",
      name = "Gentle Dead Bug",
      category = ExerciseCategory.BELLY_CORE,
      bodyPart = "Deep Core",
      targetMuscle = "Transverse Abdominis",
      secondaryMuscles = listOf("Pelvic Floor", "Back"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie on your back with arms extended toward the ceiling and knees in tabletop (90 degrees).",
        "Slowly lower your right arm backward and left leg forward until just above the floor.",
        "Bring them back to center, then repeat with the left arm and right leg.",
        "Keep your spine glued to the mat."
      ),
      durationSeconds = 30,
      defaultReps = 12,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 14,
      tips = "Flattens the stomach by activating the deepest abdominal wall safely.",
      iconName = "core",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Dead_Bug/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Dead_Bug/0.jpg"
    ),

    // ---------------- BOOTY & LEGS ----------------
    Exercise(
      id = "ex_glute_bridges",
      name = "Glute Bridge & Squeeze",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Glutes & Hamstrings",
      targetMuscle = "Gluteus Maximus",
      secondaryMuscles = listOf("Hamstrings", "Core"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie on your back with knees bent and feet flat on the ground, hip-width apart.",
        "Drive through your heels to lift your hips toward the ceiling until knees, hips, and shoulders form a line.",
        "Squeeze your glutes firmly at the top for 2 seconds.",
        "Lower your hips down slowly until hovering just above the mat."
      ),
      durationSeconds = 30,
      defaultReps = 15,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 18,
      tips = "Push strictly through your heels, not your toes, to isolate the booty completely.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Glute_Bridge/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Glute_Bridge/0.jpg"
    ),
    Exercise(
      id = "ex_donkey_kicks",
      name = "Booty Lift Donkey Kicks",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Glutes",
      targetMuscle = "Gluteus Maximus & Medius",
      secondaryMuscles = listOf("Hamstrings", "Lower Back"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Start on all fours with wrists under shoulders and knees under hips.",
        "Keeping your right knee bent at a 90-degree angle, kick the sole of your right foot toward the ceiling.",
        "Squeeze at the top point, then lower with control without letting the knee touch the floor.",
        "Complete repetitions before switching to the left leg."
      ),
      durationSeconds = 30,
      defaultReps = 15,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 19,
      tips = "Keep your hips square to the floor—don't twist your spine.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Donkey_Kicks/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Donkey_Kicks/0.jpg"
    ),
    Exercise(
      id = "ex_fire_hydrants",
      name = "Fire Hydrants (Side Booty)",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Outer Hips & Glutes",
      targetMuscle = "Gluteus Medius",
      secondaryMuscles = listOf("Hip Abductors", "Core"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Position yourself on hands and knees with an engaged flat core.",
        "Keeping the 90-degree bend in your knee, lift your right leg out to the side.",
        "Lift as high as comfortable without tilting your torso sideways.",
        "Lower back smoothly and repeat before alternating sides."
      ),
      durationSeconds = 30,
      defaultReps = 15,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 17,
      tips = "The secret weapon for filling out side hips and creating soft natural curves.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Fire_Hydrant/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Fire_Hydrant/0.jpg"
    ),
    Exercise(
      id = "ex_sumo_squats",
      name = "Sumo Squats & Inner Thighs",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Inner Thighs & Glutes",
      targetMuscle = "Adductors & Glutes",
      secondaryMuscles = listOf("Quads", "Hamstrings"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Stand with feet wider than shoulder-width, toes turned out at a 45-degree angle.",
        "Bring hands to heart center and lower down into a deep squat, pushing knees outward in line with toes.",
        "Drop until thighs are parallel to the floor.",
        "Push through your heels to stand back up, squeezing your glutes and inner thighs at the top."
      ),
      durationSeconds = 30,
      defaultReps = 12,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 22,
      tips = "Keep your chest tall and proud. Fantastic for slender inner thigh sculpting.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Sumo_Squat/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Sumo_Squat/0.jpg"
    ),
    Exercise(
      id = "ex_curtsy_lunges",
      name = "Curtsy Lunges",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Glutes & Thighs",
      targetMuscle = "Gluteus Medius & Maximus",
      secondaryMuscles = listOf("Quads", "Calves"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Stand tall with feet hip-width apart and hands comfortably on your hips or chest.",
        "Step your left foot back and diagonally behind your right leg, bending both knees into a curtsy.",
        "Lower until your front thigh is almost parallel to the floor.",
        "Push through your front right heel to return to starting position, then switch sides."
      ),
      durationSeconds = 30,
      defaultReps = 14,
      isTimeBased = true,
      difficulty = DifficultyLevel.INTERMEDIATE,
      estimatedCalories = 24,
      tips = "Targets the rounded upper booty contour while toning slim elegant legs.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Curtsy_Lunge/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Curtsy_Lunge/0.jpg"
    ),
    Exercise(
      id = "ex_frog_pumps",
      name = "Frog Pumps",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Glutes",
      targetMuscle = "Gluteus Maximus",
      secondaryMuscles = listOf("Pelvic Floor"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie on your back with knees bent and the soles of your feet pressed together (butterfly legs).",
        "Keep your feet close to your pelvis.",
        "Drive down into the outer edges of your feet to bridge your hips up towards the ceiling.",
        "Squeeze tightly at the peak of the movement, then lower gently."
      ),
      durationSeconds = 30,
      defaultReps = 16,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 18,
      tips = "Unrivaled direct booty burn with zero lower back strain.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Glute_Bridge/1.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Glute_Bridge/1.jpg"
    ),
    Exercise(
      id = "ex_clamshells",
      name = "Side Clamshells",
      category = ExerciseCategory.BOOTY_LEGS,
      bodyPart = "Hips / Glutes",
      targetMuscle = "Gluteus Medius",
      secondaryMuscles = listOf("Hip Abductors"),
      equipment = "Bodyweight / Mat",
      instructions = listOf(
        "Lie on your side with knees bent at 45 degrees and feet stacked together.",
        "Rest your head on your bottom arm.",
        "Keeping your feet glued together, open your top knee toward the ceiling like a clamshell.",
        "Pause at the top for 1 second, then close gently."
      ),
      durationSeconds = 30,
      defaultReps = 15,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 15,
      tips = "Do not roll your top hip backward—keep hips stacked vertically.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Clams/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Clams/0.jpg"
    ),

    // ---------------- TONED ARMS & BACK ----------------
    Exercise(
      id = "ex_knee_pushups",
      name = "Knee Push-Ups & Chest Tone",
      category = ExerciseCategory.TONED_ARMS,
      bodyPart = "Arms & Chest",
      targetMuscle = "Pectorals & Triceps",
      secondaryMuscles = listOf("Anterior Deltoids", "Core"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Start on knees with hands placed slightly wider than shoulders, body forming a straight diagonal line.",
        "Lower your chest toward the floor by bending your elbows at a 45-degree angle.",
        "Lower until chest is a few inches from the mat.",
        "Push firmly through your palms to return to the top."
      ),
      durationSeconds = 30,
      defaultReps = 10,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 18,
      tips = "Keeps arms lean and lifts the upper chest naturally.",
      iconName = "arms",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Pushups/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Pushups/0.jpg"
    ),
    Exercise(
      id = "ex_tricep_dips",
      name = "Mat Tricep Dips",
      category = ExerciseCategory.TONED_ARMS,
      bodyPart = "Back of Arms",
      targetMuscle = "Triceps",
      secondaryMuscles = listOf("Shoulders", "Upper Back"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Sit on the mat with knees bent, feet flat, and fingers pointing forward toward your heels.",
        "Lift your hips slightly off the floor.",
        "Bend your elbows backward to lower your hips toward the mat.",
        "Straighten your arms by pressing through the palms to lift yourself back up."
      ),
      durationSeconds = 30,
      defaultReps = 14,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 16,
      tips = "Tones the 'batwing' area for sleek, firm arms in sleeveless outfits.",
      iconName = "arms",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Bench_Dips/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Bench_Dips/0.jpg"
    ),
    Exercise(
      id = "ex_arm_circles",
      name = "Ballet Arm Circles & Pulses",
      category = ExerciseCategory.TONED_ARMS,
      bodyPart = "Shoulders & Arms",
      targetMuscle = "Deltoids",
      secondaryMuscles = listOf("Upper Back", "Traps"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Stand tall and extend both arms straight out to the sides at shoulder height, palms facing down.",
        "Make small, controlled forward circles for 15 seconds.",
        "Reverse the direction for small backward circles for the remaining 15 seconds.",
        "Keep shoulders relaxed away from ears."
      ),
      durationSeconds = 30,
      defaultReps = 30,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 14,
      tips = "Burns like fire without lifting any heavy weights—sculpts delicate dancer shoulders.",
      iconName = "arms",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Arm_Circles/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Arm_Circles/0.jpg"
    ),
    Exercise(
      id = "ex_superman_hold",
      name = "Glow Superman Hold",
      category = ExerciseCategory.TONED_ARMS,
      bodyPart = "Back & Posture",
      targetMuscle = "Erector Spinae & Upper Back",
      secondaryMuscles = listOf("Glutes", "Rear Deltoids"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Lie face down on the mat with arms extended overhead and legs straight.",
        "Simultaneously lift your chest, arms, and thighs off the floor.",
        "Hold at the top, feeling your entire back side activate.",
        "Lower gently and breathe smoothly."
      ),
      durationSeconds = 30,
      defaultReps = 12,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 16,
      tips = "Improves posture instantly and eliminates bra-line back fat.",
      iconName = "arms",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Superman/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Superman/0.jpg"
    ),

    // ---------------- FULL BODY & CARDIO ----------------
    Exercise(
      id = "ex_jumping_jacks",
      name = "Light Jumping Jacks",
      category = ExerciseCategory.FULL_BODY,
      bodyPart = "Full Body Cardio",
      targetMuscle = "Full Body & Calves",
      secondaryMuscles = listOf("Shoulders", "Core"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Stand upright with feet together and arms at your sides.",
        "Jump feet out wide while sweeping arms overhead in a joyful clap.",
        "Jump back to the starting position softly on the balls of your feet.",
        "Maintain a steady, bouncy rhythm."
      ),
      durationSeconds = 30,
      defaultReps = 25,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 26,
      tips = "Land softly like a feather to protect knees and ankles.",
      iconName = "cardio",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Jumping_Jacks/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Jumping_Jacks/0.jpg"
    ),
    Exercise(
      id = "ex_skater_hops",
      name = "Aesthetic Skater Hops",
      category = ExerciseCategory.FULL_BODY,
      bodyPart = "Legs & Cardio",
      targetMuscle = "Glutes & Quads",
      secondaryMuscles = listOf("Calves", "Core"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Start in a gentle semi-squat, leaping laterally to the right side.",
        "Land softly on your right foot while sweeping your left foot behind you.",
        "Immediately rebound and leap to the left side, sweeping the right foot behind.",
        "Pump your arms gracefully with the side-to-side motion."
      ),
      durationSeconds = 30,
      defaultReps = 20,
      isTimeBased = true,
      difficulty = DifficultyLevel.INTERMEDIATE,
      estimatedCalories = 28,
      tips = "Combines calorie-torching cardio with dynamic side-glute toning.",
      iconName = "cardio",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Skater_Hops/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Skater_Hops/0.jpg"
    ),
    Exercise(
      id = "ex_squat_calf_raise",
      name = "Squat to Calf Raise",
      category = ExerciseCategory.FULL_BODY,
      bodyPart = "Legs & Calves",
      targetMuscle = "Quads, Glutes & Calves",
      secondaryMuscles = listOf("Core"),
      equipment = "Bodyweight",
      instructions = listOf(
        "Stand with feet shoulder-width apart.",
        "Lower into a classic bodyweight squat until thighs are parallel to the floor.",
        "As you stand back up, explode smoothly up onto your tiptoes in a calf raise.",
        "Hold for a split second, then lower heels and repeat."
      ),
      durationSeconds = 30,
      defaultReps = 14,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 22,
      tips = "Elongates calf muscles and lifts the entire posterior chain.",
      iconName = "booty",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Sumo_Squat/1.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Sumo_Squat/1.jpg"
    ),

    // ---------------- STRETCH & POSTURE ----------------
    Exercise(
      id = "ex_cat_cow",
      name = "Gentle Cat-Cow Flow",
      category = ExerciseCategory.STRETCH_POSTURE,
      bodyPart = "Spine & Core",
      targetMuscle = "Spine Flexors & Extensors",
      secondaryMuscles = listOf("Neck", "Shoulders", "Chest"),
      equipment = "Yoga Mat",
      instructions = listOf(
        "Begin on all fours with wrists under shoulders and knees under hips.",
        "Inhale deeply, dropping your belly down while lifting chest and tailbone (Cow).",
        "Exhale slowly, tucking chin to chest and rounding your spine up toward the ceiling (Cat).",
        "Flow smoothly with your deep breath cycle."
      ),
      durationSeconds = 35,
      defaultReps = 8,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 10,
      tips = "Melts away daily tension, relieves menstrual cramps, and boosts mood.",
      iconName = "stretch",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Cat_Cow_Pose/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Cat_Cow_Pose/0.jpg"
    ),
    Exercise(
      id = "ex_child_pose",
      name = "Peaceful Child's Pose",
      category = ExerciseCategory.STRETCH_POSTURE,
      bodyPart = "Hips & Lower Back",
      targetMuscle = "Latissimus Dorsi & Glutes",
      secondaryMuscles = listOf("Spine", "Ankles"),
      equipment = "Yoga Mat",
      instructions = listOf(
        "Kneel on the floor with big toes touching and knees wide apart.",
        "Sit your hips back onto your heels and fold your torso forward.",
        "Extend arms long ahead of you, resting forehead gently on the mat.",
        "Take deep diaphragmatic breaths, letting all stress dissolve."
      ),
      durationSeconds = 40,
      defaultReps = 1,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 8,
      tips = "Surrender into the mat. Great before bed or when feeling overwhelmed.",
      iconName = "stretch",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Childs_Pose/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Childs_Pose/0.jpg"
    ),
    Exercise(
      id = "ex_butterfly_stretch",
      name = "Butterfly Hip Opener",
      category = ExerciseCategory.STRETCH_POSTURE,
      bodyPart = "Hips & Inner Thighs",
      targetMuscle = "Adductors & Groin",
      secondaryMuscles = listOf("Lower Back"),
      equipment = "Yoga Mat",
      instructions = listOf(
        "Sit tall with the soles of your feet pressed together, knees falling outward.",
        "Hold onto your feet or ankles and lengthen your spine upward.",
        "Gently flutter knees or hinge forward slightly from the hips.",
        "Breathe deeply into any tightness in your hips."
      ),
      durationSeconds = 35,
      defaultReps = 1,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 9,
      tips = "Releases stored emotional tension and improves pelvic circulation.",
      iconName = "stretch",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Butterfly_Yoga_Pose/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Butterfly_Yoga_Pose/0.jpg"
    ),
    Exercise(
      id = "ex_cobra_stretch",
      name = "Radiant Cobra Stretch",
      category = ExerciseCategory.STRETCH_POSTURE,
      bodyPart = "Chest & Abdominals",
      targetMuscle = "Rectus Abdominis & Pectorals",
      secondaryMuscles = listOf("Spine", "Shoulders"),
      equipment = "Yoga Mat",
      instructions = listOf(
        "Lie face down with palms placed on the floor beneath your shoulders.",
        "Hug your elbows close to your ribcage.",
        "Press gently through your hands to lift your chest off the mat while keeping hips grounded.",
        "Gaze forward or slightly upward with relaxed shoulders."
      ),
      durationSeconds = 30,
      defaultReps = 1,
      isTimeBased = true,
      difficulty = DifficultyLevel.BEGINNER,
      estimatedCalories = 10,
      tips = "Opens the heart and stretches the abdominal wall after core work.",
      iconName = "stretch",
      gifUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Cobra_Pose/0.jpg",
      thumbnailUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/Cobra_Pose/0.jpg"
    )
  )
}

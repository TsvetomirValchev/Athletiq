package com.valchev.athletiq.templates;

import com.valchev.athletiq.domain.entity.ExerciseTemplate;
import com.valchev.athletiq.repository.ExerciseTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExerciseTemplateLoader implements ApplicationRunner {

    private final ExerciseTemplateRepository exerciseTemplateRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (exerciseTemplateRepository.count() == 0) {
            List<ExerciseTemplate> templates = createExerciseTemplates();
            exerciseTemplateRepository.saveAll(templates);
            log.info("Database populated with {} exercise templates", templates.size());
        } else {
            log.info("Exercise templates already exist in the database");
        }
    }

    private List<ExerciseTemplate> createExerciseTemplates() {
        List<ExerciseTemplate> templates = new ArrayList<>();

        // Chest exercises
        templates.add(createTemplate(
                "Bench Press",
                "The bench press is a compound exercise that targets the chest, shoulders, and triceps. Lie on a flat bench, lower the barbell to your chest, and press it back up.",
                Arrays.asList("Chest", "Shoulders", "Triceps", "Arms"),
                "assets/exercise-icons/bench-press.png"
        ));

        templates.add(createTemplate(
                "Incline Bench Press",
                "A variation of bench press performed on an inclined bench to target the upper chest muscles.",
                Arrays.asList("Upper Chest", "Shoulders", "Triceps", "Chest", "Arms"),
                "assets/exercise-icons/incline-bench-press.png"
        ));

        templates.add(createTemplate(
                "Decline Barbell Bench Press",
                "A variation of bench press performed on a declined bench to target the lower chest muscles.",
                Arrays.asList("Lower Chest", "Shoulders", "Triceps", "Chest", "Arms"),
                "assets/exercise-icons/decline-barbell-bench-press.png"
        ));

        templates.add(createTemplate(
                "Dumbbell Bench Press",
                "The bench press is a compound exercise that targets the chest, shoulders, and triceps. Lie on a flat bench with a dumbbell in each hand, lower the weights to your chest, and press them back up.",
                Arrays.asList("Chest", "Shoulders", "Triceps", "Arms"),
                "assets/exercise-icons/dumbbell-bench-press.png"
        ));

        templates.add(createTemplate(
                "Dumbbell Fly",
                "An isolation exercise for the chest. Lie on a bench with dumbbells extended above your chest, then open your arms wide while keeping a slight bend in your elbows.",
                Arrays.asList("Chest", "Shoulders", "Arms"),
                "assets/exercise-icons/dumbbell-fly.png"
        ));

        templates.add(createTemplate(
                "Incline Dumbbell Bench Press",
                "A variation of the dumbbell bench press performed on an inclined bench to target the upper chest muscles with greater range of motion.",
                Arrays.asList("Upper Chest", "Shoulders", "Triceps", "Chest", "Arms"),
                "assets/exercise-icons/incline-dumbbell-bench-press.png"
        ));

        templates.add(createTemplate(
                "Decline Dumbbell Bench Press",
                "A variation of bench press performed on a declined bench to target the lower chest muscles.",
                Arrays.asList("Lower Chest", "Shoulders", "Triceps", "Chest", "Arms"),
                "assets/exercise-icons/decline-dumbbell-bench-press.png"
        ));


        templates.add(createTemplate(
                "Cable Fly",
                "An isolation exercise using cables to target the chest muscles. Stand between two cable machines and bring your hands together in a hugging motion.",
                Arrays.asList("Chest", "Shoulders", "Arms"),
                "assets/exercise-icons/cable-fly.png"
        ));

        templates.add(createTemplate(
                "Push-ups",
                "A bodyweight exercise that targets the chest, shoulders, and triceps. Start in a plank position and lower your body until your chest nearly touches the floor, then push back up.",
                Arrays.asList("Chest", "Shoulders", "Triceps", "Arms", "Core"),
                "assets/exercise-icons/push-ups.png"
        ));

        // Back exercises
        templates.add(createTemplate(
                "Deadlift",
                "A compound exercise that targets multiple muscle groups. With feet shoulder-width apart, bend at the hips and knees to grip the barbell, then stand up by extending your hips and knees.",
                Arrays.asList("Lower Back", "Hamstrings", "Glutes", "Traps", "Back", "Legs"),
                "assets/exercise-icons/deadlift.png"
        ));

        templates.add(createTemplate(
                "Pull-ups",
                "A bodyweight exercise that primarily targets the back. Hang from a bar with palms facing away, then pull yourself up until your chin is over the bar.",
                Arrays.asList("Lats", "Biceps", "Mid-Back", "Back", "Arms"),
                "assets/exercise-icons/pull-ups.png"
        ));

        templates.add(createTemplate(
                "Lat Pulldown",
                "A machine-based exercise that mimics pull-ups. Sit at a lat pulldown machine and pull the bar down to your upper chest while squeezing your shoulder blades together.",
                Arrays.asList("Lats", "Biceps", "Mid-Back", "Back", "Arms"),
                "assets/exercise-icons/lat-pulldown.png"
        ));

        templates.add(createTemplate(
                "Bent Over Row",
                "A compound exercise for the back. Bend at your hips with a slight bend in your knees, hold a barbell with an overhand grip, and pull it towards your lower abdomen.",
                Arrays.asList("Mid-Back", "Lats", "Biceps", "Rear Delts", "Back", "Arms"),
                "assets/exercise-icons/bent-over-row.png"
        ));

        templates.add(createTemplate(
                "T-Bar Row",
                "A variation of rowing exercise using a T-bar. Stand over the T-bar, bend at the hips, and pull the weight toward your chest while keeping your back straight.",
                Arrays.asList("Mid-Back", "Lats", "Biceps", "Rear Delts", "Back", "Arms"),
                "assets/exercise-icons/t-bar-row.png"
        ));

        templates.add(createTemplate(
                "Seated Cable Row",
                "A seated rowing exercise using a cable machine. Sit with your legs extended, pull the handle toward your abdomen while squeezing your shoulder blades together.",
                Arrays.asList("Mid-Back", "Lats", "Biceps", "Rear Delts", "Back", "Arms"),
                "assets/exercise-icons/seated-cable-row.png"
        ));

        // Legs exercises
        templates.add(createTemplate(
                "Squat",
                "A fundamental compound exercise for lower body strength. Place a barbell across your upper back, bend your knees and hips to lower your body, then return to standing.",
                Arrays.asList("Quadriceps", "Glutes", "Hamstrings", "Lower Back", "Legs"),
                "assets/exercise-icons/squat.png"
        ));

        templates.add(createTemplate(
                "Leg Press",
                "A machine-based exercise for the legs. Sit in the leg press machine and push the platform away by extending your knees and hips.",
                Arrays.asList("Quadriceps", "Glutes", "Hamstrings", "Legs"),
                "assets/exercise-icons/leg-press.png"
        ));

        templates.add(createTemplate(
                "Romanian Deadlift",
                "An exercise that targets the posterior chain. Hold a barbell in front of your thighs, hinge at your hips while keeping your back straight, lower the weight, then return to standing.",
                Arrays.asList("Hamstrings", "Glutes", "Lower Back", "Legs", "Back"),
                "assets/exercise-icons/romanian-deadlift.png"
        ));

        templates.add(createTemplate(
                "Lunges",
                "A unilateral leg exercise that improves balance and strength. Step forward into a lunge position, lower your back knee toward the ground, then return to standing.",
                Arrays.asList("Quadriceps", "Glutes", "Hamstrings", "Calves", "Legs", "Core"),
                "assets/exercise-icons/lunges.png"
        ));

        templates.add(createTemplate(
                "Leg Extensions",
                "An isolation exercise for the quadriceps. Sit on a leg extension machine and extend your legs by straightening your knees against the resistance.",
                Arrays.asList("Quadriceps", "Legs"),
                "assets/exercise-icons/leg-extensions.png"
        ));

        templates.add(createTemplate(
                "Leg Curls",
                "An isolation exercise for the hamstrings. Lie face down on a leg curl machine and curl your heels toward your glutes by bending your knees.",
                Arrays.asList("Hamstrings", "Legs"),
                "assets/exercise-icons/leg-curls.png"
        ));

        templates.add(createTemplate(
                "Calf Raises",
                "An isolation exercise for the calf muscles. Stand on the balls of your feet and raise your heels as high as possible, then lower them back down.",
                Arrays.asList("Calves", "Legs"),
                "assets/exercise-icons/calf-raises.png"
        ));

        // Shoulder exercises
        templates.add(createTemplate(
                "Overhead Press",
                "A compound exercise for the shoulders. Stand holding a barbell at shoulder height, then press the weight overhead until your arms are fully extended.",
                Arrays.asList("Shoulders", "Triceps", "Arms"),
                "assets/exercise-icons/overhead-press.png"
        ));

        templates.add(createTemplate(
                "Lateral Raises",
                "An isolation exercise for the side delts. Stand with dumbbells at your sides, then raise your arms out to the sides until they're parallel with the floor.",
                Arrays.asList("Side Deltoids", "Shoulders"),
                "assets/exercise-icons/lateral-raises.png"
        ));

        templates.add(createTemplate(
                "Front Raises",
                "An isolation exercise for the front deltoids. Stand with dumbbells in front of your thighs, then raise them forward to shoulder height with straight arms.",
                Arrays.asList("Front Deltoids", "Shoulders"),
                "assets/exercise-icons/front-raises.png"
        ));

        templates.add(createTemplate(
                "Face Pulls",
                "An exercise that targets the rear deltoids and upper back. Pull a rope attachment toward your face at shoulder height, separating your hands at the end.",
                Arrays.asList("Rear Deltoids", "Mid-Back", "Shoulders", "Back"),
                "assets/exercise-icons/face-pulls.png"
        ));

        templates.add(createTemplate(
                "Upright Rows",
                "A compound exercise for the shoulders and traps. Hold a barbell with a narrow grip and pull it straight up along your body to chest level.",
                Arrays.asList("Shoulders", "Traps", "Biceps", "Arms"),
                "assets/exercise-icons/upright-rows.png"
        ));

        // Arms exercises
        templates.add(createTemplate(
                "Bicep Curls",
                "An isolation exercise for the biceps. Stand holding dumbbells with palms facing forward, then curl the weights toward your shoulders.",
                Arrays.asList("Biceps", "Arms"),
                "assets/exercise-icons/bicep-curls.png"
        ));

        templates.add(createTemplate(
                "Hammer Curls",
                "A variation of bicep curls performed with a neutral grip. Hold dumbbells with palms facing each other and curl them toward your shoulders.",
                Arrays.asList("Biceps", "Forearms", "Arms"),
                "assets/exercise-icons/hammer-curls.png"
        ));

        templates.add(createTemplate(
                "Triceps Pushdowns",
                "An isolation exercise for the triceps. Stand facing a cable machine and push the handle down by extending your elbows.",
                Arrays.asList("Triceps", "Arms"),
                "assets/exercise-icons/triceps-pushdowns.png"
        ));

        templates.add(createTemplate(
                "Triceps Extensions",
                "An isolation exercise for the triceps. Can be performed overhead or lying down, extending the arms to work the triceps through their full range of motion.",
                Arrays.asList("Triceps", "Arms"),
                "assets/exercise-icons/triceps-extensions.png"
        ));

        templates.add(createTemplate(
                "Skull Crushers",
                "A triceps exercise performed lying on a bench. Lower the weight toward your forehead by bending your elbows, then extend back to the starting position.",
                Arrays.asList("Triceps", "Arms"),
                "assets/exercise-icons/skull-crushers.png"
        ));

        // Core/Abs exercises
        templates.add(createTemplate(
                "Crunches",
                "A basic abdominal exercise. Lie on your back with knees bent, then lift your shoulders off the ground by contracting your abs.",
                Arrays.asList("Abs", "Core"),
                "assets/exercise-icons/crunches.png"
        ));

        templates.add(createTemplate(
                "Leg Raises",
                "An exercise that targets the lower abs. Lie on your back and raise your legs toward the ceiling while keeping them straight.",
                Arrays.asList("Lower Abs", "Hip Flexors", "Core"),
                "assets/exercise-icons/leg-raises.png"
        ));

        templates.add(createTemplate(
                "Plank",
                "A core stabilization exercise. Hold a push-up position with your weight on your forearms and toes, keeping your body in a straight line.",
                Arrays.asList("Core", "Abs"),
                "assets/exercise-icons/plank.png"
        ));

        templates.add(createTemplate(
                "Russian Twists",
                "A rotational core exercise. Sit with your knees bent and feet off the ground, twist your torso from side to side.",
                Arrays.asList("Obliques", "Abs", "Core"),
                "assets/exercise-icons/russian-twists.png"
        ));

        templates.add(createTemplate(
                "Ab Rollouts",
                "An advanced core exercise using an ab wheel. Kneel and roll the wheel forward while maintaining a straight line from knees to head, then roll back.",
                Arrays.asList("Abs", "Core", "Shoulders"),
                "assets/exercise-icons/ab-rollouts.png"
        ));

        return templates;
    }

    private ExerciseTemplate createTemplate(String name, String description, List<String> targetMuscleGroups, String imageUrl) {
        ExerciseTemplate template = new ExerciseTemplate();
        template.setName(name);
        template.setDescription(description);
        template.setTargetMuscleGroups(targetMuscleGroups);
        template.setImageUrl(imageUrl);
        return template;
    }
}
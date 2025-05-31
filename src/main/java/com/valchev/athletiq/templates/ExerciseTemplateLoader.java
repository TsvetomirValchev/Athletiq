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
                "/images/exercises/bench-press.jpg"
        ));

        templates.add(createTemplate(
                "Incline Bench Press",
                "Similar to the flat bench press but performed on an inclined bench to target the upper chest muscles.",
                Arrays.asList("Upper Chest", "Shoulders", "Triceps", "Chest", "Arms"),
                "/images/exercises/incline-bench-press.jpg"
        ));

        templates.add(createTemplate(
                "Dumbbell Flyes",
                "An isolation exercise for the chest. Lie on a bench with dumbbells extended above your chest, then open your arms wide while keeping a slight bend in your elbows.",
                Arrays.asList("Chest", "Shoulders", "Arms"),
                "/images/exercises/dumbbell-flyes.jpg"
        ));

// Back exercises
        templates.add(createTemplate(
                "Deadlift",
                "A compound exercise that targets multiple muscle groups. With feet shoulder-width apart, bend at the hips and knees to grip the barbell, then stand up by extending your hips and knees.",
                Arrays.asList("Lower Back", "Hamstrings", "Glutes", "Traps", "Back", "Legs"),
                "/images/exercises/deadlift.jpg"
        ));

        templates.add(createTemplate(
                "Pull-ups",
                "A bodyweight exercise that primarily targets the back. Hang from a bar with palms facing away, then pull yourself up until your chin is over the bar.",
                Arrays.asList("Lats", "Biceps", "Mid-Back", "Back", "Arms"),
                "/images/exercises/pull-ups.jpg"
        ));

        templates.add(createTemplate(
                "Bent Over Row",
                "A compound exercise for the back. Bend at your hips with a slight bend in your knees, hold a barbell with an overhand grip, and pull it towards your lower abdomen.",
                Arrays.asList("Mid-Back", "Lats", "Biceps", "Rear Delts", "Back", "Arms"),
                "/images/exercises/bent-over-row.jpg"
        ));

// Legs exercises
        templates.add(createTemplate(
                "Squat",
                "A fundamental compound exercise for lower body strength. Place a barbell across your upper back, bend your knees and hips to lower your body, then return to standing.",
                Arrays.asList("Quadriceps", "Glutes", "Hamstrings", "Lower Back", "Legs"),
                "/images/exercises/squat.jpg"
        ));

        templates.add(createTemplate(
                "Leg Press",
                "A machine-based exercise for the legs. Sit in the leg press machine and push the platform away by extending your knees and hips.",
                Arrays.asList("Quadriceps", "Glutes", "Hamstrings", "Legs"),
                "/images/exercises/leg-press.jpg"
        ));

        templates.add(createTemplate(
                "Romanian Deadlift",
                "An exercise that targets the posterior chain. Hold a barbell in front of your thighs, hinge at your hips while keeping your back straight, lower the weight, then return to standing.",
                Arrays.asList("Hamstrings", "Glutes", "Lower Back", "Legs", "Back"),
                "/images/exercises/romanian-deadlift.jpg"
        ));

// Shoulder exercises
        templates.add(createTemplate(
                "Overhead Press",
                "A compound exercise for the shoulders. Stand holding a barbell at shoulder height, then press the weight overhead until your arms are fully extended.",
                Arrays.asList("Shoulders", "Triceps", "Arms"),
                "/images/exercises/overhead-press.jpg"
        ));

        templates.add(createTemplate(
                "Lateral Raises",
                "An isolation exercise for the side delts. Stand with dumbbells at your sides, then raise your arms out to the sides until they're parallel with the floor.",
                Arrays.asList("Side Deltoids", "Shoulders"),
                "/images/exercises/lateral-raises.jpg"
        ));

// Arms exercises
        templates.add(createTemplate(
                "Bicep Curls",
                "An isolation exercise for the biceps. Stand holding dumbbells with palms facing forward, then curl the weights toward your shoulders.",
                Arrays.asList("Biceps", "Arms"),
                "/images/exercises/bicep-curls.jpg"
        ));

        templates.add(createTemplate(
                "Tricep Pushdowns",
                "An isolation exercise for the triceps. Stand facing a cable machine and push the handle down by extending your elbows.",
                Arrays.asList("Triceps", "Arms"),
                "/images/exercises/tricep-pushdowns.jpg"
        ));

// Core exercises
        templates.add(createTemplate(
                "Plank",
                "A core stabilization exercise. Hold a push-up position with your weight on your forearms and toes, keeping your body in a straight line.",
                Arrays.asList("Core", "Abs"),
                "/images/exercises/plank.jpg"
        ));

        templates.add(createTemplate(
                "Russian Twists",
                "A rotational core exercise. Sit with your knees bent and feet off the ground, twist your torso from side to side.",
                Arrays.asList("Obliques", "Abs", "Core"),
                "/images/exercises/russian-twists.jpg"
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
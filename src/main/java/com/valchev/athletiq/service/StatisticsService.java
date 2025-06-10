package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.CalendarDataDTO;
import com.valchev.athletiq.domain.dto.MuscleGroupDTO;
import com.valchev.athletiq.domain.dto.WorkoutStreakDTO;
import com.valchev.athletiq.domain.entity.ExerciseHistory;
import com.valchev.athletiq.domain.entity.WorkoutHistory;
import com.valchev.athletiq.repository.ExerciseTemplateRepository;
import com.valchev.athletiq.repository.WorkoutHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final ExerciseTemplateRepository exerciseTemplateRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_STREAK_BREAK_DAYS = 6;

    public WorkoutStreakDTO getWorkoutStreaks(UUID userId) {
        List<WorkoutHistory> workouts = workoutHistoryRepository.findByUserIdOrderByDateAsc(userId);

        if (workouts.isEmpty()) {
            return new WorkoutStreakDTO(0, 0, null, Collections.emptyList());
        }

        // Extract and sort all workout dates
        List<LocalDate> workoutDates = workouts.stream()
                .map(WorkoutHistory::getDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Convert to string format for frontend
        List<String> workoutDateStrings = workoutDates.stream()
                .map(date -> date.format(DATE_FORMATTER))
                .collect(Collectors.toList());

        // Calculate current streak
        int currentStreak = calculateCurrentStreak(workoutDates);

        // Calculate longest streak
        int longestStreak = calculateLongestStreak(workoutDates);

        // Get the last workout date
        String lastWorkoutDate = workouts.isEmpty() ? null :
                workoutDates.get(workoutDates.size() - 1).format(DATE_FORMATTER);

        return new WorkoutStreakDTO(currentStreak, longestStreak, lastWorkoutDate, workoutDateStrings);
    }

    private int calculateCurrentStreak(List<LocalDate> workoutDates) {
        if (workoutDates.isEmpty()) {
            return 0;
        }

        LocalDate lastWorkoutDate = workoutDates.get(workoutDates.size() - 1);
        if (ChronoUnit.DAYS.between(lastWorkoutDate, LocalDate.now()) > MAX_STREAK_BREAK_DAYS) {
            return 0;
        }

        int currentStreak = 1;
        for (int i = workoutDates.size() - 1; i > 0; i--) {
            LocalDate current = workoutDates.get(i);
            LocalDate previous = workoutDates.get(i - 1);

            long daysBetween = ChronoUnit.DAYS.between(previous, current);

            if (daysBetween <= MAX_STREAK_BREAK_DAYS + 1) {
                currentStreak++;
            } else {
                break;
            }
        }

        return currentStreak;
    }

    private int calculateLongestStreak(List<LocalDate> workoutDates) {
        if (workoutDates.isEmpty()) {
            return 0;
        }

        int longestStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < workoutDates.size(); i++) {
            long daysBetween = ChronoUnit.DAYS.between(workoutDates.get(i - 1), workoutDates.get(i));

            if (daysBetween <= MAX_STREAK_BREAK_DAYS + 1) {
                currentStreak++;
            } else {
                longestStreak = Math.max(longestStreak, currentStreak);
                currentStreak = 1;
            }
        }

        return Math.max(longestStreak, currentStreak);
    }

    public List<CalendarDataDTO> getCalendarData(int year, int month, UUID userId) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<LocalDate> workoutDates = workoutHistoryRepository
                .findByUserIdOrderByDateAsc(userId)
                .stream()
                .map(WorkoutHistory::getDate)
                .distinct()
                .toList();

        Set<LocalDate> workoutDateSet = new HashSet<>(workoutDates);

        List<CalendarDataDTO> calendarDatumDtos = new ArrayList<>();

        // Create data for each day of the month
        for (int day = 1; day <= endDate.getDayOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            boolean hasWorkout = workoutDateSet.contains(date);

            calendarDatumDtos.add(CalendarDataDTO.builder()
                    .date(date)
                    .hasWorkout(hasWorkout)
                    .build());
        }

        return calendarDatumDtos;
    }

    public List<MuscleGroupDTO> getMuscleGroupDistribution(UUID userId) {
        // Get all workouts for the user
        List<WorkoutHistory> workouts = workoutHistoryRepository.findAllByUserId(userId);

        // Count occurrences of each muscle group
        Map<String, Integer> muscleGroupCounts = new HashMap<>();

        for (WorkoutHistory workout : workouts) {
            for (ExerciseHistory exerciseHistory : workout.getExerciseHistories()) {
                String exerciseName = exerciseHistory.getExerciseName();
                List<String> muscleGroups = exerciseTemplateRepository.findMuscleGroupsByExerciseName(exerciseName);

                // Increment counter for each muscle group
                for (String muscleGroup : muscleGroups) {
                    muscleGroupCounts.merge(muscleGroup, 1, Integer::sum);
                }
            }
        }

        return muscleGroupCounts.entrySet().stream()
                .map(entry -> new MuscleGroupDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(MuscleGroupDTO::getWorkoutCount).reversed())
                .collect(Collectors.toList());
    }
}
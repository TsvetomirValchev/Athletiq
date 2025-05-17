//package com.valchev.athletiq.service;
//
//import com.valchev.athletiq.domain.dto.ExerciseHistoryDTO;
//import com.valchev.athletiq.domain.dto.SetHistoryDTO;
//import com.valchev.athletiq.domain.entity.ExerciseHistory;
//import com.valchev.athletiq.domain.entity.SetHistory;
//import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
//import com.valchev.athletiq.domain.mapper.ExerciseHistoryMapper;
//import com.valchev.athletiq.domain.mapper.SetHistoryMapper;
//import com.valchev.athletiq.repository.ExerciseHistoryRepository;
//import com.valchev.athletiq.repository.SetHistoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ExerciseHistoryService {
//
//    private final ExerciseHistoryRepository exerciseHistoryRepository;
//    private final SetHistoryRepository setHistoryRepository;
//    private final ExerciseHistoryMapper exerciseHistoryMapper;
//    private final SetHistoryMapper setHistoryMapper;
//
//    /**
//     * Find all exercises for a workout history
//     */
//    public List<ExerciseHistoryDTO> findAllByWorkoutHistoryId(UUID workoutHistoryId) {
//        log.info("Finding all exercises for workout history: {}", workoutHistoryId);
//        List<ExerciseHistory> exercises = exerciseHistoryRepository.findAllByWorkoutHistoryIdOrderByOrderPosition(workoutHistoryId);
//        return exerciseHistoryMapper.toDTOs(exercises);
//    }
//
//    /**
//     * Find a specific exercise in a workout history
//     */
//    public Optional<ExerciseHistoryDTO> findByIdAndWorkoutHistoryId(UUID exerciseHistoryId, UUID workoutHistoryId) {
//        log.info("Finding exercise {} in workout history {}", exerciseHistoryId, workoutHistoryId);
//        return exerciseHistoryRepository.findByExerciseHistoryIdAndWorkoutHistoryId(exerciseHistoryId, workoutHistoryId)
//                .map(exerciseHistoryMapper::toDTO);
//    }
//
//    /**
//     * Find all sets for an exercise
//     */
//    public List<SetHistoryDTO> findSetsByExerciseHistoryId(UUID exerciseHistoryId) {
//        log.info("Finding sets for exercise: {}", exerciseHistoryId);
//        List<SetHistory> sets = setHistoryRepository.findAllByExerciseHistoryIdOrderByOrderPosition(exerciseHistoryId);
//        return setHistoryMapper.toDTOs(sets);
//    }
//
//    /**
//     * Find exercise by ID
//     */
//    public Optional<ExerciseHistoryDTO> findById(UUID exerciseHistoryId) {
//        log.info("Finding exercise by ID: {}", exerciseHistoryId);
//        return exerciseHistoryRepository.findById(exerciseHistoryId)
//                .map(exerciseHistoryMapper::toDTO);
//    }
//}
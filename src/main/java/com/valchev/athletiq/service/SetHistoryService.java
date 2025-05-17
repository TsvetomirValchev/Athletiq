//package com.valchev.athletiq.service;
//
//import com.valchev.athletiq.domain.dto.SetHistoryDTO;
//import com.valchev.athletiq.domain.entity.SetHistory;
//import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
//import com.valchev.athletiq.domain.mapper.SetHistoryMapper;
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
//public class SetHistoryService {
//
//    private final SetHistoryRepository setHistoryRepository;
//    private final SetHistoryMapper setHistoryMapper;
//
//    /**
//     * Find a set by ID
//     */
//    public Optional<SetHistoryDTO> findById(UUID setHistoryId) {
//        log.info("Finding set history by ID: {}", setHistoryId);
//        return setHistoryRepository.findById(setHistoryId)
//                .map(setHistoryMapper::toDTO);
//    }
//
//    /**
//     * Find all sets for an exercise
//     */
//    public List<SetHistoryDTO> findAllByExerciseHistoryId(UUID exerciseHistoryId) {
//        log.info("Finding all sets for exercise: {}", exerciseHistoryId);
//        List<SetHistory> sets = setHistoryRepository.findAllByExerciseHistoryIdOrderByOrderPosition(exerciseHistoryId);
//        return setHistoryMapper.toDTOs(sets);
//    }
//
//    /**
//     * Get the exercise ID that a set belongs to
//     */
//    public UUID getExerciseIdForSetId(UUID setHistoryId) {
//        log.info("Finding exercise ID for set: {}", setHistoryId);
//        return setHistoryRepository.findExerciseHistoryIdBySetHistoryId(setHistoryId)
//                .orElseThrow(() -> new ResourceNotFoundException("Set history not found"));
//    }
//}
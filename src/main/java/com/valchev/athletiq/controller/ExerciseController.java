package com.valchev.athletiq.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.service.ExerciseService;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    public List<ExerciseDTO> getAllExercises() {
        return exerciseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable UUID id) {
        Optional<ExerciseDTO> exerciseDTO = exerciseService.findById(id);
        return exerciseDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO exerciseDTO) {
        ExerciseDTO savedExercise = exerciseService.save(exerciseDTO);
        return ResponseEntity.ok(savedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable UUID id) {
        exerciseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

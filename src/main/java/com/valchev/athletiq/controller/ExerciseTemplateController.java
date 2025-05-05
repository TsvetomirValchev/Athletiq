package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseTemplateDTO;
import com.valchev.athletiq.service.ExerciseTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercise-templates")
@RequiredArgsConstructor
public class ExerciseTemplateController {

    private final ExerciseTemplateService exerciseTemplateService;

    @GetMapping
    public List<ExerciseTemplateDTO> getAllTemplates() {
        return exerciseTemplateService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseTemplateDTO> getTemplateById(@PathVariable UUID id) {
        return exerciseTemplateService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name")
    public ResponseEntity<ExerciseTemplateDTO> getTemplateByName(@RequestParam String name) {
        return exerciseTemplateService.findByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-muscle-group")
    public List<ExerciseTemplateDTO> getTemplatesByMuscleGroup(@RequestParam String muscleGroup) {
        return exerciseTemplateService.findByTargetMuscleGroup(muscleGroup);
    }
}
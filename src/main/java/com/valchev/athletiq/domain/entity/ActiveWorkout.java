package com.valchev.athletiq.domain.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "active_workout")
@Data
@Slf4j
@DiscriminatorValue(ActiveWorkout.JSON_TYPE_NAME)
public class ActiveWorkout extends Workout {

    public static final String JSON_TYPE_NAME = "ACTIVE";

    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

}

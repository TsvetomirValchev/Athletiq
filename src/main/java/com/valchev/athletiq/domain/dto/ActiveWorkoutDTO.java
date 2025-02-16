
package com.valchev.athletiq.domain.dto;

import java.time.OffsetDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveWorkoutDTO extends WorkoutDTO {

    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

}

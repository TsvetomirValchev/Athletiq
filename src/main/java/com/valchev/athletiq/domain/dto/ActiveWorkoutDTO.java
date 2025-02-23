
package com.valchev.athletiq.domain.dto;

import java.time.OffsetDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ActiveWorkoutDTO extends WorkoutDTO {

    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

}

package com.example.easyexpressbackend.domain.statistic;

import com.example.easyexpressbackend.entity.SequenceIdBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class PickUpOrderStatistic extends SequenceIdBaseEntity {
    private Long unfinishedOrder;
    private Long inProcessOrder;
    private Long missedDeadlineOrder;
}

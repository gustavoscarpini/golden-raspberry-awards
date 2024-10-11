package com.example.goldenraspberryawards.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MinMaxIntervalProducerResponse {
    private List<ProducerIntervalRespose> min;
    private List<ProducerIntervalRespose> max;
}

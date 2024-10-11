package com.example.goldenraspberryawards.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerIntervalRespose {
    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;
}

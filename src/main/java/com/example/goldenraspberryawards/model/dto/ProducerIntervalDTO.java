package com.example.goldenraspberryawards.model.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerIntervalDTO {
    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProducerIntervalDTO that = (ProducerIntervalDTO) o;
        return getInterval() == that.getInterval() && getPreviousWin() == that.getPreviousWin() && getFollowingWin() == that.getFollowingWin() && Objects.equals(getProducer(), that.getProducer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProducer(), getInterval(), getPreviousWin(), getFollowingWin());
    }

    @Override
    public String toString() {
        return "{" +
                "producer='" + producer + '\'' +
                ", interval=" + interval +
                ", previousWin=" + previousWin +
                ", followingWin=" + followingWin +
                '}';
    }
}

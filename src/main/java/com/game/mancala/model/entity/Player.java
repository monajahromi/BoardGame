package com.game.mancala.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Embeddable
@ToString
public class Player {

    private int[] pits;
    private int bigPit;
    private String name;
    private boolean isActive;


    public void increaseBigPit(int num) {
        this.bigPit = bigPit + num;
    }
}

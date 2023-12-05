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

    private String name;

}

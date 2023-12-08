package com.game.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Embeddable
@ToString
@EqualsAndHashCode
public class Player {

    private String name;


}

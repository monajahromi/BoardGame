package com.game.mancala.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "game")
@Builder
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ElementCollection
    @CollectionTable(name="game_player",
    joinColumns = @JoinColumn(name="game_id"))
    private List<Player> players;


}

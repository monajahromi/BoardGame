package com.game.mancala.model.entity;

import jakarta.persistence.*;
import com.game.mancala.utils.GameStatus;
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

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @ElementCollection
    @CollectionTable(name="game_player",
    joinColumns = @JoinColumn(name="game_id"))
    private List<Player> players;


}

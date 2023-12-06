package com.game.mancala.model.entity;

import com.game.mancala.utils.GameStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "game")
@Builder
@EqualsAndHashCode
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    private int[][] gameMatrix;

    private int activePlayerIndex;
    private int winnerPlayerIndex;

    @ElementCollection
    @CollectionTable(name = "game_player",
            joinColumns = @JoinColumn(name = "game_id"))
    private List<Player> players;


}

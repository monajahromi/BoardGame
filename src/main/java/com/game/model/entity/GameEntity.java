package com.game.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.utils.GameStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "game")
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    @JsonIgnore
    private Long version;

    private String name;
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    private int[][] gameMatrix;

    private Integer activePlayerIndex;
    private Integer winnerPlayerIndex;

    @ElementCollection
    @CollectionTable(name = "game_player",
            joinColumns = @JoinColumn(name = "game_id"))
    private List<Player> players;


}

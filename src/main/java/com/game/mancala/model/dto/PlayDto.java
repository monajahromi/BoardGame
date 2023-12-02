package com.game.mancala.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlayDto {
    private String playerName;
    private Long gameId;
    private int selectedPit;
}

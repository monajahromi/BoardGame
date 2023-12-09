package com.game.model.dto;

import com.game.utils.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StartDto {

    private String[] playerNames;
    private int pitsCount;
    private int stonesPerPit;
}

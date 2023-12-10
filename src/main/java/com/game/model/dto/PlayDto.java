package com.game.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlayDto {

    @NotBlank(message = "Player name cannot be blank or empty")
    private String playerName;

    @NotNull(message = "Game ID cannot be null")
    private Long gameId;

    @NotNull(message = "Selected pit cannot be null")
    private Integer selectedPit;

}

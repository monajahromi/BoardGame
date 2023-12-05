package com.game.mancala.rule.mancala;


import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.model.entity.Player;
import com.game.mancala.rule.GameCreateRule;
import com.game.mancala.utils.GameStatus;
import com.game.mancala.utils.MancalaConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MancalaGameCreateRule implements GameCreateRule {
    private final MancalaConfig config;
    public MancalaGameCreateRule(MancalaConfig config) {
        this.config = config;
    }
    @Override
    public GameEntity setupNewGame(StartDto dto) {

        List<Player> players = new ArrayList<>();

        Arrays.stream(dto.getPlayerNames())
                .forEach(item -> {
                    players.add(Player.builder()
                            .name(item)
                            .build());
                });

        int pitsCount = dto.getPitsCount() == 0 ?
                config.getDefault_player_pits_count() : dto.getPitsCount();

        int stonesPerPit = dto.getStonesPerPit() == 0 ?
                config.getDefault_stones_per_pit() : dto.getStonesPerPit();

        GameEntity game = new GameEntity();
        game.setPlayers(players);
        game.setName("Mancala");
        game.setStatus(GameStatus.PLAYING);
        game.setGameMatrix(matrixGenerator(pitsCount, stonesPerPit));

        return game;
    }
    public static int[][] matrixGenerator(int pitCount, int stonesPerPit) {
        // Create a 2D array with 2 rows and pitCount + 1 columns
        int[][] pitArray = new int[2][pitCount + 1];

        // Initialize values in each row
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < pitCount; j++) {
                pitArray[i][j] = stonesPerPit;
            }
            // Set the last index to 0 for bigPit
            pitArray[i][pitCount] = 0;
        }

        return pitArray;
    }
    @Override
    public void setInitialPlayerTurn(GameEntity gameEntity) {
        gameEntity.setActivePlayerIndex(0);
    }
}

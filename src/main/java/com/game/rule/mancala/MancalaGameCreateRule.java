package com.game.rule.mancala;


import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.model.entity.Player;
import com.game.rule.GameCreateRule;
import com.game.utils.GameStatus;
import com.game.utils.MancalaConfig;
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
                .forEach(item -> players.add(Player.builder()
                        .name(item)
                        .build()));

        int pitsCount = dto.getPitsCount() == 0 ?
                config.getDefault_player_pits_count() : dto.getPitsCount();

        int stonesPerPit = dto.getStonesPerPit() == 0 ?
                config.getDefault_stones_per_pit() : dto.getStonesPerPit();

        GameEntity game = new GameEntity();
        game.setPlayers(players);
        game.setName(config.getGame_name());
        game.setStatus(GameStatus.PLAYING);
        game.setGameMatrix(MancalaIndexGenerator.initializeGameMatrix(pitsCount, stonesPerPit, dto.getPlayerNames().length));

        return game;
    }


    @Override
    public void setInitialPlayerTurn(GameEntity gameEntity) {
        gameEntity.setActivePlayerIndex(0);
    }
}

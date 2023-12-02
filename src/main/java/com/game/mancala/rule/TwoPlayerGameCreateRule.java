package com.game.mancala.rule;


import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.model.entity.Player;
import com.game.mancala.utils.MancalaConfig;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TwoPlayerGameCreateRule implements GameCreateRule{
    private final MancalaConfig config;
    public TwoPlayerGameCreateRule(MancalaConfig config) {
        this.config = config;
    }
    @Override
    public void initializeActivePlayer(GameEntity game) {
        //first user has the first turn
        game.getPlayers().get(0).setActive(true);
    }

    @Override
    public void initializePits(GameEntity game, StartDto dto) {
        //if client didn't send players pits count or initial stones of each pit,
        //it is initialized by reading associated value from config
        int pitsCount = dto.getPitsCount() == 0 ?
                config.getDefault_player_pits_count() : dto.getPitsCount();

        int stonesPerPit = dto.getStonesPerPit() == 0 ?
                config.getDefault_stones_per_pit() : dto.getStonesPerPit();

        for (Player item : game.getPlayers()) {
            item.setPits(Arrays.stream(new int[pitsCount]).map(i -> stonesPerPit).toArray());
        }

    }
    @Override
    public int maximumAllowedPlayer() {
        return 2;
    }
}

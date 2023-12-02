package com.game.mancala.rule;


import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import org.springframework.stereotype.Service;

@Service
public class TwoPlayerGameCreateRule implements GameCreateRule{

    @Override
    public void initializeActivePlayer(GameEntity game) {

    }
    @Override
    public void initializePits(GameEntity game, StartDto dto) {

    }
    @Override
    public int maximumAllowedPlayer() {
        return 0;
    }
}

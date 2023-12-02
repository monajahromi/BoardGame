package com.game.mancala.rule;


import com.game.mancala.model.entity.GameEntity;
import org.springframework.stereotype.Service;

@Service
public class TwoPlayerGamePlayRule implements GamePlayRule {


    @Override
    public GameEntity play(GameEntity game, int selectedPit) {
        return null;
    }
}

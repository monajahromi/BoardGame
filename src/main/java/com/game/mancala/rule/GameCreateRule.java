package com.game.mancala.rule;

import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;

public interface GameCreateRule {
    GameEntity setupNewGame(StartDto initialData);

    void setInitialPlayerTurn(GameEntity gameEntity);

}

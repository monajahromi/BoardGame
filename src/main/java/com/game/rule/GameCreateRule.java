package com.game.rule;

import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;

public interface GameCreateRule {
    GameEntity setupNewGame(StartDto initialData);

    void setInitialPlayerTurn(GameEntity gameEntity);

}

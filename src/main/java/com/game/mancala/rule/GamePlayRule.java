package com.game.mancala.rule;


import com.game.mancala.model.entity.GameEntity;

public interface GamePlayRule extends  GameRule{
    GameEntity play(GameEntity game, int selectedPit);
}

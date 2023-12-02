package com.game.mancala.rule;

import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import org.springframework.stereotype.Service;

@Service
public interface GameCreateRule extends  GameRule{

    void initializeActivePlayer(GameEntity game);
    void initializePits(GameEntity game, StartDto dto);
    int maximumAllowedPlayer();


}

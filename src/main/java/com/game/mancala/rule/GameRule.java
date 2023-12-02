package com.game.mancala.rule;


import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.model.entity.Player;
import org.springframework.stereotype.Service;

@Service
public interface GameRule {

    default Player getActivePlayer(GameEntity game) {

        return game.getPlayers().stream()
                .filter(Player::isActive)
                .findFirst()
                .get();
    }

}

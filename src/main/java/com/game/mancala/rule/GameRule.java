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

    default boolean isTurnSwitchConditionMet(GameEntity game, int selectedPit, int stoneCounts) {
        // The condition for a turn switch is met if the remainder of dividing stoneCounts
        // by the total number of pits in the game is equal to the distance to the big pit.

        int distanceToBigPit = game.getPitsPerPlayer() - selectedPit;
        return stoneCounts % game.getTotalPits() != distanceToBigPit;
    }

}

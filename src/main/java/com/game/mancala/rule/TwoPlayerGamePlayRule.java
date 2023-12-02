package com.game.mancala.rule;


import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.model.entity.Player;
import org.springframework.stereotype.Service;

@Service
public class TwoPlayerGamePlayRule implements GamePlayRule {

    @Override
    public GameEntity play(GameEntity game, int selectedPit) {

        Player activePlayer = getActivePlayer(game);
        int stoneCount = activePlayer.getPits()[selectedPit];
        if (stoneCount == 0) {
            throw new IllegalArgumentException("This pit has no stone!");
        }
        int remainedStone = playOneCycle(game, selectedPit + 1, stoneCount);
        activePlayer.getPits()[selectedPit] = 0;
       while (remainedStone > 0) {
            remainedStone = playOneCycle(game, 0, remainedStone);
        }
        return game;
    }

    private int playOneCycle(GameEntity game, int currentHole, int stoneCounts) {

        Player activePlayer = getActivePlayer(game);
        Player waitingPlayer = getWaitingPlayers(game);

        while (currentHole < activePlayer.getPits().length && stoneCounts > 0) {
            if (stoneCounts == 1 && activePlayer.getPits()[currentHole] == 0) {
                getOtherOneStones(game, currentHole);
                //  stillsOtherOne = true;
                return 0;
            }
            activePlayer.getPits()[currentHole] = activePlayer.getPits()[currentHole] + 1;
            currentHole++;
            stoneCounts--;
        }
        if (stoneCounts > 0) {
            activePlayer.increaseBigPit(1);
            stoneCounts--;
            if (stoneCounts != 0) {
                switchTurn(game);
            }
        }
        currentHole = 5;
        while (currentHole >= 0 && stoneCounts > 0) {

            waitingPlayer.getPits()[currentHole] = waitingPlayer.getPits()[currentHole] + 1;
            currentHole--;
            stoneCounts--;
        }
        return stoneCounts;

    }
    public void getOtherOneStones(GameEntity game, int index) {
        Player activePlayer = getActivePlayer(game);
        Player waitingPlayer = getWaitingPlayers(game);

        activePlayer.increaseBigPit(waitingPlayer.getPits()[5 - index] + 1);
        waitingPlayer.getPits()[5 - index] = 0;
    }
    public void switchTurn(GameEntity game) {

        getWaitingPlayers(game).setActive(true);
        getActivePlayer(game).setActive(false);
    }
    public Player getWaitingPlayers(GameEntity game) {

        return game.getPlayers().stream()
                .filter(item -> !item.isActive())
                .findFirst()
                .get();
    }

}

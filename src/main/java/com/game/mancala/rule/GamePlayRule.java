package com.game.mancala.rule;


import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.utils.GameStatus;

public interface GamePlayRule {
    GameEntity play(GameEntity game, String playerName, int selectedRow);

    GameEntity performMove(GameEntity game, int selectedRow);

    void toggleTurn(GameEntity game, int selectedRow);

    boolean validatePlayerTurn(GameEntity game, String inputPlayerName);

    boolean isMoveValid(GameEntity game, int selectedRow);

    boolean hasGameEnded(GameEntity game);

    void onGameEnd(GameEntity game);

    void findWinningPlayer(GameEntity game);

    default boolean isPlayableGame(GameEntity game) {
        return game.getStatus().equals(GameStatus.PLAYING);
    }

}

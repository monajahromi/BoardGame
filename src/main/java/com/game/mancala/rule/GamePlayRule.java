package com.game.mancala.rule;


import com.game.mancala.model.entity.GameEntity;

public interface GamePlayRule {
    GameEntity play(GameEntity game, String playerName, int selectedRow);

    void validatePlayableGame(GameEntity game);

    void validatePlayerTurn(GameEntity game, String inputPlayerName);

    void validateMove(GameEntity game, int selectedPit);

    int[][] performMove(GameEntity game, int selectedRow);

    void toggleTurn(GameEntity game, int selectedRow);

    boolean hasGameEnded(int[][] gameBoard);

    int[][] onGameEnd(int[][] gameBoard);

    int findWinningPlayer(int[][] gameBoard);
}

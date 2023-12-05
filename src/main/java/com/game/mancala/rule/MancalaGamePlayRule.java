package com.game.mancala.rule;

import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.utils.GameStatus;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class MancalaGamePlayRule implements GamePlayRule {

    @Override
    public GameEntity play(GameEntity game, String playerName, int selectedPit) {
        if (!isPlayableGame(game)) {
            throw new IllegalArgumentException("The game is not in a playable state. Make sure the game is in progress.");
        }
        if (!validatePlayerTurn(game, playerName)) {
            throw new IllegalArgumentException("It's not " + playerName + "'s turn to play.");
        }
        if (!isMoveValid(game, selectedPit)) {
            throw new IllegalArgumentException("Invalid move. Please select a valid spot on the board.");
        }
        boolean needsToggleTurn = isToggleTurnConditionMet(game, selectedPit);


        performMove(game, selectedPit);

        if (needsToggleTurn)
            toggleTurn(game, selectedPit);

        if (hasGameEnded(game)) {
            onGameEnd(game);
            findWinningPlayer(game);
        }

        return game;

    }


    @Override
    public GameEntity performMove(GameEntity game, int selectedPit) {

        List<int[]> indexesForAddingStones = MancalaIndexGenerator
                .generateIndexes(game.getGameMatrix().length,
                        game.getGameMatrix()[0].length,
                        game.getActivePlayerIndex(),
                        selectedPit,
                        game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit]
                );
        indexesForAddingStones.forEach(i -> System.out.println(Arrays.toString(i)));
        for (int[] item : indexesForAddingStones) {
            System.out.println();
            game.getGameMatrix()[item[0]][item[1]] = game.getGameMatrix()[item[0]][item[1]] + 1;
        }
        game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit] = 0;
        return game;
    }

    @Override
    public void toggleTurn(GameEntity game, int selectedPit) {
        game.setActivePlayerIndex((game.getActivePlayerIndex() + 1) % 2);
    }


    boolean isToggleTurnConditionMet(GameEntity game, int selectedPit) {
        // The condition for a turn switch is met if the remainder of dividing stoneCounts
        // by the total number of pits a stone can traverse in one cycle

        int pitsPerPlayerExcludedBigPit = game.getGameMatrix()[0].length - 1;
        int pitsStoneCanTraverse = (game.getGameMatrix().length * pitsPerPlayerExcludedBigPit) + 1;
        int stoneCounts = game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit];

        int distanceToBigPit = pitsPerPlayerExcludedBigPit - selectedPit;

        return stoneCounts % pitsStoneCanTraverse != distanceToBigPit;
    }

    @Override
    public boolean validatePlayerTurn(GameEntity game, String inputPlayerName) {
        return game.getPlayers()
                .get(game.getActivePlayerIndex())
                .getName()
                .equals(inputPlayerName);
    }

    @Override
    public boolean isMoveValid(GameEntity game, int selectedPit) {
        if (selectedPit > game.getGameMatrix()[0].length - 1) {
            return false;
        }
        return game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit] > 0;
    }


    @Override
    public boolean hasGameEnded(GameEntity game) {
        return Arrays.stream(game.getGameMatrix())
                .anyMatch(pits -> IntStream.range(0, pits.length - 1).allMatch(item -> pits[item] == 0));

    }

    @Override
    public void onGameEnd(GameEntity game) {
        game.setStatus(GameStatus.FINISHED);
        Arrays.stream(game.getGameMatrix()).forEach(pits -> {
            pits[pits.length - 1] = Arrays.stream(pits).reduce(0, (a, b) -> a + b);
            for (int index = 0; index < pits.length - 1; index++) {
                pits[index] = 0;
            }
        });
    }

    @Override
    public void findWinningPlayer(GameEntity game) {
        int[] bigPits = Arrays.stream(game.getGameMatrix())
                .mapToInt(pits -> pits[pits.length - 1]).toArray();
        int indexOfWinner = IntStream.range(0, bigPits.length)
                .reduce((i, j) -> bigPits[i] > bigPits[j] ? i : j)
                .getAsInt();
        game.setWinnerPlayerIndex(indexOfWinner);

    }


}

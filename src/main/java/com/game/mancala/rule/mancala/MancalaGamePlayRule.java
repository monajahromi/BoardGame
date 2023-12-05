package com.game.mancala.rule.mancala;

import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.rule.GamePlayRule;
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

        int stoneCount = getStonesCount(game , selectedPit) ;
        performMove(game, selectedPit);
        capturingStones(game, selectedPit, stoneCount) ;
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
        for (int[] item : indexesForAddingStones) {
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
        int totalPitsExcludingBigPits = calculateTotalPitsExcludingBigPits(game);
        int distanceToBigPit = totalPitsExcludingBigPits - selectedPit;

        return getStonesCount(game, selectedPit) % (totalPitsExcludingBigPits + 1) != distanceToBigPit;
    }

    // Calculate the total number of pits in the ground, excluding big pits
    private int calculateTotalPitsExcludingBigPits(GameEntity game) {
        return playersCount(game) * playerPitsExcludingBigPits(game);
    }

    private int getStonesCount(GameEntity game, int selectedPit) {
        return game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit];
    }

    private int playerPitsExcludingBigPits(GameEntity game) {
        return game.getGameMatrix()[0].length - 1;
    }

    private int playersCount(GameEntity game) {
        return game.getGameMatrix().length;
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
        if (selectedPit > playerPitsExcludingBigPits(game)) {
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

    public void capturingStones(GameEntity game, int selectedPit, int stoneCount) {
        int lastStoneIndex = stoneCount + selectedPit;
        int pitsStoneCanTraverse = calculateTotalPitsExcludingBigPits(game) + 1;

        if (shouldCaptureStones(game, selectedPit, lastStoneIndex, pitsStoneCanTraverse)) {
            int capturingStonesCount = Arrays.stream(game.getGameMatrix())
                    .mapToInt(pits -> pits[lastStoneIndex % pitsStoneCanTraverse])
                    .sum();

            game.getGameMatrix()[game.getActivePlayerIndex()][playerPitsExcludingBigPits(game)] += capturingStonesCount;

            Arrays.stream(game.getGameMatrix())
                    .forEach(pits -> pits[lastStoneIndex % pitsStoneCanTraverse] = 0);
        }
    }
    private boolean shouldCaptureStones(GameEntity game, int selectedPit, int lastStoneIndex, int pitsStoneCanTraverse) {
        return lastStoneIndex % pitsStoneCanTraverse < playerPitsExcludingBigPits(game)
                && game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit] == 1;
    }
}

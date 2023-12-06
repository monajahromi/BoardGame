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

        int[][] updatedGameBoard = performMove(game, selectedPit);
        if (shouldCaptureStones(game.getGameMatrix(), selectedPit, game.getActivePlayerIndex())) {
            int lastStoneIndex = getStonesCount(game, selectedPit) + selectedPit;
            int pitsStoneCanTraverse = calculateTotalPitsExcludingBigPits(game) + 1;
            int captureIndex = lastStoneIndex % pitsStoneCanTraverse;
            updatedGameBoard = capturingStones(updatedGameBoard, captureIndex, game.getActivePlayerIndex());

        }

        if (shouldToggleTurn(game.getGameMatrix(), selectedPit,game.getActivePlayerIndex()))
            toggleTurn(game, selectedPit);

        if (hasGameEnded(updatedGameBoard)) {
            game.setStatus(GameStatus.FINISHED);
            updatedGameBoard = onGameEnd(updatedGameBoard);
            game.setWinnerPlayerIndex(findWinningPlayer(updatedGameBoard));
        }
        game.setGameMatrix(updatedGameBoard);

        return game;


    }

    public int[][] capturingStones(int[][] board, int captureIndex, int playerIndex) {
        int[][] updatedGame = deepCopyGameBoard(board);
        int capturingStonesCount = Arrays.stream(board)
                .mapToInt(row -> row[captureIndex])
                .sum();

        updatedGame[playerIndex][board[0].length - 1] += capturingStonesCount;

        Arrays.stream(updatedGame)
                .forEach(row -> row[captureIndex] = 0);
        return updatedGame;
    }

    public boolean shouldCaptureStones(int[][] gameBoard, int selectedPit, int playerIndex) {
        int lastStoneIndex = gameBoard[playerIndex][selectedPit] + selectedPit;
        int pitsStoneCanTraverse = (gameBoard.length * (gameBoard[0].length - 1)) + 1;
        return lastStoneIndex % pitsStoneCanTraverse < (gameBoard[0].length - 1)
                && gameBoard[playerIndex][lastStoneIndex % pitsStoneCanTraverse] == 0;
    }

    @Override
    public int[][] performMove(GameEntity game, int selectedPit) {

        List<int[]> indexesForAddingStones = MancalaIndexGenerator
                .generateIndexes(game.getGameMatrix().length,
                        game.getGameMatrix()[0].length,
                        game.getActivePlayerIndex(),
                        selectedPit,
                        game.getGameMatrix()[game.getActivePlayerIndex()][selectedPit]
                );
        int[][] updatedGame = deepCopyGameBoard(game.getGameMatrix());
        for (int[] item : indexesForAddingStones) {
            updatedGame[item[0]][item[1]] = game.getGameMatrix()[item[0]][item[1]] + 1;
        }
        updatedGame[game.getActivePlayerIndex()][selectedPit] = 0;
        return updatedGame;
    }


    @Override
    public void toggleTurn(GameEntity game, int selectedPit) {
        game.setActivePlayerIndex((game.getActivePlayerIndex() + 1) % 2);
    }

    public boolean shouldToggleTurn(int[][] gameBoard, int selectedPit, int playerIndex) {
        // The condition for a turn switch is met if the remainder of dividing stoneCounts
        // by the total number of pits a stone can traverse in one cycle
        int rowNum = gameBoard.length;
        int colNum = gameBoard[0].length - 1;
        int distanceToBigPit = colNum - selectedPit;
        return gameBoard[playerIndex][selectedPit] % (rowNum * colNum + 1) != distanceToBigPit;
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
    public boolean hasGameEnded(int[][] gameBoard) {
        return Arrays.stream(gameBoard)
                .anyMatch(pits -> IntStream.range(0, pits.length - 1)
                        .allMatch(item -> pits[item] == 0));

    }

    @Override
    public int[][] onGameEnd(int[][] gameBoard) {
        var updatedBoard = deepCopyGameBoard(gameBoard);

        Arrays.stream(updatedBoard).forEach(row -> {
            row[row.length - 1] = Arrays.stream(row).reduce(0, (a, b) -> a + b);
            for (int index = 0; index < row.length - 1; index++) {
                row[index] = 0;
            }
        });
        return updatedBoard;
    }

    @Override
    public int findWinningPlayer(int[][] gameBoard) {
        int[] bigPits = Arrays.stream(gameBoard)
                .mapToInt(pits -> pits[pits.length - 1]).toArray();
        return IntStream.range(0, bigPits.length)
                .reduce((i, j) -> bigPits[i] > bigPits[j] ? i : j)
                .getAsInt();


    }

    public int[][] deepCopyGameBoard(int[][] original) {
        return Arrays.stream(original)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }

}

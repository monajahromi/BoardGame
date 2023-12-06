package com.game.mancala.rule;

import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.rule.mancala.MancalaGamePlayRule;
import com.game.mancala.utils.GameStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayRuleTest {

    static MancalaGamePlayRule playRule;
    static GameEntity game;

    @BeforeAll
    static void setUp() {
        playRule = new MancalaGamePlayRule();
        game = new GameEntity();
    }

    @ParameterizedTest
    @EnumSource(value = GameStatus.class, names = {"FINISHED", "CANCELED"})
    void isPlayableGame_GameInFinishedOrPausedState_ReturnsFalse(GameStatus status) {
        game.setStatus(status);
        assertFalse(playRule.isPlayableGame(game));
    }

    @ParameterizedTest
    @MethodSource("provideGameEntities")
    public void testToggleTurnConditionMet(GameEntity game, int selectedPit, boolean expectedResult) {

        Arrays.stream(game.getGameMatrix()).forEach(item -> System.out.println(Arrays.toString(item)));
        System.out.println("selected Pit : " + selectedPit);
        System.out.println("expectedResult : " + expectedResult);
        System.out.println(playRule.isToggleTurnConditionMet(game, selectedPit));
        assertEquals(expectedResult, playRule.isToggleTurnConditionMet(game, selectedPit));
    }


    public static Stream<Arguments> provideGameEntities() {


        int[][] gameBoardA = {{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        GameEntity game_needsSwitchTurn_firstPlayer_atAnySelectedPit = GameEntity.builder()
                .gameMatrix(gameBoardA)
                .activePlayerIndex(0)
                .build();

        int[][] gameBoardB = {{1, 2, 3, 1, 5, 6, 7}, {1, 5, 3, 4, 2, 6, 7}};
        GameEntity game_DoNOTNeedsSwitchTurn_secondPlayer_AtIndexOneAndForth = GameEntity.builder()
                .gameMatrix(gameBoardB)
                .activePlayerIndex(1)
                .build();

        return Stream.of(Arguments.of(game_needsSwitchTurn_firstPlayer_atAnySelectedPit, 0, true),
                Arguments.of(game_needsSwitchTurn_firstPlayer_atAnySelectedPit, 1, true),
                Arguments.of(game_needsSwitchTurn_firstPlayer_atAnySelectedPit, 2, true),
                Arguments.of(game_needsSwitchTurn_firstPlayer_atAnySelectedPit, 3, true),
                Arguments.of(game_needsSwitchTurn_firstPlayer_atAnySelectedPit, 4, true),
                Arguments.of(game_needsSwitchTurn_firstPlayer_atAnySelectedPit, 5, true),
                Arguments.of(game_DoNOTNeedsSwitchTurn_secondPlayer_AtIndexOneAndForth, 1, false),
                Arguments.of(game_DoNOTNeedsSwitchTurn_secondPlayer_AtIndexOneAndForth, 4, false));


    }


    @ParameterizedTest
    @MethodSource("provideGameEntitiesForMove")
    void testPerformMove(GameEntity initialGame, int selectedPit, int[][] expectedBoard) {
        int[][] result = playRule.performMove(initialGame, selectedPit);
        assertArrayEquals(expectedBoard, result);
    }

    static Stream<Arguments> provideGameEntitiesForMove() {

        int[][] afterMoving_indexZero = {{0, 3, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexOne = {{1, 0, 4, 4, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexTwo = {{1, 2, 0, 4, 6, 7, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexTree = {{1, 2, 3, 0, 6, 7, 8}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexFour = {{1, 2, 3, 3, 0, 7, 8}, {1, 2, 3, 5, 6, 7, 7}};
        int[][] afterMoving_indexFive = {{1, 2, 3, 3, 5, 0, 8}, {1, 3, 4, 5, 6, 7, 7}};

        return Stream.of(Arguments.of(buildGameEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 0, afterMoving_indexZero),
                Arguments.of(buildGameEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 1, afterMoving_indexOne),
                Arguments.of(buildGameEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 2, afterMoving_indexTwo),
                Arguments.of(buildGameEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 3, afterMoving_indexTree),
                Arguments.of(buildGameEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 4, afterMoving_indexFour),
                Arguments.of(buildGameEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 5, afterMoving_indexFive))
                ;

    }

    private static GameEntity buildGameEntity(int[][] gameMatrix) {
        return GameEntity.builder()
                .gameMatrix(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}})
                .activePlayerIndex(0)
                .build();
    }


    private void printMatrix(int[][] matrix) {
        Arrays.stream(matrix).forEach(item -> System.out.println(Arrays.toString(item)));
    }


    @ParameterizedTest
    @MethodSource("shouldCaptureStones_data")
    public void testShouldCaptureStones(int[][] gameBoard, int selectedPit, int playerIndex, boolean expectedResult) {
        assertEquals(expectedResult, playRule.shouldCaptureStones(gameBoard, selectedPit, playerIndex));
    }


    static Stream<Arguments> shouldCaptureStones_data() {

        int[][] gameBoard = {{0, 3, 3, 3, 5, 0, 0}, {0, 2, 3, 4, 5, 8, 7}};
        return Stream.of(Arguments.of(gameBoard, 1, 0, false),
                Arguments.of(gameBoard, 2, 0, true),
                Arguments.of(gameBoard, 5, 1, true),
                Arguments.of(gameBoard, 1, 1, false),
                Arguments.of(gameBoard, 2, 1, false),
                Arguments.of(gameBoard, 3, 1, false)

        );
    }

    @ParameterizedTest
    @MethodSource("captureStones_data")
    void testCapturingStones(int[][] gameBoard, int captureIndex, int playerIndex, int[][] expectedResult) {
        assertArrayEquals(expectedResult, playRule.capturingStones(gameBoard, captureIndex, playerIndex));
    }

    static Stream<Arguments> captureStones_data() {

        var baseBoard = new int[][]{{2, 3, 1, 9, 5, 0, 4}, {1, 2, 3, 4, 3, 8, 7}};

        var afterCapturingIndexZeroPlayerZero =
                new int[][]{{0, 3, 1, 9, 5, 0, 7}, {0, 2, 3, 4, 3, 8, 7}};
        var afterCapturingIndexFourPlayerOne =
                new int[][]{{2, 3, 1, 9, 0, 0, 4}, {1, 2, 3, 4, 0, 8, 15}};
        var afterCapturingIndexFivePlayerOne =
                new int[][]{{2, 3, 1, 9, 5, 0, 4}, {1, 2, 3, 4, 3, 0, 15}};

        return Stream.of(Arguments.of(baseBoard, 0, 0, afterCapturingIndexZeroPlayerZero),
                Arguments.of(baseBoard, 4, 1, afterCapturingIndexFourPlayerOne),
                Arguments.of(baseBoard, 5, 1, afterCapturingIndexFivePlayerOne)
        );


    }


}


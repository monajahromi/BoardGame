package com.game.rule;

import com.game.model.entity.GameEntity;
import com.game.model.entity.Player;
import com.game.rule.mancala.MancalaGamePlayRule;
import com.game.utils.GameStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MancalaPlayRuleTest {

    static MancalaGamePlayRule playRule;

    @BeforeAll
    static void setUp() {
        playRule = new MancalaGamePlayRule();
    }

    @ParameterizedTest
    @MethodSource("play_data")
    public void testPlay(GameEntity game, String playerName, int selectedPit, GameEntity expectedResult) {
        GameEntity actualResult = playRule.play(game, playerName, selectedPit);
        assertEquals(expectedResult.getActivePlayerIndex(), actualResult.getActivePlayerIndex());
        assertEquals(expectedResult.getWinnerPlayerIndex(), actualResult.getWinnerPlayerIndex());
        assertArrayEquals(expectedResult.getGameMatrix(), actualResult.getGameMatrix());
        Assertions.assertEquals(expectedResult.getStatus().toString(), actualResult.getStatus().toString());
        assertEquals(expectedResult.getId(), actualResult.getId());
    }

    public static Stream<Arguments> play_data() {

        int[][] board_case_one = {{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        GameEntity game_board_case_one = GameEntity.builder()
                .gameMatrix(board_case_one)
                .players(List.of(Player.builder().name("mona").build(),
                        Player.builder().name("hana").build()))
                .activePlayerIndex(0)
                .status(GameStatus.PLAYING)
                .build();

        int[][] played_board_case_one = {{1, 2, 3, 0, 6, 7, 8}, {1, 2, 3, 4, 5, 6, 7}};
        GameEntity played_game_board_case_one = GameEntity.builder()
                .gameMatrix(played_board_case_one)
                .players(List.of(Player.builder().name("mona").build(),
                        Player.builder().name("hana").build()))
                .activePlayerIndex(0)
                .status(GameStatus.PLAYING)
                .build();


        int[][] board_case_two = {{0, 1, 0, 0, 0, 0, 7}, {1, 2, 3, 4, 5, 6, 7}};
        GameEntity game_board_case_two = GameEntity.builder()
                .gameMatrix(board_case_two)
                .players(List.of(Player.builder().name("mona").build(),
                        Player.builder().name("hana").build()))
                .activePlayerIndex(0)
                .status(GameStatus.PLAYING)
                .build();

        int[][] played_board_case_two = {{0, 0, 0, 0, 0, 0, 11}, {0, 0, 0, 0, 0, 0, 25}};
        GameEntity played_game_board_case_two = GameEntity.builder()
                .gameMatrix(played_board_case_two)
                .players(List.of(Player.builder().name("mona").build(),
                        Player.builder().name("hana").build()))
                .activePlayerIndex(1)
                .winnerPlayerIndex(1)
                .status(GameStatus.FINISHED)
                .build();

        return Stream.of(
                Arguments.of(game_board_case_one, "mona", 3, played_game_board_case_one),
                Arguments.of(game_board_case_two, "mona", 1, played_game_board_case_two)
        );


    }


    @ParameterizedTest
    @MethodSource("performMove_data")
    void testPerformMove(GameEntity initialGame, int selectedPit, int[][] expectedBoard) {
        int[][] result = playRule.performMove(initialGame, selectedPit);
        assertArrayEquals(expectedBoard, result);
    }

    static Stream<Arguments> performMove_data() {

        int[][] afterMoving_indexZero = {{0, 3, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexOne = {{1, 0, 4, 4, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexTwo = {{1, 2, 0, 4, 6, 7, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexTree = {{1, 2, 3, 0, 6, 7, 8}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] afterMoving_indexFour = {{1, 2, 3, 3, 0, 7, 8}, {1, 2, 3, 5, 6, 7, 7}};
        int[][] afterMoving_indexFive = {{1, 2, 3, 3, 5, 0, 8}, {1, 3, 4, 5, 6, 7, 7}};

        return Stream.of(Arguments.of(performMove_dataEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 0, afterMoving_indexZero),
                Arguments.of(performMove_dataEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 1, afterMoving_indexOne),
                Arguments.of(performMove_dataEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 2, afterMoving_indexTwo),
                Arguments.of(performMove_dataEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 3, afterMoving_indexTree),
                Arguments.of(performMove_dataEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 4, afterMoving_indexFour),
                Arguments.of(performMove_dataEntity(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}}), 5, afterMoving_indexFive))
                ;

    }

    private static GameEntity performMove_dataEntity(int[][] gameMatrix) {
        return GameEntity.builder()
                .gameMatrix(new int[][]{{1, 2, 3, 3, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}})
                .activePlayerIndex(0)
                .build();
    }


    private void printMatrix(int[][] matrix) {
        Arrays.stream(matrix).forEach(item -> System.out.println(Arrays.toString(item)));
    }

    @ParameterizedTest
    @MethodSource("shouldToggleTurn_data")
    public void testShouldToggleTurn(int[][] gameBoard, int selectedPit, int playerIndex, boolean expectedResult) {
        assertEquals(expectedResult, playRule.shouldToggleTurn(gameBoard, selectedPit, playerIndex));
    }

    static Stream<Arguments> shouldToggleTurn_data() {

        int[][] needToggle_AnyIndex_PlayerIndexZero = {{1, 2, 3, 4, 5, 6, 7}, {1, 2, 3, 4, 5, 6, 7}};
        int[][] NoToggle_IndexOneAndFour_playerIndexOne = {{1, 2, 3, 1, 5, 6, 7}, {1, 5, 3, 4, 2, 6, 7}};
        return Stream.of(
                Arguments.of(needToggle_AnyIndex_PlayerIndexZero, 0, 0, true),
                Arguments.of(needToggle_AnyIndex_PlayerIndexZero, 1, 0, true),
                Arguments.of(needToggle_AnyIndex_PlayerIndexZero, 2, 0, true),
                Arguments.of(needToggle_AnyIndex_PlayerIndexZero, 3, 0, true),
                Arguments.of(needToggle_AnyIndex_PlayerIndexZero, 4, 0, true),
                Arguments.of(needToggle_AnyIndex_PlayerIndexZero, 5, 0, true),
                Arguments.of(NoToggle_IndexOneAndFour_playerIndexOne, 1, 1, false),
                Arguments.of(NoToggle_IndexOneAndFour_playerIndexOne, 4, 1, false)
        );
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

    @ParameterizedTest
    @MethodSource("shouldGameEnd_data")
    void testShouldGameEnd(int[][] gameBoard, boolean expectedResult) {
        assertEquals(expectedResult, playRule.shouldGameEnd(gameBoard));
    }

    static Stream<Arguments> shouldGameEnd_data() {

        var endedBoard_case_one = new int[][]{{0, 0, 0, 0, 0, 0, 4}, {1, 2, 3, 4, 3, 8, 7}};
        var notEndedBoard = new int[][]{{1, 0, 0, 0, 0, 0, 4}, {1, 2, 3, 4, 3, 8, 7}};
        var endedBoard_case_two = new int[][]{{1, 0, 0, 3, 1, 0, 4}, {0, 0, 0, 0, 0, 0, 7}};

        return Stream.of(Arguments.of(endedBoard_case_one, true),
                Arguments.of(notEndedBoard, false),
                Arguments.of(endedBoard_case_two, true)
        );
    }


    @ParameterizedTest
    @MethodSource("onGameEnd_data")
    void testOnGameEnd(int[][] gameBoard, int[][] expectedResult) {
        assertArrayEquals(expectedResult, playRule.onGameEnd(gameBoard));
    }

    static Stream<Arguments> onGameEnd_data() {

        var gameBoard = new int[][]{{5, 0, 1, 6, 2, 0, 4}, {1, 2, 3, 4, 3, 8, 7}};
        var collect_gameBoard_to_bigPit = new int[][]{{0, 0, 0, 0, 0, 0, 18}, {0, 0, 0, 0, 0, 0, 28}};

        return Stream.of(Arguments.of(gameBoard, collect_gameBoard_to_bigPit));
    }

    @ParameterizedTest
    @MethodSource("findWinningPlayer_data")
    void testFindWinningPlayer(int[][] gameBoard, int expectedResult) {
        assertEquals(expectedResult, playRule.findWinningPlayer(gameBoard));
    }

    static Stream<Arguments> findWinningPlayer_data() {

        var gameBoard_winner_playerOne = new int[][]{{0, 0, 1, 0, 6, 0, 4}, {1, 2, 3, 4, 3, 8, 7}};
        var gameBoard_winner_playerZero = new int[][]{{0, 0, 1, 0, 6, 0, 14}, {1, 2, 3, 4, 3, 8, 7}};

        return Stream.of(Arguments.of(gameBoard_winner_playerOne, 1),
                Arguments.of(gameBoard_winner_playerZero, 0));
    }

}


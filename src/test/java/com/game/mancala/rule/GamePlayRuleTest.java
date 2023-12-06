package com.game.mancala.rule;

import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.rule.mancala.MancalaGamePlayRule;
import com.game.mancala.utils.GameStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
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


}
package com.game.service;

import com.game.exception.NotFoundException;
import com.game.model.dto.PlayDto;
import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.model.entity.Player;
import com.game.repository.GameRepository;
import com.game.rule.GameCreateRule;
import com.game.rule.GamePlayRule;
import com.game.utils.GameStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GameServiceTest {

    private static final long GAME_ID = 12;
    private static final String GAME_NAME = "Mancala";
    private static final String[] PLAYER_NAMES = new String[]{"Hana", "Mona"};
    private static final int[][] GAME_ENTITY_BOARD = new int[][]{{6, 6, 6, 6, 6, 6, 0}, {6, 6, 6, 6, 6, 6, 0}};
    private static final String ACTIVE_PLAYER_NAME = "Hana";
    private static final int SELECTED_PIT = 2;
    @Mock
    private GameRepository gameRepositoryMock;
    @Mock
    private GamePlayRule playRuleMock;
    @Mock
    private GameCreateRule createRuleMock;
    @InjectMocks
    private GameService gameService;

    @DisplayName("Create Game")
    @Test
    void test_Create() {
        GameEntity gameEntity = getGameEntity();
        when(createRuleMock.setupNewGame(any(StartDto.class))).thenReturn(gameEntity);
        when(gameRepositoryMock.save(any(GameEntity.class))).thenReturn(gameEntity);

        GameEntity actualGameEntity = gameService.create(getStartDto());

        //
        verify(gameRepositoryMock, times(1)).save(any(GameEntity.class));
        verify(createRuleMock, times(1)).setupNewGame(any(StartDto.class));
        verify(createRuleMock, times(1)).setInitialPlayerTurn(any(GameEntity.class));

        assertNotNull(actualGameEntity);
        assertEquals(gameEntity.getId(), actualGameEntity.getId());
        assertEquals(gameEntity.getName(), actualGameEntity.getName());
        assertEquals(gameEntity.getStatus(), actualGameEntity.getStatus());
        assertArrayEquals(gameEntity.getGameMatrix(), actualGameEntity.getGameMatrix());

    }


    @DisplayName("Play Game - Success Scenario")
    @Test
    void test_When_Play_Success() {
        GameEntity gameEntity = getGameEntity();
        when(gameRepositoryMock.findById(anyLong())).thenReturn(Optional.of(gameEntity));
        when(gameRepositoryMock.save(any(GameEntity.class))).thenReturn(gameEntity);
        when(playRuleMock.play(any(GameEntity.class), any(String.class), anyInt())).thenReturn(gameEntity);

        GameEntity actualGameEntity = gameService.play(getPlayDto());

        verify(gameRepositoryMock, times(1)).findById(anyLong());
        verify(gameRepositoryMock, times(1)).save(any(GameEntity.class));
        verify(playRuleMock, times(1)).play(any(GameEntity.class), any(String.class), anyInt());

        assertNotNull(actualGameEntity);
        assertEquals(gameEntity.getId(), actualGameEntity.getId());
        assertEquals(gameEntity.getName(), actualGameEntity.getName());
        assertEquals(gameEntity.getStatus(), actualGameEntity.getStatus());
        assertArrayEquals(gameEntity.getGameMatrix(), actualGameEntity.getGameMatrix());

    }

    @DisplayName("Play Game - Failure Scenario")
    @Test
    void test_When_Play_Not_Found_Game() {
        when(gameRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> gameService.play(getPlayDto()));
        assertEquals("game not found!", notFoundException.getMessage());
        verify(gameRepositoryMock, times(1)).findById(anyLong());

    }


    @DisplayName("Cancel Game - Success Scenario")
    @Test
    void test_When_Cancel_Success() {
        GameEntity spyGameEntity = spy(getGameEntity());
        when(gameRepositoryMock.findById(anyLong())).thenReturn(Optional.of(spyGameEntity));
        when(gameRepositoryMock.save(any(GameEntity.class))).thenReturn(spyGameEntity);

        GameEntity actualGameEntity = gameService.cancel(GAME_ID);
        ArgumentCaptor<GameEntity> gameEntityArgumentCaptor = ArgumentCaptor.forClass(GameEntity.class);

        verify(gameRepositoryMock, times(1)).findById(anyLong());
        verify(gameRepositoryMock, times(1)).save(any(GameEntity.class));
        verify(spyGameEntity, times(1)).setStatus(GameStatus.CANCELED);

        assertNotNull(actualGameEntity);
        assertEquals(spyGameEntity.getId(), actualGameEntity.getId());
        assertEquals(spyGameEntity.getName(), actualGameEntity.getName());
        assertArrayEquals(spyGameEntity.getGameMatrix(), actualGameEntity.getGameMatrix());

    }

    @DisplayName("Cancel Game - Failed Scenario")
    @Test
    void test_When_Cancel_Not_Found_Game() {
        when(gameRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException notFoundException =
                assertThrows(NotFoundException.class, () -> gameService.cancel(GAME_ID));
        assertEquals("game not found!", notFoundException.getMessage());
        verify(gameRepositoryMock, times(1)).findById(anyLong());

    }

    private GameEntity getGameEntity() {
        return GameEntity.builder()
                .id(GAME_ID)
                .name(GAME_NAME)
                .status(GameStatus.PLAYING)
                .activePlayerIndex(0)
                .players(List.of(Player.builder()
                                .name(PLAYER_NAMES[0])
                                .build(),
                        Player.builder()
                                .name(PLAYER_NAMES[1])
                                .build()
                )).gameMatrix(GAME_ENTITY_BOARD)
                .build();
    }

    private StartDto getStartDto() {
        return StartDto.builder()
                .playerNames(PLAYER_NAMES)
                .build();
    }

    private PlayDto getPlayDto() {
        return PlayDto.builder()
                .selectedPit(SELECTED_PIT)
                .gameId(GAME_ID)
                .playerName(ACTIVE_PLAYER_NAME)
                .build();
    }
}
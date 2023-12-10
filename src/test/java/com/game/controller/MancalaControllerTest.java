package com.game.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.exception.NotFoundException;
import com.game.model.dto.PlayDto;
import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.model.entity.Player;
import com.game.service.GameService;
import com.game.utils.GameStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = MancalaController.class)
public class MancalaControllerTest {
    private static final long GAME_ID = 12;
    private static final String GAME_NAME = "Mancala";
    private static final String[] PLAYER_NAMES = new String[]{"Hana", "Mona"};
    private static final int[][] GAME_ENTITY_BOARD = new int[][]{{6, 6, 6, 6, 6, 6, 0}, {6, 6, 6, 6, 6, 6, 0}};
    private static final String ACTIVE_PLAYER_NAME = "Hana";
    private static final int SELECTED_PIT = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Create Game")
    @Test
    void test_Create() throws Exception {
        GameEntity gameEntity = getGameEntity();
        when(gameServiceMock.create(any(StartDto.class))).thenReturn(gameEntity);

        MvcResult mvcResult = mockMvc.perform(post("/mancala/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getStartDto())))
                .andExpect(status().isCreated())
                .andReturn();

        GameEntity gameEntityResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), GameEntity.class);
        assertNotNull(gameEntityResponse);
        assertEquals(gameEntity.getId(), gameEntityResponse.getId());
        assertEquals(gameEntity.getName(), gameEntityResponse.getName());
        assertEquals(gameEntity.getStatus(), gameEntityResponse.getStatus());
        assertArrayEquals(gameEntity.getGameMatrix(), gameEntityResponse.getGameMatrix());

    }

    @DisplayName("Play Game - Success Scenario")
    @Test
    void test_When_Play_Success() throws Exception {
        GameEntity gameEntity = getGameEntity();
        when(gameServiceMock.play(any(PlayDto.class))).thenReturn(gameEntity);

        MvcResult mvcResult = mockMvc.perform(put("/mancala/play")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getPlayDto())))
                .andExpect(status().isOk())
                .andReturn();

        GameEntity gameEntityResponse = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), GameEntity.class);
        assertNotNull(gameEntityResponse);
        assertEquals(gameEntity.getId(), gameEntityResponse.getId());
        assertEquals(gameEntity.getName(), gameEntityResponse.getName());
        assertEquals(gameEntity.getStatus(), gameEntityResponse.getStatus());
        assertArrayEquals(gameEntity.getGameMatrix(), gameEntityResponse.getGameMatrix());

    }

    @DisplayName("Play Game - Failure Scenario")
    @Test
    void test_When_Play_NotFound() throws Exception {
        NotFoundException notFoundException = new NotFoundException("game not found!");
        when(gameServiceMock.play(any(PlayDto.class))).thenThrow(notFoundException);

        MvcResult mvcResult = mockMvc.perform(put("/mancala/play")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getPlayDto())))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("game not found!", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());

    }

    @DisplayName("Cancel Game - Success Scenario")
    @Test
    void test_When_Cancel_Success() throws Exception {

        MvcResult mvcResult = mockMvc.perform(delete("/mancala/{gameId}", GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Game canceled successfully", mvcResult.getResponse().getContentAsString());
    }

    @DisplayName("Cancel Game - Failure Scenario")
    @Test
    void test_When_Cancel_Not_Found() throws Exception {
        NotFoundException notFoundException = new NotFoundException("game not found!");
        when(gameServiceMock.cancel(anyLong())).thenThrow(notFoundException);
        MvcResult mvcResult = mockMvc.perform(delete("/mancala/{gameId}", GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("game not found!", Objects.requireNonNull(mvcResult.getResolvedException()).getMessage());
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
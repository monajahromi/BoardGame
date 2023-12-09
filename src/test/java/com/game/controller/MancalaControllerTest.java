package com.game.controller;

import com.game.exception.NotFoundException;
import com.game.model.dto.PlayDto;
import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.service.GameService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MancalaControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private MancalaController mancalaController;

    @Test
    @Ignore
    public void testCreate() {
        // Arrange
        StartDto startDto = new StartDto();
        GameEntity gameEntity = new GameEntity();
        when(gameService.create(startDto)).thenReturn(gameEntity);

        // Act
        ResponseEntity<GameEntity> responseEntity = mancalaController.create(startDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(gameEntity, responseEntity.getBody());
        verify(gameService, times(1)).create(startDto);
    }

    @Test
    @Ignore
    public void testPlay() throws NotFoundException {
        // Arrange
        PlayDto playDto = new PlayDto();
        GameEntity gameEntity = new GameEntity();
        when(gameService.play(playDto)).thenReturn(gameEntity);

        // Act
        ResponseEntity<GameEntity> responseEntity = mancalaController.play(playDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(gameEntity, responseEntity.getBody());
        verify(gameService, times(1)).play(playDto);
    }

    @Test
    @Ignore
    public void testCancel() throws NotFoundException {
        // Arrange
        PlayDto playDto = new PlayDto();
        GameEntity gameEntity = new GameEntity();
        when(gameService.cancel(playDto)).thenReturn(gameEntity);

        // Act
        ResponseEntity<GameEntity> responseEntity = mancalaController.cancel(playDto);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(gameEntity, responseEntity.getBody());
        verify(gameService, times(1)).cancel(playDto);
    }
}

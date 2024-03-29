package com.game.controller;

import com.game.model.dto.PlayDto;
import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.service.GameService;
import com.game.service.MancalaGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mancala")
@Validated
public class MancalaController {
    private final MancalaGameService gameService;

    public MancalaController(MancalaGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameEntity> create(@Validated @RequestBody StartDto dto) {
        return new ResponseEntity<>(gameService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/play")
    public ResponseEntity<GameEntity> play(@Validated @RequestBody PlayDto dto) {
        return new ResponseEntity<>(gameService.play(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<String> cancel(@PathVariable Long gameId) {
        gameService.cancel(gameId);
        return new ResponseEntity<>("Game canceled successfully", HttpStatus.OK);
    }


}

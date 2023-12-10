package com.game.controller;

import com.game.model.dto.PlayDto;
import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mancala")
@Validated
public class MancalaController {
    private final GameService gameService;

    public MancalaController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    @CrossOrigin(origins = "*")
    public ResponseEntity<GameEntity> create(@Validated @RequestBody StartDto dto) {
        return new ResponseEntity<>(gameService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/play")
    @CrossOrigin(origins = "*")
    public ResponseEntity<GameEntity> play(@Validated @RequestBody PlayDto dto) {
        return new ResponseEntity<>(gameService.play(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{gameId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> cancel(@PathVariable Long gameId) {
        gameService.cancel(gameId);
        return new ResponseEntity<>("Game canceled successfully", HttpStatus.OK);
    }


}

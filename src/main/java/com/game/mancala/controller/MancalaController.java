package com.game.mancala.controller;

import com.game.mancala.exception.NotFoundException;
import com.game.mancala.model.dto.PlayDto;
import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.service.GameService;
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

    @PostMapping("start")
    @CrossOrigin(origins = "*")
    public ResponseEntity<GameEntity> create(@RequestBody StartDto dto) {
        return new ResponseEntity<>(gameService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("play")
    @CrossOrigin(origins = "*")
    public ResponseEntity<GameEntity> play(@RequestBody PlayDto dto) throws NotFoundException {
        return new ResponseEntity<>(gameService.play(dto), HttpStatus.CREATED);
    }

    @PutMapping("cancel")
    @CrossOrigin(origins = "*")
    public ResponseEntity<GameEntity> cancel(@RequestBody PlayDto dto) throws NotFoundException {
        return new ResponseEntity<>(gameService.cancel(dto), HttpStatus.CREATED);
    }
}

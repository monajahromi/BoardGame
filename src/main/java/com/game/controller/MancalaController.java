package com.game.controller;

import com.game.exception.NotFoundException;
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

    @PostMapping("start")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<GameEntity> create(@RequestBody StartDto dto) {
        return new ResponseEntity<>(gameService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("play")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<GameEntity> play(@RequestBody PlayDto dto) throws NotFoundException {
        return new ResponseEntity<>(gameService.play(dto), HttpStatus.CREATED);
    }

    @PutMapping("cancel")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<GameEntity> cancel(@RequestBody PlayDto dto) throws NotFoundException {
        return new ResponseEntity<>(gameService.cancel(dto), HttpStatus.CREATED);
    }
}

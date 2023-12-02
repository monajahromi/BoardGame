package com.game.mancala.controller;

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
    private final GameService gameService ;

    public MancalaController(GameService gameService) {
        this.gameService = gameService;
    }


    @PostMapping("start")
    public ResponseEntity<GameEntity> create(@RequestBody StartDto game) {
        return new ResponseEntity<>(gameService.create(game), HttpStatus.CREATED);
    }

    @PutMapping("play")
    public ResponseEntity<GameEntity> play(@RequestBody GameEntity game)  {
        return new ResponseEntity<>(gameService.play(game), HttpStatus.CREATED);
    }

}

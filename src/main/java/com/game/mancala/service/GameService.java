package com.game.mancala.service;

import com.game.mancala.model.entity.GameEntity;
import org.springframework.stereotype.Service;
import com.game.mancala.repository.GameRepository;


@Service
public class GameService {
    private final GameRepository repository;


    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public GameEntity create(GameEntity game) {
        return repository.save(game);
    }

    public GameEntity play(GameEntity game)  {
        return repository.save(game);
    }
}

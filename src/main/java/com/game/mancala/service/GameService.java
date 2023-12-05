package com.game.mancala.service;

import com.game.mancala.exception.NotFoundException;
import com.game.mancala.model.dto.PlayDto;
import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.rule.GameCreateRule;
import com.game.mancala.rule.GamePlayRule;
import com.game.mancala.utils.GameStatus;
import org.springframework.stereotype.Service;
import com.game.mancala.repository.GameRepository;


@Service
public class GameService {
    private final GameRepository repository;
    private final GamePlayRule playRule;
    private final GameCreateRule createRule;

    public GameService(GameRepository repository, GamePlayRule playRule, GameCreateRule createRule) {
        this.repository = repository;
        this.playRule = playRule;
        this.createRule = createRule;
    }

    public GameEntity create(StartDto dto) {
        GameEntity game = createRule.setupNewGame(dto);
        createRule.setInitialPlayerTurn(game);
        repository.save(game);
        return game;

    }

    public GameEntity play(PlayDto dto) throws NotFoundException {
        GameEntity game = repository.findById(dto.getGameId()).orElseThrow(
                () -> new NotFoundException("game not found!")
        );

        return repository.save(playRule.play(game, dto.getPlayerName(), dto.getSelectedPit()));
    }
    public GameEntity cancel(PlayDto dto) throws NotFoundException {
        GameEntity game = repository.findById(dto.getGameId()).orElseThrow(
                () -> new NotFoundException("game not found!")
        );
        game.setStatus(GameStatus.CANCELED);

        return repository.save(game);
    }
}

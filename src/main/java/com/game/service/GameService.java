package com.game.service;

import com.game.exception.NotFoundException;
import com.game.model.dto.PlayDto;
import com.game.model.dto.StartDto;
import com.game.model.entity.GameEntity;
import com.game.repository.GameRepository;
import com.game.rule.GameCreateRule;
import com.game.rule.GamePlayRule;
import com.game.utils.GameStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
@Transactional
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

    public GameEntity play(PlayDto dto) {
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

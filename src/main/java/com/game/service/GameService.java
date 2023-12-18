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
import lombok.AllArgsConstructor;

@Transactional
@AllArgsConstructor
public class GameService <PR extends GamePlayRule, CR extends GameCreateRule >{
    private final GameRepository repository;
    private final PR playRule;
    private final CR createRule;

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

    public GameEntity cancel(Long gameId) throws NotFoundException {
        GameEntity game = repository.findById(gameId).orElseThrow(
                () -> new NotFoundException("game not found!")
        );
        game.setStatus(GameStatus.CANCELED);
        return repository.save(game);
    }
}

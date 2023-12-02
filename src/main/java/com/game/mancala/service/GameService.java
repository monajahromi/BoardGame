package com.game.mancala.service;

import com.game.mancala.exception.NotFoundException;
import com.game.mancala.model.dto.PlayDto;
import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.model.entity.Player;
import com.game.mancala.rule.GameCreateRule;
import com.game.mancala.rule.GamePlayRule;
import com.game.mancala.utils.GameStatus;
import org.springframework.stereotype.Service;
import com.game.mancala.repository.GameRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        List<Player> players = new ArrayList<>();


        Arrays.stream(dto.getPlayerNames())
                .limit(createRule.maximumAllowedPlayer())
                 .forEach(item -> {
                    players.add(Player.builder()
                            .name(item)
                            .build());
                });

        GameEntity game = GameEntity.builder()
                .players(players)
                .status(GameStatus.PLAYING)
                .build();

        createRule.initializeActivePlayer(game);
        createRule.initializePits(game,dto);

        return repository.save(game);
    }

    public GameEntity play(PlayDto dto) throws NotFoundException {
        GameEntity game = repository.findById(dto.getGameId()).orElseThrow(
                () -> new NotFoundException("game not found!")
        );
        //check if write player is playing!
        if (!playRule.getActivePlayer(game)
                .getName().toLowerCase()
                .trim()
                .equals(dto.getPlayerName())) {
            throw new IllegalArgumentException("It's not " + dto.getPlayerName() + " turn!");
        }

        return repository.save(playRule.play(game, dto.getSelectedPit()));
    }
}

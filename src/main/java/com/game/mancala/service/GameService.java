package com.game.mancala.service;

import com.game.mancala.model.dto.StartDto;
import com.game.mancala.model.entity.GameEntity;
import com.game.mancala.model.entity.Player;
import com.game.mancala.utils.GameStatus;
import org.springframework.stereotype.Service;
import com.game.mancala.repository.GameRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class GameService {
    private final GameRepository repository;


    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public GameEntity create(StartDto dto) {
        List<Player> players = new ArrayList<>();


        Arrays.stream(dto.getPlayerNames())
                 .forEach(item -> {
                    players.add(Player.builder()
                            .name(item)
                            .build());
                });

        GameEntity game = GameEntity.builder()
                .players(players)
                .status(GameStatus.PLAYING)
                .build();

        game.getPlayers().get(0).setActive(true);
        game.getPlayers().forEach(item->{
            item.setPits(Arrays.stream(new int[dto.getPitsCount()]).map(i -> dto.getStonesPerPit()).toArray());
        });

        return repository.save(game);
    }

    public GameEntity play(GameEntity game)  {
        return repository.save(game);
    }
}

package com.game.service;

import com.game.repository.GameRepository;
import com.game.rule.mancala.MancalaGamePlayRule;
import com.game.rule.mancala.MancalaGameCreateRule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MancalaGameService extends GameService<MancalaGamePlayRule, MancalaGameCreateRule> {
    public MancalaGameService(GameRepository repository,
                              MancalaGamePlayRule playRule,
                              MancalaGameCreateRule createRule) {
        super(repository, playRule, createRule);
    }

}

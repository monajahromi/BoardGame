package com.game.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("game.mancala")
@Data
public class MancalaConfig {

    private int default_player_pits_count;
    private int default_stones_per_pit;
    private String game_name;

}

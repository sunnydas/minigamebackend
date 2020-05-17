package com.sunny.minigame.score.backend.datastore.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Setter @Getter
public class UserScoreDAO {

    private int userId;

    public UserScoreDAO(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    private int score;

    private volatile boolean newBorn = true;

}

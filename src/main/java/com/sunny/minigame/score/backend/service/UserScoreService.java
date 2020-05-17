package com.sunny.minigame.score.backend.service;

import com.sunny.minigame.score.backend.datastore.UserScoreDS;
import com.sunny.minigame.score.backend.domain.UserPointsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserScoreService {

    @Autowired
    private UserScoreDS userScoreDS;

    public void addPointsToUser(UserPointsDTO userPointsDTO){
        userScoreDS.addUserPoints(userPointsDTO);
    }
}

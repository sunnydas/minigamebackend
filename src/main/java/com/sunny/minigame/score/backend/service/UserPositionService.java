package com.sunny.minigame.score.backend.service;

import com.sunny.minigame.score.backend.datastore.UserScoreDS;
import com.sunny.minigame.score.backend.datastore.exception.UserNotFoundException;
import com.sunny.minigame.score.backend.domain.HighScoresDTO;
import com.sunny.minigame.score.backend.domain.UserStandingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPositionService {

    private static final int RESULTS_LIMIT= 20000;

    @Autowired
    private UserScoreDS userScoreDS;

    public UserStandingDTO getUserStanding(int userId)throws UserNotFoundException {
        return userScoreDS.getUserStanding(userId);
    }

    public HighScoresDTO getHighScores(){
        List<UserStandingDTO> userStandingDTOList =  userScoreDS.getSortedUsersStandingDTO(RESULTS_LIMIT
        );
        return new HighScoresDTO(userStandingDTOList);
    }

}

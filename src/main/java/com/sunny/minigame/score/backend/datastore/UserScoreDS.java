package com.sunny.minigame.score.backend.datastore;

import com.sunny.minigame.score.backend.datastore.exception.UserNotFoundException;
import com.sunny.minigame.score.backend.domain.UserPointsDTO;
import com.sunny.minigame.score.backend.domain.UserStandingDTO;

import java.util.List;

public interface UserScoreDS {

    public void addUserPoints(UserPointsDTO userPointsDTO);

    public UserStandingDTO getUserStanding(int userId) throws UserNotFoundException;

    public List<UserStandingDTO> getSortedUsersStandingDTO(int limit);

}

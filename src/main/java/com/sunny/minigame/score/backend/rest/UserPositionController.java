package com.sunny.minigame.score.backend.rest;

import com.sunny.minigame.score.backend.datastore.exception.UserNotFoundException;
import com.sunny.minigame.score.backend.domain.HighScoresDTO;
import com.sunny.minigame.score.backend.domain.UserStandingDTO;
import com.sunny.minigame.score.backend.service.UserPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserPositionController {

    @Autowired
    UserPositionService userPositionService;

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/{userId}/position"
    )
    public UserStandingDTO getUserPosition(@PathVariable(value = "userId")
                                                       Integer userId){
        try {
            return userPositionService.getUserStanding(userId);
        }catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }


    @RequestMapping(
            method = RequestMethod.GET,
            path = "/highscorelist"
    )
    public HighScoresDTO getHighestScores(){
        try {
            return userPositionService.getHighScores();
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

}
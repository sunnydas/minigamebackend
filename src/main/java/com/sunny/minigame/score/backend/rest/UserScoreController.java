package com.sunny.minigame.score.backend.rest;

import com.sunny.minigame.score.backend.domain.UserPointsDTO;
import com.sunny.minigame.score.backend.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserScoreController {

    @Autowired
    UserScoreService userScoreService;

    @RequestMapping(
            path = "/score",
            method = RequestMethod.POST
    )
    public void addPoints(@RequestBody UserPointsDTO userPointsPayload){
        userScoreService.addPointsToUser(userPointsPayload);
    }

}

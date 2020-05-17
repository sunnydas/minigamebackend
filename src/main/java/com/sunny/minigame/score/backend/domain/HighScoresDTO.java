package com.sunny.minigame.score.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class HighScoresDTO {
    private List<UserStandingDTO> highscores;

}

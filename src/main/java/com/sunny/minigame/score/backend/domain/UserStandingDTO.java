package com.sunny.minigame.score.backend.domain;

import lombok.*;

@NonNull @Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class UserStandingDTO {

    private int userId;

    private int score;

    private int position;

}

package com.sunny.minigame.score.backend.domain;

import lombok.*;

@Getter @Setter @NonNull @AllArgsConstructor @NoArgsConstructor
public class UserPointsDTO {
    private int userId;

    private int points;

}

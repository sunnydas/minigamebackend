package com.sunny.minigame.score.backend.datastore.config;

import com.sunny.minigame.score.backend.datastore.UserScoreDS;
import com.sunny.minigame.score.backend.datastore.factory.UserScoreDSFactory;
import org.springframework.context.annotation.Bean;

public class FactoryBeanAppConfig {

    @Bean(name = "userscoreds")
    public UserScoreDSFactory toolFactory() {
        return new UserScoreDSFactory();
    }

    @Bean
    public UserScoreDS tool() throws Exception {
        return toolFactory().getObject();
    }

}

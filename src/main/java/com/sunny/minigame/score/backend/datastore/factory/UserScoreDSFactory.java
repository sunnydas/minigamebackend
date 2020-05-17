package com.sunny.minigame.score.backend.datastore.factory;

import com.sunny.minigame.score.backend.datastore.UserScoreDS;
import com.sunny.minigame.score.backend.datastore.impl.InMemoryUserScoreDS;
import org.springframework.beans.factory.FactoryBean;

public class UserScoreDSFactory implements FactoryBean<UserScoreDS> {

    private InMemoryUserScoreDS inMemoryUserScoreDS = new InMemoryUserScoreDS();

    @Override
    public UserScoreDS getObject() throws Exception {
        return inMemoryUserScoreDS;
    }

    @Override
    public Class<?> getObjectType() {
        return inMemoryUserScoreDS.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}

package com.sunny.minigame.score.backend.datastore.impl;

import com.sunny.minigame.score.backend.datastore.UserScoreDS;
import com.sunny.minigame.score.backend.datastore.dao.UserScoreDAO;
import com.sunny.minigame.score.backend.datastore.exception.UserNotFoundException;
import com.sunny.minigame.score.backend.domain.UserPointsDTO;
import com.sunny.minigame.score.backend.domain.UserStandingDTO;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
/**
 * The class represents the in memory datas store to be used for the minigame backend.
 * The brute force approach is to store the score in a map and essentially sort the values aided by
 * some locking.
 * However, I have tried to optimize this using concurrent data structures. the concurrent skip
 * list set has been used, because it provides sorting and also allows the map API. The purpose of the
 * concurrent skip list is sorting for the /highscore API.
 * The comparator has been used to compare primarily based on the score and then use conflict
 * resolution strategy in case two scores are equal.
 * The primary map simply keeps track of userId and score.
 */
public class InMemoryUserScoreDS implements UserScoreDS {

    private final Map<Integer, UserScoreDAO> userScoreMap = new ConcurrentHashMap<>();

    /*
    Custom comparator that uses score only.
     */
    private final NavigableSet<UserScoreDAO> userScoreStandings = new ConcurrentSkipListSet<>((o1, o2) -> {
        if(o1 ==null && o2 == null){
            return 0;
        }
        if(o1 != null && o2 != null) {
            if (o1.getScore() != o2.getScore()) {
                return o1.getScore() - o2.getScore();
            }
            if(o1.getUserId() == o2.getUserId()){
                return 0;
            }
            // If two entries have same score use conflict resolution
            return o1.getUserId() - o2.getUserId();
        }
        return (o1 == null)? o2.getScore() : o1.getScore();
    });

    @Override
    public void addUserPoints(UserPointsDTO userPointsDTO) {
        int userId = userPointsDTO.getUserId();
        int points = userPointsDTO.getPoints();
        UserScoreDAO userScoreDAO = new UserScoreDAO(userId,points);
        //Merge API used for atomicty
        userScoreDAO = userScoreMap.merge(userId,userScoreDAO,(userScoreDAO1, userScoreDAO2) -> {
            userScoreDAO1.setScore(userScoreDAO1.getScore() + userScoreDAO2.getScore());
            //the skip list set is to be updated only once for a user id
            userScoreDAO1.setNewBorn(false);
            return userScoreDAO1;
        });
        //New user so update concurrent skip list
        if(userScoreDAO.isNewBorn()) {
            userScoreStandings.add(userScoreDAO);
        }
    }

    @Override
    public UserStandingDTO getUserStanding(int userId) throws UserNotFoundException{
        UserStandingDTO userStandingDTO = new UserStandingDTO();
        UserScoreDAO userScoreDAO = userScoreMap.getOrDefault(userId,null);
        userStandingDTO.setUserId(userId);
        if(userScoreDAO != null) {
            userStandingDTO.setScore(userScoreDAO.getScore());
            //This takes up time
            userStandingDTO.setPosition(findPosition(userScoreDAO));
        } else{
            throw new UserNotFoundException("User not found " + userId);
        }
        return userStandingDTO;
    }

    private int findPosition(UserScoreDAO userScoreDAO){
        int position;
        //Since we know the score, we can use that to fetch a subset
        SortedSet<UserScoreDAO> relevantScoreSubset = userScoreStandings.tailSet(userScoreDAO);
        Iterator<UserScoreDAO> daoIterator = relevantScoreSubset.iterator();
        int offsetCount = 0;
        while(daoIterator.hasNext()){
            //Make sure you are finding the poistion of the relevant user
            if(daoIterator.next().getUserId() == userScoreDAO.getUserId()){
                break;
            }
            offsetCount++;
        }
        position = relevantScoreSubset.size() - offsetCount;
        return position;
    }

    @Override
    public List<UserStandingDTO> getSortedUsersStandingDTO(int limit) {
        List<UserStandingDTO> userStandingDTOS = new ArrayList<>();
        Iterator<UserScoreDAO> userScoreDAOIterator = userScoreStandings.descendingIterator();
        int positionCount = 1;
        //Populate high score list, a linear traversal
        if(userScoreDAOIterator != null) {
            while (userScoreDAOIterator.hasNext() && positionCount <= limit) {
                UserScoreDAO userScoreDAO = userScoreDAOIterator.next();
                UserStandingDTO userStandingDTO = new UserStandingDTO(userScoreDAO.getUserId(),
                        userScoreDAO.getScore(),
                        positionCount);
                userStandingDTOS.add(userStandingDTO);
                positionCount++;
            }
        }
        return userStandingDTOS;
    }
}
package com.sunny.minigame.score.backend.service;

import com.sunny.minigame.score.backend.datastore.exception.UserNotFoundException;
import com.sunny.minigame.score.backend.domain.HighScoresDTO;
import com.sunny.minigame.score.backend.domain.UserPointsDTO;
import com.sunny.minigame.score.backend.domain.UserStandingDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.util.Assert.isTrue;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private UserPositionService userPositionService;

    @Test
    public void testConcurrentScoreUpdateSameUserId() throws UserNotFoundException {
        int userId = 1;
        UserPointsDTO userPointsDTO1 = new UserPointsDTO(userId,20);
        UserPointsDTO userPointsDTO2 = new UserPointsDTO(userId,30);
        ExecutorService service =  null;
        try {
            service = Executors.newFixedThreadPool(2);
            service.submit(() -> userScoreService.addPointsToUser(userPointsDTO1));
            service.submit(() -> userScoreService.addPointsToUser(userPointsDTO2));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final UserStandingDTO userStanding = userPositionService.getUserStanding(userId);
            isTrue(userStanding.getScore() == 50,
                    "Concurrency assertion failed");
        }finally {
            service.shutdown();
        }
    }

    @Test
    public void testConcurrentScoreUpdateSameUserIdHighScale() throws UserNotFoundException, BrokenBarrierException, InterruptedException {
        int userId = 2;
        UserPointsDTO userPointsDTO = new UserPointsDTO(userId,10);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
        Runnable pointsUpdateTask = new Runnable() {
            @Override
            public void run() {
                userScoreService.addPointsToUser(userPointsDTO);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread[] pointsUpdateTasks = new Thread[50];
        for(int i = 0; i < pointsUpdateTasks.length ; i++){
            pointsUpdateTasks[i] = new Thread(pointsUpdateTask);
        }
        for(int i = 0; i < pointsUpdateTasks.length ; i++){
            pointsUpdateTasks[i].start();
        }
        Thread.sleep(6000);
        final UserStandingDTO userStanding = userPositionService.getUserStanding(userId);
        System.out.println(userStanding.getScore());
        isTrue(userStanding.getScore() == 500,
                "Concurrency assertion failed");
    }

    @Test
    public void testHighScoreListMultipleUser() throws InterruptedException {
        int userId1 = 100;
        final int user1Point = 5000;
        int userId2 = 101;
        int user2Point = 6000;
        Runnable user1Worker = new Runnable() {
            @Override
            public void run() {
                int point  = user1Point;
                for(int i = 0 ; i < 20; i++){
                    UserPointsDTO userPointsDTO = new UserPointsDTO(userId1,point);
                    userScoreService.addPointsToUser(userPointsDTO);
                    point += 10;
                }
            }
        };
        Runnable user2Worker = new Runnable() {
            @Override
            public void run() {
                int point  = user2Point;
                for(int i = 0 ; i < 20; i++){
                    UserPointsDTO userPointsDTO = new UserPointsDTO(userId2,point);
                    userScoreService.addPointsToUser(userPointsDTO);
                    point += 10;
                }
            }
        };
        ExecutorService service = null;
        try{
            service = Executors.newFixedThreadPool(2);
            service.submit(user1Worker);
            service.submit(user2Worker);
            Thread.sleep(6000);
            HighScoresDTO highScoresDTO = userPositionService.getHighScores();
            isTrue(!highScoresDTO.getHighscores().isEmpty(),"High scores is empty");
            List<UserStandingDTO> userStandingDTOS = highScoresDTO.getHighscores();
            boolean user1Found = true;
            boolean user2Found = true;
            for(UserStandingDTO userStandingDTO : userStandingDTOS){
                if(userStandingDTO.getUserId() == userId1){
                    isTrue(userStandingDTO.getPosition() == 1,
                            "high score list not calculated properly");
                    user1Found = true;
                }
                if(userStandingDTO.getUserId() == userId2){
                    isTrue(userStandingDTO.getPosition() == 2,
                            "high score list not calculated properly");
                    user2Found = true;
                }
            }
            isTrue(user1Found && user2Found);
        }finally {
            if(service != null){
                service.shutdown();
            }
        }
    }

}
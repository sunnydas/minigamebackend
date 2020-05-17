package com.sunny.minigame.score.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.minigame.score.backend.domain.HighScoresDTO;
import com.sunny.minigame.score.backend.domain.UserPointsDTO;
import com.sunny.minigame.score.backend.domain.UserStandingDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;


@SpringBootTest
@AutoConfigureMockMvc
class ScoretrackerApplicationTests {


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testAddPointsFlowSingleUser() throws Exception {
		int userId = 1;
		int userPoints = 20;
		UserPointsDTO userPointsDTO = new UserPointsDTO(userId,userPoints);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
		.content(objectMapper.writeValueAsString(userPointsDTO))).andExpect(status().isOk());
		MvcResult result = mockMvc.perform(get("/" + userId + "/position")).andExpect(status().
				isOk()).andReturn();
		UserStandingDTO userStandingDTO = objectMapper.readValue(result.getResponse().
				getContentAsString(),UserStandingDTO.class);
		notNull(userStandingDTO,"Basic check failed for score updation");
		isTrue(userStandingDTO.getUserId()== userId,"UserId assertion failed");
		isTrue(userStandingDTO.getScore() == userPoints,"Score assertion failed");
	}


	@Test
	public void testAddPointsFlowMultipleUsers() throws Exception {
		int userId1 = 3;
		int userPoints1 = 40;
		int userId2 = 4;
		int userPoints2 = 50;
		UserPointsDTO userPointsDTO = new UserPointsDTO(userId1,userPoints1);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).andExpect(status().isOk());
		MvcResult result = mockMvc.perform(get("/" + userId1 + "/position")).andExpect(status().
				isOk()).andReturn();
		UserStandingDTO userStandingDTO = objectMapper.readValue(result.getResponse().
				getContentAsString(),UserStandingDTO.class);
		notNull(userStandingDTO,"Basic check failed for score updation");
		isTrue(userStandingDTO.getUserId()== userId1,"UserId assertion failed");
		isTrue(userStandingDTO.getScore() == userPoints1,"Score assertion failed");
		int user1Position = userStandingDTO.getPosition();
		userPointsDTO = new UserPointsDTO(userId2,userPoints2);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).andExpect(status().isOk());
		result = mockMvc.perform(get("/" + userId2 + "/position")).andExpect(status().
				isOk()).andReturn();
		userStandingDTO = objectMapper.readValue(result.getResponse().
				getContentAsString(),UserStandingDTO.class);
		notNull(userStandingDTO,"Basic check failed for score updation");
		isTrue(userStandingDTO.getUserId()== userId2,"UserId assertion failed");
		isTrue(userStandingDTO.getScore() == userPoints2,"Score assertion failed");
		int user2Position = userStandingDTO.getPosition();
		result = mockMvc.perform(get("/" + userId1 + "/position")).andExpect(status().
				isOk()).andReturn();
		userStandingDTO = objectMapper.readValue(result.getResponse().
				getContentAsString(),UserStandingDTO.class);
		isTrue(userStandingDTO.getPosition() > user2Position,"Position assertion check failed " +
				"for user2 = "
		+ userStandingDTO.getPosition()  + " user 1 position = " + user2Position);
	}

	@Test
	public void testAddPointsMultipleUpdateSingleUser() throws Exception {
		int userId = 5;
		int userPoints = 20;
		UserPointsDTO userPointsDTO = new UserPointsDTO(userId,userPoints);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).andExpect(status().isOk());
		MvcResult result = mockMvc.perform(get("/" + userId + "/position")).andExpect(status().
				isOk()).andReturn();
		UserStandingDTO userStandingDTO = objectMapper.readValue(result.getResponse().
				getContentAsString(),UserStandingDTO.class);
		notNull(userStandingDTO,"Basic check failed for score updation");
		isTrue(userStandingDTO.getUserId()== userId,"UserId assertion failed");
		isTrue(userStandingDTO.getScore() == userPoints,"Score assertion failed");
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).andExpect(status().isOk());
		result = mockMvc.perform(get("/" + userId + "/position")).andExpect(status().
				isOk()).andReturn();
		userStandingDTO = objectMapper.readValue(result.getResponse().
				getContentAsString(),UserStandingDTO.class);
		notNull(userStandingDTO,"Basic check failed for score updation");
		isTrue(userStandingDTO.getUserId()== userId,"UserId assertion failed");
		int expectScore = userPoints * 2;
		isTrue(userStandingDTO.getScore() == expectScore,"Score assertion failed score = "
				+ userStandingDTO.getScore() + " expected = " + expectScore);
	}

	@Test
	public void testHighScoresFacility() throws Exception {
		int userId1 = 10;
		int userPoint1 = 50;
		int userId2 = 11;
		int userPoint2 = 60;
		int userId3 = 12;
		int userPoint3 = 40;
		int userId4 = 13;
		int userPoint4 = 30;
		UserPointsDTO userPointsDTO = new UserPointsDTO(userId1,userPoint1);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		userPointsDTO = new UserPointsDTO(userId2,userPoint2);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		userPointsDTO = new UserPointsDTO(userId3,userPoint3);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		userPointsDTO = new UserPointsDTO(userId4,userPoint4);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		MvcResult result = mockMvc.perform(get("/highscorelist")).andExpect(status().
				isOk()).andReturn();
		HighScoresDTO highScoresDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
				HighScoresDTO.class);
		notNull(highScoresDTO,"The wrapper DTO check is null and invalidates this test");
		List<UserStandingDTO> highscores = highScoresDTO.getHighscores();
		isTrue(highscores.size() >=  4,
				"High scores not updated correctly ");
		List<UserStandingDTO> userStandingDTOS = highscores;
		Map<Integer,Integer> fetchedUserStandingsMap = new HashMap<>();
		for(UserStandingDTO userStandingDTO : highscores){
			fetchedUserStandingsMap.put(userStandingDTO.getUserId(),userStandingDTO.getPosition());
		}
		System.out.println(fetchedUserStandingsMap);
		isTrue(fetchedUserStandingsMap.get(userId1)
				< fetchedUserStandingsMap.get(userId4),"failed assertion u1 " + userId1
				+ " u2 " + userId4);
		isTrue(fetchedUserStandingsMap.get(userId2)
				< fetchedUserStandingsMap.get(userId1),"failed assertion ");
		isTrue(fetchedUserStandingsMap.get(userId3)
				< fetchedUserStandingsMap.get(userId4),"failed assertion ");
	}


	@Test
	public void testHighScoresFacilityResultsLimit() throws Exception {
		for(int i = 100 ; i < 19000 ; i++){
			UserPointsDTO userPointsDTO = new UserPointsDTO(i,i+10);
			mockMvc.perform(post(
					"/score"
			).contentType("application/json")
					.content(objectMapper.writeValueAsString(userPointsDTO))).
					andExpect(status().isOk());
		}
		MvcResult result = mockMvc.perform(get("/highscorelist")).andExpect(status().
				isOk()).andReturn();
		HighScoresDTO highScoresDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
				HighScoresDTO.class);
		notNull(highScoresDTO,"The wrapper DTO check is null and invalidates this test");
		List<UserStandingDTO> highscores = highScoresDTO.getHighscores();
		isTrue(highscores.size() <=  20000,
				"High scores not updated correctly ");
	}

	@Test
	public void testUserNotFound() throws Exception {
		mockMvc.perform(get("/-1/position")).andExpect(status().
				isNotFound());
	}

	@Test
	public void testUserScoreRankingChange() throws Exception {
		int userId1 = 50000;
		int userPoint1 = 1000;
		int userId2 = 50001;
		int userPoint2 = 2000;
		UserPointsDTO userPointsDTO = new UserPointsDTO(userId1,userPoint1);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		userPointsDTO = new UserPointsDTO(userId2,userPoint2);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		MvcResult result = mockMvc.perform(get("/highscorelist")).andExpect(status().
				isOk()).andReturn();
		HighScoresDTO highScoresDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
				HighScoresDTO.class);
		notNull(highScoresDTO,"The wrapper DTO check is null and invalidates this test");
		List<UserStandingDTO> highscores = highScoresDTO.getHighscores();
		int position1 = -1;
		int position2 = -1;
		for(UserStandingDTO userStandingDTO : highscores){
			if(userStandingDTO.getUserId() == userId1){
				position1 = userStandingDTO.getPosition();
			}
			else if(userStandingDTO.getUserId() == userId2){
				position2 = userStandingDTO.getPosition();
			}
		}
		isTrue(position1 > position2,"high score calculation failure");
		userPointsDTO = new UserPointsDTO(userId1,userPoint1 + 2000);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		result = mockMvc.perform(get("/highscorelist")).andExpect(status().
				isOk()).andReturn();
		highScoresDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
				HighScoresDTO.class);
		notNull(highScoresDTO,"The wrapper DTO check is null and invalidates this test");
		highscores = highScoresDTO.getHighscores();
		position1 = -1;
		position2 = -1;
		for(UserStandingDTO userStandingDTO : highscores){
			if(userStandingDTO.getUserId() == userId1){
				position1 = userStandingDTO.getPosition();
			}
			else if(userStandingDTO.getUserId() == userId2){
				position2 = userStandingDTO.getPosition();
			}
		}
		isTrue(position1 < position2,"high score calculation failure");
	}


	@Test
	public void testSameScoreRanking() throws Exception {
		int userId1 = 60000;
		int userPoint1 = 10000;
		int userId2 = 60001;
		int userPoint2 = 10000;
		UserPointsDTO userPointsDTO = new UserPointsDTO(userId1,userPoint1);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		userPointsDTO = new UserPointsDTO(userId2,userPoint2);
		mockMvc.perform(post(
				"/score"
		).contentType("application/json")
				.content(objectMapper.writeValueAsString(userPointsDTO))).
				andExpect(status().isOk());
		MvcResult result = mockMvc.perform(get("/highscorelist")).andExpect(status().
				isOk()).andReturn();
		HighScoresDTO highScoresDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
				HighScoresDTO.class);
		notNull(highScoresDTO,"The wrapper DTO check is null and invalidates this test");
		List<UserStandingDTO> highscores = highScoresDTO.getHighscores();
		int position1 = -1;
		int position2 = -1;
		for(UserStandingDTO userStandingDTO : highscores){
			if(userStandingDTO.getUserId() == userId1){
				position1 = userStandingDTO.getPosition();
			}
			else if(userStandingDTO.getUserId() == userId2){
				position2 = userStandingDTO.getPosition();
			}
		}
		isTrue(position1 > position2,"high score calculation failure");
	}


}
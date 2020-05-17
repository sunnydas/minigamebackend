# Minigame Score Calculation Backend API

### Description

This project represents a simple illustration of game back end, primarily the score and position tracking of a player.
The HTTP API are based on REST and use SpringBoot as the underlying technology. The backend used
for this project is a combination of ConcurrentHashMap and ConcurrentSkipListSet. The sorting of players
based on scores have been delegated to the ConcurrentSkipListSet and the ConcurentHashMap is to maintain individual 
scores and scores updates.

### Pre-requisites

* Java 1.8+
* Gradle 6.4

### Steps To Build
To perform a fresh build,use:

`gradle clean build`

To perform a standalone run,use:

`gradle bootRun`

### API Documentation

* [Swagger Local API Link](http://localhost:8081/swagger-ui.html)

* Score Add/Update Example:
  
  `curl -X POST --header "Content-Type: application/json" --header "Accept: */*" -d "{
     \"points\": 0,
     \"userId\": 0
   }" "http://localhost:8081/score"`

* Get Position For User Example:

`curl -X GET --header "Accept: */*" "http://localhost:8081/1/position"`

* Get High Scores List Example:

`curl -X GET --header "Accept: */*" "http://localhost:8081/highscorelist"`
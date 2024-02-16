# Gold Medal Metrics Challenge Code Academy Project

### Overview
In this project, I tackled the task of powering an Olympics analytics web app using Spring Data JPA.

### Installation
To run the project, you need to have Java 8 and Maven installed on your machine. You can download Java 8 from [here](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html) and Maven from [here](https://maven.apache.org/download.cgi).

Once you have Java and Maven installed, you can clone the repository and navigate to the project's root directory. Then, run the following command to start the application:

```shell
mvn spring-boot:run
```

This will start the application on port 3001. You can then access the API endpoints using a tool like cURL or Postman.

### Testing
I manually tested the API endpoints using cURL. Here are some examples of how I did it:

#### Get countries, sorted by name in ascending order 

To fetch the countries sorted by their names in ascending order, I used the following cURL request:

```shell
curl --request GET "http://localhost:3001/countries?sort_by=name&ascending=y"                                   

{"countries":[{"name":"Afghanistan","code":"AFG","gdp":594.32,"population":32526562,"medals":0},...]}
```

This request successfully retrieved a list of countries, including Afghanistan, sorted by name.

#### Get the details for the United States Olympic team
For detailed information about the United States Olympic team, I executed:

```shell
curl --request GET "http://localhost:3001/countries/united%20states"                                            

{"name":"United States","gdp":"56115.72","population":"321418820","numberMedals":"2477","numberSummerWins":"2302","percentageTotalSummerWins":"21.957268","yearFirstSummerWin":"1896","numberWinterWins":"175","percentageTotalWinterWins":"9.1098385","yearFirstWinterWin":"1924","numberEventsWonByFemaleAthletes":"747","numberEventsWonByMaleAthletes":"1730"}

```
This gave me a comprehensive breakdown of the United States Olympic team's statistics.


#### Get the list of Gold medal winners for the United States Olympic team, sorted by the athlete's name in descending order
To see a list of Gold medal winners for the United States, sorted by the athlete's name in descending order, I used:

```shell
curl --request GET "http://localhost:3001/countries/united%20states/medals?sort_by=name&ascending=n"            

{"medals":[{"year":1968,"city":"Mexico","season":"Summer","name":"ZORN, Zachary","country":"United States","gender":"Men","sport":"Aquatics","discipline":"Swimming","event":"4X100M Freestyle Relay"},...]}
```
The response included athletes like Zachary Zorn, listing them in the desired order.


# Musala Soft - Drone Service

### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the drone**. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the drone has the potential to leapfrog traditional transportation infrastructure.

Useful drone functions include delivery of small items that are (urgently) needed in locations with difficult access.

---

### Task description

We have a fleet of **10 drones**. A drone is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case **the load is medications**.


### Technologies

- Java
- Maven
- H2 Database
- Spring Boot
- Spring Data JPA

### Requirements

You need the following to build and run the application:

- [JDK 17](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven 3.8.6](https://maven.apache.org) (This is the build tool used.)


## How to run Application(s)
### step 1 - clone project with Terminal from [here](https://gitlab.com/TryG70/MusalaSoft-DroneService)

```
git clone git@github.com:TryG70/MusalaSoft-DroneService.git
```

### step 2 - move into the project directory
```
cd MusalaSoft-DroneService/
```

### step 3 - Open the MusalaSoft-DroneService Folder in an IDE, As a maven Project.
 
```
mvn spring-boot:run
```


### step 6 - Generate the .jar files with Terminal

```
mvn clean install 
```
OR
```
./mvnw clean install
```


## PostMan Collection for Integration Tests
- Musala-Soft Drone-Service [here](https://api.postman.com/collections/22955162-8cf57c1f-0c62-4ec0-b487-09eab8282cd6?access_key=PMAT-01GK9NZEKC0T5R886YZ4WMHQFG)


## Running The Tests with Maven

To run the tests with maven, you would need to run the maven command for testing, after the code has been compiled.
```
mvn <option> test
```


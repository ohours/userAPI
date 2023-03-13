# userAPI

Usage :

1. pull branch "main"
2. build and test from main directory with ./mvnw test or other 'maven test' command depending on your local environment
3. run from main directory with ./mvnw spring-boot:run or other 'maven spring-boot:run' command depending on your local environment
4. user provided postman collection to probe API on your local url http://localhost/users
5. AOP input output and exception logs can be seen on console as long as log level fixed to DEBUG (line "logging.level.org.ohours.userAPI=DEBUG" in application.properties, on by default)

# userAPI

Usage :

1. pull branch "main"
2. build and test from project root with **./mvnw test** or other 'maven test' command depending on your local environment
3. run from project root with **./mvnw spring-boot:run** or other 'maven spring-boot:run' command depending on your local environment
4. manually probe the API at your local url http://localhost/users with the provided tests (postman collection at project's root)
5. AOP input output and exception logs can be seen on console as long as log level fixed to DEBUG (line "logging.level.org.ohours.userAPI=DEBUG" in application.properties, on by default)
